-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: accounting101
-- ------------------------------------------------------
-- Server version	5.7.19-log

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
-- Table structure for table `sample`
--

DROP TABLE IF EXISTS `sample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample` (
  `recordId` int(11) NOT NULL,
  `dateOfTransaction` date NOT NULL,
  `accountTitle` varchar(50) NOT NULL,
  `debit` decimal(10,2) NOT NULL,
  `credit` decimal(10,2) NOT NULL,
  `note` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`recordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sample`
--

LOCK TABLES `sample` WRITE;
/*!40000 ALTER TABLE `sample` DISABLE KEYS */;
INSERT INTO `sample` VALUES (0,'2017-01-01','Cash',20000.00,0.00,''),(1,'2017-01-01','Owner\'s Capital',0.00,20000.00,'Owner\'s Investment of Cash in Business'),(2,'2017-01-01','Equipment',5000.00,0.00,''),(3,'2017-01-01','Accounts Payable',0.00,5000.00,'Purchased Equipment on Account From OriginPC'),(4,'2017-01-02','Cash',1000.00,0.00,''),(5,'2017-01-02','Unearned Service Revenue',0.00,1000.00,'Received Cash from R. Knox for future service'),(6,'2017-01-05','Rent Expense',900.00,0.00,''),(7,'2017-01-05','Cash',0.00,900.00,'Paid Jan. Rent'),(8,'2017-01-06','Prepaid Insurance',300.00,0.00,''),(9,'2017-01-06','Cash',0.00,300.00,'Paid 1 Year Policy, Effective on Jan. 1'),(10,'2017-01-20','Owner\'s Drawings',500.00,0.00,''),(11,'2017-01-20','Cash',0.00,500.00,'Withdrew Cash for Personal Use'),(12,'2017-01-20','Salaries & Wages Expense',400.00,0.00,''),(13,'2017-01-20','Cash',0.00,400.00,'Paid Salaries to Date'),(14,'2017-01-26','Cash',10000.00,0.00,''),(15,'2017-01-26','Service Revenue',0.00,10000.00,'Received Cash for Services Performed'),(16,'2017-01-29','Unearned Service Revenue',1000.00,0.00,''),(17,'2017-01-29','Service Revenue',0.00,1000.00,'Performed Services for R. Knox'),(18,'2017-01-31','Utilities Expense',250.00,0.00,''),(19,'2017-01-31','Cash',0.00,250.00,'Paid Electricity Bill');
/*!40000 ALTER TABLE `sample` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-20 20:06:03
