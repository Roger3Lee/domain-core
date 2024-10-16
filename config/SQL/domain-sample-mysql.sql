DROP TABLE IF EXISTS family;
CREATE TABLE family(
                       `ID` SERIAL(8) NOT NULL AUTO_INCREMENT  COMMENT '自增主键' ,
                       `name` VARCHAR(255)    COMMENT '名称' ,
                       `person_count` DECIMAL(6)    COMMENT '家庭成员数量' ,
                       `householder` VARCHAR(255)    COMMENT '户主姓名' ,
                       PRIMARY KEY (ID)
)  COMMENT = '用户表';

DROP TABLE IF EXISTS family_address;
CREATE TABLE family_address(
                               `ID` SERIAL(8) NOT NULL AUTO_INCREMENT  COMMENT '自增主键' ,
                               `family_id` DECIMAL(8)    COMMENT '关联用户' ,
                               `address_name` VARCHAR(255)    COMMENT '地址' ,
                               PRIMARY KEY (ID)
)  COMMENT = '用户家庭住址';

DROP TABLE IF EXISTS family_member;
CREATE TABLE family_member(
                              `ID` SERIAL(8) NOT NULL AUTO_INCREMENT  COMMENT '自增主键' ,
                              `family_id` DECIMAL(8)    COMMENT '家庭ID' ,
                              `name` VARCHAR(255)    COMMENT '姓名' ,
                              `phone` VARCHAR(255)    COMMENT '电话' ,
                              `type` VARCHAR(255)    COMMENT '成员关系' ,
                              PRIMARY KEY (ID)
)  COMMENT = '家庭成员';

