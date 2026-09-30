-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: mysql:3306
-- Generation Time: Sep 30, 2026 at 01:42 PM
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

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`id`, `owner_login`, `service_id`, `service_title`, `emoji`, `price`, `service_fee`, `date`, `time_slot`, `address`, `status`, `assigned_pro_name`, `pro_rating`, `updated_at`) VALUES
('8964609e-7b73-4eb8-a2f9-918644d64dc1', 'admin', 'hm-ac', 'AC Service & Repair', '❄️', 400, 49, '2099-01-01', '10:00 AM - 12:00 PM', '123 Backend Test Street', 'REQUESTED', 'Not assigned yet', 0.00, '2026-09-30 11:52:53');

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
('00000000000000', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', '2026-09-30 11:16:24', 1, 'EXECUTED', '9:b6b4a3e0d2a6d7f1e5139675af65d7b0', 'createSequence sequenceName=sequence_generator', '', NULL, '4.29.2', NULL, NULL, '0766984223'),
('00000000000001', 'jhipster', 'config/liquibase/changelog/00000000000000_initial_schema.xml', '2026-09-30 11:16:24', 2, 'EXECUTED', '9:4e734400486c054d8030ceca7233c056', 'createTable tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableName=jhi_user_authority; addForeignKeyConstraint baseTableName=jhi_user_authority, constraintName=fk_authority_name, ...', '', NULL, '4.29.2', NULL, NULL, '0766984223'),
('00000000000003', 'jhipster', 'config/liquibase/changelog/00000000000003_facility_schema.xml', '2026-09-30 11:21:33', 3, 'EXECUTED', '9:c1962bc9292aca5bb48afc99c93cba78', 'addColumn tableName=jhi_user; createTable tableName=facility_service; createTable tableName=booking; insert tableName=facility_service; insert tableName=facility_service; insert tableName=facility_service; insert tableName=facility_service; insert...', '', NULL, '4.29.2', NULL, NULL, '0767293162'),
('00000000000004', 'jhipster', 'config/liquibase/changelog/00000000000004_mysql_identity.xml', '2026-09-30 11:44:38', 4, 'EXECUTED', '9:982eca082d9a1217da6b7f4e524e066e', 'sql', '', NULL, '4.29.2', NULL, NULL, '0768678659');

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
('beauty-women', 'Salon at Home', '💇', 'beautyWomen', 'Beauty and wellness services at your doorstep', '₹499 - ₹2,999', 499, 4.90, 1850, '45-120 mins', 'Verified experts|Premium products|Private appointments', 1),
('clean-home', 'Full Home Cleaning', '🧹', 'cleaning', 'Deep cleaning for kitchens, bathrooms and rooms', '₹999 - ₹3,999', 999, 4.87, 2210, '2-4 hours', 'Trained professionals|Eco-friendly products|Quality checklist', 1),
('hm-ac', 'AC Service & Repair', '❄️', 'homeMaintenance', 'Service, gas refill, deep clean, installation', '₹400 - ₹2,500', 400, 4.86, 4520, '45-60 mins', 'Power jet foam clean|Gas leak detection|30-day warranty', 1),
('hm-electric', 'Electrician', '⚡', 'homeMaintenance', 'Wiring, fan, switch, MCB repair', '₹300 - ₹1,500', 300, 4.84, 3890, '30-60 mins', 'Background verified pros|High grade equipment|Post-service test', 1),
('hm-plumb', 'Plumbing', '🔧', 'homeMaintenance', 'Leaks, tap repair, pipe fitting, blockage', '₹300 - ₹1,500', 300, 4.81, 3100, '30-60 mins', 'Certified plumbers|Standardized rate card|Genuine spare parts', 0);

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
(2, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'User', 'User', 'user@localhost', '', 1, 'en', NULL, NULL, 'system', NULL, NULL, 'system', NULL, NULL),
(3, 'frontend-check-20261003@example.com', '$2a$10$q8gFfmD0yXnIowWL9VSGLe2ajZcFBKvsVzzbTvXfihne7CkS3pgxm', 'Frontend', 'Check', 'frontend-check-20261003@example.com', NULL, 0, 'en', 'zbZsUiAOStcK5BFXrX2b', NULL, 'anonymousUser', '2026-09-30 11:44:59', NULL, 'anonymousUser', '2026-09-30 11:44:59', NULL),
(4, 'frontend-check-20261004@example.com', '$2a$10$9x/T81f5rUM7.37oURHCVu0VluST.nX7Vuhq9O74CgDnG9PsuY9Cy', 'Frontend', 'Check', 'frontend-check-20261004@example.com', NULL, 1, 'en', NULL, NULL, 'anonymousUser', '2026-09-30 11:46:36', NULL, 'anonymousUser', '2026-09-30 11:46:37', NULL),
(5, 'npavprashant@gmail.com', '$2a$10$PcqBica6FZwDL.D0vSr.0.WWBfL5HrHBa5TUfxTCNogleDElNOP8m', 'Prashant', '', 'npavprashant@gmail.com', NULL, 1, 'en', NULL, NULL, 'anonymousUser', '2026-09-30 11:47:41', NULL, 'anonymousUser', '2026-09-30 11:47:42', NULL);

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
(2, 'ROLE_USER'),
(3, 'ROLE_USER'),
(4, 'ROLE_USER'),
(5, 'ROLE_USER');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `DATABASECHANGELOGLOCK`
--
ALTER TABLE `DATABASECHANGELOGLOCK`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `facility_service`
--
ALTER TABLE `facility_service`
  ADD PRIMARY KEY (`id`);

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
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jhi_user`
--
ALTER TABLE `jhi_user`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `jhi_user_authority`
--
ALTER TABLE `jhi_user_authority`
  ADD CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
