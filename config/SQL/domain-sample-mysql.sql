DROP TABLE IF EXISTS family;
CREATE TABLE family(
                       `ID` BIGINT NOT NULL AUTO_INCREMENT  COMMENT '自增主键' ,
                       `name` VARCHAR(255)    COMMENT '名称' ,
                       `person_count` INT    COMMENT '家庭成员数量' ,
                       `householder` VARCHAR(255)    COMMENT '户主姓名' ,
                       PRIMARY KEY (ID)
)  COMMENT = '家庭表';

DROP TABLE IF EXISTS family_address;
CREATE TABLE family_address(
                               `ID` BIGINT NOT NULL AUTO_INCREMENT  COMMENT '自增主键' ,
                               `family_id` BIGINT    COMMENT '关联家庭ID' ,
                               `family_name` VARCHAR(255)    COMMENT '冗余字段：家庭名称' ,
                               `address_name` VARCHAR(500)    COMMENT '地址' ,
                               PRIMARY KEY (ID),
                               INDEX idx_family_address_family_id (`family_id`)
)  COMMENT = '家庭住址表';

DROP TABLE IF EXISTS family_member;
CREATE TABLE family_member(
                              `ID` BIGINT NOT NULL AUTO_INCREMENT  COMMENT '自增主键' ,
                              `family_id` BIGINT    COMMENT '家庭ID' ,
                              `family_name` VARCHAR(255)    COMMENT '冗余字段：家庭名称' ,
                              `name` VARCHAR(255)    COMMENT '姓名' ,
                              `phone` VARCHAR(50)    COMMENT '电话' ,
                              `type` VARCHAR(50)    COMMENT '成员关系' ,
                              PRIMARY KEY (ID),
                              INDEX idx_family_member_family_id (`family_id`)
)  COMMENT = '家庭成员表';