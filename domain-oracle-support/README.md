# Domain Oracle Support

Oracle 数据库支持模块，提供 Oracle 特有的增强批量操作实现。

## 功能特性

- **高性能批量插入**：基于 INSERT ALL 语法的批量插入
- **主键序列支持**：支持 Oracle SEQUENCE 主键自动生成
- **Ignore Null 策略**：只更新非空字段，空字段保持原值
- **逻辑删除支持**：自动排除逻辑删除字段
- **MERGE 语句**：使用高效的 MERGE 语法进行批量更新
- **自动配置检测**：零配置自动检测 Oracle 数据库

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-oracle-support</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置数据源

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:XE
    username: your_username
    password: your_password
    driver-class-name: oracle.jdbc.OracleDriver
```

### 3. 启用 Oracle 支持

```yaml
domain:
  oracle:
    enabled: true
```

### 4. 创建 Mapper

```java
@Mapper
public interface UserMapper extends BaseMapper<UserDO>, BatchBaseMapper<UserDO> {
    // 继承所有批量操作方法
}
```

### 5. 使用增强批量操作

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public void batchOperations(List<UserDO> users) {
        // 增强批量插入 - 支持序列主键回填
        userMapper.batchInsert(users);
        // 如果使用序列主键，插入后会自动回填主键值
        
        // 增强批量更新 - 支持 ignore null 策略
        userMapper.batchUpdate(users);
        // 只更新非空字段，空字段保持数据库原值不变
    }
}
```

## Oracle 增强功能详解

### 1. 序列主键支持
```java
@TableName("user")
public class UserDO {
    @TableId(type = IdType.INPUT)
    @KeySequence("user_id_seq")  // 指定序列名
    private Long id;
    
    // 批量插入时会自动使用 user_id_seq.NEXTVAL
}

List<UserDO> users = Arrays.asList(new UserDO().setName("张三"));
userMapper.batchInsert(users);
// 插入后序列主键会自动回填到对象中
```

### 2. Ignore Null 策略
```java
List<UserDO> users = Arrays.asList(
    new UserDO().setId(1L).setName("新名字").setEmail(null),  // email为null
    new UserDO().setId(2L).setName(null).setAge(25)        // name为null  
);

userMapper.batchUpdate(users);
// 结果：第一条记录只更新name，email保持原值
//      第二条记录只更新age，name保持原值
```

### 3. 高效的 Oracle SQL 语法

#### 批量插入（INSERT ALL）
```sql
INSERT ALL 
  INTO user (name, email) VALUES ('张三', 'zhang@test.com')
  INTO user (name, email) VALUES ('李四', 'li@test.com')
SELECT 1 FROM DUAL
```

#### 批量更新（MERGE + NVL2）
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

1. 合理设置批量大小（建议 500-2000）
2. 使用事务包装批量操作
3. 为经常查询的字段建立索引
4. 监控 Oracle 的执行计划
5. 考虑使用 Oracle 的分区表

## 配置选项

```yaml
domain:
  oracle:
    enabled: true  # 是否启用 Oracle 支持，默认 false
```

## 版本兼容性

- Oracle 11g+：支持 INSERT ALL 和 MERGE 语法
- Oracle 12c+：支持更多高级特性
- Oracle 19c+：推荐版本，性能最佳 