-- =====================================================================
-- FacilityBackend : ADVANCED FEATURES (run AFTER facility_production_schema.sql)
-- MySQL 8.4 / InnoDB / utf8mb4
--
-- Contents
--   A. Slot capacity + checkout holds        H. Commission, tiers, incentives, training
--   B. Recurring bookings (subscriptions)    I. Chat + masked calls
--   C. Quote / inspection flow               J. Live tracking (partitioned)
--   D. Cancellation policy + reschedule      K. Trust & safety, consent (DPDP/GDPR)
--   E. Warranty / redo claims                L. Config, feature flags, banners, i18n, search
--   F. Customer wallet, loyalty, referrals   M. Analytics rollups
--   G. GST invoicing                         N. Webhooks + idempotency
--   O. Stored procedures, events, views
--
-- Notes
--   * Stored procedures use DELIMITER $$ (mysql client). In Liquibase use
--     <sqlFile splitStatements="false" endDelimiter="$$"/> for section O.
--   * Events need:  SET GLOBAL event_scheduler = ON;
--   * Partitioned tables cannot have foreign keys, so they reference ids logically.
-- =====================================================================

SET NAMES utf8mb4;

-- =====================================================================
-- H (first, because professional/booking ALTERs reference it)
-- COMMISSION, TIERS, INCENTIVES, TRAINING
-- =====================================================================
CREATE TABLE professional_tier (             -- Bronze / Silver / Gold / Platinum
  id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
  code                VARCHAR(20)  NOT NULL,
  name                VARCHAR(50)  NOT NULL,
  min_rating          DECIMAL(3,2) NOT NULL DEFAULT 0,
  min_jobs            INT          NOT NULL DEFAULT 0,
  commission_percent  DECIMAL(5,2) NOT NULL,          -- platform cut
  dispatch_priority   INT          NOT NULL DEFAULT 0, -- higher = offered jobs first
  UNIQUE KEY ux_tier_code (code)
) ENGINE=InnoDB;

CREATE TABLE commission_rule (               -- most specific rule wins (service > category > default)
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  service_id    VARCHAR(50)  NULL,
  category_id   BIGINT       NULL,
  city_id       BIGINT       NULL,
  tier_id       BIGINT       NULL,
  commission_percent DECIMAL(5,2) NOT NULL,
  flat_fee      DECIMAL(12,2) NOT NULL DEFAULT 0,
  valid_from    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valid_to      TIMESTAMP    NULL,
  KEY ix_comm_lookup (service_id, category_id, city_id, tier_id),
  CONSTRAINT fk_comm_service  FOREIGN KEY (service_id)  REFERENCES service(id),
  CONSTRAINT fk_comm_category FOREIGN KEY (category_id) REFERENCES service_category(id),
  CONSTRAINT fk_comm_city     FOREIGN KEY (city_id)     REFERENCES city(id),
  CONSTRAINT fk_comm_tier     FOREIGN KEY (tier_id)     REFERENCES professional_tier(id)
) ENGINE=InnoDB;

CREATE TABLE professional_tier_history (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  professional_id BIGINT       NOT NULL,
  tier_id         BIGINT       NOT NULL,
  effective_from  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  reason          VARCHAR(120) NULL,
  KEY ix_tierhist_pro (professional_id, effective_from),
  CONSTRAINT fk_tierhist_pro  FOREIGN KEY (professional_id) REFERENCES professional(id),
  CONSTRAINT fk_tierhist_tier FOREIGN KEY (tier_id)         REFERENCES professional_tier(id)
) ENGINE=InnoDB;

CREATE TABLE incentive_rule (                -- e.g. "10 jobs this week => Rs 500"
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  name           VARCHAR(120) NOT NULL,
  city_id        BIGINT       NULL,
  metric         VARCHAR(20)  NOT NULL,       -- JOBS_COMPLETED / RATING / PEAK_HOUR_JOBS
  threshold      DECIMAL(10,2) NOT NULL,
  reward_amount  DECIMAL(12,2) NOT NULL,
  period         VARCHAR(10)  NOT NULL DEFAULT 'WEEKLY',   -- DAILY / WEEKLY / MONTHLY
  is_active      TINYINT(1)   NOT NULL DEFAULT 1,
  CONSTRAINT fk_incentive_city FOREIGN KEY (city_id) REFERENCES city(id)
) ENGINE=InnoDB;

CREATE TABLE professional_incentive_award (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  professional_id BIGINT       NOT NULL,
  rule_id         BIGINT       NOT NULL,
  period_start    DATE         NOT NULL,
  period_end      DATE         NOT NULL,
  amount          DECIMAL(12,2) NOT NULL,
  wallet_txn_id   BIGINT       NULL,
  created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY ux_award_once (professional_id, rule_id, period_start),
  CONSTRAINT fk_award_pro  FOREIGN KEY (professional_id) REFERENCES professional(id),
  CONSTRAINT fk_award_rule FOREIGN KEY (rule_id)         REFERENCES incentive_rule(id)
) ENGINE=InnoDB;

