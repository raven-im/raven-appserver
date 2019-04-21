-- -----------------------------------------------------
-- Table `t_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` VARCHAR(36) NOT NULL COMMENT 'uuid',
  `username` VARCHAR(32) NOT NULL COMMENT 'login username',
  `password` VARCHAR(64) NULL COMMENT 'login password',
  `create_dt` DATETIME NULL COMMENT 'create date',
  `update_dt` DATETIME NULL COMMENT 'update date',
  `pwdsalt` VARCHAR(36) NULL COMMENT 'Hash salt',
  `name` VARCHAR(16) NULL COMMENT 'user name',
  `type` int(4) NOT NULL DEFAULT '0' COMMENT '默认0为老师，1为客户, 2为超管',
  `state` int(4) NOT NULL DEFAULT '0' COMMENT '默认0为激活，1为去激活，2为删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
