CREATE DATABASE  IF NOT EXISTS `chitchat` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `chitchat`;
-- MySQL dump 10.13  Distrib 5.7.24, for Win64 (x86_64)
--
-- Host: localhost    Database: chitchat
-- ------------------------------------------------------
-- Server version	5.7.24-log

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
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_edited` datetime NOT NULL,
  `sender` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `receiver` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `message_data` varchar(255) NOT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,'2018-11-16 21:48:29','2018-11-16 22:21:08','admin','klean83','Kalimera ola kala?'),(3,'2018-11-16 22:22:11','2018-11-16 22:22:11','admin','katia95','Pos paei?'),(4,'2018-11-16 22:31:14','2018-11-16 22:31:14','klean83','katia95','variemai ti zoi mou...'),(5,'2018-11-16 22:31:35','2018-11-16 22:31:35','klean83','katia95','poli omos.....'),(6,'2018-11-16 22:32:19','2018-11-16 22:32:19','klean83','klean83','na kleiso to thermosifono'),(7,'2018-11-17 16:57:44','2018-11-17 16:57:44','admin','admin','yolo'),(8,'2018-11-17 16:58:01','2018-11-17 16:58:01','admin','admin','ola kala'),(9,'2018-11-17 17:05:59','2018-11-17 17:05:59','admin','admin','tipota'),(10,'2018-11-17 17:09:40','2018-11-17 17:09:40','admin','admin','nai'),(11,'2018-11-17 17:21:08','2018-11-17 17:21:08','admin','admin','oxi'),(12,'2018-11-17 17:24:46','2018-11-17 17:24:46','admin','admin','ola kala'),(13,'2018-11-17 17:27:42','2018-11-17 17:27:42','admin','admin','varethilka'),(14,'2018-11-17 17:31:43','2018-11-17 17:31:43','admin','admin','test test test'),(15,'2018-11-17 18:11:10','2018-11-17 18:11:10','admin','admin','admin'),(16,'2018-11-17 18:23:20','2018-11-17 18:23:20','admin','admin','pos eisai?'),(17,'2018-11-18 14:21:07','2018-11-18 14:31:32','admin','katia95','ti ginetai?'),(18,'2018-11-18 14:30:34','2018-11-18 14:30:34','katia95','admin','ela re admninara'),(19,'2018-11-18 14:55:43','2018-11-18 14:55:43','katia95','admin','User: katia95 requested a new temporary password');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `birthDate` date DEFAULT NULL,
  `gender` varchar(3) DEFAULT NULL,
  `email_address` varchar(45) CHARACTER SET latin1 NOT NULL,
  `user_name` varchar(45) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  `accessLevel` int(11) NOT NULL,
  `tempPassword` varchar(250) NOT NULL DEFAULT 'temp',
  `message_count` int(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin','1900-01-01','u','admin','admin','D033E22AE348AEB5660FC2140AEC35850C4DA997',1,'temp',12),(2,'Kleanthis','Mitsioulis','1983-07-30','m','KleanthisM83@chitchat.com','klean83','6A6CEE6850BEC587534A206D323E17B4A0CDC7E8',2,'temp',1),(3,'Kostas','Pakiroglou','1987-05-03','m','KostasP87@chitchat.com','kostas87',NULL,4,'777777',NULL),(4,'Katerina','Papakosta','1995-04-25','f','KaterinaP95@chitchat.com','katia95',NULL,3,'444444',4),(5,'user','user','2001-01-01','m','useru01@chitchat.com','user',NULL,1,'test',NULL),(6,'Fillippos','Bourdenas','1978-05-03','m','FillipposB78@chitchat.com','felipe',NULL,3,'123456',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-18 15:40:29