CREATE TABLE training_module (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  service_id  VARCHAR(50)  NULL,
  title       VARCHAR(150) NOT NULL,
  content_url VARCHAR(255) NULL,
  is_mandatory TINYINT(1)  NOT NULL DEFAULT 1,
  pass_score  INT          NOT NULL DEFAULT 70,
  CONSTRAINT fk_training_service FOREIGN KEY (service_id) REFERENCES service(id)
) ENGINE=InnoDB;

CREATE TABLE professional_training (
  professional_id BIGINT NOT NULL,
  module_id       BIGINT NOT NULL,
  score           INT    NULL,
  status          VARCHAR(12) NOT NULL DEFAULT 'ASSIGNED',   -- ASSIGNED/PASSED/FAILED
  completed_at    TIMESTAMP NULL,
  PRIMARY KEY (professional_id, module_id),
  CONSTRAINT fk_ptrain_pro    FOREIGN KEY (professional_id) REFERENCES professional(id) ON DELETE CASCADE,
  CONSTRAINT fk_ptrain_module FOREIGN KEY (module_id)       REFERENCES training_module(id)
) ENGINE=InnoDB;

INSERT INTO professional_tier (code, name, min_rating, min_jobs, commission_percent, dispatch_priority) VALUES
 ('BRONZE',   'Bronze',   0.00,   0, 25.00, 0),
 ('SILVER',   'Silver',   4.50,  50, 22.00, 1),
 ('GOLD',     'Gold',     4.70, 200, 20.00, 2),
 ('PLATINUM', 'Platinum', 4.85, 500, 18.00, 3);

-- =====================================================================
-- A. SLOT CAPACITY + CHECKOUT HOLDS
-- =====================================================================
CREATE TABLE slot_capacity (                 -- how many jobs a zone can take per slot
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  zone_id       BIGINT      NOT NULL,
  category_id   BIGINT      NOT NULL,
  slot_start    DATETIME    NOT NULL,         -- local time of the city
  slot_end      DATETIME    NOT NULL,
  capacity      INT         NOT NULL,
  booked_count  INT         NOT NULL DEFAULT 0,
  is_blocked    TINYINT(1)  NOT NULL DEFAULT 0,   -- ops can close a slot (rain, strike)
  price_multiplier DECIMAL(4,2) NOT NULL DEFAULT 1.00,  -- peak-hour surge
  version       BIGINT      NOT NULL DEFAULT 0,
  UNIQUE KEY ux_slot (zone_id, category_id, slot_start),
  KEY ix_slot_day (category_id, slot_start),
  CONSTRAINT fk_slot_zone     FOREIGN KEY (zone_id)     REFERENCES service_zone(id),
  CONSTRAINT fk_slot_category FOREIGN KEY (category_id) REFERENCES service_category(id),
  CONSTRAINT ck_slot_cap CHECK (booked_count >= 0 AND booked_count <= capacity)
) ENGINE=InnoDB;

CREATE TABLE slot_hold (                     -- reserved while the customer pays (5-10 min)
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  slot_capacity_id BIGINT      NOT NULL,
  user_id          BIGINT      NOT NULL,
  status           VARCHAR(10) NOT NULL DEFAULT 'HELD',   -- HELD/CONVERTED/EXPIRED/RELEASED
  booking_id       CHAR(36)    NULL,
  expires_at       TIMESTAMP   NOT NULL,
  created_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY ix_hold_slot (slot_capacity_id, status, expires_at),
  KEY ix_hold_expiry (status, expires_at),
  CONSTRAINT fk_hold_slot FOREIGN KEY (slot_capacity_id) REFERENCES slot_capacity(id),
  CONSTRAINT fk_hold_user FOREIGN KEY (user_id)          REFERENCES jhi_user(id)
) ENGINE=InnoDB;

-- =====================================================================
-- B. RECURRING BOOKINGS (weekly cleaning, monthly AC service)
-- =====================================================================
CREATE TABLE booking_subscription (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id    BIGINT       NOT NULL,
  service_id     VARCHAR(50)  NOT NULL,
  package_id     BIGINT       NOT NULL,
  address_id     BIGINT       NOT NULL,
  preferred_professional_id BIGINT NULL,
  frequency      VARCHAR(10)  NOT NULL,        -- WEEKLY / BIWEEKLY / MONTHLY
  days_of_week   JSON         NULL,            -- [1,4] = Mon, Thu
  preferred_time TIME         NOT NULL,
  start_date     DATE         NOT NULL,
  end_date       DATE         NULL,
  next_run_date  DATE         NOT NULL,
  status         VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE',   -- ACTIVE/PAUSED/CANCELLED
  auto_pay       TINYINT(1)   NOT NULL DEFAULT 0,
  created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY ix_sub_run (status, next_run_date),
  CONSTRAINT fk_sub_customer FOREIGN KEY (customer_id) REFERENCES jhi_user(id),
  CONSTRAINT fk_sub_service  FOREIGN KEY (service_id)  REFERENCES service(id),
  CONSTRAINT fk_sub_package  FOREIGN KEY (package_id)  REFERENCES service_package(id),
  CONSTRAINT fk_sub_address  FOREIGN KEY (address_id)  REFERENCES customer_address(id),
  CONSTRAINT fk_sub_pro      FOREIGN KEY (preferred_professional_id) REFERENCES professional(id)
) ENGINE=InnoDB;

