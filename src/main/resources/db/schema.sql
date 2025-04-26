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
    `kcat` DOUBLE NOT NULL COMMENT 'kcat value',
    `sub` VARCHAR(255) DEFAULT NULL COMMENT '底物名称',
    `smiles` TEXT DEFAULT NULL COMMENT '底物SMILES结构',
    `sequences` TEXT DEFAULT NULL COMMENT '酶的氨基酸序列',
    `predicted` TINYINT(1) DEFAULT 0 COMMENT '数据来源: 0-实验数据, 1-UniKP预测, 2-DLTKcat预测, 3-DLKcat预测',
    `temperature` DOUBLE DEFAULT NULL COMMENT '温度参数（摄氏度）',
    `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_sub` (`sub`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Enzyme kcat information';