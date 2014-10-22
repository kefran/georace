-- --------------------------------------------------------
-- Hôte:                         127.0.0.1
-- Version du serveur:           5.6.20 - MySQL Community Server (GPL)
-- Serveur OS:                   Win32
-- HeidiSQL Version:             8.3.0.4694
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Export de la structure de la base pour georace
DROP DATABASE IF EXISTS `georace`;
CREATE DATABASE IF NOT EXISTS `georace` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `georace`;


-- Export de la structure de table georace. check
DROP TABLE IF EXISTS `check`;
CREATE TABLE IF NOT EXISTS `check` (
  `user` int(11) unsigned NOT NULL,
  `race` int(11) unsigned NOT NULL,
  `checkpoint` int(11) unsigned NOT NULL,
  `date_check` datetime NOT NULL,
  PRIMARY KEY (`user`,`race`,`checkpoint`),
  KEY `FK2_CHECK_RACE` (`race`),
  KEY `FK3_CHECK_CHECKPOINT` (`checkpoint`),
  CONSTRAINT `FK1_CHECK_USER` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK2_CHECK_RACE` FOREIGN KEY (`race`) REFERENCES `race` (`id`),
  CONSTRAINT `FK3_CHECK_CHECKPOINT` FOREIGN KEY (`checkpoint`) REFERENCES `checkpoint` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.check: ~0 rows (environ)
DELETE FROM `check`;
/*!40000 ALTER TABLE `check` DISABLE KEYS */;
/*!40000 ALTER TABLE `check` ENABLE KEYS */;


-- Export de la structure de table georace. checkpoint
DROP TABLE IF EXISTS `checkpoint`;
CREATE TABLE IF NOT EXISTS `checkpoint` (
  `id` int(11) unsigned NOT NULL,
  `name` varchar(50) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `photo` int(11) unsigned DEFAULT NULL,
  `creator` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1_CHECKPOINT_PHOTO` (`photo`),
  KEY `FK2_CHECKPOINT_CREATOR` (`creator`),
  CONSTRAINT `FK1_CHECKPOINT_PHOTO` FOREIGN KEY (`photo`) REFERENCES `photo` (`id`),
  CONSTRAINT `FK2_CHECKPOINT_CREATOR` FOREIGN KEY (`creator`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.checkpoint: ~0 rows (environ)
DELETE FROM `checkpoint`;
/*!40000 ALTER TABLE `checkpoint` DISABLE KEYS */;
/*!40000 ALTER TABLE `checkpoint` ENABLE KEYS */;


-- Export de la structure de table georace. friendship
DROP TABLE IF EXISTS `friendship`;
CREATE TABLE IF NOT EXISTS `friendship` (
  `user` int(11) unsigned NOT NULL,
  `friend` int(11) unsigned NOT NULL,
  `date` datetime,
  PRIMARY KEY (`user`,`friend`),
  KEY `FK2_FRIENDSHIP_FRIEND` (`friend`),
  CONSTRAINT `FK1_FRIENDSHIP_USER` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK2_FRIENDSHIP_FRIEND` FOREIGN KEY (`friend`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='relation d''amitié entre deux utilisateurs';

-- Export de données de la table georace.friendship: ~0 rows (environ)
DELETE FROM `friendship`;
/*!40000 ALTER TABLE `friendship` DISABLE KEYS */;
/*!40000 ALTER TABLE `friendship` ENABLE KEYS */;


-- Export de la structure de table georace. invitation
DROP TABLE IF EXISTS `invitation`;
CREATE TABLE IF NOT EXISTS `invitation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user` int(11) unsigned NOT NULL,
  `guest` int(11) unsigned NOT NULL,
  `race` int(11) unsigned NOT NULL,
  `accepted` tinyint(4) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK1_INVITATION_USER` (`user`),
  KEY `FK2_INVITATION_GUEST` (`guest`),
  KEY `FK3_INVITATION_RACE` (`race`),
  CONSTRAINT `FK1_INVITATION_USER` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK2_INVITATION_GUEST` FOREIGN KEY (`guest`) REFERENCES `user` (`id`),
  CONSTRAINT `FK3_INVITATION_RACE` FOREIGN KEY (`race`) REFERENCES `race` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.invitation: ~0 rows (environ)
DELETE FROM `invitation`;
/*!40000 ALTER TABLE `invitation` DISABLE KEYS */;
/*!40000 ALTER TABLE `invitation` ENABLE KEYS */;


-- Export de la structure de table georace. participation
DROP TABLE IF EXISTS `participation`;
CREATE TABLE IF NOT EXISTS `participation` (
  `user` int(11) unsigned NOT NULL,
  `race` int(11) unsigned NOT NULL,
  `start` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  `finished` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user`,`race`),
  KEY `FK2_PARTICIPATION_RACE` (`race`),
  CONSTRAINT `FK1_PARTICIPATION_USER` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK2_PARTICIPATION_RACE` FOREIGN KEY (`race`) REFERENCES `race` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.participation: ~0 rows (environ)
DELETE FROM `participation`;
/*!40000 ALTER TABLE `participation` DISABLE KEYS */;
/*!40000 ALTER TABLE `participation` ENABLE KEYS */;


-- Export de la structure de table georace. photo
DROP TABLE IF EXISTS `photo`;
CREATE TABLE IF NOT EXISTS `photo` (
  `id` int(11) unsigned NOT NULL,
  `name` int(11) NOT NULL,
  `taken_date` int(11) NOT NULL,
  `path` varchar(250) NOT NULL,
  `user` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1_PHOTO_USER` (`user`),
  CONSTRAINT `FK1_PHOTO_USER` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.photo: ~0 rows (environ)
DELETE FROM `photo`;
/*!40000 ALTER TABLE `photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `photo` ENABLE KEYS */;


-- Export de la structure de table georace. race
DROP TABLE IF EXISTS `race`;
CREATE TABLE IF NOT EXISTS `race` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `date_start` datetime DEFAULT NULL,
  `date_end` datetime DEFAULT NULL,
  `track` int(11) unsigned DEFAULT NULL,
  `organizer` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2_RACE_ORGANIZER` (`organizer`),
  KEY `FK1_RACE_TRACK` (`track`),
  CONSTRAINT `FK1_RACE_TRACK` FOREIGN KEY (`track`) REFERENCES `track` (`id`),
  CONSTRAINT `FK2_RACE_ORGANIZER` FOREIGN KEY (`organizer`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.race: ~0 rows (environ)
DELETE FROM `race`;
/*!40000 ALTER TABLE `race` DISABLE KEYS */;
/*!40000 ALTER TABLE `race` ENABLE KEYS */;


-- Export de la structure de table georace. team
DROP TABLE IF EXISTS `team`;
CREATE TABLE IF NOT EXISTS `team` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.team: ~0 rows (environ)
DELETE FROM `team`;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
/*!40000 ALTER TABLE `team` ENABLE KEYS */;


-- Export de la structure de table georace. track
DROP TABLE IF EXISTS `track`;
CREATE TABLE IF NOT EXISTS `track` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.track: ~0 rows (environ)
DELETE FROM `track`;
/*!40000 ALTER TABLE `track` DISABLE KEYS */;
/*!40000 ALTER TABLE `track` ENABLE KEYS */;


-- Export de la structure de table georace. track_checkpoint
DROP TABLE IF EXISTS `track_checkpoint`;
CREATE TABLE IF NOT EXISTS `track_checkpoint` (
  `track` int(11) unsigned NOT NULL,
  `checkpoint` int(11) NOT NULL,
  PRIMARY KEY (`track`,`checkpoint`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.track_checkpoint: ~0 rows (environ)
DELETE FROM `track_checkpoint`;
/*!40000 ALTER TABLE `track_checkpoint` DISABLE KEYS */;
/*!40000 ALTER TABLE `track_checkpoint` ENABLE KEYS */;


-- Export de la structure de table georace. user
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `latitude` float NOT NULL DEFAULT '47.6424',
  `longitude` float NOT NULL DEFAULT '6.84419',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Export de données de la table georace.user: ~1 rows (environ)
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `login`, `password`, `firstname`, `lastname`, `email`, `latitude`, `longitude`) VALUES
	(1, 'hans', '8800578b51f022c8d8adb9606a8b3db4fedbdac6', 'Han', 'Solo', 'han.solo@starwars.com', 47.6424, 6.84419);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;


-- Export de la structure de table georace. user_team
DROP TABLE IF EXISTS `user_team`;
CREATE TABLE IF NOT EXISTS `user_team` (
  `user` int(11) unsigned NOT NULL,
  `team` int(11) unsigned NOT NULL,
  PRIMARY KEY (`user`,`team`),
  KEY `FK2_UT_TEAM` (`team`),
  CONSTRAINT `FK1_UT_USER` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK2_UT_TEAM` FOREIGN KEY (`team`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export de données de la table georace.user_team: ~0 rows (environ)
DELETE FROM `user_team`;
/*!40000 ALTER TABLE `user_team` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_team` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
