# Domain Model Generation Reference

## Complete Example: Family Domain

### Input SQL (MySQL)

```sql
DROP TABLE IF EXISTS family;
CREATE TABLE family(
    `ID` SERIAL(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `name` VARCHAR(255) COMMENT '名称',
    `person_count` DECIMAL(6) COMMENT '家庭成员数量',
    `householder` VARCHAR(255) COMMENT '户主姓名',
    PRIMARY KEY (ID)
) COMMENT = '用户表';

DROP TABLE IF EXISTS family_address;
CREATE TABLE family_address(
    `ID` SERIAL(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `family_id` DECIMAL(8) COMMENT '关联用户',
    `address_name` VARCHAR(255) COMMENT '地址',
    PRIMARY KEY (ID)
) COMMENT = '用户家庭住址';

DROP TABLE IF EXISTS family_member;
CREATE TABLE family_member(
    `ID` SERIAL(8) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `family_id` DECIMAL(8) COMMENT '家庭ID',
    `name` VARCHAR(255) COMMENT '姓名',
    `phone` VARCHAR(255) COMMENT '电话',
    `type` VARCHAR(255) COMMENT '成员关系',
    PRIMARY KEY (ID)
) COMMENT = '家庭成员';
```

### Input Domain XML

```xml
<domains>
    <domain name="family" description="家庭领域模型" main-table="family">
        <related table="family_address" many="false" fk="id:family_id"/>
        <related table="family_member" many="true" fk="id:family_id"/>
    </domain>
</domains>
```

### Generated: FamilyDO.java

```java
package com.example.project.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName(value="family", autoResultMap = true)
public class FamilyDO {
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("person_count")
    private Integer personCount;

    @TableField("householder")
    private String householder;
}
```

### Generated: FamilyMapper.java

```java
package com.example.project.mappers;

import org.apache.ibatis.annotations.Mapper;
import io.github.roger3lee.domain.core.mapper.BatchBaseMapper;
import com.example.project.entities.*;

@Mapper
public interface FamilyMapper extends BatchBaseMapper<FamilyDO> {
}
```

### Generated: FamilyLambdaExp.java

```java
package com.example.project.domains.family.lambdaexp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.project.domains.family.domain.*;
import com.example.project.entities.*;
import java.util.function.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class FamilyLambdaExp {
    public static SFunction<FamilyDomain, Serializable> dtoKeyLambda = FamilyDomain::getId;
    public static SFunction<FamilyDO, Serializable> doKeyLambda = FamilyDO::getId;

    // family_address related
    public static SFunction<FamilyDomain.FamilyAddressDomain, Serializable> familyAddressDomainKeyLambda =
        FamilyDomain.FamilyAddressDomain::getId;
    public static SFunction<FamilyDomain, Serializable> family_Id__RelatedFamily_address_SourceLambda =
        FamilyDomain::getId;
    public static SFunction<FamilyDomain.FamilyAddressDomain, Serializable> family_addressfamily_idTargetLambda =
        FamilyDomain.FamilyAddressDomain::getFamilyId;
    public static BiConsumer<FamilyDomain.FamilyAddressDomain, Long> family_addressfamily_idTargetSetLambda =
        FamilyDomain.FamilyAddressDomain::setFamilyId;

    // family_member related
    public static SFunction<FamilyDomain.FamilyMemberDomain, Serializable> familyMemberDomainKeyLambda =
        FamilyDomain.FamilyMemberDomain::getId;
    public static SFunction<FamilyDomain, Serializable> family_Id__RelatedFamily_member_SourceLambda =
        FamilyDomain::getId;
    public static SFunction<FamilyDomain.FamilyMemberDomain, Serializable> family_memberfamily_idTargetLambda =
        FamilyDomain.FamilyMemberDomain::getFamilyId;
    public static BiConsumer<FamilyDomain.FamilyMemberDomain, Long> family_memberfamily_idTargetSetLambda =
        FamilyDomain.FamilyMemberDomain::setFamilyId;
}
```