-- =====================================================================
-- C. QUOTE / INSPECTION FLOW (painting, repairs, carpentry)
-- =====================================================================
CREATE TABLE booking_quote (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id      CHAR(36)      NOT NULL,
  professional_id BIGINT        NOT NULL,
  status          VARCHAR(10)   NOT NULL DEFAULT 'PROPOSED',  -- PROPOSED/ACCEPTED/REJECTED/EXPIRED
  total_amount    DECIMAL(12,2) NOT NULL,
  notes           VARCHAR(1000) NULL,
  valid_until     TIMESTAMP     NULL,
  responded_at    TIMESTAMP     NULL,
  created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY ix_quote_booking (booking_id, status),
  CONSTRAINT fk_quote_booking FOREIGN KEY (booking_id)      REFERENCES booking(id) ON DELETE CASCADE,
  CONSTRAINT fk_quote_pro     FOREIGN KEY (professional_id) REFERENCES professional(id)
) ENGINE=InnoDB;

CREATE TABLE booking_quote_item (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  quote_id     BIGINT        NOT NULL,
  item_type    VARCHAR(10)   NOT NULL,        -- LABOUR / MATERIAL / PART
  description  VARCHAR(200)  NOT NULL,
  unit_price   DECIMAL(12,2) NOT NULL,
  quantity     DECIMAL(8,2)  NOT NULL DEFAULT 1,
  line_total   DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_qitem_quote FOREIGN KEY (quote_id) REFERENCES booking_quote(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- D. CANCELLATION POLICY + RESCHEDULE
-- =====================================================================
CREATE TABLE cancellation_policy (           -- tiers: <2h => 50% fee, <6h => 20% fee
  id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
  category_id         BIGINT       NULL,
  service_id          VARCHAR(50)  NULL,
  hours_before_start  INT          NOT NULL,
  fee_percent         DECIMAL(5,2) NOT NULL,
  min_fee             DECIMAL(12,2) NOT NULL DEFAULT 0,
  applies_to          VARCHAR(10)  NOT NULL DEFAULT 'CUSTOMER',  -- CUSTOMER / PRO
  is_active           TINYINT(1)   NOT NULL DEFAULT 1,
  CONSTRAINT fk_cpol_category FOREIGN KEY (category_id) REFERENCES service_category(id),
  CONSTRAINT fk_cpol_service  FOREIGN KEY (service_id)  REFERENCES service(id)
) ENGINE=InnoDB;

CREATE TABLE booking_reschedule (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id     CHAR(36)   NOT NULL,
  old_start      TIMESTAMP  NOT NULL,
  old_end        TIMESTAMP  NOT NULL,
  new_start      TIMESTAMP  NOT NULL,
  new_end        TIMESTAMP  NOT NULL,
  requested_by   VARCHAR(10) NOT NULL,        -- CUSTOMER / PRO / ADMIN
  reason         VARCHAR(255) NULL,
  fee_charged    DECIMAL(12,2) NOT NULL DEFAULT 0,
  created_at     TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY ix_resched_booking (booking_id),
  CONSTRAINT fk_resched_booking FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- E. WARRANTY / REDO CLAIMS
-- =====================================================================
CREATE TABLE warranty_claim (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id      CHAR(36)     NOT NULL,       -- original job
  customer_id     BIGINT       NOT NULL,
  issue           VARCHAR(1000) NOT NULL,
  status          VARCHAR(12)  NOT NULL DEFAULT 'RAISED',   -- RAISED/APPROVED/REJECTED/REDO_BOOKED/CLOSED
  redo_booking_id CHAR(36)     NULL,
  within_warranty TINYINT(1)   NOT NULL,
  created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  resolved_at     TIMESTAMP    NULL,
  KEY ix_claim_booking (booking_id),
  CONSTRAINT fk_claim_booking  FOREIGN KEY (booking_id)      REFERENCES booking(id),
  CONSTRAINT fk_claim_customer FOREIGN KEY (customer_id)     REFERENCES jhi_user(id),
  CONSTRAINT fk_claim_redo     FOREIGN KEY (redo_booking_id) REFERENCES booking(id)
) ENGINE=InnoDB;

-- =====================================================================
-- F. CUSTOMER WALLET, LOYALTY, REFERRALS
-- =====================================================================
CREATE TABLE customer_wallet (
  user_id     BIGINT PRIMARY KEY,
  balance     DECIMAL(12,2) NOT NULL DEFAULT 0,
  updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  version     BIGINT NOT NULL DEFAULT 0,
  CONSTRAINT fk_cwallet_user FOREIGN KEY (user_id) REFERENCES jhi_user(id),
  CONSTRAINT ck_cwallet_bal CHECK (balance >= 0)
) ENGINE=InnoDB;

CREATE TABLE customer_wallet_txn (           -- append-only
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id       BIGINT        NOT NULL,
  booking_id    CHAR(36)      NULL,
  txn_type      VARCHAR(15)   NOT NULL,       -- TOPUP / SPEND / REFUND / CASHBACK / EXPIRY
  amount        DECIMAL(12,2) NOT NULL,       -- signed
  balance_after DECIMAL(12,2) NOT NULL,
  expires_at    TIMESTAMP     NULL,           -- promo cash can expire
  note          VARCHAR(200)  NULL,
  created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY ix_cwtxn_user (user_id, created_at),
  CONSTRAINT fk_cwtxn_user FOREIGN KEY (user_id) REFERENCES jhi_user(id)
) ENGINE=InnoDB;

CREATE TABLE loyalty_ledger (                -- points earned per booking, redeemed later
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id     BIGINT      NOT NULL,
  booking_id  CHAR(36)    NULL,
  points      INT         NOT NULL,           -- signed
  reason      VARCHAR(30) NOT NULL,           -- EARN_BOOKING / REDEEM / EXPIRE / BONUS
  expires_at  DATE        NULL,
  created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY ix_loyalty_user (user_id, created_at),
  CONSTRAINT fk_loyalty_user FOREIGN KEY (user_id) REFERENCES jhi_user(id)
) ENGINE=InnoDB;

CREATE TABLE referral_reward (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  referrer_id   BIGINT        NOT NULL,
  referee_id    BIGINT        NOT NULL,
  trigger_booking_id CHAR(36) NULL,            -- reward after referee's 1st completed booking
  reward_amount DECIMAL(12,2) NOT NULL,
  status        VARCHAR(10)   NOT NULL DEFAULT 'PENDING',   -- PENDING/GRANTED/REJECTED
  created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY ux_referral_pair (referrer_id, referee_id),
  CONSTRAINT fk_ref_referrer FOREIGN KEY (referrer_id) REFERENCES jhi_user(id),
  CONSTRAINT fk_ref_referee  FOREIGN KEY (referee_id)  REFERENCES jhi_user(id)
) ENGINE=InnoDB;

-- =====================================================================
-- G. GST INVOICING
-- =====================================================================
CREATE TABLE invoice_sequence (              -- gapless numbering per FY + series
  series      VARCHAR(10) NOT NULL,          -- e.g. INV, CN (credit note)
  fiscal_year VARCHAR(9)  NOT NULL,          -- 2026-27
  last_number BIGINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (series, fiscal_year)
) ENGINE=InnoDB;

CREATE TABLE invoice (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  invoice_no      VARCHAR(30)  NOT NULL,
  invoice_type    VARCHAR(12)  NOT NULL DEFAULT 'TAX_INVOICE',   -- TAX_INVOICE / CREDIT_NOTE
  booking_id      CHAR(36)     NOT NULL,
  customer_id     BIGINT       NOT NULL,
  customer_gstin  VARCHAR(15)  NULL,
  seller_gstin    VARCHAR(15)  NOT NULL,
  place_of_supply VARCHAR(40)  NOT NULL,
  taxable_amount  DECIMAL(12,2) NOT NULL,
  cgst            DECIMAL(12,2) NOT NULL DEFAULT 0,
  sgst            DECIMAL(12,2) NOT NULL DEFAULT 0,
  igst            DECIMAL(12,2) NOT NULL DEFAULT 0,
  total_amount    DECIMAL(12,2) NOT NULL,
  pdf_url         VARCHAR(255) NULL,
  issued_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY ux_invoice_no (invoice_no),
  KEY ix_invoice_booking (booking_id),
  CONSTRAINT fk_invoice_booking  FOREIGN KEY (booking_id)  REFERENCES booking(id),
  CONSTRAINT fk_invoice_customer FOREIGN KEY (customer_id) REFERENCES jhi_user(id)
) ENGINE=InnoDB;

CREATE TABLE invoice_line (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  invoice_id   BIGINT        NOT NULL,
  description  VARCHAR(200)  NOT NULL,
  sac_code     VARCHAR(10)   NULL,
  quantity     DECIMAL(8,2)  NOT NULL DEFAULT 1,
  unit_price   DECIMAL(12,2) NOT NULL,
  gst_percent  DECIMAL(5,2)  NOT NULL,
  line_total   DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_iline_invoice FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- I. CHAT + MASKED CALLS
-- =====================================================================
CREATE TABLE chat_thread (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id  CHAR(36)    NOT NULL,
  customer_id BIGINT      NOT NULL,
  professional_id BIGINT  NOT NULL,
  status      VARCHAR(8)  NOT NULL DEFAULT 'OPEN',    -- closed 24h after completion
  created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY ux_thread_booking (booking_id),
  CONSTRAINT fk_thread_booking FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE chat_message (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  thread_id   BIGINT       NOT NULL,
  sender_id   BIGINT       NOT NULL,
  body        VARCHAR(2000) NULL,
  media_url   VARCHAR(255) NULL,
  is_flagged  TINYINT(1)   NOT NULL DEFAULT 0,        -- phone number / off-platform payment detected
  created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  read_at     TIMESTAMP    NULL,
  KEY ix_msg_thread (thread_id, id),
  CONSTRAINT fk_msg_thread FOREIGN KEY (thread_id) REFERENCES chat_thread(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE call_session (                  -- privacy: virtual number bridging both parties
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id     CHAR(36)    NOT NULL,
  virtual_number VARCHAR(20) NOT NULL,
  caller_id      BIGINT      NOT NULL,
  callee_id      BIGINT      NOT NULL,
  provider_call_id VARCHAR(80) NULL,
  duration_sec   INT         NOT NULL DEFAULT 0,
  status         VARCHAR(12) NOT NULL DEFAULT 'INITIATED',
  created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY ix_call_booking (booking_id),
  CONSTRAINT fk_call_booking FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- J. LIVE TRACKING (partitioned by month, no FKs)
-- =====================================================================
CREATE TABLE professional_location_log (
  id              BIGINT AUTO_INCREMENT,
  professional_id BIGINT       NOT NULL,
  booking_id      CHAR(36)     NULL,
  latitude        DECIMAL(9,6) NOT NULL,
  longitude       DECIMAL(9,6) NOT NULL,
  speed_kmph      DECIMAL(5,1) NULL,
  battery_pct     TINYINT      NULL,
  recorded_at     DATETIME     NOT NULL,
  PRIMARY KEY (id, recorded_at),
  KEY ix_loc_booking (booking_id, recorded_at),
  KEY ix_loc_pro (professional_id, recorded_at)
) ENGINE=InnoDB
PARTITION BY RANGE COLUMNS (recorded_at) (
  PARTITION p202609 VALUES LESS THAN ('2026-10-01'),
  PARTITION p202610 VALUES LESS THAN ('2026-11-01'),
  PARTITION p202611 VALUES LESS THAN ('2026-12-01'),
  PARTITION pmax    VALUES LESS THAN (MAXVALUE)
);
-- Retention: ALTER TABLE professional_location_log DROP PARTITION p202609;  (instant, no big DELETE)

-- =====================================================================
-- K. TRUST & SAFETY + CONSENT
-- =====================================================================
CREATE TABLE fraud_flag (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  entity_type  VARCHAR(15) NOT NULL,          -- USER / PRO / BOOKING / PAYMENT
  entity_id    VARCHAR(64) NOT NULL,
  rule_code    VARCHAR(40) NOT NULL,          -- MULTI_ACCOUNT_DEVICE, COUPON_ABUSE, OFF_PLATFORM_PAYMENT
  risk_score   INT         NOT NULL DEFAULT 0,
  status       VARCHAR(10) NOT NULL DEFAULT 'OPEN',   -- OPEN/CONFIRMED/DISMISSED
  details      JSON        NULL,
  created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY ix_fraud_entity (entity_type, entity_id),
  KEY ix_fraud_open (status, created_at)
) ENGINE=InnoDB;

CREATE TABLE blocked_entity (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  entity_type VARCHAR(10) NOT NULL,           -- PHONE / EMAIL / DEVICE / IP
  value_hash  CHAR(64)    NOT NULL,           -- SHA-256, never store raw value
  reason      VARCHAR(200) NULL,
  blocked_until TIMESTAMP NULL,
  created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY ux_blocked (entity_type, value_hash)
) ENGINE=InnoDB;

CREATE TABLE sos_alert (                     -- safety button during a job
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  booking_id  CHAR(36)     NOT NULL,
  raised_by   BIGINT       NOT NULL,
  latitude    DECIMAL(9,6) NULL,
  longitude   DECIMAL(9,6) NULL,
  status      VARCHAR(12)  NOT NULL DEFAULT 'OPEN',
  handled_by  BIGINT       NULL,
  created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  resolved_at TIMESTAMP    NULL,
  CONSTRAINT fk_sos_booking FOREIGN KEY (booking_id) REFERENCES booking(id)
) ENGINE=InnoDB;

CREATE TABLE user_consent (                  -- DPDP Act / GDPR evidence
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id      BIGINT       NOT NULL,
  purpose      VARCHAR(30)  NOT NULL,         -- TERMS, PRIVACY, MARKETING, LOCATION
  version      VARCHAR(10)  NOT NULL,
  granted      TINYINT(1)   NOT NULL,
  ip_address   VARCHAR(45)  NULL,
  created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY ix_consent_user (user_id, purpose, created_at),
  CONSTRAINT fk_consent_user FOREIGN KEY (user_id) REFERENCES jhi_user(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE data_deletion_request (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id      BIGINT      NOT NULL,
  status       VARCHAR(12) NOT NULL DEFAULT 'REQUESTED',  -- REQUESTED/IN_PROGRESS/DONE/REJECTED
  requested_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP   NULL,
  note         VARCHAR(255) NULL
) ENGINE=InnoDB;

-- =====================================================================
-- L. CONFIG, FEATURE FLAGS, BANNERS, I18N, SEARCH
-- =====================================================================
CREATE TABLE app_config (
  config_key   VARCHAR(80) PRIMARY KEY,
  config_value JSON        NOT NULL,
  description  VARCHAR(200) NULL,
  updated_by   VARCHAR(50) NULL,
  updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE feature_flag (
  flag_key         VARCHAR(60) PRIMARY KEY,
  is_enabled       TINYINT(1)  NOT NULL DEFAULT 0,
  rollout_percent  TINYINT     NOT NULL DEFAULT 0,      -- gradual rollout by user id hash
  city_ids         JSON        NULL,
  updated_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE banner (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR(120) NOT NULL,
  image_url   VARCHAR(255) NOT NULL,
  deep_link   VARCHAR(255) NULL,
  city_id     BIGINT       NULL,
  placement   VARCHAR(20)  NOT NULL DEFAULT 'HOME_TOP',
  sort_order  INT          NOT NULL DEFAULT 0,
  starts_at   TIMESTAMP    NOT NULL,
  ends_at     TIMESTAMP    NULL,
  is_active   TINYINT(1)   NOT NULL DEFAULT 1,
  CONSTRAINT fk_banner_city FOREIGN KEY (city_id) REFERENCES city(id)
) ENGINE=InnoDB;

CREATE TABLE service_translation (           -- Hindi / Marathi etc.
  service_id  VARCHAR(50)  NOT NULL,
  lang_key    VARCHAR(10)  NOT NULL,
  title       VARCHAR(120) NOT NULL,
  description VARCHAR(500) NULL,
  PRIMARY KEY (service_id, lang_key),
  CONSTRAINT fk_stran_service FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE search_keyword (                -- synonyms: "aircon", "a.c" -> hm-ac
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  keyword     VARCHAR(80) NOT NULL,
  service_id  VARCHAR(50) NOT NULL,
  weight      INT         NOT NULL DEFAULT 1,
  UNIQUE KEY ux_kw (keyword, service_id),
  CONSTRAINT fk_kw_service FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE CASCADE
) ENGINE=InnoDB;

ALTER TABLE service ADD FULLTEXT KEY ftx_service_search (title, description);

-- =====================================================================
-- M. ANALYTICS ROLLUPS (filled by nightly job)
-- =====================================================================
CREATE TABLE city_daily_metrics (
  metric_date      DATE   NOT NULL,
  city_id          BIGINT NOT NULL,
  category_id      BIGINT NOT NULL,
  bookings_created INT    NOT NULL DEFAULT 0,
  bookings_completed INT  NOT NULL DEFAULT 0,
  bookings_cancelled INT  NOT NULL DEFAULT 0,
  gmv              DECIMAL(14,2) NOT NULL DEFAULT 0,
  platform_revenue DECIMAL(14,2) NOT NULL DEFAULT 0,
  avg_rating       DECIMAL(3,2)  NULL,
  avg_assignment_sec INT  NULL,
  PRIMARY KEY (metric_date, city_id, category_id)
) ENGINE=InnoDB;

CREATE TABLE professional_daily_metrics (
  metric_date      DATE   NOT NULL,
  professional_id  BIGINT NOT NULL,
  jobs_offered     INT    NOT NULL DEFAULT 0,
  jobs_accepted    INT    NOT NULL DEFAULT 0,
  jobs_completed   INT    NOT NULL DEFAULT 0,
  jobs_cancelled   INT    NOT NULL DEFAULT 0,
  online_minutes   INT    NOT NULL DEFAULT 0,
  earnings         DECIMAL(12,2) NOT NULL DEFAULT 0,
  avg_rating       DECIMAL(3,2)  NULL,
  PRIMARY KEY (metric_date, professional_id)
) ENGINE=InnoDB;

-- =====================================================================
-- N. WEBHOOKS + IDEMPOTENCY
-- =====================================================================
CREATE TABLE payment_webhook_event (         -- store raw, process once
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  gateway       VARCHAR(20)  NOT NULL,
  event_id      VARCHAR(100) NOT NULL,
  event_type    VARCHAR(60)  NOT NULL,
  signature_ok  TINYINT(1)   NOT NULL,
  payload       JSON         NOT NULL,
  status        VARCHAR(10)  NOT NULL DEFAULT 'RECEIVED',   -- RECEIVED/PROCESSED/FAILED
  received_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  processed_at  TIMESTAMP    NULL,
  UNIQUE KEY ux_webhook_event (gateway, event_id)
) ENGINE=InnoDB;

CREATE TABLE api_idempotency_key (           -- Idempotency-Key header for POST /bookings etc.
  idem_key       VARCHAR(64)  NOT NULL,
  user_id        BIGINT       NOT NULL,
  request_hash   CHAR(64)     NOT NULL,
  response_code  INT          NULL,
  response_body  JSON         NULL,
  created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expires_at     TIMESTAMP    NOT NULL,
  PRIMARY KEY (user_id, idem_key),
  KEY ix_idem_exp (expires_at)
) ENGINE=InnoDB;

-- =====================================================================
-- ALTERs ON EXISTING TABLES (wire the new features in)
-- =====================================================================
ALTER TABLE service
  ADD COLUMN sac_code    VARCHAR(10)  NULL AFTER warranty_days,
  ADD COLUMN gst_percent DECIMAL(5,2) NOT NULL DEFAULT 18.00 AFTER sac_code;

ALTER TABLE professional
  ADD COLUMN tier_id            BIGINT       NULL,
  ADD COLUMN cash_in_hand       DECIMAL(12,2) NOT NULL DEFAULT 0,   -- cash collected, not yet deposited
  ADD COLUMN cash_limit         DECIMAL(12,2) NOT NULL DEFAULT 5000, -- block cash jobs above this
  ADD COLUMN max_daily_jobs     TINYINT      NOT NULL DEFAULT 8,
  ADD COLUMN languages          JSON         NULL,                  -- ["en","hi","mr"]
  ADD COLUMN bank_account_enc   VARBINARY(255) NULL,
  ADD COLUMN ifsc_code          VARCHAR(11)  NULL,
  ADD CONSTRAINT fk_pro_tier FOREIGN KEY (tier_id) REFERENCES professional_tier(id);

ALTER TABLE booking
  ADD COLUMN source               VARCHAR(10)  NOT NULL DEFAULT 'APP',   -- APP / WEB / ADMIN / SUBSCRIPTION
  ADD COLUMN subscription_id      BIGINT       NULL,
  ADD COLUMN slot_capacity_id     BIGINT       NULL,
  ADD COLUMN wallet_amount_used   DECIMAL(12,2) NOT NULL DEFAULT 0,
  ADD COLUMN loyalty_points_used  INT          NOT NULL DEFAULT 0,
  ADD COLUMN cancellation_fee     DECIMAL(12,2) NOT NULL DEFAULT 0,
  ADD COLUMN reschedule_count     TINYINT      NOT NULL DEFAULT 0,
  ADD COLUMN platform_commission  DECIMAL(12,2) NOT NULL DEFAULT 0,     -- frozen at completion
  ADD COLUMN pro_earning          DECIMAL(12,2) NOT NULL DEFAULT 0,
  ADD COLUMN arrival_eta          TIMESTAMP    NULL,
  ADD COLUMN is_priority          TINYINT(1)   NOT NULL DEFAULT 0,
  ADD CONSTRAINT fk_booking_sub  FOREIGN KEY (subscription_id)  REFERENCES booking_subscription(id),
  ADD CONSTRAINT fk_booking_slot FOREIGN KEY (slot_capacity_id) REFERENCES slot_capacity(id);

-- =====================================================================
-- O. STORED PROCEDURES, EVENTS, VIEWS
-- =====================================================================
DELIMITER $$

-- Atomically hold one seat in a slot while the customer pays.
CREATE PROCEDURE sp_hold_slot(
  IN  p_slot_capacity_id BIGINT,
  IN  p_user_id          BIGINT,
  IN  p_hold_minutes     INT,
  OUT p_hold_id          BIGINT)
BEGIN
  DECLARE v_cap INT; DECLARE v_booked INT; DECLARE v_blocked TINYINT; DECLARE v_held INT;
  DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

  START TRANSACTION;
  SELECT capacity, booked_count, is_blocked INTO v_cap, v_booked, v_blocked
    FROM slot_capacity WHERE id = p_slot_capacity_id FOR UPDATE;      -- serialises competing buyers

  SELECT COUNT(*) INTO v_held FROM slot_hold
   WHERE slot_capacity_id = p_slot_capacity_id AND status = 'HELD' AND expires_at > NOW();

  IF v_blocked = 1 OR v_booked + v_held >= v_cap THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'SLOT_FULL';
  END IF;

  INSERT INTO slot_hold (slot_capacity_id, user_id, expires_at)
  VALUES (p_slot_capacity_id, p_user_id, DATE_ADD(NOW(), INTERVAL p_hold_minutes MINUTE));
  SET p_hold_id = LAST_INSERT_ID();
  COMMIT;
END$$

-- Confirm a professional for a booking without double-booking them.
CREATE PROCEDURE sp_confirm_assignment(
  IN p_booking_id CHAR(36),
  IN p_pro_id     BIGINT)
BEGIN
  DECLARE v_start TIMESTAMP; DECLARE v_end TIMESTAMP;
  DECLARE v_status VARCHAR(20); DECLARE v_conflicts INT; DECLARE v_jobs_today INT; DECLARE v_max INT;
  DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;

  START TRANSACTION;
  SELECT max_daily_jobs INTO v_max FROM professional WHERE id = p_pro_id FOR UPDATE;  -- lock the pro row

  SELECT scheduled_start, scheduled_end, status INTO v_start, v_end, v_status
    FROM booking WHERE id = p_booking_id FOR UPDATE;

  IF v_status NOT IN ('REQUESTED','PENDING_ASSIGNMENT') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'BOOKING_NOT_ASSIGNABLE';
  END IF;

  SELECT COUNT(*) INTO v_conflicts FROM professional_time_off
   WHERE professional_id = p_pro_id AND starts_at < v_end AND ends_at > v_start;
  IF v_conflicts > 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'PRO_NOT_AVAILABLE';
  END IF;

  SELECT COUNT(*) INTO v_jobs_today FROM booking
   WHERE professional_id = p_pro_id AND DATE(scheduled_start) = DATE(v_start)
     AND status IN ('CONFIRMED','PRO_EN_ROUTE','PRO_ARRIVED','IN_PROGRESS');
  IF v_jobs_today >= v_max THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'PRO_DAILY_LIMIT';
  END IF;

  -- block the pro's calendar incl. 30 min travel buffer each side
  INSERT INTO professional_time_off (professional_id, starts_at, ends_at, reason, booking_id)
  VALUES (p_pro_id, DATE_SUB(v_start, INTERVAL 30 MINUTE), DATE_ADD(v_end, INTERVAL 30 MINUTE), 'BOOKING', p_booking_id);

  UPDATE booking SET professional_id = p_pro_id, status = 'CONFIRMED', version = version + 1
   WHERE id = p_booking_id;
  UPDATE booking_assignment SET status = 'ACCEPTED', responded_at = NOW()
   WHERE booking_id = p_booking_id AND professional_id = p_pro_id;
  UPDATE booking_assignment SET status = 'EXPIRED'
   WHERE booking_id = p_booking_id AND professional_id <> p_pro_id AND status = 'OFFERED';
  INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note)
  VALUES (p_booking_id, v_status, 'CONFIRMED', 'SYSTEM', CONCAT('Assigned professional ', p_pro_id));
  COMMIT;
END$$

-- Housekeeping: expire stale slot holds and unanswered offers every minute.
CREATE EVENT ev_expire_holds_and_offers
ON SCHEDULE EVERY 1 MINUTE DO
BEGIN
  UPDATE slot_hold SET status = 'EXPIRED' WHERE status = 'HELD' AND expires_at < NOW();
  UPDATE booking_assignment SET status = 'EXPIRED' WHERE status = 'OFFERED' AND expires_at < NOW();
END$$

CREATE EVENT ev_purge_expired_keys
ON SCHEDULE EVERY 1 HOUR DO
  DELETE FROM api_idempotency_key WHERE expires_at < NOW()$$

DELIMITER ;

-- Available professionals for a service: use from dispatch (add zone/time filters in the query).
CREATE OR REPLACE VIEW v_dispatch_candidates AS
SELECT p.id AS professional_id, p.display_name, p.home_city_id, p.avg_rating,
       p.cancellation_rate, p.last_lat, p.last_lng, p.last_location_at,
       COALESCE(t.dispatch_priority, 0) AS tier_priority,
       ps.service_id, pz.zone_id
FROM professional p
JOIN professional_skill ps ON ps.professional_id = p.id
JOIN professional_zone  pz ON pz.professional_id = p.id
LEFT JOIN professional_tier t ON t.id = p.tier_id
WHERE p.onboarding_status = 'ACTIVE' AND p.is_online = 1 AND p.deleted_at IS NULL
  AND p.cash_in_hand < p.cash_limit;

CREATE OR REPLACE VIEW v_professional_scorecard AS
SELECT p.id AS professional_id, p.display_name,
       COUNT(DISTINCT b.id)                                        AS total_jobs,
       SUM(b.status = 'COMPLETED')                                 AS completed_jobs,
       SUM(b.status = 'CANCELLED' AND b.cancelled_by = 'PRO')      AS pro_cancellations,
       ROUND(AVG(r.rating), 2)                                     AS avg_rating,
       COALESCE(SUM(b.pro_earning), 0)                             AS lifetime_earning
FROM professional p
LEFT JOIN booking b ON b.professional_id = p.id
LEFT JOIN review  r ON r.booking_id = b.id
GROUP BY p.id, p.display_name;

-- =====================================================================
-- SEED: sensible defaults
-- =====================================================================
INSERT INTO cancellation_policy (hours_before_start, fee_percent, min_fee, applies_to) VALUES
 (24,  0.00,  0, 'CUSTOMER'),
 (6,  10.00, 49, 'CUSTOMER'),
 (2,  25.00, 99, 'CUSTOMER'),
 (0,  50.00, 99, 'CUSTOMER');

INSERT INTO app_config (config_key, config_value, description) VALUES
 ('booking.hold_minutes',        JSON_OBJECT('value', 10),   'Slot hold time during checkout'),
 ('dispatch.offer_ttl_seconds',  JSON_OBJECT('value', 45),   'Time a pro has to accept an offer'),
 ('dispatch.max_offers',         JSON_OBJECT('value', 5),    'Pros offered before escalating to ops'),
 ('pro.travel_buffer_minutes',   JSON_OBJECT('value', 30),   'Buffer between jobs'),
 ('wallet.cashback_expiry_days', JSON_OBJECT('value', 90),   'Promo cash validity');

INSERT INTO feature_flag (flag_key, is_enabled, rollout_percent) VALUES
 ('recurring_bookings', 1, 100), ('quote_flow', 1, 50), ('loyalty_points', 0, 0);
