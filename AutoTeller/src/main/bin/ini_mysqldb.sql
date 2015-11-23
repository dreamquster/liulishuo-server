drop table if exists user_coins;
CREATE TABLE user_coins (
  id int(11) unsigned AUTO_INCREMENT NOT NULL,
  user_id bigint(20) NOT NULL ,
  coins int(11),
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists transfer_log;
CREATE TABLE transfer_log (
  id int(11) unsigned AUTO_INCREMENT NOT NULL,
  from_user bigint(20)  NOT NULL ,
  to_user bigint(20)  NOT NULL ,
  coins int(11) NOT NULL,
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE USER 'liulishuo'@'%' IDENTIFIED BY 'LLS2015119';
GRANT ALL PRIVILEGES ON coins.* TO 'liulishuo'@'%';

