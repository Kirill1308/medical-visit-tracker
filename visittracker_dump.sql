-- MySQL dump 10.13  Distrib 8.3.0, for Win64 (x86_64)
--
-- Host: localhost    Database: visittracker
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors`
(
    `id`         bigint                                  NOT NULL AUTO_INCREMENT,
    `first_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `last_name`  varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `timezone`   varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `created_at` timestamp                               NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp                               NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors`
    DISABLE KEYS */;
INSERT INTO `doctors`
VALUES (1, 'John', 'Smith', 'America/New_York', '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (2, 'Mary', 'Johnson', 'America/Chicago', '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (3, 'David', 'Lee', 'America/Los_Angeles', '2025-03-07 10:13:14', '2025-03-07 10:13:14');
/*!40000 ALTER TABLE `doctors`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history`
(
    `installed_rank` int                                      NOT NULL,
    `version`        varchar(50) COLLATE utf8mb4_unicode_ci            DEFAULT NULL,
    `description`    varchar(200) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `type`           varchar(20) COLLATE utf8mb4_unicode_ci   NOT NULL,
    `script`         varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
    `checksum`       int                                               DEFAULT NULL,
    `installed_by`   varchar(100) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `installed_on`   timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `execution_time` int                                      NOT NULL,
    `success`        tinyint(1)                               NOT NULL,
    PRIMARY KEY (`installed_rank`),
    KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history`
    DISABLE KEYS */;
INSERT INTO `flyway_schema_history`
VALUES (1, '1', 'init', 'SQL', 'V1__init.sql', 1294026659, 'user', '2025-03-07 10:13:14', 390, 1),
       (2, '2', 'insert', 'SQL', 'V2__insert.sql', -19976026, 'user', '2025-03-07 10:13:14', 29, 1);
/*!40000 ALTER TABLE `flyway_schema_history`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients`
(
    `id`         bigint                                  NOT NULL AUTO_INCREMENT,
    `first_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `last_name`  varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` timestamp                               NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp                               NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_patient_name` (`first_name`, `last_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients`
    DISABLE KEYS */;
INSERT INTO `patients`
VALUES (1, 'Alice', 'Brown', '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (2, 'Bob', 'White', '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (3, 'Carol', 'Davis', '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (4, 'Dan', 'Miller', '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (5, 'Eve', 'Wilson', '2025-03-07 10:13:14', '2025-03-07 10:13:14');
/*!40000 ALTER TABLE `patients`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visits`
--

DROP TABLE IF EXISTS `visits`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visits`
(
    `id`              bigint    NOT NULL AUTO_INCREMENT,
    `start_date_time` datetime  NOT NULL,
    `end_date_time`   datetime  NOT NULL,
    `patient_id`      bigint    NOT NULL,
    `doctor_id`       bigint    NOT NULL,
    `created_at`      timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `patient_id` (`patient_id`),
    KEY `idx_doctor_start_end` (`doctor_id`, `start_date_time`, `end_date_time`),
    CONSTRAINT `visits_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
    CONSTRAINT `visits_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visits`
--

LOCK TABLES `visits` WRITE;
/*!40000 ALTER TABLE `visits`
    DISABLE KEYS */;
INSERT INTO `visits`
VALUES (1, '2025-03-08 10:13:14', '2025-03-08 11:13:14', 1, 1, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (2, '2025-03-09 10:13:14', '2025-03-09 11:13:14', 2, 1, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (3, '2025-03-10 10:13:14', '2025-03-10 11:13:14', 3, 1, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (4, '2025-03-08 10:13:14', '2025-03-08 11:13:14', 2, 2, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (5, '2025-03-09 10:13:14', '2025-03-09 11:13:14', 3, 2, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (6, '2025-03-10 10:13:14', '2025-03-10 11:13:14', 4, 2, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (7, '2025-03-08 10:13:14', '2025-03-08 11:13:14', 3, 3, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (8, '2025-03-09 10:13:14', '2025-03-09 11:13:14', 4, 3, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (9, '2025-03-10 10:13:14', '2025-03-10 11:13:14', 5, 3, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (10, '2025-02-25 10:13:14', '2025-02-25 11:13:14', 1, 1, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (11, '2025-02-25 10:13:14', '2025-02-25 11:13:14', 2, 2, '2025-03-07 10:13:14', '2025-03-07 10:13:14'),
       (12, '2025-02-25 10:13:14', '2025-02-25 11:13:14', 3, 3, '2025-03-07 10:13:14', '2025-03-07 10:13:14');
/*!40000 ALTER TABLE `visits`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2025-03-07 12:26:19
