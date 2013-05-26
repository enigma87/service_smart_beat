delimiter $$

CREATE DATABASE `genie` /*!40100 DEFAULT CHARACTER SET utf8 */$$


delimiter $$

CREATE TABLE genie.`user` (
  `userid` varchar(128) NOT NULL,
  `access_token` varchar(200) NOT NULL,
  `access_token_type` text NOT NULL,
  `first_name` text NOT NULL,
  `middle_name` text,
  `last_name` text,
  `dob` date DEFAULT NULL,
  `email` varchar(100) NOT NULL,  
  `image_url` text,  
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

CREATE TABLE genie.`user_heart_rate_test` (
  `userid` varchar(128) NOT NULL,
  `resting_heart_rate` int(11) DEFAULT NULL,
  `resting_heart_rate_timestamp` timestamp NULL DEFAULT NULL,
  `maximal_heart_rate` int(11) DEFAULT NULL,
  `maximal_heart_rate_timestamp` timestamp NULL DEFAULT NULL,
  `threshold_heart_rate` int(11) DEFAULT NULL,
  `threshold_heart_rate_timestamp` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `userid_UNIQUE` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE genie.`fitness_training_session` (
  `userid` varchar(128) NOT NULL,
  `training_session_id` MEDIUMINT UNSIGNED NOT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `hrz_1_time` DOUBLE DEFAULT NULL,
  `hrz_2_time` DOUBLE DEFAULT NULL,
  `hrz_3_time` DOUBLE DEFAULT NULL,
  `hrz_4_time` DOUBLE DEFAULT NULL,
  `hrz_5_time` DOUBLE DEFAULT NULL,
  `hrz_6_time` DOUBLE DEFAULT NULL,
  `hrz_1_distance` DOUBLE DEFAULT NULL,
  `hrz_2_distance` DOUBLE DEFAULT NULL,
  `hrz_3_distance` DOUBLE DEFAULT NULL,
  `hrz_4_distance` DOUBLE DEFAULT NULL,
  `hrz_5_distance` DOUBLE DEFAULT NULL,
  `hrz_6_distance` DOUBLE DEFAULT NULL,  
  PRIMARY KEY(`training_session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$



CREATE USER 'genie'@'localhost' IDENTIFIED BY 'genie';

GRANT ALL PRIVILEGES ON genie.* To 'genie'@'localhost' IDENTIFIED BY 'genie';

