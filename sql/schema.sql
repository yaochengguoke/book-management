-- 图书管理系统 MySQL 建表脚本
CREATE DATABASE IF NOT EXISTS book_db
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE book_db;

DROP TABLE IF EXISTS book;

CREATE TABLE book (
  id     VARCHAR(32)  NOT NULL COMMENT '图书编号',
  name   VARCHAR(128) NOT NULL COMMENT '书名',
  author VARCHAR(64)  NOT NULL COMMENT '作者',
  price  DECIMAL(10, 2) NOT NULL COMMENT '价格',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书表';

-- 示例数据（可选）
INSERT INTO book (id, name, author, price) VALUES
  ('B001', 'Java 核心技术', 'Cay S. Horstmann', 128.00),
  ('B002', 'Spring Boot 实战', 'Craig Walls', 89.50);

DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
  id       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(64)  NOT NULL COMMENT '用户名',
  password VARCHAR(128) NOT NULL COMMENT '密码（BCrypt）',
  role     VARCHAR(32)  NOT NULL DEFAULT 'USER' COMMENT '角色',
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';
