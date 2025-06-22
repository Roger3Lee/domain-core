# Domain PolarDB Support

PolarDB 数据库支持模块，提供 PolarDB 特有的批量操作实现。

## 功能特性

- 高性能批量插入
- 并行批量处理
- 分区表优化
- 热点数据处理
- 自动配置

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
public interface UserMapper extends PolarDbBatchBaseMapper<UserDO> {
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
        // 普通批量插入
        userMapper.insertBatch(users);
        
        // 并行批量插入（推荐大数据量使用）
        userMapper.parallelInsertBatch(users, 4);
        
        // 分区表批量插入
        userMapper.partitionInsertOrUpdateBatch(users, "user_id");
        
        // 热点数据处理
        userMapper.hotDataBatchProcess(users, 1000);
    }
}
```

## PolarDB 特有功能

### 并行处理
```java
// 并行批量插入，分区数建议设置为 CPU 核心数
userMapper.parallelInsertBatch(users, Runtime.getRuntime().availableProcessors());

// 并行批量更新
userMapper.parallelBatchUpdate(users, 4);
```

### 分区优化
```java
// 分区表批量操作
userMapper.partitionInsertOrUpdateBatch(users, "partition_key");
```

### 热点数据处理
```java
// 针对热点数据进行优化处理
userMapper.hotDataBatchProcess(users, hotDataThreshold);
```

## 性能优化建议

1. 利用 PolarDB 的多核并行能力
2. 合理设置并行度（建议 2-4）
3. 针对分区表使用分区优化功能
4. 对热点数据使用专门的处理方法
5. 批量大小建议 2000-10000

## 配置选项

```yaml
domain:
  polardb:
    enabled: true  # 是否启用 PolarDB 支持，默认 false
    parallel-degree: 4  # 默认并行度
    hot-data-threshold: 1000  # 热点数据阈值
``` 