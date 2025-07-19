# KeySequence 在 TableInfo 中找不到的解决方案

## 问题描述

在使用 MyBatis Plus 时，您可能遇到以下问题：

```java
@KeySequence("seq_family_address_id")
public class FamilyAddressDO {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    // ...
}
```

在代码中调用 `tableInfo.getKeySequence()` 时返回 `null`，或者该方法根本不存在。

## 问题原因

### 1. MyBatis Plus 版本差异
- **3.5.2 及以下版本**：`TableInfo` 类没有 `getKeySequence()` 方法
- **3.5.3 及以上版本**：添加了 `getKeySequence()` 方法支持

### 2. 注解解析时机问题
- TableInfo 可能在 `@KeySequence` 注解被完全解析之前就已经被缓存
- 某些情况下需要手动触发注解解析

### 3. 配置缺失
- 缺少必要的配置让 KeySequence 被正确识别

## 解决方案

### 方案1：使用工具类（推荐）

创建一个工具类来兼容不同版本的 MyBatis Plus：

```java
package com.artframework.domain.core.utils;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class TableInfoUtils {

    /**
     * 获取实体类的 KeySequence 注解
     */
    public static KeySequence getKeySequence(TableInfo tableInfo) {
        try {
            // 方法1：直接从 TableInfo 获取（新版本支持）
            KeySequence keySequence = getKeySequenceFromTableInfo(tableInfo);
            if (keySequence != null) {
                return keySequence;
            }

            // 方法2：从实体类直接获取注解
            Class<?> entityType = tableInfo.getEntityType();
            return getKeySequenceFromEntityClass(entityType);

        } catch (Exception e) {
            log.warn("获取 KeySequence 失败", e);
            return null;
        }
    }

    /**
     * 从 TableInfo 直接获取 KeySequence（适用于支持的版本）
     */
    private static KeySequence getKeySequenceFromTableInfo(TableInfo tableInfo) {
        try {
            Method method = tableInfo.getClass().getMethod("getKeySequence");
            return (KeySequence) method.invoke(tableInfo);
        } catch (NoSuchMethodException e) {
            // 方法不存在，使用备用方案
            return null;
        } catch (Exception e) {
            log.warn("从 TableInfo 获取 KeySequence 失败", e);
            return null;
        }
    }

    /**
     * 从实体类直接获取 KeySequence 注解
     */
    private static KeySequence getKeySequenceFromEntityClass(Class<?> entityType) {
        if (entityType == null) {
            return null;
        }
        return entityType.getAnnotation(KeySequence.class);
    }

    /**
     * 获取 KeySequence 的值
     */
    public static String getKeySequenceValue(TableInfo tableInfo) {
        KeySequence keySequence = getKeySequence(tableInfo);
        return keySequence != null ? keySequence.value() : null;
    }

    /**
     * 判断实体类是否使用了 KeySequence
     */
    public static boolean hasKeySequence(TableInfo tableInfo) {
        return getKeySequence(tableInfo) != null;
    }
}
```

### 方案2：直接从实体类获取

如果您知道具体的实体类，可以直接获取：

```java
// 直接从实体类获取 KeySequence 注解
KeySequence keySeq = FamilyAddressDO.class.getAnnotation(KeySequence.class);
String sequenceName = keySeq != null ? keySeq.value() : null;

if (sequenceName != null) {
    // 使用序列：nextval('seq_family_address_id')
    System.out.println("序列名: " + sequenceName);
} else {
    // 没有序列，使用其他策略
    System.out.println("未配置序列");
}
```

### 方案3：升级 MyBatis Plus 版本

如果可能，建议升级到 3.5.3+ 版本：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.1</version>
</dependency>
```

### 方案4：在批量操作中的应用

在增强批量操作方法中使用工具类：

```java
// 原来的代码（可能有问题）
if (tableInfo.getKeySequence() != null) {
    params.append("nextval('").append(tableInfo.getKeySequence().value()).append("')");
}

