CREATE SCHEMA IF NOT EXISTS `banking`;

CREATE TABLE `banking`.`account` (
  `uuid` binary(16) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` enum('PERSONAL','BUSINESS') NOT NULL DEFAULT 'PERSONAL',
  `email` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`uuid`),
  KEY `user_id` (`user_id`)
);


CREATE TABLE `banking`.`balance` (
  `id` int NOT NULL AUTO_INCREMENT,
  `currency_code` varchar(255) DEFAULT NULL,
  `balance_value` decimal(10,2) DEFAULT NULL,
  `account_uuid` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_account_id_currency_code` (`account_uuid`,`currency_code`),
  KEY `fk_balance_account_id_idx` (`account_uuid`),
  CONSTRAINT `fk_balance_account_uuid` FOREIGN KEY (`account_uuid`) REFERENCES `account` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE `banking`.`transaction` (
  `uuid` binary(16) NOT NULL,
  `account_uuid` binary(16) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `currency_code` varchar(255) DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `payee_name` varchar(255) DEFAULT NULL,
  `payer_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `fk_transaction_account_idx` (`account_uuid`),
  CONSTRAINT `fk_transaction_account_id_account_uuid` FOREIGN KEY (`account_uuid`) REFERENCES `account` (`uuid`)
);

-- Dummy data to transfer into
INSERT INTO `banking`.`account` (`uuid`,`name`,`type`,`email`,`user_id`,`created`) VALUES (UUID_TO_BIN('98d9fa19-b1fb-4243-b828-c9c6b0988bd2'),'user1','PERSONAL','user1@gmail.com','8231edcf-1e8a-4376-a90f-b54c2056e80d','2021-05-31 20:03:58');
INSERT INTO `banking`.`balance` (`id`,`currency_code`,`balance_value`,`account_uuid`) VALUES (1,'USD',200.00,UUID_TO_BIN('98d9fa19-b1fb-4243-b828-c9c6b0988bd2'));

