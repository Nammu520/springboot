-- 新增后台管理系统菜单数据表
CREATE TABLE `back_menus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '菜单名',
  `level` tinyint(4) NOT NULL COMMENT '级别',
  `url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'URL',
  `code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '编码',
  `p_code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父级编码',
  `type` tinyint(4) DEFAULT NULL COMMENT '类型（1、菜单，2、按钮）',
  `remarks` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;