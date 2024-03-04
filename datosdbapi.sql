-- MySQL dump 10.13  Distrib 8.0.32, for Linux (x86_64)
--
-- Host: localhost    Database: dbapi
-- ------------------------------------------------------
-- Server version	8.0.36-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Configuracio`
--

DROP TABLE IF EXISTS `Configuracio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Configuracio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Configuracio`
--

LOCK TABLES `Configuracio` WRITE;
/*!40000 ALTER TABLE `Configuracio` DISABLE KEYS */;
INSERT INTO `Configuracio` VALUES (1,'Configuració 1');
/*!40000 ALTER TABLE `Configuracio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Grups`
--

DROP TABLE IF EXISTS `Grups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Grups` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Grups`
--

LOCK TABLES `Grups` WRITE;
/*!40000 ALTER TABLE `Grups` DISABLE KEYS */;
INSERT INTO `Grups` VALUES (1,'Cliente'),(2,'Administrador');
/*!40000 ALTER TABLE `Grups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Peticions`
--

DROP TABLE IF EXISTS `Peticions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Peticions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `Prompt` varchar(255) DEFAULT NULL,
  `data` datetime(6) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `id_usuari` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrf9m8w76pluhaibh3lx0m384q` (`id_usuari`),
  CONSTRAINT `FKrf9m8w76pluhaibh3lx0m384q` FOREIGN KEY (`id_usuari`) REFERENCES `Usuaris` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Peticions`
--

LOCK TABLES `Peticions` WRITE;
/*!40000 ALTER TABLE `Peticions` DISABLE KEYS */;
/*!40000 ALTER TABLE `Peticions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pla`
--

DROP TABLE IF EXISTS `Pla`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Pla` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) DEFAULT NULL,
  `quota` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pla`
--

LOCK TABLES `Pla` WRITE;
/*!40000 ALTER TABLE `Pla` DISABLE KEYS */;
INSERT INTO `Pla` VALUES (1,'Free',20),(2,'Premium',50);
/*!40000 ALTER TABLE `Pla` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Propietat`
--

DROP TABLE IF EXISTS `Propietat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Propietat` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clau` varchar(255) DEFAULT NULL,
  `valor` varchar(255) DEFAULT NULL,
  `configuracio_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm9mrll9akqb0d2kyjpwm8an9s` (`configuracio_id`),
  CONSTRAINT `FKm9mrll9akqb0d2kyjpwm8an9s` FOREIGN KEY (`configuracio_id`) REFERENCES `Configuracio` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Propietat`
--

LOCK TABLES `Propietat` WRITE;
/*!40000 ALTER TABLE `Propietat` DISABLE KEYS */;
INSERT INTO `Propietat` VALUES (1,'versió','1.0.1',1);
/*!40000 ALTER TABLE `Propietat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Quota`
--

DROP TABLE IF EXISTS `Quota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Quota` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `consumida` int DEFAULT NULL,
  `disponible` int DEFAULT NULL,
  `total` int DEFAULT NULL,
  `usuari_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_fyekwkysaua491i2fmnfok1h8` (`usuari_id`),
  CONSTRAINT `FKc9o98r5ncua2y1t2hxyu1l1t` FOREIGN KEY (`usuari_id`) REFERENCES `Usuaris` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Quota`
--

LOCK TABLES `Quota` WRITE;
/*!40000 ALTER TABLE `Quota` DISABLE KEYS */;
INSERT INTO `Quota` VALUES (1,0,20,20,4),(4,0,50,50,6);
/*!40000 ALTER TABLE `Quota` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Resposta`
--

DROP TABLE IF EXISTS `Resposta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Resposta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `text` mediumtext,
  `peticions_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c7ecx58vhs1f5dbn27rncfs4a` (`peticions_id`),
  CONSTRAINT `FKradnjgb2b6oqosnskcxvn8bn7` FOREIGN KEY (`peticions_id`) REFERENCES `Peticions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Resposta`
--

LOCK TABLES `Resposta` WRITE;
/*!40000 ALTER TABLE `Resposta` DISABLE KEYS */;
/*!40000 ALTER TABLE `Resposta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuaris`
--

DROP TABLE IF EXISTS `Usuaris`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuaris` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ToS` bit(1) DEFAULT NULL,
  `apitoken` varchar(255) DEFAULT NULL,
  `codi_validacio` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `telefon` varchar(255) DEFAULT NULL,
  `pla_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ctuaxaidwhxx0cifm4gx45rby` (`contrasena`),
  UNIQUE KEY `UK_gyeyn5ty2swxm363ri18hge3c` (`email`),
  UNIQUE KEY `UK_q3qwxms8d4ndcpceuqlw09pr4` (`telefon`),
  KEY `FKje0hy6f12w2pevfvi4g3s9iyp` (`pla_id`),
  CONSTRAINT `FKje0hy6f12w2pevfvi4g3s9iyp` FOREIGN KEY (`pla_id`) REFERENCES `Pla` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuaris`
--

LOCK TABLES `Usuaris` WRITE;
/*!40000 ALTER TABLE `Usuaris` DISABLE KEYS */;
INSERT INTO `Usuaris` VALUES (4,NULL,'sxw34pG3BCtS','296272',NULL,'Alex@gmail.com','Alex','123456789',1),(6,NULL,NULL,'333444555',NULL,'manuel@gmail.com','manu','1234567',2);
/*!40000 ALTER TABLE `Usuaris` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `peticions_paths`
--

DROP TABLE IF EXISTS `peticions_paths`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `peticions_paths` (
  `peticion_id` bigint NOT NULL,
  `path` varchar(255) DEFAULT NULL,
  KEY `FK3ytdeiih2pf9san71etuwu1ow` (`peticion_id`),
  CONSTRAINT `FK3ytdeiih2pf9san71etuwu1ow` FOREIGN KEY (`peticion_id`) REFERENCES `Peticions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `peticions_paths`
--

LOCK TABLES `peticions_paths` WRITE;
/*!40000 ALTER TABLE `peticions_paths` DISABLE KEYS */;
/*!40000 ALTER TABLE `peticions_paths` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuaris_grups`
--

DROP TABLE IF EXISTS `usuaris_grups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuaris_grups` (
  `usuari_id` bigint NOT NULL,
  `grup_id` bigint NOT NULL,
  KEY `FKcgj9ngu02c83nomf6tj7b1i5k` (`grup_id`),
  KEY `FK1mjejcdu7fc9eo2u4e82vn5dd` (`usuari_id`),
  CONSTRAINT `FK1mjejcdu7fc9eo2u4e82vn5dd` FOREIGN KEY (`usuari_id`) REFERENCES `Usuaris` (`id`),
  CONSTRAINT `FKcgj9ngu02c83nomf6tj7b1i5k` FOREIGN KEY (`grup_id`) REFERENCES `Grups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuaris_grups`
--

LOCK TABLES `usuaris_grups` WRITE;
/*!40000 ALTER TABLE `usuaris_grups` DISABLE KEYS */;
INSERT INTO `usuaris_grups` VALUES (4,1),(4,2),(6,1);
/*!40000 ALTER TABLE `usuaris_grups` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-04 19:05:25
