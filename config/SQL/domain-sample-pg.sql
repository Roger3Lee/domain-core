DROP TABLE IF EXISTS family;
CREATE TABLE family(
                       ID NUMERIC(6) NOT NULL,
                       name VARCHAR(255),
                       person_count NUMERIC(6),
                       PRIMARY KEY (ID)
);

COMMENT ON TABLE family IS '用户表';
COMMENT ON COLUMN family.ID IS '自增主键';
COMMENT ON COLUMN family.name IS '名称';
COMMENT ON COLUMN family.person_count IS '家庭成员数量';
CREATE SEQUENCE IF NOT EXISTS seq_family_id AS BIGINT START WITH 1 INCREMENT BY 1;

DROP TABLE IF EXISTS family_address;
CREATE TABLE family_address(
                               ID NUMERIC(6) NOT NULL,
                               family_id NUMERIC(6),
                               family_name VARCHAR(255),
                               address_name VARCHAR(255),
                               PRIMARY KEY (ID)
);

COMMENT ON TABLE family_address IS '用户家庭住址';
COMMENT ON COLUMN family_address.ID IS '自增主键';
COMMENT ON COLUMN family_address.family_id IS '家庭ID';
COMMENT ON COLUMN family_address.family_name IS '家庭名称';
COMMENT ON COLUMN family_address.address_name IS '地址';
CREATE SEQUENCE  IF NOT EXISTS seq_family_address_id AS BIGINT START WITH 1 INCREMENT BY 1;

DROP TABLE IF EXISTS family_member;
CREATE TABLE family_member(
                              ID NUMERIC(6) NOT NULL,
                              family_id NUMERIC(6),
                              family_name VARCHAR(255),
                              name VARCHAR(255),
                              phone VARCHAR(255),
                              type VARCHAR(255),
                              PRIMARY KEY (ID)
);

COMMENT ON TABLE family_member IS '家庭成员';
COMMENT ON COLUMN family_member.ID IS '自增主键';
COMMENT ON COLUMN family_member.family_id IS '家庭ID';
COMMENT ON COLUMN family_member.family_name IS '家庭名称';
COMMENT ON COLUMN family_member.name IS '姓名';
COMMENT ON COLUMN family_member.phone IS '电话';
COMMENT ON COLUMN family_member.type IS '成员关系';
CREATE SEQUENCE IF NOT EXISTS seq_family_member_id AS BIGINT START WITH 1 INCREMENT BY 1;

