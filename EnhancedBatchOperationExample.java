package com.artframework.domain.example;

import com.artframework.domain.core.mapper.BatchBaseMapper;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 增强的批量操作使用示例
 * 展示主键回填、ignore null策略等功能
 */
public class EnhancedBatchOperationExample {

    /**
     * 示例实体类 - 支持主键自动生成和逻辑删除
     */
    @Data
    @TableName("user")
    public static class User {
        @TableId(type = IdType.AUTO)
        private Long id;

        private String name;
        private Integer age;
        private String email;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;

        @TableLogic // 逻辑删除字段
        private Integer deleted;
    }

    /**
     * 增强的Mapper - 继承BatchBaseMapper获得批量操作能力
     */
    @Mapper
    public interface UserMapper extends BatchBaseMapper<User> {
        // 自动获得以下增强方法：
        // int batchInsert(List<User> users); // 支持主键回填
        // int batchUpdate(List<User> users); // 支持ignore null策略
    }

    /**
     * 示例服务类 - 演示增强功能的使用
     */
    @Service
    public static class UserService {

        @Autowired
        private UserMapper userMapper;

        /**
         * 批量插入示例 - 支持主键自动回填
         */
        public void batchInsertWithKeyGeneration() {
            List<User> users = Arrays.asList(
                    createUser("张三", 25, "zhangsan@example.com"),
                    createUser("李四", 30, "lisi@example.com"),
                    createUser("王五", 28, "wangwu@example.com"));

            // 插入前，主键为null
            System.out.println("插入前 - 用户ID: " + users.get(0).getId()); // null

            // 执行批量插入 - 自动生成主键并回填到实体对象
            int result = userMapper.batchInsert(users);

            // 插入后，主键已自动回填
            System.out.println("插入后 - 用户ID: " + users.get(0).getId()); // 自动生成的ID
            System.out.println("批量插入成功：" + result + " 条记录");

            // 验证主键回填
            users.forEach(user -> System.out.println("用户: " + user.getName() + ", ID: " + user.getId()));
        }

        /**
         * 批量更新示例 - 支持ignore null策略
         */
        public void batchUpdateWithIgnoreNull() {
            List<User> users = Arrays.asList(
                    // 部分字段为null - 这些字段不会被更新
                    updateUser(1L, "张三修改", null, "zhangsan_new@example.com"), // age为null，不更新
                    updateUser(2L, null, 31, "lisi_new@example.com"), // name为null，不更新
                    updateUser(3L, "王五修改", 29, null) // email为null，不更新
            );

            // 批量更新 - 只更新非null字段
            // MySQL: 使用CASE WHEN语法，自动跳过null值
            // PostgreSQL: 使用UPDATE FROM VALUES语法，自动跳过null值
            int result = userMapper.batchUpdate(users);
            System.out.println("批量更新成功：" + result + " 条记录（ignore null策略生效）");
        }

        /**
         * 演示不同数据库的SQL优化
         */
        public void demonstrateDatabaseOptimization() {
            List<User> users = Arrays.asList(
                    createUser("测试用户1", 20, "test1@example.com"),
                    createUser("测试用户2", 21, "test2@example.com"));

            // 根据数据库类型自动生成优化的SQL：

            // MySQL:
            // INSERT INTO user (name, age, email, create_time)
            // VALUES (?, ?, ?, ?), (?, ?, ?, ?)

            // PostgreSQL:
            // INSERT INTO user (name, age, email, create_time)
            // VALUES (?, ?, ?, ?), (?, ?, ?, ?)

            // Oracle:
            // INSERT ALL
            // INTO user (name, age, email, create_time) VALUES (?, ?, ?, ?)
            // INTO user (name, age, email, create_time) VALUES (?, ?, ?, ?)
            // SELECT 1 FROM DUAL

            userMapper.batchInsert(users);
        }

        private User createUser(String name, Integer age, String email) {
            User user = new User();
            user.setName(name);
            user.setAge(age);
            user.setEmail(email);
            user.setCreateTime(LocalDateTime.now());
            return user;
        }

        private User updateUser(Long id, String name, Integer age, String email) {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setAge(age);
            user.setEmail(email);
            user.setUpdateTime(LocalDateTime.now());
            return user;
        }
    }
}

/**
 * 主要增强功能说明：
 * 
 * 1. 主键自动回填
 * - 支持AUTO_INCREMENT主键自动生成
 * - 插入后自动回填到原实体对象
 * - 支持批量插入时的主键回填
 * 
 * 2. Ignore Null策略
 * - 批量更新时自动跳过null字段
 * - 避免将null值更新到数据库
 * - 提高数据安全性
 * 
 * 3. 逻辑删除支持
 * - 自动识别@TableLogic字段
 * - 批量操作时自动排除逻辑删除字段
 * - 保持数据一致性
 * 
 * 4. 数据库特定优化
 * - MySQL: VALUES语法 + CASE WHEN更新
 * - PostgreSQL: VALUES语法 + FROM子句更新
 * - Oracle: INSERT ALL + MERGE语句
 * - PolarDB: 兼容Oracle语法
 * 
 * 5. 类型安全
 * - 编译时类型检查
 * - 自动参数绑定
 * - 防止SQL注入
 */