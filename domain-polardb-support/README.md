# Domain PolarDB Support

PolarDB 数据库支持模块，提供 PolarDB 特有的增强批量操作实现。

## 功能特性

- **高性能批量插入**：基于 Oracle 兼容的 INSERT ALL 语法
- **主键序列支持**：支持 Oracle 兼容的 SEQUENCE 主键自动生成
- **Ignore Null 策略**：只更新非空字段，空字段保持原值
- **逻辑删除支持**：自动排除逻辑删除字段
- **MERGE 语句**：使用 Oracle 兼容的 MERGE 语法进行批量更新
- **自动配置检测**：零配置自动检测 PolarDB 数据库

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-polardb-support</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置数据源

```yaml
spring:
  datasource:
    url: jdbc:mysql://pc-xxx.mysql.polardb.rds.aliyuncs.com:3306/test
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3. 启用 PolarDB 支持

```yaml
domain:
  polardb:
    enabled: true
```

### 4. 创建 Mapper

```java
@Mapper
public interface UserMapper extends BatchBaseMapper<UserDO> {
    // 继承增强的批量操作方法
}
```

### 5. 使用增强批量操作

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public void batchOperations(List<UserDO> users) {
        // 增强批量插入 - 支持序列主键回填（Oracle兼容）
        userMapper.batchInsert(users);
        // PolarDB-O 兼容模式下，会自动使用 INSERT ALL 语法
        
        // 增强批量更新 - 支持 ignore null 策略
        userMapper.batchUpdate(users);
        // 使用 MERGE 语法，只更新非空字段
    }
}
```

## PolarDB 增强功能详解

### 1. Oracle 兼容序列主键
```java
// PolarDB-O 模式，兼容 Oracle 序列语法
@TableName("user")
public class UserDO {
    @TableId(type = IdType.INPUT)
    @KeySequence("user_id_seq")  // Oracle 兼容序列
    private Long id;
}

List<UserDO> users = Arrays.asList(new UserDO().setName("张三"));
userMapper.batchInsert(users);
// 自动使用 user_id_seq.NEXTVAL
```

### 2. Ignore Null 策略
```java
List<UserDO> users = Arrays.asList(
    new UserDO().setId(1L).setName("新名字").setEmail(null),
    new UserDO().setId(2L).setName(null).setAge(25)
);

userMapper.batchUpdate(users);
// 使用 NVL2 函数实现 ignore null 策略（Oracle 兼容）
```

### 3. 高效的 PolarDB-O SQL 语法

#### 批量插入（INSERT ALL）
```sql
INSERT ALL 
  INTO user (name, email) VALUES ('张三', 'zhang@test.com')
  INTO user (name, email) VALUES ('李四', 'li@test.com')
SELECT 1 FROM DUAL
```

#### 批量更新（MERGE）
```sql
MERGE INTO user target
USING (
  SELECT 1 AS id, '新名字' AS name, NULL AS email FROM DUAL
  UNION ALL  
  SELECT 2 AS id, NULL AS name, 'new@test.com' AS email FROM DUAL
) source ON (target.id = source.id)
WHEN MATCHED THEN UPDATE SET 
  target.name = NVL2(source.name, source.name, target.name),
  target.email = NVL2(source.email, source.email, target.email)
```

## 性能优化建议

1. **合理的批量大小**：建议每批 1000-5000 条记录
2. **利用 Oracle 兼容性**：充分使用 MERGE 和 INSERT ALL 语法
3. **序列主键优化**：使用序列避免主键冲突
4. **Ignore null 策略**：减少不必要的字段更新
5. **索引优化**：为批量更新的WHERE条件字段建立索引

## 配置选项

```yaml
# 自动配置，无需额外配置
# 系统会自动检测 PolarDB 数据库并启用相应功能
spring:
  datasource:
    url: jdbc:mysql://pc-xxx.mysql.polardb.rds.aliyuncs.com:3306/test
    # ... 其他配置
```

## 版本兼容性

- **PolarDB-O**：完全支持 Oracle 兼容语法
- **PolarDB-MySQL**：兼容 MySQL 语法
- **PolarDB-PostgreSQL**：兼容 PostgreSQL 语法 