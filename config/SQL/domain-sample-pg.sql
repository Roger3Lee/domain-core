DROP TABLE IF EXISTS family;
CREATE TABLE family(
                       ID BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255),
                       person_count NUMERIC(6)
);

COMMENT ON TABLE family IS '用户表';
COMMENT ON COLUMN family.ID IS '自增主键';
COMMENT ON COLUMN family.name IS '名称';
COMMENT ON COLUMN family.person_count IS '家庭成员数量';

DROP TABLE IF EXISTS family_address;
CREATE TABLE family_address(
                               ID BIGSERIAL PRIMARY KEY,
                               family_id BIGINT,
                               family_name VARCHAR(255),
                               address_name VARCHAR(255)
);

COMMENT ON TABLE family_address IS '用户家庭住址';
COMMENT ON COLUMN family_address.ID IS '自增主键';
COMMENT ON COLUMN family_address.family_id IS '家庭ID';
COMMENT ON COLUMN family_address.family_name IS '家庭名称';
COMMENT ON COLUMN family_address.address_name IS '地址';

DROP TABLE IF EXISTS family_member;
CREATE TABLE family_member(
                              ID BIGSERIAL PRIMARY KEY,
                              family_id BIGINT,
                              family_name VARCHAR(255),
                              name VARCHAR(255),
                              phone VARCHAR(255),
                              type VARCHAR(255)
);

COMMENT ON TABLE family_member IS '家庭成员';
COMMENT ON COLUMN family_member.ID IS '自增主键';
COMMENT ON COLUMN family_member.family_id IS '家庭ID';
COMMENT ON COLUMN family_member.family_name IS '家庭名称';
COMMENT ON COLUMN family_member.name IS '姓名';
COMMENT ON COLUMN family_member.phone IS '电话';
COMMENT ON COLUMN family_member.type IS '成员关系';