// 修改后的代码（兼容所有版本）
KeySequence keySequence = TableInfoUtils.getKeySequence(tableInfo);
if (keySequence != null) {
    params.append("nextval('").append(keySequence.value()).append("')");
}
```

## 测试和验证

### 1. 测试端点

访问以下端点来验证 KeySequence 配置：

```
GET /family/v1/debug-keysequence
```

这个端点会分析您的实体类并显示 KeySequence 配置信息。

### 2. 手动测试代码

```java
@RestController
public class TestController {
    
    @GetMapping("/test-keysequence")
    public String testKeySequence() {
        StringBuilder result = new StringBuilder();
        
        // 测试 FamilyAddressDO
        KeySequence keySeq = FamilyAddressDO.class.getAnnotation(KeySequence.class);
        result.append("FamilyAddressDO KeySequence: ");
        if (keySeq != null) {
            result.append(keySeq.value()).append(" ✅\n");
        } else {
            result.append("未找到 ❌\n");
        }
        
        // 测试其他实体类...
        
        return result.toString();
    }
}
```

### 3. 检查版本兼容性

```java
public void checkMybatisPlusVersion() {
    try {
        // 检查 TableInfo 是否支持 getKeySequence
        Method[] methods = TableInfo.class.getMethods();
        boolean hasGetKeySequence = false;
        for (Method method : methods) {
            if ("getKeySequence".equals(method.getName())) {
                hasGetKeySequence = true;
                break;
            }
        }
        
        System.out.println("TableInfo 支持 getKeySequence(): " + hasGetKeySequence);
        
    } catch (Exception e) {
        System.err.println("检查版本失败: " + e.getMessage());
    }
}
```

## 实际应用场景

### PostgreSQL 序列主键

```java
@TableName("family_address")
@KeySequence("seq_family_address_id")  // PostgreSQL 序列
public class FamilyAddressDO {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    // ...
}
```

### Oracle 序列主键

```java
@TableName("family_address")
@KeySequence("SEQ_FAMILY_ADDRESS_ID")  // Oracle 序列
public class FamilyAddressDO {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    // ...
}
```

### 批量插入时的序列处理

```java
// PostgreSQL
INSERT INTO family_address (id, family_id, address_name) VALUES
(nextval('seq_family_address_id'), ?, ?),
(nextval('seq_family_address_id'), ?, ?);

// Oracle  
INSERT ALL
INTO family_address (id, family_id, address_name) VALUES (SEQ_FAMILY_ADDRESS_ID.NEXTVAL, ?, ?)
INTO family_address (id, family_id, address_name) VALUES (SEQ_FAMILY_ADDRESS_ID.NEXTVAL, ?, ?)
SELECT 1 FROM DUAL;
```

## 常见错误及解决

### 错误1：NoSuchMethodException
```
java.lang.NoSuchMethodException: TableInfo.getKeySequence()
```

**解决**：使用工具类方案，自动兼容不同版本。

### 错误2：KeySequence 注解存在但获取为 null
```java
// 实体类有注解，但获取为 null
@KeySequence("seq_test")
public class TestDO { ... }
```

**解决**：检查注解是否在运行时保留：
```java
@Retention(RetentionPolicy.RUNTIME)  // 确保运行时可见
@KeySequence("seq_test")
```

### 错误3：序列名不正确
**检查**：
- PostgreSQL：序列名通常是小写，如 `seq_table_name_id`
- Oracle：序列名通常是大写，如 `SEQ_TABLE_NAME_ID`

## 最佳实践

1. **使用工具类**：统一通过 `TableInfoUtils` 获取 KeySequence
2. **版本兼容**：确保代码在不同版本的 MyBatis Plus 下都能运行
3. **序列命名**：遵循数据库的命名约定
4. **主键类型**：序列主键使用 `IdType.INPUT`，自增主键使用 `IdType.AUTO`
5. **错误处理**：获取 KeySequence 时要处理可能的异常

## 项目中的应用

在 domain-core 项目中，所有增强批量操作方法都已经使用 `TableInfoUtils` 来兼容 KeySequence：

- **PostgreSQL**: `EnhancedPostgreSqlBatchMethod`
- **Oracle**: `EnhancedOracleBatchMethod`  
- **PolarDB**: `EnhancedPolarDbBatchMethod`

您可以直接使用这些增强方法，无需担心 KeySequence 兼容性问题。 