### Generated: FamilyDomain.java (excerpt)

```java
package com.example.project.domains.family.domain;

import io.github.roger3lee.domain.core.domain.*;
import io.github.roger3lee.domain.core.lambda.query.*;
import io.github.roger3lee.domain.core.constants.*;
import io.github.roger3lee.domain.core.utils.LambdaQueryUtils;
import io.github.roger3lee.domain.core.utils.LoadFlagUtils;
import lombok.*;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import com.example.project.domains.family.convertor.*;
import com.example.project.domains.family.service.*;
import com.example.project.domains.family.lambdaexp.*;
import cn.hutool.core.util.*;
import cn.hutool.core.collection.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

@Schema(description = "家庭领域模型")
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDomain extends BaseAggregateDomain<FamilyDomain,FamilyService> {

    public FamilyDomain(Long key, FamilyService service){
        this.id = key;
        this._service = service;
    }

    @Getter @Setter @Schema(description = "自增主键")
    private Long id;
    @Getter @Setter @Schema(description = "名称")
    private String name;
    @Getter @Setter @Schema(description = "家庭成员数量")
    private Integer personCount;

    // Related: family_address (many=false)
    @Setter @Schema(description = "RELATE family_address")
    private FamilyAddressDomain familyAddress;
    public FamilyAddressDomain getFamilyAddress(){
        if(ObjectUtil.isNotEmpty(this.familyAddress)){
            ListUtil.toList(this.familyAddress).forEach(x -> x.set_thisDomain(this));
        }
        return this.familyAddress;
    }

    // Related: family_member (many=true)
    @Setter @Schema(description = "RELATE family_member")
    private java.util.List<FamilyMemberDomain> familyMemberList;
    public java.util.List<FamilyMemberDomain> getFamilyMemberList(){
        if(ObjectUtil.isNotEmpty(this.familyMemberList)){
            ListUtil.toList(this.familyMemberList).forEach(x -> x.set_thisDomain(this));
        }
        return this.familyMemberList;
    }

    @Getter @Setter @Schema(description = "加载数据标识类")
    private LoadFlag loadFlag;

    // Inner related DTOs
    @NoArgsConstructor @AllArgsConstructor
    public static class FamilyAddressDomain extends BaseDomain {
        @Getter @Setter @Schema(description = "自增主键")
        private Long id;
        @Getter @Setter @Schema(description = "家庭ID")
        private Long familyId;
        @Getter @Setter @Schema(description = "地址")
        private String addressName;
    }

    @NoArgsConstructor @AllArgsConstructor
    public static class FamilyMemberDomain extends BaseDomain {
        @Getter @Setter @Schema(description = "自增主键")
        private Long id;
        @Getter @Setter @Schema(description = "家庭ID")
        private Long familyId;
        @Getter @Setter @Schema(description = "姓名")
        private String name;
        @Getter @Setter @Schema(description = "电话")
        private String phone;
        @Getter @Setter @Schema(description = "成员关系")
        private String type;
    }

    // LoadFlag, load(), loadByKey(), loadRelated(), copy() methods...
    // (follow the template-dto.ftl pattern exactly)

    /**
     * Load by primary key (strongly typed)
     */
    public static FamilyDomain load(Long key, FamilyService service) {
        FamilyDomain domain = service.find(FamilyFindDomain.builder().key(key).build());
        if(ObjectUtil.isNotNull(domain)){
            domain._service = service;
        }
        return domain;
    }

    /**
     * Load by any field (generic Serializable key)
     */
    public static FamilyDomain loadByKey(Serializable key, SFunction<FamilyDomain, Serializable> keyLambda, FamilyService service) {
        FamilyDomain domain = service.findByKey(key, keyLambda);
        if(ObjectUtil.isNotNull(domain)){
            domain._service = service;
        }
        return domain;
    }
}
```

## Domain XML Reference

### Generated: FamilyFindDomain.java

```java
package com.example.project.domains.family.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilyFindDomain {
    private Long key;  // Strongly typed (matches main table key type)
    /**
    * 默认加载所有
    */
    @Builder.Default
    private FamilyDomain.LoadFlag loadFlag = new FamilyDomain.LoadFlag();
}
```

