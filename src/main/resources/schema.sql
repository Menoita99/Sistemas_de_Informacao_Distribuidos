USE `sid_2`;
-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: sid_2
-- ------------------------------------------------------
-- Server version	8.0.18

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
-- Table structure for table `alerta`
--

DROP TABLE IF EXISTS `alerta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alerta` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DataHoraMedicao` timestamp NOT NULL,
  `TipoSensor` varchar(3) NOT NULL,
  `ValorMedicao` decimal(6,2) NOT NULL,
  `Limite` decimal(6,2) NOT NULL,
  `Descricao` varchar(1000) NOT NULL,
  `Controlo` tinyint(4) DEFAULT NULL,
  `Extra` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alerta`
--

LOCK TABLES `alerta` WRITE;
/*!40000 ALTER TABLE `alerta` DISABLE KEYS */;
INSERT INTO `alerta` VALUES (1,'2020-05-30 17:43:56','eth',0.00,0.00,'Isto está tudo fodido',0,'Okey, está bem'),(2,'2020-05-30 18:23:27','eth',0.00,0.00,'Mas bem fodido',0,'Okey, está bem'),(3,'2020-05-30 18:25:17','eth',0.00,0.00,'Mas bem fodido',0,'Okey, está bem'),(4,'2020-05-30 18:35:08','eth',0.00,0.00,'Mas bem fodido',0,'Okey, está bem'),(5,'2020-05-30 18:35:54','eth',0.00,0.00,'Mas bem fodido',0,'Okey, está bem'),(6,'2020-05-30 18:43:36','eth',0.00,0.00,'Mas bem fodido',0,'Okey, está bem'),(7,'2020-05-30 18:58:12','eth',0.00,0.00,'Mas bem fodido',0,'Okey, está bem'),(8,'2020-05-30 18:59:28','eth',0.00,0.00,'Mas bem fodido',0,'Okey, está bem'),(9,'2020-05-30 20:13:02','eth',0.00,0.00,'Mas bem fodido',0,'Okey, está bem'),(10,'2020-05-30 20:15:07','eth',0.00,0.00,'Mas bem fodido',0,'Okey, está bem'),(11,'2020-05-31 20:41:06','tmp',18.50,50.00,'Temperatura a subir',0,' '),(12,'2020-05-31 21:43:03','tmp',18.00,50.00,'Temperatura a descer',0,' '),(13,'2020-05-31 21:43:05','tmp',18.50,50.00,'Temperatura a subir',0,' ');
/*!40000 ALTER TABLE `alerta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicaosensores`
--

DROP TABLE IF EXISTS `medicaosensores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicaosensores` (
  `ValorMedicao` decimal(6,2) NOT NULL,
  `TipoSensor` varchar(3) NOT NULL,
  `DataHoraMedicao` timestamp NOT NULL,
  `Controlo` tinyint(4) DEFAULT NULL,
  `Extra` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ValorMedicao`,`TipoSensor`,`DataHoraMedicao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicaosensores`
--

LOCK TABLES `medicaosensores` WRITE;
/*!40000 ALTER TABLE `medicaosensores` DISABLE KEYS */;
INSERT INTO `medicaosensores` VALUES (0.00,'MOV','2020-05-31 20:41:04',1,' '),(0.00,'MOV','2020-05-31 20:41:06',1,' '),(0.00,'MOV','2020-05-31 20:41:08',1,' '),(0.00,'MOV','2020-05-31 20:41:10',1,' '),(0.00,'MOV','2020-05-31 20:41:12',1,' '),(0.00,'MOV','2020-05-31 20:41:17',1,' '),(0.00,'MOV','2020-05-31 20:41:19',1,' '),(0.00,'MOV','2020-05-31 20:41:21',1,' '),(0.00,'MOV','2020-05-31 20:41:23',1,' '),(0.00,'MOV','2020-05-31 20:41:25',1,' '),(0.00,'MOV','2020-05-31 20:41:27',1,' '),(0.00,'MOV','2020-05-31 20:41:29',1,' '),(0.00,'MOV','2020-05-31 20:41:31',1,' '),(0.00,'MOV','2020-05-31 20:41:33',1,' '),(0.00,'MOV','2020-05-31 20:41:36',1,' '),(0.00,'MOV','2020-05-31 20:41:38',1,' '),(0.00,'MOV','2020-05-31 20:41:40',1,' '),(0.00,'MOV','2020-05-31 20:41:42',1,' '),(0.00,'MOV','2020-05-31 20:41:44',1,' '),(0.00,'MOV','2020-05-31 20:41:48',1,' '),(0.00,'MOV','2020-05-31 20:41:50',1,' '),(0.00,'MOV','2020-05-31 20:41:52',1,' '),(0.00,'MOV','2020-05-31 20:41:55',1,' '),(0.00,'MOV','2020-05-31 20:41:57',1,' '),(0.00,'MOV','2020-05-31 20:41:59',1,' '),(0.00,'MOV','2020-05-31 20:42:01',1,' '),(0.00,'MOV','2020-05-31 20:42:03',1,' '),(0.00,'MOV','2020-05-31 20:42:07',1,' '),(0.00,'MOV','2020-05-31 20:42:09',1,' '),(0.00,'MOV','2020-05-31 20:42:11',1,' '),(0.00,'MOV','2020-05-31 20:42:14',1,' '),(0.00,'MOV','2020-05-31 20:42:16',1,' '),(0.00,'MOV','2020-05-31 20:42:18',1,' '),(0.00,'MOV','2020-05-31 20:42:22',1,' '),(0.00,'MOV','2020-05-31 20:42:24',1,' '),(0.00,'MOV','2020-05-31 20:42:26',1,' '),(0.00,'MOV','2020-05-31 20:42:28',1,' '),(0.00,'MOV','2020-05-31 21:42:33',1,' '),(0.00,'MOV','2020-05-31 21:42:37',1,' '),(0.00,'MOV','2020-05-31 21:42:44',1,' '),(0.00,'MOV','2020-05-31 21:42:46',1,' '),(0.00,'MOV','2020-05-31 21:42:50',1,' '),(0.00,'MOV','2020-05-31 21:43:03',1,' '),(0.00,'MOV','2020-05-31 21:43:07',1,' '),(0.00,'MOV','2020-05-31 21:43:09',1,' '),(0.00,'MOV','2020-05-31 21:43:11',1,' '),(0.00,'MOV','2020-05-31 21:43:14',1,' '),(0.00,'MOV','2020-05-31 21:43:16',1,' '),(0.00,'MOV','2020-05-31 21:43:18',1,' '),(0.00,'MOV','2020-05-31 21:43:24',1,' '),(0.00,'MOV','2020-05-31 21:43:26',1,' '),(0.00,'MOV','2020-05-31 21:43:28',1,' '),(0.00,'MOV','2020-05-31 21:43:30',1,' '),(0.00,'MOV','2020-05-31 21:43:32',1,' '),(1.00,'MOV','2020-05-31 20:41:15',1,' '),(1.00,'MOV','2020-05-31 20:41:46',1,' '),(1.00,'MOV','2020-05-31 20:42:05',1,' '),(1.00,'MOV','2020-05-31 20:42:20',1,' '),(1.00,'MOV','2020-05-31 20:42:30',1,' '),(1.00,'MOV','2020-05-31 21:42:35',1,' '),(1.00,'MOV','2020-05-31 21:42:39',1,' '),(1.00,'MOV','2020-05-31 21:42:41',1,' '),(1.00,'MOV','2020-05-31 21:42:48',1,' '),(1.00,'MOV','2020-05-31 21:43:05',1,' '),(1.00,'MOV','2020-05-31 21:43:20',1,' '),(1.00,'MOV','2020-05-31 21:43:22',1,' '),(18.00,'TMP','2020-05-31 20:41:04',1,' '),(18.00,'TMP','2020-05-31 21:42:33',1,' '),(18.00,'TMP','2020-05-31 21:43:03',1,' '),(18.50,'TMP','2020-05-31 20:41:06',1,' '),(18.50,'TMP','2020-05-31 21:42:35',1,' '),(18.50,'TMP','2020-05-31 21:43:05',1,' '),(19.00,'TMP','2020-05-31 20:41:08',1,' '),(19.00,'TMP','2020-05-31 21:42:37',1,' '),(19.00,'TMP','2020-05-31 21:43:07',1,' '),(19.50,'TMP','2020-05-31 20:41:10',1,' '),(19.50,'TMP','2020-05-31 21:42:39',1,' '),(19.50,'TMP','2020-05-31 21:43:09',1,' '),(20.00,'TMP','2020-05-31 20:41:12',1,' '),(20.00,'TMP','2020-05-31 21:42:41',1,' '),(20.00,'TMP','2020-05-31 21:43:11',1,' '),(20.50,'TMP','2020-05-31 20:41:15',1,' '),(20.50,'TMP','2020-05-31 21:42:44',1,' '),(20.50,'TMP','2020-05-31 21:43:14',1,' '),(21.00,'TMP','2020-05-31 20:41:17',1,' '),(21.00,'TMP','2020-05-31 21:42:46',1,' '),(21.00,'TMP','2020-05-31 21:43:16',1,' '),(21.50,'TMP','2020-05-31 20:41:19',1,' '),(21.50,'TMP','2020-05-31 21:42:48',1,' '),(21.50,'TMP','2020-05-31 21:43:18',1,' '),(22.00,'TMP','2020-05-31 20:41:21',1,' '),(22.00,'TMP','2020-05-31 21:42:50',1,' '),(22.00,'TMP','2020-05-31 21:43:20',1,' '),(22.50,'TMP','2020-05-31 20:41:23',1,' '),(22.50,'TMP','2020-05-31 21:43:22',1,' '),(23.00,'TMP','2020-05-31 20:41:25',1,' '),(23.00,'TMP','2020-05-31 21:43:24',1,' '),(23.50,'TMP','2020-05-31 20:41:27',1,' '),(23.50,'TMP','2020-05-31 21:43:26',1,' '),(24.00,'TMP','2020-05-31 20:41:29',1,' '),(24.00,'TMP','2020-05-31 21:43:28',1,' '),(24.50,'TMP','2020-05-31 20:41:31',1,' '),(24.50,'TMP','2020-05-31 21:43:30',1,' '),(25.00,'TMP','2020-05-31 20:41:33',1,' '),(25.00,'TMP','2020-05-31 21:43:32',1,' '),(25.50,'TMP','2020-05-31 20:41:36',1,' '),(26.00,'TMP','2020-05-31 20:41:38',1,' '),(26.50,'TMP','2020-05-31 20:41:40',1,' '),(27.00,'TMP','2020-05-31 20:41:42',1,' '),(27.50,'TMP','2020-05-31 20:41:44',1,' '),(28.00,'TMP','2020-05-31 20:41:46',1,' '),(28.50,'TMP','2020-05-31 20:41:48',1,' '),(29.00,'TMP','2020-05-31 20:41:50',1,' '),(29.50,'TMP','2020-05-31 20:41:52',1,' '),(30.00,'TMP','2020-05-31 20:41:55',1,' '),(30.50,'TMP','2020-05-31 20:41:57',1,' '),(31.00,'TMP','2020-05-31 20:41:59',1,' '),(31.50,'TMP','2020-05-31 20:42:01',1,' '),(32.00,'TMP','2020-05-31 20:42:03',1,' '),(32.50,'TMP','2020-05-31 20:42:05',1,' '),(33.00,'TMP','2020-05-31 20:42:07',1,' '),(33.50,'TMP','2020-05-31 20:42:09',1,' '),(34.00,'TMP','2020-05-31 20:42:11',1,' '),(34.50,'TMP','2020-05-31 20:42:14',1,' '),(35.00,'HUM','2020-05-31 20:41:04',1,' '),(35.00,'HUM','2020-05-31 20:41:06',1,' '),(35.00,'HUM','2020-05-31 20:41:08',1,' '),(35.00,'HUM','2020-05-31 20:41:10',1,' '),(35.00,'HUM','2020-05-31 20:41:12',1,' '),(35.00,'HUM','2020-05-31 20:41:15',1,' '),(35.00,'HUM','2020-05-31 20:41:17',1,' '),(35.00,'HUM','2020-05-31 20:41:19',1,' '),(35.00,'HUM','2020-05-31 20:41:21',1,' '),(35.00,'HUM','2020-05-31 20:41:23',1,' '),(35.00,'HUM','2020-05-31 20:41:25',1,' '),(35.00,'HUM','2020-05-31 20:41:27',1,' '),(35.00,'HUM','2020-05-31 20:41:29',1,' '),(35.00,'HUM','2020-05-31 20:41:31',1,' '),(35.00,'HUM','2020-05-31 20:41:33',1,' '),(35.00,'HUM','2020-05-31 20:41:36',1,' '),(35.00,'HUM','2020-05-31 20:41:38',1,' '),(35.00,'HUM','2020-05-31 20:41:40',1,' '),(35.00,'HUM','2020-05-31 20:41:42',1,' '),(35.00,'HUM','2020-05-31 20:41:44',1,' '),(35.00,'HUM','2020-05-31 20:41:46',1,' '),(35.00,'HUM','2020-05-31 20:41:48',1,' '),(35.00,'HUM','2020-05-31 20:41:50',1,' '),(35.00,'HUM','2020-05-31 20:41:52',1,' '),(35.00,'HUM','2020-05-31 20:41:55',1,' '),(35.00,'HUM','2020-05-31 20:41:57',1,' '),(35.00,'HUM','2020-05-31 20:41:59',1,' '),(35.00,'HUM','2020-05-31 20:42:01',1,' '),(35.00,'HUM','2020-05-31 20:42:03',1,' '),(35.00,'HUM','2020-05-31 20:42:05',1,' '),(35.00,'HUM','2020-05-31 20:42:07',1,' '),(35.00,'HUM','2020-05-31 20:42:09',1,' '),(35.00,'HUM','2020-05-31 20:42:11',1,' '),(35.00,'HUM','2020-05-31 20:42:14',1,' '),(35.00,'HUM','2020-05-31 20:42:16',1,' '),(35.00,'HUM','2020-05-31 20:42:18',1,' '),(35.00,'HUM','2020-05-31 20:42:20',1,' '),(35.00,'HUM','2020-05-31 20:42:22',1,' '),(35.00,'HUM','2020-05-31 20:42:24',1,' '),(35.00,'HUM','2020-05-31 20:42:26',1,' '),(35.00,'HUM','2020-05-31 20:42:28',1,' '),(35.00,'HUM','2020-05-31 20:42:30',1,' '),(35.00,'HUM','2020-05-31 21:42:33',1,' '),(35.00,'HUM','2020-05-31 21:42:35',1,' '),(35.00,'HUM','2020-05-31 21:42:37',1,' '),(35.00,'HUM','2020-05-31 21:42:39',1,' '),(35.00,'HUM','2020-05-31 21:42:41',1,' '),(35.00,'HUM','2020-05-31 21:42:44',1,' '),(35.00,'HUM','2020-05-31 21:42:46',1,' '),(35.00,'HUM','2020-05-31 21:42:48',1,' '),(35.00,'HUM','2020-05-31 21:42:50',1,' '),(35.00,'HUM','2020-05-31 21:43:03',1,' '),(35.00,'HUM','2020-05-31 21:43:05',1,' '),(35.00,'HUM','2020-05-31 21:43:07',1,' '),(35.00,'HUM','2020-05-31 21:43:09',1,' '),(35.00,'HUM','2020-05-31 21:43:11',1,' '),(35.00,'HUM','2020-05-31 21:43:14',1,' '),(35.00,'HUM','2020-05-31 21:43:16',1,' '),(35.00,'HUM','2020-05-31 21:43:18',1,' '),(35.00,'HUM','2020-05-31 21:43:20',1,' '),(35.00,'HUM','2020-05-31 21:43:22',1,' '),(35.00,'HUM','2020-05-31 21:43:24',1,' '),(35.00,'HUM','2020-05-31 21:43:26',1,' '),(35.00,'HUM','2020-05-31 21:43:28',1,' '),(35.00,'HUM','2020-05-31 21:43:30',1,' '),(35.00,'HUM','2020-05-31 21:43:32',1,' '),(35.00,'TMP','2020-05-31 20:42:16',1,' '),(35.50,'TMP','2020-05-31 20:42:18',1,' '),(36.00,'TMP','2020-05-31 20:42:20',1,' '),(36.50,'TMP','2020-05-31 20:42:22',1,' '),(37.00,'TMP','2020-05-31 20:42:24',1,' '),(37.50,'TMP','2020-05-31 20:42:26',1,' '),(38.00,'TMP','2020-05-31 20:42:28',1,' '),(38.50,'TMP','2020-05-31 20:42:30',1,' '),(2807.00,'LUM','2020-05-31 20:41:12',1,' '),(2871.00,'LUM','2020-05-31 20:42:07',1,' '),(2875.00,'LUM','2020-05-31 20:41:33',1,' '),(2886.00,'LUM','2020-05-31 21:42:46',1,' '),(2889.00,'LUM','2020-05-31 20:41:29',1,' '),(2898.00,'LUM','2020-05-31 20:41:55',1,' '),(2911.00,'LUM','2020-05-31 20:41:57',1,' '),(2918.00,'LUM','2020-05-31 20:41:40',1,' '),(2920.00,'LUM','2020-05-31 20:42:16',1,' '),(2923.00,'LUM','2020-05-31 21:43:14',1,' '),(2925.00,'LUM','2020-05-31 21:43:07',1,' '),(2936.00,'LUM','2020-05-31 20:42:28',1,' '),(2946.00,'LUM','2020-05-31 20:41:21',1,' '),(2948.00,'LUM','2020-05-31 20:41:19',1,' '),(2948.00,'LUM','2020-05-31 20:41:31',1,' '),(2948.00,'LUM','2020-05-31 21:42:37',1,' '),(2952.00,'LUM','2020-05-31 20:42:03',1,' '),(2968.00,'LUM','2020-05-31 21:43:09',1,' '),(2981.00,'LUM','2020-05-31 20:42:22',1,' '),(2982.00,'LUM','2020-05-31 20:41:06',1,' '),(3002.00,'LUM','2020-05-31 21:43:16',1,' '),(3030.00,'LUM','2020-05-31 21:43:32',1,' '),(3035.00,'LUM','2020-05-31 20:41:25',1,' '),(3035.00,'LUM','2020-05-31 21:43:11',1,' '),(3038.00,'LUM','2020-05-31 21:42:33',1,' '),(3042.00,'LUM','2020-05-31 20:41:10',1,' '),(3063.00,'LUM','2020-05-31 21:42:50',1,' '),(3066.00,'LUM','2020-05-31 21:43:24',1,' '),(3072.00,'LUM','2020-05-31 21:42:39',1,' '),(3076.00,'LUM','2020-05-31 20:41:46',1,' '),(3077.00,'LUM','2020-05-31 21:42:48',1,' '),(3092.00,'LUM','2020-05-31 20:42:01',1,' '),(3096.00,'LUM','2020-05-31 20:41:27',1,' '),(3097.00,'LUM','2020-05-31 20:41:50',1,' '),(3099.00,'LUM','2020-05-31 20:41:42',1,' '),(3102.00,'LUM','2020-05-31 20:41:04',1,' '),(3107.00,'LUM','2020-05-31 21:42:44',1,' '),(3126.00,'LUM','2020-05-31 20:41:23',1,' '),(3137.00,'LUM','2020-05-31 20:41:17',1,' '),(3140.00,'LUM','2020-05-31 21:43:20',1,' '),(3142.00,'LUM','2020-05-31 20:42:24',1,' '),(3142.00,'LUM','2020-05-31 21:42:35',1,' '),(3143.00,'LUM','2020-05-31 20:41:59',1,' '),(3153.00,'LUM','2020-05-31 21:43:22',1,' '),(3161.00,'LUM','2020-05-31 21:43:30',1,' '),(3167.00,'LUM','2020-05-31 20:42:26',1,' '),(3188.00,'LUM','2020-05-31 20:42:14',1,' '),(3194.00,'LUM','2020-05-31 20:41:36',1,' '),(3200.00,'LUM','2020-05-31 20:41:44',1,' '),(3206.00,'LUM','2020-05-31 20:42:05',1,' '),(3213.00,'LUM','2020-05-31 20:41:15',1,' '),(3220.00,'LUM','2020-05-31 20:41:08',1,' '),(3221.00,'LUM','2020-05-31 20:42:18',1,' '),(3221.00,'LUM','2020-05-31 20:42:20',1,' '),(3225.00,'LUM','2020-05-31 20:42:11',1,' '),(3232.00,'LUM','2020-05-31 20:41:38',1,' '),(3232.00,'LUM','2020-05-31 21:43:05',1,' '),(3236.00,'LUM','2020-05-31 20:42:30',1,' '),(3243.00,'LUM','2020-05-31 21:43:18',1,' '),(3244.00,'LUM','2020-05-31 21:42:41',1,' '),(3246.00,'LUM','2020-05-31 20:41:48',1,' '),(3261.00,'LUM','2020-05-31 21:43:26',1,' '),(3264.00,'LUM','2020-05-31 20:41:52',1,' '),(3264.00,'LUM','2020-05-31 20:42:09',1,' '),(3269.00,'LUM','2020-05-31 21:43:03',1,' '),(3296.00,'LUM','2020-05-31 21:43:28',1,' ');
/*!40000 ALTER TABLE `medicaosensores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ronda_extra`
--

DROP TABLE IF EXISTS `ronda_extra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ronda_extra` (
  `email` varchar(100) NOT NULL,
  `ronda_inicio` timestamp NOT NULL,
  `ronda_fim` timestamp NOT NULL,
  PRIMARY KEY (`email`,`ronda_inicio`),
  CONSTRAINT `fk_ronda_inicio_email` FOREIGN KEY (`email`) REFERENCES `utilizador` (`EmailUtilizador`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ronda_extra`
--

LOCK TABLES `ronda_extra` WRITE;
/*!40000 ALTER TABLE `ronda_extra` DISABLE KEYS */;
/*!40000 ALTER TABLE `ronda_extra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ronda_planeada`
--

DROP TABLE IF EXISTS `ronda_planeada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ronda_planeada` (
  `email` varchar(100) NOT NULL,
  `ronda_inicio` timestamp NOT NULL,
  `ronda_fim` timestamp NOT NULL,
  PRIMARY KEY (`email`,`ronda_inicio`),
  CONSTRAINT `fk_ronda_fim_email` FOREIGN KEY (`email`) REFERENCES `utilizador` (`EmailUtilizador`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ronda_planeada`
--

LOCK TABLES `ronda_planeada` WRITE;
/*!40000 ALTER TABLE `ronda_planeada` DISABLE KEYS */;
/*!40000 ALTER TABLE `ronda_planeada` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sistema`
--

DROP TABLE IF EXISTS `sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sistema` (
  `LimiteTemperatura` decimal(6,2) NOT NULL,
  `LimiteHumidade` decimal(6,2) NOT NULL,
  `LimiteLuminosidade` decimal(6,2) NOT NULL,
  `margem_temperatura` decimal(6,2) NOT NULL,
  `margem_humidade` decimal(6,2) NOT NULL,
  `margem_luminosidade` decimal(6,2) NOT NULL,
  `num_mov_alerta` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sistema`
--

LOCK TABLES `sistema` WRITE;
/*!40000 ALTER TABLE `sistema` DISABLE KEYS */;
INSERT INTO `sistema` VALUES (50.00,30.00,20.00,0.00,0.00,0.00,0);
/*!40000 ALTER TABLE `sistema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilizador`
--

DROP TABLE IF EXISTS `utilizador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilizador` (
  `EmailUtilizador` varchar(100) NOT NULL,
  `NomeUtilizador` varchar(200) NOT NULL,
  `TipoUtilizador` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`EmailUtilizador`),
  UNIQUE KEY `EmailUtilizador_UNIQUE` (`EmailUtilizador`),
  UNIQUE KEY `NomeUtilizador_UNIQUE` (`NomeUtilizador`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilizador`
--

LOCK TABLES `utilizador` WRITE;
/*!40000 ALTER TABLE `utilizador` DISABLE KEYS */;
/*!40000 ALTER TABLE `utilizador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'sid_2'
--

--
-- Dumping routines for database 'sid_2'
--
/*!50003 DROP PROCEDURE IF EXISTS `Medicao_de_sensor_erradas` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`php`@`localhost` PROCEDURE `Medicao_de_sensor_erradas`()
BEGIN
	select * from sid_2.medicaosensores where Controlo = false;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-31 20:44:10
