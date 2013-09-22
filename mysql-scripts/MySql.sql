delimiter $$

CREATE DATABASE `genie` /*!40100 DEFAULT CHARACTER SET utf8 */$$


delimiter $$

CREATE TABLE genie.`user` (
  `userid` varchar(128) NOT NULL,
  `access_token` varchar(300) NOT NULL,
  `access_token_type` text NOT NULL,
  `first_name` text NOT NULL,
  `middle_name` text,
  `last_name` text,
  `dob` date DEFAULT NULL,
  `gender` TINYINT UNSIGNED DEFAULT NULL, 
  `email` varchar(100) NOT NULL,  
  `image_url` text,  
  `created_ts` timestamp NULL DEFAULT NULL,
  `last_updated_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login_ts` timestamp NULL DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `privilege_level` TINYINT UNSIGNED NOT NULL DEFAULT 1, 
  PRIMARY KEY (`userid`),
  UNIQUE KEY `uniq_user_email` (`email`) USING BTREE,
  UNIQUE KEY `userid_UNIQUE` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE genie.`fitness_training_session` (
  `userid` varchar(128) NOT NULL,
  `training_session_id` varchar(128) NOT NULL,
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
  `surface_index` INTEGER DEFAULT NULL,
  `vdot` DOUBLE DEFAULT NULL,
  `health_perception_index` INTEGER DEFAULT NULL,
  `muscle_state_perception_index` INTEGER DEFAULT NULL,
  `session_stress_perception_index` INTEGER DEFAULT NULL,
  `average_altitude` DOUBLE DEFAULT NULL,
  `extra_load` DOUBLE DEFAULT NULL,
  PRIMARY KEY(`training_session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE genie.`fitness_shape_index_model` (
  `userid` varchar(128) NOT NULL,  
  `shape_index` DOUBLE DEFAULT NULL,
  `time_of_record` timestamp NULL DEFAULT NULL,  
  `session_of_record` varchar(128) NOT NULL,
  PRIMARY KEY(`session_of_record`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE genie.`fitness_homeostasis_index_model` (
  `userid` varchar(128) NOT NULL,
  `trainee_classification` INTEGER DEFAULT NULL,
  `local_regression_minimum_of_homeostasis_index` DOUBLE DEFAULT NULL,
  `recent_minimum_of_homeostasis_index` DOUBLE DEFAULT NULL,
  `recent_total_load_of_exercise` DOUBLE DEFAULT NULL,
  `recent_end_time` timestamp NULL DEFAULT NULL,
  `previous_total_load_of_exercise` DOUBLE DEFAULT NULL,  
  `previous_end_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY(`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE genie.`fitness_speed_heartrate_model` (
  `userid` varchar(128) NOT NULL,
  `current_vdot` DOUBLE DEFAULT NULL,
  `previous_vdot` DOUBLE DEFAULT NULL,   
  PRIMARY KEY(`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

delimiter $$

CREATE TABLE genie.`fitness_heartrate_test` (
  `userid` varchar(128) NOT NULL,
  `heartrate_test_id` varchar(128) NOT NULL,
  `heartrate_type` int(3) DEFAULT NULL,  
  `heartrate` DOUBLE DEFAULT NULL,
  `time_of_record` timestamp NULL DEFAULT NULL,
  `day_of_record` INTEGER DEFAULT NULL,
  UNIQUE KEY `userid_UNIQUE` (`heartrate_test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE genie.`fitness_heartrate_zone_model` (
  `userid` varchar(128) NOT NULL,
  `heartrate_zone_1_start` DOUBLE DEFAULT NULL,
  `heartrate_zone_1_end` DOUBLE DEFAULT NULL,
  `heartrate_zone_2_start` DOUBLE DEFAULT NULL,
  `heartrate_zone_2_end` DOUBLE DEFAULT NULL,
  `heartrate_zone_3_start` DOUBLE DEFAULT NULL,
  `heartrate_zone_3_end` DOUBLE DEFAULT NULL,
  `heartrate_zone_4_start` DOUBLE DEFAULT NULL,
  `heartrate_zone_4_end` DOUBLE DEFAULT NULL,
  `heartrate_zone_5_start` DOUBLE DEFAULT NULL,
  `heartrate_zone_5_end` DOUBLE DEFAULT NULL,
  `heartrate_zone_6_start` DOUBLE DEFAULT NULL,
  `heartrate_zone_6_end` DOUBLE DEFAULT NULL,
  PRIMARY KEY(`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$
