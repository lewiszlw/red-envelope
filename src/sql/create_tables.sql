CREATE TABLE `envelope_detail` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user` varchar(25) DEFAULT NULL COMMENT '发红包者',
  `type` varchar(10) NOT NULL DEFAULT 'UNKONWN' COMMENT '红包类型',
  `amount` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '红包总额',
  `size` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '红包份数',
  `remain_money` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '红包余额',
  `remain_size` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '红包余下份数',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `envelope_grabber` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `envelope_id` int(11) unsigned NOT NULL COMMENT '红包id',
  `grabber` varchar(25) NOT NULL DEFAULT '' COMMENT '抢到用户',
  `money` int(11) unsigned NOT NULL COMMENT '抢到金额',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_envelope_id_grabber` (`envelope_id`,`grabber`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4;