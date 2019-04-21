DROP FUNCTION IF EXISTS rand_string;
DELIMITER $$
CREATE FUNCTION rand_string(n INT) RETURNS VARCHAR(255)
NO SQL
	BEGIN
		DECLARE chars_str VARCHAR(100) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
		DECLARE return_str VARCHAR(255) DEFAULT '';
		DECLARE i INT DEFAULT 0;
		WHILE i < n DO
			SET return_str = concat(return_str, substr(chars_str, floor(1+RAND()*62),1));
			SET i = i+1;
		END WHILE;
		RETURN return_str;
	END$$
DELIMITER ;

LOCK TABLES `t_user` WRITE;

INSERT INTO `t_user` (`uid`,`username`,`password`,`create_dt`,`update_dt`,`name`,
											`pwdsalt`,`type`,  `state`)
VALUES (${superadmin.uid},'superadmin',${superadmin.password},CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,
	'superadmin',${superadmin.salt},0, 0);

UNLOCK TABLES;
# t_role
INSERT INTO `t_role` VALUES (1, 'superadmin', '超级管理员', 0, CURRENT_TIMESTAMP , CURRENT_TIMESTAMP);
INSERT INTO `t_role` VALUES (2, 'user', '普通用户', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

# t_permission
INSERT INTO `t_permission` VALUES (1, '*', '全部', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

# t_role_permission
INSERT INTO `t_role_permission` VALUES (1, 1, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

# t_user_role
INSERT INTO `t_user_role` VALUES (1, 1, 1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);