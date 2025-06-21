# Domain PostgreSQL Support

PostgreSQL 数据库支持模块，提供 PostgreSQL 特有的批量操作实现。

## 功能特性

- 高性能批量插入
- ON CONFLICT DO UPDATE/NOTHING
- COPY 批量导入
- RETURNING 子句支持
- MERGE 操作（PostgreSQL 15+）
- 分区表支持
- 自动配置和检测

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-postgresql-support</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置数据源

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/test
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
```

### 3. 创建 Mapper

```java
@Mapper
public interface UserMapper extends PostgreSqlBatchBaseMapper<UserDO> {
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
        userMapper.insertBatch(users);
        
        // 插入或更新（ON CONFLICT DO UPDATE）
        userMapper.insertOrUpdateBatch(users, new String[]{"email"});
        
        // 忽略重复插入（ON CONFLICT DO NOTHING）
        userMapper.insertIgnoreBatch(users, new String[]{"email"});
        
        // COPY 批量插入（超高性能）
        userMapper.copyInsertBatch(users);
    }
}
```

## PostgreSQL 特有功能

### ON CONFLICT 处理
```java
// 当 email 冲突时更新其他字段
userMapper.insertOrUpdateBatch(users, new String[]{"email"});

// 当 email 冲突时忽略该记录
userMapper.insertIgnoreBatch(users, new String[]{"email"});
```

### RETURNING 子句
```java
// 批量插入并返回生成的 ID
List<Long> generatedIds = userMapper.insertBatchWithReturning(users);

// 批量更新并返回更新后的完整记录
List<UserDO> updatedUsers = userMapper.batchUpdateWithReturning(users);
```

### COPY 批量导入
```java
// 使用 COPY 命令进行超高性能批量插入
userMapper.copyInsertBatch(users);
```

### MERGE 操作（PostgreSQL 15+）
```java
// 使用 MERGE 语句进行复杂的合并操作
userMapper.mergeBatch(users, "target.id = source.id");
```

### 分区表支持
```java
// 分区表批量插入
userMapper.partitionInsertBatch(users, "created_date");
```

## 性能优化建议

1. 对于大批量数据使用 COPY 命令
2. 合理使用 ON CONFLICT 处理重复数据
3. 利用 RETURNING 子句减少查询次数
4. 针对分区表使用分区优化功能
5. 批量大小建议 1000-5000

## 配置选项

```yaml
domain:
  postgresql:
    enabled: true  # 是否启用 PostgreSQL 支持，默认 true
```

## 版本兼容性

- PostgreSQL 9.5+：支持 ON CONFLICT
- PostgreSQL 10+：支持分区表
- PostgreSQL 15+：支持 MERGE 语句 