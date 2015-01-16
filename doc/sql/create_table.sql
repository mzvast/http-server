####登录mysql###
mysql -u dinner -p


DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` varchar(255)  NOT NULL,
  `account` varchar(255) NOT NULL,
  `mobile` varchar(255) ,
  `nick_name` varchar(255) ,
  `password` varchar(255) NOT NULL,
  `gender` varchar(1024) ,
  `portrait_file` varchar(255),
  `signature` varchar(255) ,
  `birthdate` varchar(255),
  `token_id` varchar(255),
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;