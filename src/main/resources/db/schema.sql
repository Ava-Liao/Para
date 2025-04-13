USE para;

CREATE TABLE IF NOT EXISTS `user` (
  `number` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`number`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `enzyme` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `ec_number` VARCHAR(20) NOT NULL COMMENT 'EC number',
    `prot_id` VARCHAR(20) NOT NULL COMMENT 'Protein ID',
    `kcat` DOUBLE NOT NULL COMMENT 'kcat value',
    `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_ec_number` (`ec_number`),
    INDEX `idx_prot_id` (`prot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Enzyme kcat information'; 