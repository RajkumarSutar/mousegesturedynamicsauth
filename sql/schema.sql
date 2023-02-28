CREATE DATABASE `behaviometrics` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `behaviometrics`;
CREATE TABLE `features` (
  `FeatureID` bigint NOT NULL AUTO_INCREMENT,
  `FeatureName` varchar(30) DEFAULT NULL,
  `FeatureData` longtext,
  `ModuleName` varchar(3) DEFAULT NULL,
  `UserID` varchar(10) DEFAULT NULL,
  `FeatureTime` date DEFAULT NULL,
  `GestureName` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`FeatureID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `featureset` (
  `FeatureSetID` bigint NOT NULL AUTO_INCREMENT,
  `FeatureSetName` varchar(3) DEFAULT NULL,
  `FeatureSetData` text CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `UserID` varchar(10) DEFAULT NULL,
  `SessionID` bigint DEFAULT NULL,
  `GestureName` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`FeatureSetID`),
  KEY `UNIQ` (`UserID`,`SessionID`,`FeatureSetName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `gesture` (
  `GestureID` int NOT NULL,
  `GestureName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`GestureID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `replications` (
  `ReplicationID` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ReplicationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `session` (
  `SessionID` bigint NOT NULL AUTO_INCREMENT,
  `UserID` varchar(10) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  PRIMARY KEY (`SessionID`),
  KEY `IDX_USR_SESSION` (`SessionID`,`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `user` (
  `Catagory` int NOT NULL AUTO_INCREMENT,
  `UserName` varchar(20) DEFAULT NULL,
  `Mobile` varchar(10) NOT NULL DEFAULT '',
  `Profession` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`Catagory`),
  UNIQUE KEY `Mobile` (`Mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `usergesture` (
  `UserGestureID` bigint NOT NULL AUTO_INCREMENT,
  `UserID` varchar(10) NOT NULL,
  `GestureID` int DEFAULT NULL,
  `GestureData` varchar(5000) DEFAULT NULL,
  `CreationDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `UpdationDate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserGestureID`)
) ENGINE=InnoDB AUTO_INCREMENT=601 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `weights` (
  `Category` int DEFAULT '0',
  `Weights` text CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `ModuleID` varchar(3) DEFAULT NULL,
  `Gesture` varchar(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





