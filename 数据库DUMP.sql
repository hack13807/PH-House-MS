CREATE DATABASE  IF NOT EXISTS `hms` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `hms`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 115.159.112.233    Database: library
-- ------------------------------------------------------
-- Server version	5.7.22-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary view structure for view `arrive_reminder`
--

DROP TABLE IF EXISTS `arrive_reminder`;
/*!50001 DROP VIEW IF EXISTS `arrive_reminder`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `arrive_reminder` AS SELECT 
 1 AS `user_name`,
 1 AS `bname`,
 1 AS `email`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `book` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `ISBN` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `location` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` int(11) NOT NULL,
  `operator` int(11) NOT NULL,
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `BID_UNIQUE` (`book_id`),
  KEY `op2_idx` (`operator`),
  KEY `bo_idx` (`ISBN`),
  CONSTRAINT `bo` FOREIGN KEY (`ISBN`) REFERENCES `booklist` (`ISBN`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `op2` FOREIGN KEY (`operator`) REFERENCES `manager` (`manager_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (151,'0000000000001','流通室',0,6),(152,'0000000000001','流通室',0,6),(153,'0000000000001','流通室',0,6),(154,'9787100000000','流通室',0,6),(155,'9787100000000','流通室',0,6),(156,'9787100000000','流通室',0,6),(157,'9787100000000','流通室',0,6),(158,'9787100000000','流通室',0,6),(159,'9787100000000','流通室',0,6),(160,'9787100000000','流通室',0,6),(161,'9787100000000','流通室',0,6),(162,'9787111421900','流通室',0,6),(163,'9787111421900','流通室',0,6),(164,'9787115417305','流通室',0,6),(165,'9787115417305','流通室',0,6),(166,'9787115417305','流通室',0,6),(167,'9787115417305','流通室',0,6),(168,'9787115417305','流通室',0,6),(169,'9787115417305','流通室',0,6),(170,'9787121254437','流通室',0,6),(171,'9787121254437','流通室',0,6),(172,'9787121254437','流通室',0,6),(173,'9787121254437','流通室',0,6),(174,'9787121254437','流通室',0,6),(175,'9787121254437','流通室',0,6);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;
DELIMITER ;;

DROP TABLE IF EXISTS `booklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `booklist` (
  `ISBN` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `bname` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `publisher` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `writer` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ptime` date DEFAULT NULL,
  `number` int(11) NOT NULL DEFAULT '0',
  `operator` int(11) NOT NULL,
  `is_popular` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ISBN`),
  UNIQUE KEY `ISBN_UNIQUE` (`ISBN`),
  KEY `op_idx` (`operator`),
  CONSTRAINT `op` FOREIGN KEY (`operator`) REFERENCES `manager` (`manager_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booklist`
--

LOCK TABLES `booklist` WRITE;
/*!40000 ALTER TABLE `booklist` DISABLE KEYS */;
INSERT INTO `booklist` VALUES ('0000000000001','SQL必知必会','芳文社','陈之豪','8102-05-20',3,6,0),('9787100000000','Qian\'s C Compiler——The speed of the art','路边社','钱晨','2018-05-14',8,6,0),('9787111421900','深入理解Java虚拟机','机械工业出版社','周志明','2013-06-01',2,6,1),('9787115417305','spring实战','人民邮电出版社','Adrian Mouat','2017-04-01',6,6,1),('9787121254437','集体智慧编程','人民邮电出版社','Toby Segaran','2015-03-01',6,6,0);
