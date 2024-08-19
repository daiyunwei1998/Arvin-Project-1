-- MySQL dump 10.13  Distrib 9.0.0, for macos14 (arm64)
--
-- Host: localhost    Database: stylish
-- ------------------------------------------------------
-- Server version	9.0.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `campaigns`
--

DROP TABLE IF EXISTS `campaigns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaigns` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `product_id` bigint unsigned NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `story` text,
  `content` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaigns`
--

LOCK TABLES `campaigns` WRITE;
/*!40000 ALTER TABLE `campaigns` DISABLE KEYS */;
INSERT INTO `campaigns` VALUES (1,2,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/9e301d2849e443b0b446c4e181ee72d4.jpg','印象《都會故事集》','瞬間 \\n 在城市的角落 \\n 找到失落多時的記憶。'),(2,8,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/1383a2df87ea49228b4a6bda7fa0948e.jpg','復古《再一次經典》','永遠 \\n 展現自信與專業 \\n 無法抵擋的男人魅力。'),(3,9,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/f99681b9aa864111a3744796ba643251.jpg','不朽《與自己和好如初》','於是 \\n 我也想要給你 \\n 一個那麼美好的自己。');
/*!40000 ALTER TABLE `campaigns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colors`
--

DROP TABLE IF EXISTS `colors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `colors` (
  `name` varchar(255) NOT NULL,
  `code` varchar(6) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colors`
--

LOCK TABLES `colors` WRITE;
/*!40000 ALTER TABLE `colors` DISABLE KEYS */;
INSERT INTO `colors` VALUES ('深藍','334455'),('淺棕','bb7744'),('淺灰','cccccc'),('淺藍','ddf0ff'),('亮綠','ddffbb'),('粉紅','ffdddd'),('白色','ffffff');
/*!40000 ALTER TABLE `colors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `order_item_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `product_id` varchar(20) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `variant_id` bigint DEFAULT NULL,
  PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `shipping` varchar(50) DEFAULT NULL,
  `payment` varchar(50) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `freight` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `recipient_id` int DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '0',
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` int NOT NULL,
  `rec_trade_id` varchar(255) NOT NULL,
  `bank_transaction_id` varchar(255) NOT NULL,
  `card_identifier` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `order_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `places`
--

DROP TABLE IF EXISTS `places`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `places` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `places`
--

LOCK TABLES `places` WRITE;
/*!40000 ALTER TABLE `places` DISABLE KEYS */;
INSERT INTO `places` VALUES (1,'中國'),(2,'越南');
/*!40000 ALTER TABLE `places` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_colors`
--

DROP TABLE IF EXISTS `product_colors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_colors` (
  `fk_product_id` bigint unsigned NOT NULL,
  `fk_color_code` varchar(6) NOT NULL,
  PRIMARY KEY (`fk_product_id`,`fk_color_code`),
  KEY `product_colors_ibfk_2` (`fk_color_code`),
  CONSTRAINT `product_colors_ibfk_1` FOREIGN KEY (`fk_product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `product_colors_ibfk_2` FOREIGN KEY (`fk_color_code`) REFERENCES `colors` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_colors`
--

LOCK TABLES `product_colors` WRITE;
/*!40000 ALTER TABLE `product_colors` DISABLE KEYS */;
INSERT INTO `product_colors` VALUES (3,'334455'),(8,'334455'),(17,'334455'),(4,'bb7744'),(9,'bb7744'),(1,'cccccc'),(2,'cccccc'),(3,'cccccc'),(4,'cccccc'),(6,'cccccc'),(7,'cccccc'),(15,'cccccc'),(17,'cccccc'),(3,'ddf0ff'),(5,'ddf0ff'),(9,'ddf0ff'),(12,'ddf0ff'),(14,'ddf0ff'),(16,'ddf0ff'),(17,'ddf0ff'),(1,'ddffbb'),(2,'ddffbb'),(4,'ddffbb'),(7,'ddffbb'),(14,'ddffbb'),(11,'ffdddd'),(13,'ffdddd'),(1,'ffffff'),(5,'ffffff'),(6,'ffffff'),(7,'ffffff'),(11,'ffffff'),(12,'ffffff'),(13,'ffffff'),(15,'ffffff'),(16,'ffffff');
/*!40000 ALTER TABLE `product_colors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `image_url` varchar(255) NOT NULL,
  `fk_product_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`fk_product_id`),
  CONSTRAINT `product_images_ibfk_1` FOREIGN KEY (`fk_product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (1,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/373bcd73616a47cf855378a2575cd966.jpg',1),(2,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/f70b221564164ae8875fed8158f8d2de.jpg',1),(3,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/bd8a9e91a98146d2a14888db4159193d.jpg',1),(4,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/4f87815ab0294e26914c473e7ae37c23.jpg',2),(5,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/84d1fe11f26b4dab9e2f268a82b9ec6d.jpg',2),(6,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/519bd6cfed6a467cb38adc0807568723.jpg',2),(7,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/44729514519843a594efcdf37c2f3f5d.jpg',4),(8,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/65b950af3b6c47eababaa8a9752732c5.jpg',4),(9,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/0e298f1316b64c1099013b59d151896b.jpg',4),(10,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/27ac96337ad849b6a237461ae19dfc83.jpg',5),(11,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/70bc698295f646e9bf4907c6b6cbc603.jpg',5),(12,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/b0bef208b09149d0aaa12e3d6b426007.jpg',5),(13,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/667621c0e1ff434798330d7a0642cb5b.jpg',6),(14,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/db74f4bda3fc4a94b0b9262a4bc2e103.jpg',6),(15,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/a9b0bd4765934197b39669fad73a58ae.jpg',6),(16,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/42bab1e3c3a743e69d40af55577c4c6f.jpg',7),(17,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/5375c6a2688d4d408d70c112eb0b62f8.jpg',7),(18,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/d68744a476c5428a9c9f92fc63bfe839.jpg',7),(19,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/4b9a122d6d1547888f06ae40286d340d.jpg',8),(20,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/3e2ba61163074d8db1c1d426e3714108.jpg',8),(21,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/56cbf3bda1864bcbaaeb88636aa9597d.jpg',8),(22,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/0e72253d282341aeae9e163d1d2fe643.jpg',9),(23,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/8330c53d4d5346ec9d9ca9d7c67c91e1.jpg',9),(24,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/0715550ae9b04993a8ac5561edc0e16c.jpg',9),(25,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/348d5de9af6446548a2ba98d8a37dd33.jpg',10),(26,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/506cbf91335e43f182c0f6f24784e973.jpg',10),(27,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/14b78fb614f54aa3ae3801edcdb1b253.jpg',10),(28,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/09dbd5befc5047168af8a5c0df877685.jpg',11),(29,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/750c2d771ff542e2b54fed435a9cec13.jpg',11),(30,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/fb8902b7fe2240d489acd74b1320159f.jpg',11),(31,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/387b4d330a8a4c2e88563880c9cc786c.jpg',12),(32,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/62aa8f04f85f41bf90f771ebea8f1056.jpg',12),(33,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/6a8a37749a1e433a9dca125323072395.jpg',12),(34,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/869e052efcca4feba3e52212058abab3.jpg',13),(35,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/afcd218fe16341019fc1681c8ae385a9.jpg',13),(36,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/08d3bbda2d8c426eadb476a5b2cd35ba.jpg',13),(37,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/39495ece40e247d09d53a9ebdbf328a6.jpg',14),(38,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/55d0a34de04c4331b6a4eaa39f30778b.jpg',14),(39,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/bb2ecad20b3c4ebd88e14489cfd0e1b4.jpg',14),(40,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/d67eb7570d4c4316ad45b12ff505226d.jpg',15),(41,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/a6d7080e8373450fbd6451853fe08600.jpg',15),(42,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/5ecb3253bd604a5382557dc6f194b0e5.jpg',15),(43,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/2945af3ddbd0410998554c487a5d8cf1.jpg',16),(44,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/2a99f1f0fc304b05ba7076e7562f3292.jpg',16),(45,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/ba111fe335d34ee69fc97e1c1763a22b.jpg',16),(46,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/be4d8d2afdb14953af697a6fc7c31e51.jpg',17),(47,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/52e893e644d04a7bb8529bffaf5d1aa0.jpg',17),(48,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/b4754617b95444fabcd5dd82976874f5.jpg',17),(49,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/1383a2df87ea49228b4a6bda7fa0948e.jpg',8),(50,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/f99681b9aa864111a3744796ba643251.jpg',9),(51,'https://stylish-matcha.s3.ap-northeast-3.amazonaws.com/9e301d2849e443b0b446c4e181ee72d4.jpg',2);
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_sizes`
--

DROP TABLE IF EXISTS `product_sizes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_sizes` (
  `fk_product_id` bigint unsigned NOT NULL,
  `fk_size_id` int unsigned NOT NULL,
  PRIMARY KEY (`fk_product_id`,`fk_size_id`),
  KEY `fk_size_id` (`fk_size_id`),
  CONSTRAINT `fk_product_id` FOREIGN KEY (`fk_product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_size_id` FOREIGN KEY (`fk_size_id`) REFERENCES `sizes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_sizes`
--

LOCK TABLES `product_sizes` WRITE;
/*!40000 ALTER TABLE `product_sizes` DISABLE KEYS */;
INSERT INTO `product_sizes` VALUES (1,1),(2,1),(4,1),(5,1),(6,1),(7,1),(8,1),(11,1),(12,1),(13,1),(16,1),(17,1),(1,2),(2,2),(4,2),(5,2),(6,2),(7,2),(8,2),(9,2),(10,2),(13,2),(14,2),(15,2),(16,2),(17,2),(1,3),(2,3),(5,3),(6,3),(7,3),(8,3),(9,3),(10,3),(14,3),(15,3),(16,3),(17,3);
/*!40000 ALTER TABLE `product_sizes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `fk_category_id` int unsigned NOT NULL,
  `price` decimal(10,0) NOT NULL,
  `texture` varchar(255) NOT NULL,
  `wash` varchar(255) NOT NULL,
  `fk_place_id` int unsigned NOT NULL,
  `note` text,
  `story` text,
  `fk_main_image_id` bigint unsigned DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'前開衩扭結洋裝',2,799,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',3,'厚薄：薄\\r\\n彈性：無'),(2,'透肌澎澎防曬襯衫',2,599,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',6,'厚薄：薄\\r\\n彈性：無'),(4,'小扇紋細織上衣',2,599,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',9,'厚薄：薄\\r\\n彈性：無'),(5,'純色輕薄百搭襯衫',1,799,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',12,'厚薄：薄\\r\\n彈性：無'),(6,'時尚輕鬆休閒西裝',1,2399,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',15,'厚薄：薄\\r\\n彈性：無'),(8,'經典商務西裝',1,3999,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',21,'厚薄：薄\\r\\n彈性：無'),(9,'夏日海灘戶外遮陽帽',3,1499,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',24,'厚薄：薄\\r\\n彈性：無'),(10,'經典牛仔帽',3,799,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',27,'厚薄：薄\\r\\n彈性：無'),(11,'卡哇伊多功能隨身包',3,1299,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',30,'厚薄：薄\\r\\n彈性：無'),(12,'柔軟氣質羊毛圍巾',3,1799,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',33,'厚薄：薄\\r\\n彈性：無'),(13,'精緻扭結洋裝',2,999,'棉 100%','手洗',2,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',36,'厚薄：薄\\r\\n彈性：無'),(14,'透肌澎澎薄紗襯衫',2,999,'棉 100%','手洗',2,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',39,'厚薄：薄\\r\\n彈性：無'),(15,'小扇紋質感上衣',2,999,'棉 100%','手洗',2,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',42,'厚薄：薄\\r\\n彈性：無'),(16,'經典修身長筒牛仔褲',2,1999,'棉 100%','手洗',2,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',45,'厚薄：薄\\r\\n彈性：無'),(17,'活力花紋長筒牛仔褲',2,1299,'棉 100%','手洗，溫水',1,'實品顏色依單品照為主','O.N.S is all about options, which is why we took our staple polo shirt and upgraded it with slubby linen jersey, making it even lighter for those who prefer their summer style extra-breezy.',48,'厚薄：薄\\r\\n彈性：無');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipients`
--

DROP TABLE IF EXISTS `recipients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recipients` (
  `recipient_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` text,
  `time` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`recipient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipients`
--

LOCK TABLES `recipients` WRITE;
/*!40000 ALTER TABLE `recipients` DISABLE KEYS */;
/*!40000 ALTER TABLE `recipients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sizes`
--

DROP TABLE IF EXISTS `sizes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sizes` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `size` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sizes`
--

LOCK TABLES `sizes` WRITE;
/*!40000 ALTER TABLE `sizes` DISABLE KEYS */;
INSERT INTO `sizes` VALUES (1,'S'),(2,'M'),(3,'L');
/*!40000 ALTER TABLE `sizes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `provider` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'facebook','Yun-wei Dai','daiyunwei1998@gmail.com','https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=2890145804470738&height=200&width=200&ext=1724760277&hash=AbY-jG2aowS1OmYxAvVNsTyI','$2a$10$cnVeHnJptRNzkSTogPTALu6p4GjhgG/izaVAL9QsfSMVYygto/c5e'),(2,'native','1111','1@gma11il.com',NULL,'$2a$10$DA2fLtWpjYC4mQ9wzvicTu8fF4mQ80hj0SWRL/ZGinqsPQY7OxPVi'),(4,'native','test','test@test.com','','$2a$10$UHur4mXLfDBiE3GsI4v8WO6/aqJAL4WYIPwY0VswRFPLxfak5Kcji'),(6,'native','test1','tes1t@test.com','','$2a$10$a5UDaltcnNFGZtWJj5LgJOJ/nqT9lFpSDkiVPo2nHT8pvNKYps3Tu'),(8,'native','test21','tes21t@test.com','','$2a$10$IweqgSw7JsCqFCoc3fbDy.eVIvafSw2yCJV37EwKztmCB5wLqMh0.'),(10,'native','tes13121','tes21t@tes331t.com','','$2a$10$B0uOZs7kg.IACsEDXDc54.qgnahIvv378o4hXsYKy5uzM0wMzdStC'),(11,'native','tes11313121','tes21t13@tes331t.com','null','$2a$10$Zms.QhTxbq5Bi1RL53GNi.d2eh9Ii/72pAF5OLsRt0QoAXWZULcYW'),(12,'native','tes131313121','tes231t13@tes331t.com','null','$2a$10$gGJ5o2KZUcuUQSzUTHCH/uOatO5rmBYTY7WF2A69C3tOJMXszMaDG'),(14,'native','tes1321313121','tes2321t13@tes331t.com','null','$2a$10$83aBssWPfAZcgQyAzvqSru4OVS3qSSyBl./IZ9h.O.T0Fbfyh5Yj2'),(15,'native','tes1323131313121','tes23211331t13@tes331t.com','null','$2a$10$DsBfoQl4V7H7GxL8Dqh6euFEsNiT/N4SUdsKhAq96rfgr9mO0aRWy'),(17,'native','tes1323133131313121','tes2321131331t13@tes331t.com','null','$2a$10$gqx6Ub/swet625Hi4vXb5u1VyzV.ZfIhneh4We6.nr1P0.2L/6IDu'),(18,'native','tes1323133131313121','tes232113123331t13@tes331t.com','null','$2a$10$W.2fo2n/5zRo2HEtFgu2OO5ux0IwEcWyydW5l350khEDtMxcD/YL.'),(20,'native','native','m3232@qq.com','null','$2a$10$kZqFpBw0co8jw/KlXLwLYOVqTxpQ0TXkeaC6AMIrmFibOMKZW.13W'),(28,'native','nat3ive','m33232@qq.com','null','$2a$10$aAz5955cvBF4ncWahAxxA.pYuEOWL.Rt/sn3v.yMofTY3LduVfXca'),(30,'native','nat3qive','m33q232@qq.com','null','$2a$10$f2fXUv/SWLWvKXUFZRrog.aRhQzM2/g8YO0BKE9w2MWDHLr6B/7oO'),(32,'native','natqq3qive','m3q3q232@qq.com','null','$2a$10$Lh6et20SI1Qr4j3ewU.tG.cXezLPU2EPUkNwj5NZ2KKSuL0UqRQku'),(36,'native','natq23qive','m3q3q232@2qq.com','null','$2a$10$XD93TXL5UCHi8X7Spoh/j.ze.FaWimQEmeqZ8tviyiXN3dqtvSeMa'),(38,'native','natq23eqive','m3qe3q232@2qq.com','null','$2a$10$jkWgRz1XZcQPdoo4wsz/wuSgXh2NGZnFiyY7Yjj5nWEJuS7R58wpu'),(40,'native','natq23eqqive','m3qe3q232q@2qq.com','null','$2a$10$Zc9Z38mSzpuGdzZBzPtvJODaW7TG0v2n.ZZ5unCDqCsg5IaIa3RQa'),(41,'native','ArvinTest','testArvin@gmail.com','null','$2a$10$ZOhINy1DJpOZi48Llee7OO3E0uMXwaIGJox5fHxpcMooV6vgAyXUG'),(43,'native','ArvinTest2','ArvinTest2@gmail.com','null','$2a$10$sPMU289AAfREx/LoDUGxjuLIGkSmv3UOkcYH12qQHJw8AHShV3AiO'),(45,'native','yeqeqwe','1@1.com','null','$2a$10$j.lOAuwfaane/LZxKm0DFu.m61sPL9sIgn6ME5xwKSFoigbObaUl6'),(46,'native','123213131','31232@1.1313','null','$2a$10$m69Hqf3UOCCsNF2aiyGY9eGaPgzgAZqQUY5Zi1zizBOZ2on6N.VmO'),(47,'native','arvinTestTest','arvin@arvin.com','null','$2a$10$ERNiv0GeXc6x71MJBSGTcOCt5z8cGiIrOj22Kv6b1NEHGJpv69oji'),(48,'native','test123','test123@123.com','null','$2a$10$LP4Gat9eLZY.oFvbbsS52ONakIXN7sW4QIIcDMkGT/wQTH/3mfo1.'),(49,'native','Test2','test2@test2.com','null','$2a$10$TiEizLvNtPyBB4cq6Lsh7OdUR7gbL4nz2qKk16vOZVK2pyiyQX3Aa'),(52,'native','Test2','test3@test3.com','null','$2a$10$/1csesoEPYoeo2gHuW3ZvuMBjCoeYOa.LKdqgF4u72wi.0SlOYV5W'),(53,'native','123213132131','1313132131@1313131313.com','null','$2a$10$RD1RcOlqzIG38ca1rf2uR.qKYLQvsi8wcbnE80QNzAKqqFoq6nsRS'),(54,'native','1232131321231','13131322131@1313131313.com','null','$2a$10$c2y/ZFz5DSN2McmC25Q7UuqG/OykbVxdLK9b3RBTxMfNAAbLgJxgq'),(55,'native','1232131321231113131','13131322131@13313131313.com','null','$2a$10$J1inOz3yFJRSL/Qr/5jzZOXifkakmS0R5oqqM6bdJyErHlmBtY9J2'),(56,'native','1232132312','13131233@13.com','null','$2a$10$SDE9CzxY4NsnaKKKVgFS8.avJfeSZ5FUubM8q3PMritl.DxmEja1y'),(58,'native','test4','test4@test4.com','null','$2a$10$4oQ0ua3NeQiZitVL5xmQzO3yzrlFvHXElH3.zNn.S6VEFIUMKlgLy');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `variants`
--

DROP TABLE IF EXISTS `variants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `variants` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `fk_product_id` bigint unsigned NOT NULL,
  `fk_color_code` varchar(255) DEFAULT NULL,
  `fk_size_id` int unsigned NOT NULL,
  `stock` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`fk_product_id`),
  KEY `color_id` (`fk_color_code`),
  KEY `size_id` (`fk_size_id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `variants`
--

LOCK TABLES `variants` WRITE;
/*!40000 ALTER TABLE `variants` DISABLE KEYS */;
INSERT INTO `variants` VALUES (1,1,'FFFFFF',1,2),(2,1,'FFFFFF',2,1),(3,1,'FFFFFF',3,2),(4,1,'DDFFBB',1,9),(5,1,'DDFFBB',2,0),(6,1,'DDFFBB',3,5),(7,1,'CCCCCC',1,8),(8,1,'CCCCCC',2,5),(9,1,'CCCCCC',3,9),(10,2,'DDFFBB',1,7),(11,2,'DDFFBB',2,5),(12,2,'DDFFBB',3,8),(13,2,'CCCCCC',1,1),(14,2,'CCCCCC',2,6),(15,2,'CCCCCC',3,2),(16,4,'DDFFBB',1,3),(17,4,'DDFFBB',2,5),(18,4,'CCCCCC',1,4),(19,4,'CCCCCC',2,1),(20,4,'BB7744',1,2),(21,4,'BB7744',2,6),(22,5,'FFFFFF',1,5),(23,5,'FFFFFF',2,7),(24,5,'FFFFFF',3,1),(25,5,'DDF0FF',1,1),(26,5,'DDF0FF',2,4),(27,5,'DDF0FF',3,3),(28,6,'FFFFFF',1,10),(29,6,'FFFFFF',2,5),(30,6,'FFFFFF',3,6),(31,6,'CCCCCC',1,1),(32,6,'CCCCCC',2,3),(33,6,'CCCCCC',3,10),(34,7,'FFFFFF',1,5),(35,7,'FFFFFF',2,5),(36,7,'FFFFFF',3,5),(37,7,'DDFFBB',1,5),(38,7,'DDFFBB',2,5),(39,7,'DDFFBB',3,5),(40,7,'CCCCCC',1,5),(41,7,'CCCCCC',2,5),(42,7,'CCCCCC',3,5),(43,8,'334455',1,9),(44,8,'334455',2,10),(45,8,'334455',3,7),(46,9,'DDF0FF',2,7),(47,9,'DDF0FF',3,1),(48,9,'BB7744',2,3),(49,9,'BB7744',3,1),(50,10,'BB7744',2,5),(51,10,'BB7744',3,1),(52,10,'334455',2,5),(53,10,'334455',3,2),(54,11,'FFFFFF',1,1),(55,11,'FFDDDD',1,2),(56,12,'FFFFFF',1,4),(57,12,'DDF0FF',1,7),(58,13,'FFFFFF',1,0),(59,13,'FFFFFF',2,9),(60,13,'FFDDDD',1,2),(61,13,'FFDDDD',2,1),(62,14,'DDFFBB',2,1),(63,14,'DDFFBB',3,3),(64,14,'DDF0FF',2,4),(65,14,'DDF0FF',3,5),(66,15,'FFFFFF',2,3),(67,15,'FFFFFF',3,1),(68,15,'CCCCCC',2,2),(69,15,'CCCCCC',3,4),(70,16,'FFFFFF',1,9),(71,16,'FFFFFF',2,4),(72,16,'FFFFFF',3,2),(73,16,'DDF0FF',1,0),(74,16,'DDF0FF',2,10),(75,16,'DDF0FF',3,5),(76,17,'DDF0FF',1,3),(77,17,'DDF0FF',1,5),(78,17,'DDF0FF',1,6),(79,17,'CCCCCC',2,2),(80,17,'CCCCCC',2,3),(81,17,'CCCCCC',2,4),(82,17,'334455',1,1),(83,17,'334455',2,6),(84,17,'334455',3,7);
/*!40000 ALTER TABLE `variants` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-05 20:57:52
