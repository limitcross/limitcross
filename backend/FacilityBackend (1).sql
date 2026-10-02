-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: mysql:3306
-- Generation Time: Oct 01, 2026 at 12:59 PM
-- Server version: 8.4.11
-- PHP Version: 8.3.35

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `FacilityBackend`
--

-- --------------------------------------------------------

--
-- Table structure for table `app_config`
--

CREATE TABLE `app_config` (
  `config_key` varchar(80) NOT NULL,
  `config_value` json NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `banner`
--

CREATE TABLE `banner` (
  `id` bigint NOT NULL,
  `title` varchar(120) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `deep_link` varchar(255) DEFAULT NULL,
  `city_id` bigint DEFAULT NULL,
  `placement` varchar(20) NOT NULL DEFAULT 'HOME_TOP',
  `sort_order` int NOT NULL DEFAULT '0',
  `starts_at` timestamp NOT NULL,
  `ends_at` timestamp NULL DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blocked_entity`
--

CREATE TABLE `blocked_entity` (
  `id` bigint NOT NULL,
  `entity_type` varchar(10) NOT NULL,
  `value_hash` char(64) NOT NULL,
  `reason` varchar(200) DEFAULT NULL,
  `blocked_until` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `id` varchar(36) NOT NULL,
  `owner_login` varchar(100) NOT NULL,
  `service_id` varchar(50) NOT NULL,
  `service_title` varchar(120) NOT NULL,
  `emoji` varchar(10) NOT NULL,
  `price` int NOT NULL,
  `service_fee` int NOT NULL DEFAULT '49',
  `date` date NOT NULL,
  `time_slot` varchar(60) NOT NULL,
  `address` varchar(500) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'REQUESTED',
  `assigned_pro_name` varchar(120) NOT NULL DEFAULT 'Not assigned yet',
  `pro_rating` decimal(3,2) NOT NULL DEFAULT '0.00',
  `updated_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `booking_quote`
--

CREATE TABLE `booking_quote` (
  `id` bigint NOT NULL,
  `booking_id` char(36) NOT NULL,
  `professional_id` bigint NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'PROPOSED',
  `total_amount` decimal(12,2) NOT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `valid_until` timestamp NULL DEFAULT NULL,
  `responded_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `booking_quote_item`
--

CREATE TABLE `booking_quote_item` (
  `id` bigint NOT NULL,
  `quote_id` bigint NOT NULL,
  `item_type` varchar(10) NOT NULL,
  `description` varchar(200) NOT NULL,
  `unit_price` decimal(12,2) NOT NULL,
  `quantity` decimal(8,2) NOT NULL DEFAULT '1.00',
  `line_total` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `booking_reschedule`
--

CREATE TABLE `booking_reschedule` (
  `id` bigint NOT NULL,
  `booking_id` char(36) NOT NULL,
  `old_start` timestamp NOT NULL,
  `old_end` timestamp NOT NULL,
  `new_start` timestamp NOT NULL,
  `new_end` timestamp NOT NULL,
  `requested_by` varchar(10) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `fee_charged` decimal(12,2) NOT NULL DEFAULT '0.00',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `booking_subscription`
--

CREATE TABLE `booking_subscription` (
  `id` bigint NOT NULL,
  `customer_id` bigint NOT NULL,
  `service_id` varchar(50) NOT NULL,
  `package_id` bigint NOT NULL,
  `address_id` bigint NOT NULL,
  `preferred_professional_id` bigint DEFAULT NULL,
  `frequency` varchar(10) NOT NULL,
  `days_of_week` json DEFAULT NULL,
  `preferred_time` time NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date DEFAULT NULL,
  `next_run_date` date NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'ACTIVE',
  `auto_pay` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `call_session`
--

CREATE TABLE `call_session` (
  `id` bigint NOT NULL,
  `booking_id` char(36) NOT NULL,
  `virtual_number` varchar(20) NOT NULL,
  `caller_id` bigint NOT NULL,
  `callee_id` bigint NOT NULL,
  `provider_call_id` varchar(80) DEFAULT NULL,
  `duration_sec` int NOT NULL DEFAULT '0',
  `status` varchar(12) NOT NULL DEFAULT 'INITIATED',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `cancellation_policy`
--

CREATE TABLE `cancellation_policy` (
  `id` bigint NOT NULL,
  `category_id` bigint DEFAULT NULL,
  `service_id` varchar(50) DEFAULT NULL,
  `hours_before_start` int NOT NULL,
  `fee_percent` decimal(5,2) NOT NULL,
  `min_fee` decimal(12,2) NOT NULL DEFAULT '0.00',
  `applies_to` varchar(10) NOT NULL DEFAULT 'CUSTOMER',
  `is_active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chat_message`
--

CREATE TABLE `chat_message` (
  `id` bigint NOT NULL,
  `thread_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  `body` varchar(2000) DEFAULT NULL,
  `media_url` varchar(255) DEFAULT NULL,
  `is_flagged` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `read_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chat_thread`
--

CREATE TABLE `chat_thread` (
  `id` bigint NOT NULL,
  `booking_id` char(36) NOT NULL,
  `customer_id` bigint NOT NULL,
  `professional_id` bigint NOT NULL,
  `status` varchar(8) NOT NULL DEFAULT 'OPEN',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `commission_rule`
--

CREATE TABLE `commission_rule` (
  `id` bigint NOT NULL,
  `service_id` varchar(50) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `city_id` bigint DEFAULT NULL,
  `tier_id` bigint DEFAULT NULL,
  `commission_percent` decimal(5,2) NOT NULL,
  `flat_fee` decimal(12,2) NOT NULL DEFAULT '0.00',
  `valid_from` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `valid_to` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `customer_profile`
--

CREATE TABLE `customer_profile` (
  `uid` varchar(128) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(254) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL,
  `last_login_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `customer_wallet`
--

CREATE TABLE `customer_wallet` (
  `user_id` bigint NOT NULL,
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint NOT NULL DEFAULT '0'
) ;

-- --------------------------------------------------------

--
-- Table structure for table `customer_wallet_txn`
--

CREATE TABLE `customer_wallet_txn` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `booking_id` char(36) DEFAULT NULL,
  `txn_type` varchar(15) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `balance_after` decimal(12,2) NOT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `note` varchar(200) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `DATABASECHANGELOG`
--

CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `DATABASECHANGELOG`
--

INSERT INTO `DATABASECHANGELOG` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES
('00000000000000', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', '2026-10-01 07:51:40', 1, 'EXECUTED', '9:b6b4a3e0d2a6d7f1e5139675af65d7b0', 'createSequence sequenceName=sequence_generator', '', NULL, '4.29.2', NULL, NULL, '0841100168'),
('00000000000001', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', '2026-10-01 07:51:43', 2, 'EXECUTED', '9:4e734400486c054d8030ceca7233c056', 'createTable tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableName=jhi_user_authority; addForeignKeyConstraint baseTableName=jhi_user_authority, constraintName=fk_authority_name, ...', '', NULL, '4.29.2', NULL, NULL, '0841100168'),
('00000000000003', 'jhipster', 'config/liquibase/changelog/00000000000003_facility_schema.xml', '2026-10-01 07:51:43', 3, 'EXECUTED', '9:c1962bc9292aca5bb48afc99c93cba78', 'addColumn tableName=jhi_user; createTable tableName=facility_service; createTable tableName=booking; insert tableName=facility_service; insert tableName=facility_service; insert tableName=facility_service; insert tableName=facility_service; insert...', '', NULL, '4.29.2', NULL, NULL, '0841100168'),
('00000000000004', 'jhipster', 'config/liquibase/changelog/00000000000004_mysql_identity.xml', '2026-10-01 07:55:03', 4, 'EXECUTED', '9:c5ea04329a965c1c92058e2634886ece', 'sql', '', NULL, '4.29.2', NULL, NULL, '0841302817'),
('00000000000005', 'limitcross', 'config/liquibase/changelog/00000000000005_customer_profile_credentials.xml', '2026-10-01 07:55:03', 5, 'EXECUTED', '9:db2db8da85e0424e7bb8599a56af6ff3', 'createTable tableName=customer_profile; createTable tableName=saved_app_credential; addUniqueConstraint constraintName=uk_saved_credential_owner_app, tableName=saved_app_credential', '', NULL, '4.29.2', NULL, NULL, '0841302817'),
('00000000000006', 'limitcross', 'config/liquibase/changelog/00000000000006_service_catalog.xml', '2026-10-01 07:55:04', 6, 'EXECUTED', '9:49a391bbfff0367e51a2f8f0c5337f48', 'delete tableName=facility_service; loadUpdateData tableName=facility_service', '', NULL, '4.29.2', NULL, NULL, '0841302817'),
('00000000000008', 'limitcross', 'config/liquibase/changelog/00000000000008_mysql_user_authority_fk.xml', '2026-10-01 07:55:05', 7, 'EXECUTED', '9:bc19851cbd533c921b57a375b0241a92', 'addForeignKeyConstraint baseTableName=jhi_user_authority, constraintName=fk_user_id, referencedTableName=jhi_user', '', NULL, '4.29.2', NULL, NULL, '0841302817');

-- --------------------------------------------------------

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int NOT NULL,
  `LOCKED` tinyint NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

INSERT INTO `DATABASECHANGELOGLOCK` (`ID`, `LOCKED`, `LOCKGRANTED`, `LOCKEDBY`) VALUES
(1, 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `data_deletion_request`
--

CREATE TABLE `data_deletion_request` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `status` varchar(12) NOT NULL DEFAULT 'REQUESTED',
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `completed_at` timestamp NULL DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `facility_service`
--

CREATE TABLE `facility_service` (
  `id` varchar(50) NOT NULL,
  `title` varchar(120) NOT NULL,
  `emoji` varchar(10) NOT NULL,
  `category` varchar(40) NOT NULL,
  `description` varchar(500) NOT NULL,
  `price_range` varchar(60) NOT NULL,
  `starting_price` int NOT NULL,
  `rating` decimal(3,2) NOT NULL,
  `reviews_count` int NOT NULL,
  `duration` varchar(40) NOT NULL,
  `highlights` varchar(1000) NOT NULL,
  `is_popular` tinyint NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `facility_service`
--

INSERT INTO `facility_service` (`id`, `title`, `emoji`, `category`, `description`, `price_range`, `starting_price`, `rating`, `reviews_count`, `duration`, `highlights`, `is_popular`) VALUES
('bw-hair', 'Haircut & Style', '💈', 'beautyWomen', 'Cut, colour, keratin, smoothening', '₹400 – ₹3,000', 400, 4.85, 1950, '45-90 mins', 'Hair consultation|L’Oréal / Matrix products|Blow dry included', 0),
('bw-massage', 'Massage Therapy', '💆', 'beautyWomen', 'Swedish, deep tissue, aromatherapy', '₹800 – ₹2,500', 800, 4.91, 3600, '60-90 mins', 'Certified female therapists|Premium aromatic oils|Massage bed setup', 0),
('bw-mehendi', 'Mehendi', '🌿', 'beautyWomen', 'Bridal, party, festival designs', '₹500 – ₹3,000', 500, 4.89, 1100, '60-180 mins', '100% natural organic henna|Dark stain guarantee|Custom bridal styles', 0),
('bw-pedicure', 'Pedicure & Manicure', '🧴', 'beautyWomen', 'Classic, gel, nail art', '₹400 – ₹1,800', 400, 4.86, 2800, '45-75 mins', 'Sterilized tools|Sea salt scrub & massage|Premium gel polishes', 0),
('bw-salon', 'Salon at Home', '💅', 'beautyWomen', 'Waxing, threading, facial, cleanup', '₹200 – ₹2,000', 200, 4.92, 7800, '45-120 mins', 'Single-use disposable kits|Top branded products|Clean up after service', 1),
('bw-skincare', 'Skincare & Facial', '🧖', 'beautyWomen', 'Anti-aging, hydrating, cleanup facial', '₹800 – ₹3,000', 800, 4.87, 2400, '60-75 mins', 'Skin-type specific serums|Gold & pearl options|Facial massage', 0),
('cl-bath', 'Bathroom Cleaning', '🛁', 'cleaning', 'Tiles, commode, fixtures deep clean', '₹600 – ₹1,500', 600, 4.85, 4100, '45-60 mins', 'Hard water stain removal|Disinfection included|Shining fixtures', 0),
('cl-car', 'Car Cleaning', '🚗', 'cleaning', 'Interior, exterior, foam wash, detailing', '₹500 – ₹3,000', 500, 4.84, 2890, '45-90 mins', 'High-pressure waterless/foam wash|Interior vacuuming|Tire shine', 0),
('cl-deep', 'Home Deep Cleaning', '🏡', 'cleaning', 'Full home, kitchen, bathroom deep clean', '₹2,000 – ₹8,000', 2000, 4.90, 5200, '3-5 hours', 'Industrial grade machines|Eco-safe chemicals|3-5 member crew', 1),
('cl-pest', 'Pest Control', '🐛', 'cleaning', 'Cockroach, termite, bed bug, rat', '₹800 – ₹4,000', 800, 4.78, 2300, '45-90 mins', 'Odorless gel & spray|Govt approved formulas|Warranty re-visit', 0),
('cl-sofa', 'Sofa & Carpet Clean', '🪑', 'cleaning', 'Dry clean, stain removal, steam clean', '₹800 – ₹3,000', 800, 4.83, 1850, '60-90 mins', 'Hot water extraction|Fabric safe treatment|Drying in 2-3 hrs', 0),
('cl-tank', 'Water Tank Cleaning', '💧', 'cleaning', 'Underground and overhead tank cleaning', '₹800 – ₹2,500', 800, 4.81, 940, '60-120 mins', '6-stage cleaning process|UV antibacterial treatment|Sludge pump out', 0),
('ha-doctor', 'Doctor Visit', '🩺', 'healthAtHome', 'General physician at home', '₹500 – ₹1,500', 500, 4.88, 1820, '30-45 mins', 'MBBS verified physicians|Digital prescription generated|Basic vitals & ECG at home', 0),
('ha-lab', 'Lab Tests at Home', '💉', 'healthAtHome', 'Blood test, urine, ECG at home', '₹300 – ₹3,000', 300, 4.90, 4200, '15-20 mins sample collection', 'NABL accredited lab partners|Digital reports in 12-24 hrs|Smart sample cooling kit', 1),
('ha-physio', 'Physiotherapy', '🦵', 'healthAtHome', 'Joint pain, injury, post-surgery', '₹800 – ₹2,000/session', 800, 4.91, 1450, '45-60 mins', 'BPT/MPT qualified doctors|Rehab exercise equipment|Custom recovery plan', 0),
('ha-trainer', 'Personal Trainer', '🏋️', 'healthAtHome', 'Weight loss, muscle, yoga', '₹5,000 – ₹15,000/mo', 5000, 4.87, 980, '12-24 sessions/mo', 'Certified fitness coach|Nutrition & diet chart|Progress body tracking', 0),
('hh-cook', 'Cook at Home', '🍳', 'homeHelp', 'Daily cook, party cook, monthly plans', '₹8,000 – ₹20,000/mo', 8000, 4.83, 1200, 'Custom Schedule', 'Cuisine specialists (North/South/Continental)|Hygiene background verified|Free replacement guarantee', 1),
('hh-elder', 'Elder Care', '👴', 'homeHelp', 'Caregiver, nurse, companion', '₹15,000 – ₹40,000/mo', 15000, 4.92, 650, '12hr / 24hr Live-in', 'Medical & mobility assistance|Vitals monitoring|Compassionate trained staff', 0),
('hh-maid', 'Maid / Home Help', '🧹', 'homeHelp', 'Daily cleaning, utensils, mopping', '₹6,000 – ₹12,000/mo', 6000, 4.79, 2900, 'Daily visits', 'Verified attendance tracking|Trained housekeeping standards|Substitute available on leaves', 0),
('hh-nanny', 'Babysitter / Nanny', '👶', 'homeHelp', 'Daily, hourly, overnight care', '₹10,000 – ₹25,000/mo', 10000, 4.89, 880, 'Hourly / Monthly', 'Child safety certified|Police verified|Infant & toddler trained', 0),
('hm-ac', 'AC Service & Repair', '❄️', 'homeMaintenance', 'Service, gas refill, deep clean, installation', '₹400 – ₹2,500', 400, 4.86, 4520, '45-60 mins', 'Power jet foam clean|Gas leak detection|30-day warranty', 1),
('hm-appliance', 'Appliance Repair', '📺', 'homeMaintenance', 'TV, washing machine, fridge, microwave', '₹400 – ₹2,000', 400, 4.82, 2750, '60-90 mins', 'Multi-brand specialists|90-day parts warranty|Digital quote', 0),
('hm-carpenter', 'Carpentry', '🪚', 'homeMaintenance', 'Furniture repair, door fix, installation', '₹400 – ₹2,000', 400, 4.79, 1940, '45-90 mins', 'Precision tools|Custom fitting|Wood care consultation', 0),
('hm-electric', 'Electrician', '⚡', 'homeMaintenance', 'Wiring, fan, switch, MCB repair', '₹300 – ₹1,500', 300, 4.84, 3890, '30-60 mins', 'Background verified pros|High grade equipment|Post-service test', 1),
('hm-paint', 'Painting & Wall Décor', '🎨', 'homeMaintenance', 'Interior, exterior, texture, waterproofing', '₹5,000 – ₹50,000', 5000, 4.88, 820, '1-3 days', 'Laser measurement site visit|Low-VOC paints|Post-paint clean', 0),
('hm-plumb', 'Plumbing', '🔧', 'homeMaintenance', 'Leaks, tap repair, pipe fitting, blockage', '₹300 – ₹1,500', 300, 4.81, 3100, '30-60 mins', 'Certified plumbers|Standardized rate card|Genuine spare parts', 0),
('mg-facial', 'Men\'s Facial', '🧴', 'mensGrooming', 'Cleanup, de-tan, acne care', '₹400 – ₹1,500', 400, 4.81, 1650, '40-60 mins', 'Charcoal de-tan pack|Blackhead extraction|Hydrating cooling mask', 0),
('mg-haircut', 'Haircut at Home', '✂️', 'mensGrooming', 'Cut, beard trim, styling', '₹200 – ₹600', 200, 4.88, 4900, '30-45 mins', 'Single-use cape|Disinfected trimmers|Post-cut head massage', 1),
('mg-massage', 'Men\'s Massage', '💆‍♂️', 'mensGrooming', 'Body massage, head massage', '₹700 – ₹2,000', 700, 4.87, 2100, '45-90 mins', 'Deep tissue stress relief|Herbal pain oils|Expert male masseurs', 0),
('mg-shave', 'Shave & Beard Grooming', '🪒', 'mensGrooming', 'Shave, beard shape, cleanup', '₹200 – ₹500', 200, 4.84, 3200, '20-35 mins', 'Hot towel treatment|Fresh safety blades|Aftershave balm', 0),
('np-lock', 'Smart Door Lock', '🔐', 'nativeProducts', 'Electronic door locks, smart home', '₹5,000 – ₹15,000', 5000, 4.89, 1350, 'Free 90 min installation', 'Fingerprint, PIN, RFID & App unlock|Emergency physical key|Built-in tamper alarm', 0),
('np-ro', 'Water Purifier (RO)', '💧', 'nativeProducts', 'Native branded RO, IoT enabled', '₹8,000 – ₹15,000', 8000, 4.93, 2100, 'Free 2hr installation', 'Needs zero service for 2 years|Smart app filter health check|10-stage RO+UV+Copper alkaline', 1);

-- --------------------------------------------------------

--
-- Table structure for table `feature_flag`
--

CREATE TABLE `feature_flag` (
  `flag_key` varchar(60) NOT NULL,
  `is_enabled` tinyint(1) NOT NULL DEFAULT '0',
  `rollout_percent` tinyint NOT NULL DEFAULT '0',
  `city_ids` json DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `fraud_flag`
--

CREATE TABLE `fraud_flag` (
  `id` bigint NOT NULL,
  `entity_type` varchar(15) NOT NULL,
  `entity_id` varchar(64) NOT NULL,
  `rule_code` varchar(40) NOT NULL,
  `risk_score` int NOT NULL DEFAULT '0',
  `status` varchar(10) NOT NULL DEFAULT 'OPEN',
  `details` json DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `incentive_rule`
--

CREATE TABLE `incentive_rule` (
  `id` bigint NOT NULL,
  `name` varchar(120) NOT NULL,
  `city_id` bigint DEFAULT NULL,
  `metric` varchar(20) NOT NULL,
  `threshold` decimal(10,2) NOT NULL,
  `reward_amount` decimal(12,2) NOT NULL,
  `period` varchar(10) NOT NULL DEFAULT 'WEEKLY',
  `is_active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE `invoice` (
  `id` bigint NOT NULL,
  `invoice_no` varchar(30) NOT NULL,
  `invoice_type` varchar(12) NOT NULL DEFAULT 'TAX_INVOICE',
  `booking_id` char(36) NOT NULL,
  `customer_id` bigint NOT NULL,
  `customer_gstin` varchar(15) DEFAULT NULL,
  `seller_gstin` varchar(15) NOT NULL,
  `place_of_supply` varchar(40) NOT NULL,
  `taxable_amount` decimal(12,2) NOT NULL,
  `cgst` decimal(12,2) NOT NULL DEFAULT '0.00',
  `sgst` decimal(12,2) NOT NULL DEFAULT '0.00',
  `igst` decimal(12,2) NOT NULL DEFAULT '0.00',
  `total_amount` decimal(12,2) NOT NULL,
  `pdf_url` varchar(255) DEFAULT NULL,
  `issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `invoice_line`
--

CREATE TABLE `invoice_line` (
  `id` bigint NOT NULL,
  `invoice_id` bigint NOT NULL,
  `description` varchar(200) NOT NULL,
  `sac_code` varchar(10) DEFAULT NULL,
  `quantity` decimal(8,2) NOT NULL DEFAULT '1.00',
  `unit_price` decimal(12,2) NOT NULL,
  `gst_percent` decimal(5,2) NOT NULL,
  `line_total` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `invoice_sequence`
--

CREATE TABLE `invoice_sequence` (
  `series` varchar(10) NOT NULL,
  `fiscal_year` varchar(9) NOT NULL,
  `last_number` bigint NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `jhi_authority`
--

CREATE TABLE `jhi_authority` (
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jhi_authority`
--

INSERT INTO `jhi_authority` (`name`) VALUES
('ROLE_ADMIN'),
('ROLE_USER');

-- --------------------------------------------------------

--
-- Table structure for table `jhi_user`
--

CREATE TABLE `jhi_user` (
  `id` bigint NOT NULL,
  `login` varchar(50) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(191) DEFAULT NULL,
  `image_url` varchar(256) DEFAULT NULL,
  `activated` tinyint NOT NULL,
  `lang_key` varchar(10) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jhi_user`
--

INSERT INTO `jhi_user` (`id`, `login`, `password_hash`, `first_name`, `last_name`, `email`, `image_url`, `activated`, `lang_key`, `activation_key`, `reset_key`, `created_by`, `created_date`, `reset_date`, `last_modified_by`, `last_modified_date`, `phone`) VALUES
(1, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'Administrator', 'Administrator', 'admin@localhost', '', 1, 'en', NULL, NULL, 'system', NULL, NULL, 'system', NULL, NULL),
(2, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User', 'User', 'user@localhost', '', 1, 'en', NULL, NULL, 'system', NULL, NULL, 'system', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `jhi_user_authority`
--

CREATE TABLE `jhi_user_authority` (
  `user_id` bigint NOT NULL,
  `authority_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jhi_user_authority`
--

INSERT INTO `jhi_user_authority` (`user_id`, `authority_name`) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Table structure for table `loyalty_ledger`
--

CREATE TABLE `loyalty_ledger` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `booking_id` char(36) DEFAULT NULL,
  `points` int NOT NULL,
  `reason` varchar(30) NOT NULL,
  `expires_at` date DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `professional_incentive_award`
--

CREATE TABLE `professional_incentive_award` (
  `id` bigint NOT NULL,
  `professional_id` bigint NOT NULL,
  `rule_id` bigint NOT NULL,
  `period_start` date NOT NULL,
  `period_end` date NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `wallet_txn_id` bigint DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `professional_location_log`
--

CREATE TABLE `professional_location_log` (
  `id` bigint NOT NULL,
  `professional_id` bigint NOT NULL,
  `booking_id` char(36) DEFAULT NULL,
  `latitude` decimal(9,6) NOT NULL,
  `longitude` decimal(9,6) NOT NULL,
  `speed_kmph` decimal(5,1) DEFAULT NULL,
  `battery_pct` tinyint DEFAULT NULL,
  `recorded_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
PARTITION BY RANGE COLUMNS(recorded_at)
(
PARTITION p202609 VALUES LESS THAN ('2026-10-01') ENGINE=InnoDB,
PARTITION p202610 VALUES LESS THAN ('2026-11-01') ENGINE=InnoDB,
PARTITION p202611 VALUES LESS THAN ('2026-12-01') ENGINE=InnoDB,
PARTITION pmax VALUES LESS THAN (MAXVALUE) ENGINE=InnoDB
);

-- --------------------------------------------------------

--
-- Table structure for table `professional_tier`
--

CREATE TABLE `professional_tier` (
  `id` bigint NOT NULL,
  `code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `min_rating` decimal(3,2) NOT NULL DEFAULT '0.00',
  `min_jobs` int NOT NULL DEFAULT '0',
  `commission_percent` decimal(5,2) NOT NULL,
  `dispatch_priority` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `professional_tier`
--

INSERT INTO `professional_tier` (`id`, `code`, `name`, `min_rating`, `min_jobs`, `commission_percent`, `dispatch_priority`) VALUES
(1, 'BRONZE', 'Bronze', 0.00, 0, 25.00, 0),
(2, 'SILVER', 'Silver', 4.50, 50, 22.00, 1),
(3, 'GOLD', 'Gold', 4.70, 200, 20.00, 2),
(4, 'PLATINUM', 'Platinum', 4.85, 500, 18.00, 3);

-- --------------------------------------------------------

--
-- Table structure for table `professional_tier_history`
--

CREATE TABLE `professional_tier_history` (
  `id` bigint NOT NULL,
  `professional_id` bigint NOT NULL,
  `tier_id` bigint NOT NULL,
  `effective_from` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reason` varchar(120) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `professional_training`
--

CREATE TABLE `professional_training` (
  `professional_id` bigint NOT NULL,
  `module_id` bigint NOT NULL,
  `score` int DEFAULT NULL,
  `status` varchar(12) NOT NULL DEFAULT 'ASSIGNED',
  `completed_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `referral_reward`
--

CREATE TABLE `referral_reward` (
  `id` bigint NOT NULL,
  `referrer_id` bigint NOT NULL,
  `referee_id` bigint NOT NULL,
  `trigger_booking_id` char(36) DEFAULT NULL,
  `reward_amount` decimal(12,2) NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'PENDING',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `saved_app_credential`
--

CREATE TABLE `saved_app_credential` (
  `id` varchar(36) NOT NULL,
  `owner_uid` varchar(128) NOT NULL,
  `app_name` varchar(120) NOT NULL,
  `email` varchar(254) NOT NULL,
  `app_password_ciphertext` varchar(4096) NOT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `search_keyword`
--

CREATE TABLE `search_keyword` (
  `id` bigint NOT NULL,
  `keyword` varchar(80) NOT NULL,
  `service_id` varchar(50) NOT NULL,
  `weight` int NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `service_translation`
--

CREATE TABLE `service_translation` (
  `service_id` varchar(50) NOT NULL,
  `lang_key` varchar(10) NOT NULL,
  `title` varchar(120) NOT NULL,
  `description` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `slot_capacity`
--

CREATE TABLE `slot_capacity` (
  `id` bigint NOT NULL,
  `zone_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  `slot_start` datetime NOT NULL,
  `slot_end` datetime NOT NULL,
  `capacity` int NOT NULL,
  `booked_count` int NOT NULL DEFAULT '0',
  `is_blocked` tinyint(1) NOT NULL DEFAULT '0',
  `price_multiplier` decimal(4,2) NOT NULL DEFAULT '1.00',
  `version` bigint NOT NULL DEFAULT '0'
) ;

-- --------------------------------------------------------

--
-- Table structure for table `slot_hold`
--

CREATE TABLE `slot_hold` (
  `id` bigint NOT NULL,
  `slot_capacity_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'HELD',
  `booking_id` char(36) DEFAULT NULL,
  `expires_at` timestamp NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sos_alert`
--

CREATE TABLE `sos_alert` (
  `id` bigint NOT NULL,
  `booking_id` char(36) NOT NULL,
  `raised_by` bigint NOT NULL,
  `latitude` decimal(9,6) DEFAULT NULL,
  `longitude` decimal(9,6) DEFAULT NULL,
  `status` varchar(12) NOT NULL DEFAULT 'OPEN',
  `handled_by` bigint DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resolved_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `training_module`
--

CREATE TABLE `training_module` (
  `id` bigint NOT NULL,
  `service_id` varchar(50) DEFAULT NULL,
  `title` varchar(150) NOT NULL,
  `content_url` varchar(255) DEFAULT NULL,
  `is_mandatory` tinyint(1) NOT NULL DEFAULT '1',
  `pass_score` int NOT NULL DEFAULT '70'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_consent`
--

CREATE TABLE `user_consent` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `purpose` varchar(30) NOT NULL,
  `version` varchar(10) NOT NULL,
  `granted` tinyint(1) NOT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `warranty_claim`
--

CREATE TABLE `warranty_claim` (
  `id` bigint NOT NULL,
  `booking_id` char(36) NOT NULL,
  `customer_id` bigint NOT NULL,
  `issue` varchar(1000) NOT NULL,
  `status` varchar(12) NOT NULL DEFAULT 'RAISED',
  `redo_booking_id` char(36) DEFAULT NULL,
  `within_warranty` tinyint(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resolved_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `app_config`
--
ALTER TABLE `app_config`
  ADD PRIMARY KEY (`config_key`);

--
-- Indexes for table `banner`
--
ALTER TABLE `banner`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_banner_city` (`city_id`);

--
-- Indexes for table `blocked_entity`
--
ALTER TABLE `blocked_entity`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_blocked` (`entity_type`,`value_hash`);

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `booking_quote`
--
ALTER TABLE `booking_quote`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_quote_booking` (`booking_id`,`status`),
  ADD KEY `fk_quote_pro` (`professional_id`);

--
-- Indexes for table `booking_quote_item`
--
ALTER TABLE `booking_quote_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_qitem_quote` (`quote_id`);

--
-- Indexes for table `booking_reschedule`
--
ALTER TABLE `booking_reschedule`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_resched_booking` (`booking_id`);

--
-- Indexes for table `booking_subscription`
--
ALTER TABLE `booking_subscription`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_sub_run` (`status`,`next_run_date`),
  ADD KEY `fk_sub_customer` (`customer_id`),
  ADD KEY `fk_sub_service` (`service_id`),
  ADD KEY `fk_sub_package` (`package_id`),
  ADD KEY `fk_sub_address` (`address_id`),
  ADD KEY `fk_sub_pro` (`preferred_professional_id`);

--
-- Indexes for table `call_session`
--
ALTER TABLE `call_session`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_call_booking` (`booking_id`);

--
-- Indexes for table `cancellation_policy`
--
ALTER TABLE `cancellation_policy`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_cpol_category` (`category_id`),
  ADD KEY `fk_cpol_service` (`service_id`);

--
-- Indexes for table `chat_message`
--
ALTER TABLE `chat_message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_msg_thread` (`thread_id`,`id`);

--
-- Indexes for table `chat_thread`
--
ALTER TABLE `chat_thread`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_thread_booking` (`booking_id`);

--
-- Indexes for table `commission_rule`
--
ALTER TABLE `commission_rule`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_comm_lookup` (`service_id`,`category_id`,`city_id`,`tier_id`),
  ADD KEY `fk_comm_category` (`category_id`),
  ADD KEY `fk_comm_city` (`city_id`),
  ADD KEY `fk_comm_tier` (`tier_id`);

--
-- Indexes for table `customer_profile`
--
ALTER TABLE `customer_profile`
  ADD PRIMARY KEY (`uid`);

--
-- Indexes for table `customer_wallet`
--
ALTER TABLE `customer_wallet`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `customer_wallet_txn`
--
ALTER TABLE `customer_wallet_txn`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_cwtxn_user` (`user_id`,`created_at`);

--
-- Indexes for table `DATABASECHANGELOGLOCK`
--
ALTER TABLE `DATABASECHANGELOGLOCK`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `data_deletion_request`
--
ALTER TABLE `data_deletion_request`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `facility_service`
--
ALTER TABLE `facility_service`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `feature_flag`
--
ALTER TABLE `feature_flag`
  ADD PRIMARY KEY (`flag_key`);

--
-- Indexes for table `fraud_flag`
--
ALTER TABLE `fraud_flag`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_fraud_entity` (`entity_type`,`entity_id`),
  ADD KEY `ix_fraud_open` (`status`,`created_at`);

--
-- Indexes for table `incentive_rule`
--
ALTER TABLE `incentive_rule`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_incentive_city` (`city_id`);

--
-- Indexes for table `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_invoice_no` (`invoice_no`),
  ADD KEY `ix_invoice_booking` (`booking_id`),
  ADD KEY `fk_invoice_customer` (`customer_id`);

--
-- Indexes for table `invoice_line`
--
ALTER TABLE `invoice_line`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_iline_invoice` (`invoice_id`);

--
-- Indexes for table `invoice_sequence`
--
ALTER TABLE `invoice_sequence`
  ADD PRIMARY KEY (`series`,`fiscal_year`);

--
-- Indexes for table `jhi_authority`
--
ALTER TABLE `jhi_authority`
  ADD PRIMARY KEY (`name`);

--
-- Indexes for table `jhi_user`
--
ALTER TABLE `jhi_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_user_login` (`login`),
  ADD UNIQUE KEY `ux_user_email` (`email`);

--
-- Indexes for table `jhi_user_authority`
--
ALTER TABLE `jhi_user_authority`
  ADD PRIMARY KEY (`user_id`,`authority_name`),
  ADD KEY `fk_authority_name` (`authority_name`);

--
-- Indexes for table `loyalty_ledger`
--
ALTER TABLE `loyalty_ledger`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_loyalty_user` (`user_id`,`created_at`);

--
-- Indexes for table `professional_incentive_award`
--
ALTER TABLE `professional_incentive_award`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_award_once` (`professional_id`,`rule_id`,`period_start`),
  ADD KEY `fk_award_rule` (`rule_id`);

--
-- Indexes for table `professional_location_log`
--
ALTER TABLE `professional_location_log`
  ADD PRIMARY KEY (`id`,`recorded_at`),
  ADD KEY `ix_loc_booking` (`booking_id`,`recorded_at`),
  ADD KEY `ix_loc_pro` (`professional_id`,`recorded_at`);

--
-- Indexes for table `professional_tier`
--
ALTER TABLE `professional_tier`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_tier_code` (`code`);

--
-- Indexes for table `professional_tier_history`
--
ALTER TABLE `professional_tier_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_tierhist_pro` (`professional_id`,`effective_from`),
  ADD KEY `fk_tierhist_tier` (`tier_id`);

--
-- Indexes for table `professional_training`
--
ALTER TABLE `professional_training`
  ADD PRIMARY KEY (`professional_id`,`module_id`),
  ADD KEY `fk_ptrain_module` (`module_id`);

--
-- Indexes for table `referral_reward`
--
ALTER TABLE `referral_reward`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_referral_pair` (`referrer_id`,`referee_id`),
  ADD KEY `fk_ref_referee` (`referee_id`);

--
-- Indexes for table `saved_app_credential`
--
ALTER TABLE `saved_app_credential`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_saved_credential_owner_app` (`owner_uid`,`app_name`);

--
-- Indexes for table `search_keyword`
--
ALTER TABLE `search_keyword`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_kw` (`keyword`,`service_id`),
  ADD KEY `fk_kw_service` (`service_id`);

--
-- Indexes for table `service_translation`
--
ALTER TABLE `service_translation`
  ADD PRIMARY KEY (`service_id`,`lang_key`);

--
-- Indexes for table `slot_capacity`
--
ALTER TABLE `slot_capacity`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ux_slot` (`zone_id`,`category_id`,`slot_start`),
  ADD KEY `ix_slot_day` (`category_id`,`slot_start`);

--
-- Indexes for table `slot_hold`
--
ALTER TABLE `slot_hold`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_hold_slot` (`slot_capacity_id`,`status`,`expires_at`),
  ADD KEY `ix_hold_expiry` (`status`,`expires_at`),
  ADD KEY `fk_hold_user` (`user_id`);

--
-- Indexes for table `sos_alert`
--
ALTER TABLE `sos_alert`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_sos_booking` (`booking_id`);

--
-- Indexes for table `training_module`
--
ALTER TABLE `training_module`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_training_service` (`service_id`);

--
-- Indexes for table `user_consent`
--
ALTER TABLE `user_consent`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_consent_user` (`user_id`,`purpose`,`created_at`);

--
-- Indexes for table `warranty_claim`
--
ALTER TABLE `warranty_claim`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ix_claim_booking` (`booking_id`),
  ADD KEY `fk_claim_customer` (`customer_id`),
  ADD KEY `fk_claim_redo` (`redo_booking_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `banner`
--
ALTER TABLE `banner`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `blocked_entity`
--
ALTER TABLE `blocked_entity`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `booking_quote`
--
ALTER TABLE `booking_quote`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `booking_quote_item`
--
ALTER TABLE `booking_quote_item`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `booking_reschedule`
--
ALTER TABLE `booking_reschedule`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `booking_subscription`
--
ALTER TABLE `booking_subscription`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `call_session`
--
ALTER TABLE `call_session`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `cancellation_policy`
--
ALTER TABLE `cancellation_policy`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `chat_message`
--
ALTER TABLE `chat_message`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `chat_thread`
--
ALTER TABLE `chat_thread`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `commission_rule`
--
ALTER TABLE `commission_rule`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `customer_wallet_txn`
--
ALTER TABLE `customer_wallet_txn`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `data_deletion_request`
--
ALTER TABLE `data_deletion_request`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `fraud_flag`
--
ALTER TABLE `fraud_flag`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `incentive_rule`
--
ALTER TABLE `incentive_rule`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoice`
--
ALTER TABLE `invoice`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoice_line`
--
ALTER TABLE `invoice_line`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `jhi_user`
--
ALTER TABLE `jhi_user`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `loyalty_ledger`
--
ALTER TABLE `loyalty_ledger`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `professional_incentive_award`
--
ALTER TABLE `professional_incentive_award`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `professional_location_log`
--
ALTER TABLE `professional_location_log`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `professional_tier`
--
ALTER TABLE `professional_tier`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `professional_tier_history`
--
ALTER TABLE `professional_tier_history`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `referral_reward`
--
ALTER TABLE `referral_reward`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `search_keyword`
--
ALTER TABLE `search_keyword`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `slot_capacity`
--
ALTER TABLE `slot_capacity`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `slot_hold`
--
ALTER TABLE `slot_hold`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sos_alert`
--
ALTER TABLE `sos_alert`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `training_module`
--
ALTER TABLE `training_module`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user_consent`
--
ALTER TABLE `user_consent`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `warranty_claim`
--
ALTER TABLE `warranty_claim`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `banner`
--
ALTER TABLE `banner`
  ADD CONSTRAINT `fk_banner_city` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`);

--
-- Constraints for table `booking_quote`
--
ALTER TABLE `booking_quote`
  ADD CONSTRAINT `fk_quote_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_quote_pro` FOREIGN KEY (`professional_id`) REFERENCES `professional` (`id`);

--
-- Constraints for table `booking_quote_item`
--
ALTER TABLE `booking_quote_item`
  ADD CONSTRAINT `fk_qitem_quote` FOREIGN KEY (`quote_id`) REFERENCES `booking_quote` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `booking_reschedule`
--
ALTER TABLE `booking_reschedule`
  ADD CONSTRAINT `fk_resched_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `booking_subscription`
--
ALTER TABLE `booking_subscription`
  ADD CONSTRAINT `fk_sub_address` FOREIGN KEY (`address_id`) REFERENCES `customer_address` (`id`),
  ADD CONSTRAINT `fk_sub_customer` FOREIGN KEY (`customer_id`) REFERENCES `jhi_user` (`id`),
  ADD CONSTRAINT `fk_sub_package` FOREIGN KEY (`package_id`) REFERENCES `service_package` (`id`),
  ADD CONSTRAINT `fk_sub_pro` FOREIGN KEY (`preferred_professional_id`) REFERENCES `professional` (`id`),
  ADD CONSTRAINT `fk_sub_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`);

--
-- Constraints for table `call_session`
--
ALTER TABLE `call_session`
  ADD CONSTRAINT `fk_call_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `cancellation_policy`
--
ALTER TABLE `cancellation_policy`
  ADD CONSTRAINT `fk_cpol_category` FOREIGN KEY (`category_id`) REFERENCES `service_category` (`id`),
  ADD CONSTRAINT `fk_cpol_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`);

--
-- Constraints for table `chat_message`
--
ALTER TABLE `chat_message`
  ADD CONSTRAINT `fk_msg_thread` FOREIGN KEY (`thread_id`) REFERENCES `chat_thread` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `chat_thread`
--
ALTER TABLE `chat_thread`
  ADD CONSTRAINT `fk_thread_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `commission_rule`
--
ALTER TABLE `commission_rule`
  ADD CONSTRAINT `fk_comm_category` FOREIGN KEY (`category_id`) REFERENCES `service_category` (`id`),
  ADD CONSTRAINT `fk_comm_city` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  ADD CONSTRAINT `fk_comm_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`),
  ADD CONSTRAINT `fk_comm_tier` FOREIGN KEY (`tier_id`) REFERENCES `professional_tier` (`id`);

--
-- Constraints for table `customer_wallet`
--
ALTER TABLE `customer_wallet`
  ADD CONSTRAINT `fk_cwallet_user` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`);

--
-- Constraints for table `customer_wallet_txn`
--
ALTER TABLE `customer_wallet_txn`
  ADD CONSTRAINT `fk_cwtxn_user` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`);

--
-- Constraints for table `incentive_rule`
--
ALTER TABLE `incentive_rule`
  ADD CONSTRAINT `fk_incentive_city` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`);

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `fk_invoice_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`),
  ADD CONSTRAINT `fk_invoice_customer` FOREIGN KEY (`customer_id`) REFERENCES `jhi_user` (`id`);

--
-- Constraints for table `invoice_line`
--
ALTER TABLE `invoice_line`
  ADD CONSTRAINT `fk_iline_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `jhi_user_authority`
--
ALTER TABLE `jhi_user_authority`
  ADD CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`);

--
-- Constraints for table `loyalty_ledger`
--
ALTER TABLE `loyalty_ledger`
  ADD CONSTRAINT `fk_loyalty_user` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`);

--
-- Constraints for table `professional_incentive_award`
--
ALTER TABLE `professional_incentive_award`
  ADD CONSTRAINT `fk_award_pro` FOREIGN KEY (`professional_id`) REFERENCES `professional` (`id`),
  ADD CONSTRAINT `fk_award_rule` FOREIGN KEY (`rule_id`) REFERENCES `incentive_rule` (`id`);

--
-- Constraints for table `professional_tier_history`
--
ALTER TABLE `professional_tier_history`
  ADD CONSTRAINT `fk_tierhist_pro` FOREIGN KEY (`professional_id`) REFERENCES `professional` (`id`),
  ADD CONSTRAINT `fk_tierhist_tier` FOREIGN KEY (`tier_id`) REFERENCES `professional_tier` (`id`);

--
-- Constraints for table `professional_training`
--
ALTER TABLE `professional_training`
  ADD CONSTRAINT `fk_ptrain_module` FOREIGN KEY (`module_id`) REFERENCES `training_module` (`id`),
  ADD CONSTRAINT `fk_ptrain_pro` FOREIGN KEY (`professional_id`) REFERENCES `professional` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `referral_reward`
--
ALTER TABLE `referral_reward`
  ADD CONSTRAINT `fk_ref_referee` FOREIGN KEY (`referee_id`) REFERENCES `jhi_user` (`id`),
  ADD CONSTRAINT `fk_ref_referrer` FOREIGN KEY (`referrer_id`) REFERENCES `jhi_user` (`id`);

--
-- Constraints for table `search_keyword`
--
ALTER TABLE `search_keyword`
  ADD CONSTRAINT `fk_kw_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `service_translation`
--
ALTER TABLE `service_translation`
  ADD CONSTRAINT `fk_stran_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `slot_capacity`
--
ALTER TABLE `slot_capacity`
  ADD CONSTRAINT `fk_slot_category` FOREIGN KEY (`category_id`) REFERENCES `service_category` (`id`),
  ADD CONSTRAINT `fk_slot_zone` FOREIGN KEY (`zone_id`) REFERENCES `service_zone` (`id`);

--
-- Constraints for table `slot_hold`
--
ALTER TABLE `slot_hold`
  ADD CONSTRAINT `fk_hold_slot` FOREIGN KEY (`slot_capacity_id`) REFERENCES `slot_capacity` (`id`),
  ADD CONSTRAINT `fk_hold_user` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`);

--
-- Constraints for table `sos_alert`
--
ALTER TABLE `sos_alert`
  ADD CONSTRAINT `fk_sos_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`);

--
-- Constraints for table `training_module`
--
ALTER TABLE `training_module`
  ADD CONSTRAINT `fk_training_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`);

--
-- Constraints for table `user_consent`
--
ALTER TABLE `user_consent`
  ADD CONSTRAINT `fk_consent_user` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `warranty_claim`
--
ALTER TABLE `warranty_claim`
  ADD CONSTRAINT `fk_claim_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`),
  ADD CONSTRAINT `fk_claim_customer` FOREIGN KEY (`customer_id`) REFERENCES `jhi_user` (`id`),
  ADD CONSTRAINT `fk_claim_redo` FOREIGN KEY (`redo_booking_id`) REFERENCES `booking` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
