DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info(
    ID SERIAL NOT NULL,
    name VARCHAR(255),
    phone VARCHAR(255),
    family_member_count INTEGER(2),
    CREATED_BY VARCHAR(32),
    CREATED_TIME TIMESTAMP,
    UPDATED_BY VARCHAR(32),
    UPDATED_TIME TIMESTAMP,
    PRIMARY KEY (ID)
);

COMMENT ON TABLE user_info IS '用户表';
COMMENT ON COLUMN user_info.ID IS '自增主键';
COMMENT ON COLUMN user_info.name IS '名称';
COMMENT ON COLUMN user_info.phone IS '手机';
COMMENT ON COLUMN user_info.family_member_count IS '家庭成員數';
COMMENT ON COLUMN user_info.CREATED_BY IS '创建人';
COMMENT ON COLUMN user_info.CREATED_TIME IS '创建时间';
COMMENT ON COLUMN user_info.UPDATED_BY IS '更新人';
COMMENT ON COLUMN user_info.UPDATED_TIME IS '更新时间';

DROP TABLE IF EXISTS user_address;
CREATE TABLE user_address(
    ID SERIAL NOT NULL,
    user_id INTEGER(8),
    address_name VARCHAR(255),
    CREATED_BY VARCHAR(32),
    CREATED_TIME TIMESTAMP,
    UPDATED_BY VARCHAR(32),
    UPDATED_TIME TIMESTAMP,
    PRIMARY KEY (ID)
);

COMMENT ON TABLE user_address IS '用户家庭住址';
COMMENT ON COLUMN user_address.ID IS '自增主键';
COMMENT ON COLUMN user_address.user_id IS '关联用户';
COMMENT ON COLUMN user_address.address_name IS '地址';
COMMENT ON COLUMN user_address.CREATED_BY IS '创建人';
COMMENT ON COLUMN user_address.CREATED_TIME IS '创建时间';
COMMENT ON COLUMN user_address.UPDATED_BY IS '更新人';
COMMENT ON COLUMN user_address.UPDATED_TIME IS '更新时间';

DROP TABLE IF EXISTS user_family_member;
CREATE TABLE user_family_member(
    ID SERIAL NOT NULL,
    user_id INTEGER(8),
    name VARCHAR(255),
    rel_type VARCHAR(255),
    CREATED_BY VARCHAR(32),
    CREATED_TIME TIMESTAMP,
    UPDATED_BY VARCHAR(32),
    UPDATED_TIME TIMESTAMP,
    PRIMARY KEY (ID)
);

COMMENT ON TABLE user_family_member IS '家庭成员';
COMMENT ON COLUMN user_family_member.ID IS '自增主键';
COMMENT ON COLUMN user_family_member.user_id IS '用户ID';
COMMENT ON COLUMN user_family_member.name IS '姓名';
COMMENT ON COLUMN user_family_member.rel_type IS '家属关系';
COMMENT ON COLUMN user_family_member.CREATED_BY IS '创建人';
COMMENT ON COLUMN user_family_member.CREATED_TIME IS '创建时间';
COMMENT ON COLUMN user_family_member.UPDATED_BY IS '更新人';
COMMENT ON COLUMN user_family_member.UPDATED_TIME IS '更新时间';

