
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info(
    `ID` INT(8) NOT NULL AUTO_INCREMENT  COMMENT '自增主键' ,
    `name` VARCHAR(255)    COMMENT '名称' ,
    `phone` VARCHAR(255)    COMMENT '手机' ,
    `family_member_count` INT(2)    COMMENT '家庭成員數' ,
    `CREATED_BY` VARCHAR(32)    COMMENT '创建人' ,
    `CREATED_TIME` DATETIME    COMMENT '创建时间' ,
    `UPDATED_BY` VARCHAR(32)    COMMENT '更新人' ,
    `UPDATED_TIME` DATETIME    COMMENT '更新时间' ,
    PRIMARY KEY (ID)
)  COMMENT = '用户表';


DROP TABLE IF EXISTS user_address;
CREATE TABLE user_address(
                             `ID` INT(8) NOT NULL AUTO_INCREMENT  COMMENT '自增主键' ,
                             `user_id` INT(8)    COMMENT '关联用户' ,
                             `address_name` VARCHAR(255)    COMMENT '地址' ,
                             `CREATED_BY` VARCHAR(32)    COMMENT '创建人' ,
                             `CREATED_TIME` DATETIME    COMMENT '创建时间' ,
                             `UPDATED_BY` VARCHAR(32)    COMMENT '更新人' ,
                             `UPDATED_TIME` DATETIME    COMMENT '更新时间' ,
                             PRIMARY KEY (ID)
)  COMMENT = '用户家庭住址';

DROP TABLE IF EXISTS user_family_member;
CREATE TABLE user_family_member(
                                   `ID` INT(8) NOT NULL AUTO_INCREMENT  COMMENT '自增主键' ,
                                   `user_id` INT(8)    COMMENT '用户ID' ,
                                   `name` VARCHAR(255)    COMMENT '姓名' ,
                                   `rel_type` VARCHAR(255)    COMMENT '家属关系' ,
                                   `CREATED_BY` VARCHAR(32)    COMMENT '创建人' ,
                                   `CREATED_TIME` DATETIME    COMMENT '创建时间' ,
                                   `UPDATED_BY` VARCHAR(32)    COMMENT '更新人' ,
                                   `UPDATED_TIME` DATETIME    COMMENT '更新时间' ,
                                   PRIMARY KEY (ID)
)  COMMENT = '家庭成员';