### Generated: FamilyService.java (excerpt)

```java
package com.example.project.domains.family.service;

import com.example.project.domains.family.domain.*;
import io.github.roger3lee.domain.core.service.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import java.io.Serializable;

public interface FamilyService extends BaseDomainService {
    // Find by primary key (uses FindDomain with strongly-typed key)
    FamilyDomain find(FamilyFindDomain request);

    // Find by entity + loadFlag
    FamilyDomain find(FamilyDomain response, FamilyDomain.LoadFlag loadFlag);

    // Find by any field (generic Serializable key, no FindDomain wrapper)
    FamilyDomain findByKey(Serializable key, SFunction<FamilyDomain, Serializable> keyLambda);

    Long insert(FamilyDomain request);
    Boolean update(FamilyDomain request);
    Boolean delete(Long key);
    Boolean delete(Long key, FamilyDomain.LoadFlag loadFlag);
}
```

### Key Design Decisions

| Method | Key Type | Rationale |
|--------|----------|----------|
| `FindDomain.key` | `${mainTable.keyType}` (e.g. `Long`) | Strongly typed for compile-time safety |
| `find(FindDomain)` | Uses FindDomain | Primary key lookup with optional LoadFlag |
| `findByKey(Serializable, SFunction)` | `Serializable` | Generic lookup by any field, no FindDomain |
| `load(keyType, service)` | `${mainTable.keyType}` | Strongly typed static factory |
| `loadByKey(Serializable, SFunction, service)` | `Serializable` | Generic static factory for any field |
| `delete(keyType)` | `${mainTable.keyType}` | Strongly typed delete |

## Domain XML Reference (Continued)

### Simple Domain (no related tables)

```xml
<domains>
    <domain name="config" description="配置" main-table="sys_config"/>
</domains>
```
Generates `ConfigDomain extends BaseDomain` with simple CRUD only.

### Domain with Aggregate

```xml
<domains>
    <domain name="order" description="订单" main-table="order_header">
        <aggregate table="order_item" many="true" fk="id:order_id"/>
    </domain>
</domains>
```
The `<aggregate>` element is treated the same as `<related>` but semantically represents a root-owned entity.

### Domain with Redundancy

```xml
<domains>
    <domain name="family" description="家庭" main-table="family">
        <related table="family_address" many="false"
                 fk="id:family_id" redundancy="name:family_name"/>
    </domain>
</domains>
```
`redundancy` specifies denormalized fields copied from the main table into the related table (e.g. family name copied into family_address.family_name).

### Domain with Ref Tables (nested relationships)

```xml
<domains>
    <domain name="app" description="应用" main-table="app">
        <related table="app_dir" many="true" fk="id:app_id">
            <ref table="app_dir_doc_rel" many="true" fk="id:dir_id"/>
        </related>
    </domain>
</domains>
```
Ref tables are sub-entities within a related table, accessed via CacheDomain with predicate filtering.

## FK Format Reference

| Format | Meaning | Example |
|--------|---------|---------|
| `srcCol:tgtCol` | Direct FK mapping | `id:family_id` |
| `srcCol:tgtCol\|srcCol2:tgtCol2` | Multiple FK (pipe-separated) | `id:family_id\|type:type_code` |
| `srcCol(Conv):tgtCol` | FK with type converter | `status(String.valueOf):status_code` |
| `CONST_VALUE:tgtCol` | Constant value FK | `1:status` |

When source column contains `.` it is treated as a Java constant reference, not a column name.

## PostgreSQL-specific Notes

- Use `BIGSERIAL` for auto-increment primary keys
- Use `IdType.INPUT` with `@KeySequence("seq_{table_name}_id")` instead of `IdType.AUTO`
- Set `keyGenerator=false` on `TableMetaInfo` for PostgreSQL tables
- Use `COMMENT ON TABLE/COLUMN` syntax instead of inline `COMMENT`
