# Domain Oracle Support

Oracle 数据库支持模块，提供 Oracle 特有的批量操作实现。

## 功能特性

- 高性能批量插入（INSERT ALL）
- 批量更新（MERGE）
- Oracle 特有语法优化
- 自动配置和检测

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-oracle-support</artifactId>
    <version>1.0-SNAPSHOT</version>
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

### 5. 使用批量操作

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public void batchInsertUsers(List<UserDO> users) {
        // 批量插入（使用 INSERT ALL 语法）
        userMapper.insertBatch(users);
        
        // 批量更新（使用 MERGE 语法）
        userMapper.batchUpdate(users);
    }
}
```

## Oracle 特有功能

### INSERT ALL 批量插入
```sql
INSERT ALL 
  INTO user_table (id, name, email) VALUES (?, ?, ?)
  INTO user_table (id, name, email) VALUES (?, ?, ?)
  INTO user_table (id, name, email) VALUES (?, ?, ?)
SELECT 1 FROM DUAL
```

### MERGE 批量更新
```sql
MERGE INTO user_table target
USING (
  SELECT ? AS id, ? AS name, ? AS email FROM DUAL
  UNION ALL
  SELECT ? AS id, ? AS name, ? AS email FROM DUAL
) source ON (target.id = source.id)
WHEN MATCHED THEN UPDATE SET 
  target.name = source.name,
  target.email = source.email
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