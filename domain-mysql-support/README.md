# Domain MySQL Support

MySQL 数据库支持模块，提供 MySQL 特有的批量操作实现。

## 功能特性

- 高性能批量插入（INSERT VALUES）
- 批量更新（CASE WHEN）
- 插入或更新（ON DUPLICATE KEY UPDATE）
- 批量替换（REPLACE INTO）
- 忽略重复插入（INSERT IGNORE）
- 自动配置和检测

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-mysql-support</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置数据源

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3. 创建 Mapper

```java
@Mapper
public interface UserMapper extends MySqlBatchBaseMapper<UserDO> {
    // 继承所有批量操作方法
}
```

### 4. 使用批量操作

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public void batchInsertUsers(List<UserDO> users) {
        // 批量插入
        userMapper.batchInsert(users);
        
        // 插入或更新
        userMapper.insertOrUpdateBatch(users);
        
        // 忽略重复插入
        userMapper.insertIgnoreBatch(users);
    }
}
```

## 性能优化建议

1. 设置 `rewriteBatchedStatements=true`
2. 调整 `max_allowed_packet` 参数
3. 使用事务包装批量操作
4. 合理设置批量大小（建议 1000-5000）

## 配置选项

```yaml
domain:
  mysql:
    enabled: true  # 是否启用 MySQL 支持，默认 true
``` 