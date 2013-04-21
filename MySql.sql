delimiter $$

CREATE DATABASE `genie` /*!40100 DEFAULT CHARACTER SET utf8 */$$


delimiter $$

CREATE TABLE genie.`user` (
  `userid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` text NOT NULL,
  `middle_name` text,
  `last_name` text,
  `email` varchar(100) NOT NULL,
  `password` text,
  `facebook_login` tinyint(1) DEFAULT '0',
  `google_login` tinyint(1) DEFAULT '0',
  `twitter_login` tinyint(1) DEFAULT '0',
  `image_url` text,
  `dob` date DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `created_ts` timestamp NULL DEFAULT NULL,
  `last_updated_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login_ts` timestamp NULL DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `uniq_user_email` (`email`) USING BTREE,
  UNIQUE KEY `userid_UNIQUE` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$


delimiter $$

delimiter $$

CREATE TABLE genie.`user_heart_rate_zone` (
  `userid` bigint(20) NOT NULL,
  `hrz1_start` decimal(9,2) DEFAULT NULL,
  `hrz1_end` decimal(9,2) DEFAULT NULL,
  `hrz2_start` decimal(9,2) DEFAULT NULL,
  `hrz2_end` decimal(9,2) DEFAULT NULL,
  `hrz3_start` decimal(9,2) DEFAULT NULL,
  `hrz3_end` decimal(9,2) DEFAULT NULL,
  `hrz4_start` decimal(9,2) DEFAULT NULL,
  `hrz4_end` decimal(9,2) DEFAULT NULL,
  `hrz5_start` decimal(9,2) DEFAULT NULL,
  `hrz5_end` decimal(9,2) DEFAULT NULL,
  `hrz6_start` decimal(9,2) DEFAULT NULL,
  `hrz6_end` decimal(9,2) DEFAULT NULL,
  `created_ts` timestamp NULL DEFAULT NULL,
  `updated_ts` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `hrt_id_UNIQUE` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE genie.`user_heart_rate_test` (
  `userid` bigint(20) NOT NULL,
  `resting_heart_rate` int(11) DEFAULT NULL,
  `resting_heart_rate_timestamp` timestamp NULL DEFAULT NULL,
  `maximal_heart_rate` int(11) DEFAULT NULL,
  `maximal_heart_rate_timestamp` timestamp NULL DEFAULT NULL,
  `threshold_heart_rate` int(11) DEFAULT NULL,
  `threshold_heart_rate_timestamp` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `userid_UNIQUE` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$




CREATE USER 'genie'@'localhost' IDENTIFIED BY 'genie';

GRANT ALL PRIVILEGES ON genie.* To 'genie'@'localhost' IDENTIFIED BY 'genie';

