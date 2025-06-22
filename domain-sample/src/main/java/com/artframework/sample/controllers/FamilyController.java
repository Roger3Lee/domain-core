package com.artframework.sample.controllers;

import cn.hutool.core.collection.ListUtil;
import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.domains.family.service.*;
import com.artframework.sample.entities.FamilyDO;
import com.artframework.sample.mappers.FamilyMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 家庭控制器
 * 
 * SQL测试端点列表：
 * /family/v1/test-sql - 主测试方法（基本混合OR逻辑）
 * /family/v1/test-sql-1 - 基本混合OR逻辑: name = ? OR name LIKE ?
 * /family/v1/test-sql-2 - 纯AND条件: name = ? AND phone = ?
 * /family/v1/test-sql-3 - 纯OR条件: name = ? OR phone = ?
 * /family/v1/test-sql-4 - 复杂混合: name = ? AND (phone LIKE ? OR type = ?)
 * /family/v1/test-sql-5 - 多个OR条件: name = ? AND (phone = ? OR phone = ? OR type
 * = ?)
 * /family/v1/test-sql-6 - 不同操作符: name LIKE ? AND (phone != ? OR type IN (...))
 * /family/v1/test-sql-7 - 嵌套AND/OR: (name = ? AND phone = ?) AND (type = ? OR
 * type = ?)
 * /family/v1/test-sql-all - 运行所有测试场景
 */
@RestController()
@RequestMapping("/family/v1")
public class FamilyController {
    @Autowired
    private FamilyService familyService;

    @Autowired
    private FamilyMapper familyMapper;

    /**
     * 查找
     * 
     * @param request 请求体
     * @return FamilyDomain
     */
    @PostMapping("/query")
    public FamilyDomain find(@RequestBody FamilyFindDomain request) {
        FamilyDomain findDomain = FamilyDomain.load(request.getKey(), familyService);
        findDomain.loadRelated(FamilyDomain.FamilyMemberDomain.class,
                x -> x.eq(FamilyDomain.FamilyMemberDomain::getName, "王芳")
                        .or(y -> y.like(FamilyDomain.FamilyMemberDomain::getName, "张三")));
        findDomain.loadRelated(FamilyDomain.FamilyAddressDomain.class);
        return findDomain;
    }

    /**
     * 新增
     * 
     * @param request 请求体
     * @return Integer
     */
    @PutMapping()
    public Integer insert(@RequestBody FamilyDomain request) {
        return familyService.insert(request);
    }

    /**
     * 修改
     * 
     * @param request 请求体
     * @return 成功OR失败
     */
    @PostMapping()
    public Boolean update(@RequestBody FamilyDomain request) {
        return familyService.update(request);
    }

    /**
     * 删除
     * 
     * @param key 数据ID
     * @return 成功OR失败
     */
    @DeleteMapping
    public Boolean delete(@RequestParam("key") Integer key) {
        return familyService.delete(key);
    }

    /**
     * 测试场景1：基本混合OR逻辑
     * 期望SQL: WHERE family_id = ? AND (name = ? OR name LIKE ?)
     */
    @GetMapping("/test-sql-1")
    public String testBasicMixedOr() {
        try {
            FamilyDomain findDomain = FamilyDomain.load(1778216962, familyService);
            findDomain.loadRelated(FamilyDomain.FamilyMemberDomain.class,
                    x -> x.eq(FamilyDomain.FamilyMemberDomain::getName, "王芳")
                            .or(y -> y.like(FamilyDomain.FamilyMemberDomain::getName, "张三")));
            return "测试1完成 - 基本混合OR逻辑，请查看控制台SQL输出";
        } catch (Exception e) {
            return "测试1失败: " + e.getMessage();
        }
    }

    /**
     * 测试场景2：纯AND条件
     * 期望SQL: WHERE family_id = ? AND name = ? AND phone = ?
     */
    @GetMapping("/test-sql-2")
    public String testPureAnd() {
        try {
            FamilyDomain findDomain = FamilyDomain.load(1778216962, familyService);
            findDomain.loadRelated(FamilyDomain.FamilyMemberDomain.class,
                    x -> x.eq(FamilyDomain.FamilyMemberDomain::getName, "王芳")
                            .eq(FamilyDomain.FamilyMemberDomain::getPhone, "13800138000"));
            return "测试2完成 - 纯AND条件，请查看控制台SQL输出";
        } catch (Exception e) {
            return "测试2失败: " + e.getMessage();
        }
    }

    /**
     * 测试场景3：纯OR条件
     * 期望SQL: WHERE family_id = ? AND (name = ? OR phone = ?)
     */
    @GetMapping("/test-sql-3")
    public String testPureOr() {
        try {
            FamilyDomain findDomain = FamilyDomain.load(1778216962, familyService);
            findDomain.loadRelated(FamilyDomain.FamilyMemberDomain.class,
                    x -> x.eq(FamilyDomain.FamilyMemberDomain::getName, "王芳")
                            .or(y -> y.eq(FamilyDomain.FamilyMemberDomain::getPhone, "13800138000")));
            return "测试3完成 - 纯OR条件，请查看控制台SQL输出";
        } catch (Exception e) {
            return "测试3失败: " + e.getMessage();
        }
    }

    /**
     * 测试场景4：复杂混合条件
     * 期望SQL: WHERE family_id = ? AND name = ? AND (phone LIKE ? OR type = ?)
     */
    @GetMapping("/test-sql-4")
    public String testComplexMixed() {
        try {
            FamilyDomain findDomain = FamilyDomain.load(1778216962, familyService);
            findDomain.loadRelated(FamilyDomain.FamilyMemberDomain.class,
                    x -> x.eq(FamilyDomain.FamilyMemberDomain::getName, "王芳")
                            .or(y -> y.like(FamilyDomain.FamilyMemberDomain::getPhone, "138%"))
                            .or(y -> y.eq(FamilyDomain.FamilyMemberDomain::getType, "父亲")));
            return "测试4完成 - 复杂混合条件，请查看控制台SQL输出";
        } catch (Exception e) {
            return "测试4失败: " + e.getMessage();
        }
    }

    /**
     * 测试场景5：多个OR条件
     * 期望SQL: WHERE family_id = ? AND name = ? AND (phone = ? OR phone = ? OR type =
     * ?)
     */
    @GetMapping("/test-sql-5")
    public String testMultipleOr() {
        try {
            FamilyDomain findDomain = FamilyDomain.load(1778216962, familyService);
            findDomain.loadRelated(FamilyDomain.FamilyMemberDomain.class,
                    x -> x.eq(FamilyDomain.FamilyMemberDomain::getName, "王芳")
                            .or(y -> y.eq(FamilyDomain.FamilyMemberDomain::getPhone, "13800138000"))
                            .or(y -> y.eq(FamilyDomain.FamilyMemberDomain::getPhone, "13900139000"))
                            .or(y -> y.eq(FamilyDomain.FamilyMemberDomain::getType, "母亲")));
            return "测试5完成 - 多个OR条件，请查看控制台SQL输出";
        } catch (Exception e) {
            return "测试5失败: " + e.getMessage();
        }
    }

    /**
     * 测试场景6：不同操作符混合
     * 期望SQL: WHERE family_id = ? AND name LIKE ? AND (phone != ? OR type IN (...))
     */
    @GetMapping("/test-sql-6")
    public String testDifferentOperators() {
        try {
            FamilyDomain findDomain = FamilyDomain.load(1778216962, familyService);
            findDomain.loadRelated(FamilyDomain.FamilyMemberDomain.class,
                    x -> x.like(FamilyDomain.FamilyMemberDomain::getName, "%王%")
                            .or(y -> y.ne(FamilyDomain.FamilyMemberDomain::getPhone, "13800138000"))
                            .or(y -> y.in(FamilyDomain.FamilyMemberDomain::getType,
                                    ListUtil.toList("父亲", "母亲", "儿子"))));
            return "测试6完成 - 不同操作符混合，请查看控制台SQL输出";
        } catch (Exception e) {
            return "测试6失败: " + e.getMessage();
        }
    }

    /**
     * 测试场景7：嵌套AND和OR
     * 期望SQL: WHERE family_id = ? AND (name = ? AND phone = ?) AND (type = ? OR type
     * = ?)
     */
    @GetMapping("/test-sql-7")
    public String testNestedAndOr() {
        try {
            FamilyDomain findDomain = FamilyDomain.load(1778216962, familyService);
            findDomain.loadRelated(FamilyDomain.FamilyMemberDomain.class,
                    x -> x.and(y -> y.eq(FamilyDomain.FamilyMemberDomain::getName, "王芳")
                            .eq(FamilyDomain.FamilyMemberDomain::getPhone, "13800138000"))
                            .and(z -> z.eq(FamilyDomain.FamilyMemberDomain::getType, "父亲")
                                    .or(w -> w.eq(FamilyDomain.FamilyMemberDomain::getType, "母亲"))));
            return "测试7完成 - 嵌套AND和OR，请查看控制台SQL输出";
        } catch (Exception e) {
            return "测试7失败: " + e.getMessage();
        }
    }

    /**
     * 运行所有测试场景
     */
    @GetMapping("/test-sql-all")
    public String testAllScenarios() {
        StringBuilder result = new StringBuilder();
        try {
            result.append("开始运行所有测试场景...\n\n");

            result.append("1. ").append(testBasicMixedOr()).append("\n");
            result.append("2. ").append(testPureAnd()).append("\n");
            result.append("3. ").append(testPureOr()).append("\n");
            result.append("4. ").append(testComplexMixed()).append("\n");
            result.append("5. ").append(testMultipleOr()).append("\n");
            result.append("6. ").append(testDifferentOperators()).append("\n");
            result.append("7. ").append(testNestedAndOr()).append("\n");

            result.append("\n所有测试完成！请查看控制台SQL输出验证结果。");

            return result.toString();
        } catch (Exception e) {
            return "批量测试失败: " + e.getMessage();
        }
    }

    /**
     * 主测试方法 - 基本混合OR逻辑测试
     * 保持原有URL路径，测试最常见的场景
     */
    @GetMapping("/test-sql")
    public String testSqlGeneration() {
        return testBasicMixedOr();
    }

    /**
     * 查看测试数据状态 - 不执行查询，只显示可用的测试ID
     */
    @GetMapping("/test-data-info")
    public String testDataInfo() {
        return "可用的测试ID: 1, 2, 3\n" +
                "测试字段说明:\n" +
                "- name: 姓名 (例: 王芳, 张三)\n" +
                "- phone: 电话 (例: 13800138000)\n" +
                "- type: 关系 (例: 父亲, 母亲, 儿子)\n" +
                "- family_id: 家庭ID (系统自动添加的关联条件)\n\n" +
                "使用任意 test-sql-* 端点查看生成的SQL语句";
    }

    // ==================== 新增：分页和批量操作示例 ====================

    /**
     * 分页查询家庭列表
     * 演示MyBatis Plus分页插件的使用
     * 
     * 示例URL:
     * /family/v1/page?current=1&size=10
     * /family/v1/page?current=1&size=5&name=张
     */
    @GetMapping("/page")
    public IPage<FamilyDO> getPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String name) {

        // 创建分页对象
        Page<FamilyDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<FamilyDO> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like(FamilyDO::getName, name);
        }
        queryWrapper.orderByDesc(FamilyDO::getId);

        // 执行分页查询
        return familyMapper.selectPage(page, queryWrapper);
    }

    /**
     * 批量插入家庭数据
     * 演示增强批量操作和ID生成器的使用
     * 
     * 请求体示例：
     * [
     * {"name": "张三家庭", "personCount": 4},
     * {"name": "李四家庭", "personCount": 3},
     * {"name": "王五家庭", "personCount": 5}
     * ]
     */
    @PostMapping("/batch-insert")
    public String batchInsert(@RequestBody List<FamilyDO> families) {
        try {
            // 记录插入前的状态
            StringBuilder result = new StringBuilder();
            result.append("🔧 MyBatis配置功能演示 - 批量插入测试\n\n");
            result.append("插入前ID状态:\n");
            for (int i = 0; i < families.size(); i++) {
                result.append(String.format("  家庭%d: %s, ID: %s\n",
                        i + 1, families.get(i).getName(), families.get(i).getId()));
            }

            // 执行批量插入（使用增强的批量操作）
            int insertCount = familyMapper.batchInsert(families);

            // 记录插入后的状态
            result.append("\n插入后ID状态（展示自定义ID生成器效果）:\n");
            for (int i = 0; i < families.size(); i++) {
                result.append(String.format("  家庭%d: %s, ID: %s ✅\n",
                        i + 1, families.get(i).getName(), families.get(i).getId()));
            }

            result.append(String.format("\n✅ 成功插入 %d 条记录", insertCount));
            result.append("\n\n🎯 功能特性展示:");
            result.append("\n- ✅ 自定义ID生成器：自动为每条记录生成唯一ID");
            result.append("\n- ✅ 增强批量插入：支持主键回填功能");
            result.append("\n- ✅ 数据库特定优化：使用PostgreSQL特有的批量插入语法");

            return result.toString();

        } catch (Exception e) {
            return "❌ 批量插入失败: " + e.getMessage();
        }
    }

    /**
     * 批量更新家庭数据
     * 演示ignore null策略的批量更新
     * 
     * 请求体示例：
     * [
     * {"id": 1, "name": "张三家庭更新"},
     * {"id": 2, "personCount": 6},
     * {"id": 3, "name": "王五家庭更新", "personCount": 4}
     * ]
     */
    @PostMapping("/batch-update")
    public String batchUpdate(@RequestBody List<FamilyDO> families) {
        try {
            StringBuilder result = new StringBuilder();
            result.append("🔧 MyBatis配置功能演示 - 批量更新测试\n\n");

            // 记录更新的字段信息
            result.append("批量更新信息:\n");
            for (int i = 0; i < families.size(); i++) {
                FamilyDO family = families.get(i);
                result.append(String.format("  记录%d (ID: %s):\n", i + 1, family.getId()));
                if (family.getName() != null) {
                    result.append(String.format("    - 更新name: %s\n", family.getName()));
                }
                if (family.getPersonCount() != null) {
                    result.append(String.format("    - 更新personCount: %s\n", family.getPersonCount()));
                }
                if (family.getName() == null && family.getPersonCount() == null) {
                    result.append("    - 仅提供ID，其他字段为null (将跳过更新)\n");
                }
            }

            // 执行批量更新
            int updateCount = familyMapper.batchUpdate(families);

            result.append(String.format("\n✅ 成功更新 %d 条记录", updateCount));
            result.append("\n\n🎯 功能特性展示:");
            result.append("\n- ✅ Ignore Null策略：null字段不会被更新");
            result.append("\n- ✅ 增强批量更新：支持部分字段更新");
            result.append("\n- ✅ 逻辑删除字段过滤：自动跳过逻辑删除字段");

            return result.toString();
        } catch (Exception e) {
            return "❌ 批量更新失败: " + e.getMessage();
        }
    }

    /**
     * 测试ID生成器单独功能
     * 演示自定义ID生成器的效果
     */
    @GetMapping("/test-id-generator")
    public String testIdGenerator() {
        try {
            StringBuilder result = new StringBuilder();
            result.append("🔧 自定义ID生成器测试\n\n");

            // 创建测试数据
            FamilyDO family = new FamilyDO();
            family.setName("测试家庭_" + System.currentTimeMillis());
            family.setPersonCount(3);

            result.append("插入前状态:\n");
            result.append(String.format("  名称: %s\n", family.getName()));
            result.append(String.format("  ID: %s (null - 待生成)\n", family.getId()));

            // 插入数据，观察ID生成
            int insertResult = familyMapper.insert(family);

            result.append("\n插入后状态:\n");
            result.append(String.format("  名称: %s\n", family.getName()));
            result.append(String.format("  生成的ID: %s ✅\n", family.getId()));
            result.append(String.format("  影响行数: %d\n", insertResult));

            result.append("\n🎯 ID生成器特性:");
            result.append("\n- ✅ 基于时间戳：确保ID递增趋势");
            result.append("\n- ✅ 机器标识：支持分布式环境");
            result.append("\n- ✅ 序列号：同一时间戳内保证唯一性");
            result.append("\n- ✅ 自动回填：插入后实体对象包含生成的ID");

            return result.toString();
        } catch (Exception e) {
            return "❌ ID生成器测试失败: " + e.getMessage();
        }
    }

    /**
     * 分页插件功能演示
     * 展示分页配置的各种特性
     */
    @GetMapping("/test-pagination")
    public String testPagination() {
        try {
            StringBuilder result = new StringBuilder();
            result.append("🔧 分页插件功能演示\n\n");

            // 测试基本分页
            Page<FamilyDO> page1 = new Page<>(1, 3);
            IPage<FamilyDO> result1 = familyMapper.selectPage(page1, null);

            result.append("基本分页测试 (第1页，每页3条):\n");
            result.append(String.format("  总记录数: %d\n", result1.getTotal()));
            result.append(String.format("  总页数: %d\n", result1.getPages()));
            result.append(String.format("  当前页: %d\n", result1.getCurrent()));
            result.append(String.format("  每页大小: %d\n", result1.getSize()));
            result.append(String.format("  本页记录数: %d\n", result1.getRecords().size()));

            // 测试条件分页
            Page<FamilyDO> page2 = new Page<>(1, 5);
            LambdaQueryWrapper<FamilyDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(FamilyDO::getId);
            IPage<FamilyDO> result2 = familyMapper.selectPage(page2, wrapper);

            result.append("\n条件分页测试 (按ID降序):\n");
            result.append(String.format("  查询条件: ORDER BY id DESC\n"));
            result.append(String.format("  返回记录数: %d\n", result2.getRecords().size()));
            if (!result2.getRecords().isEmpty()) {
                result.append(String.format("  第一条记录ID: %s\n", result2.getRecords().get(0).getId()));
            }

            result.append("\n🎯 分页插件特性:");
            result.append("\n- ✅ 自动检测数据库类型");
            result.append("\n- ✅ 自动添加LIMIT/OFFSET语句");
            result.append("\n- ✅ 自动执行COUNT查询");
            result.append("\n- ✅ 防止超大分页查询（最大10000条）");
            result.append("\n- ✅ 支持复杂查询条件");

            return result.toString();
        } catch (Exception e) {
            return "❌ 分页测试失败: " + e.getMessage();
        }
    }

    /**
     * MyBatis配置总览
     * 展示所有配置的功能特性
     */
    @GetMapping("/config-overview")
    public String configOverview() {
        return "🔧 MyBatis Plus 配置功能总览\n\n" +
                "1️⃣ 分页插件 (PaginationInnerInterceptor):\n" +
                "   ✅ 自动检测数据库类型 (MySQL/PostgreSQL/Oracle等)\n" +
                "   ✅ 自动添加分页SQL语句\n" +
                "   ✅ 支持最大分页限制 (当前设置: 10000条)\n" +
                "   ✅ 防止分页溢出处理\n" +
                "   📍 测试端点: /family/v1/page, /family/v1/test-pagination\n\n" +

                "2️⃣ 自定义ID生成器 (SimpleIdentifierGenerator):\n" +
                "   ✅ 基于时间戳的ID生成\n" +
                "   ✅ 机器标识集成 (支持分布式)\n" +
                "   ✅ 序列号机制 (避免冲突)\n" +
                "   ✅ 自动回填到实体对象\n" +
                "   📍 测试端点: /family/v1/test-id-generator\n\n" +

                "3️⃣ 乐观锁插件 (OptimisticLockerInnerInterceptor):\n" +
                "   ✅ 自动处理@Version注解字段\n" +
                "   ✅ 防止并发更新数据丢失\n" +
                "   📍 在有@Version字段的实体上自动生效\n\n" +

                "4️⃣ 防全表操作插件 (BlockAttackInnerInterceptor):\n" +
                "   ✅ 防止无WHERE条件的UPDATE\n" +
                "   ✅ 防止无WHERE条件的DELETE\n" +
                "   ✅ 保护数据安全\n\n" +

                "5️⃣ 增强批量操作:\n" +
                "   ✅ 批量插入 (batchInsert) - 支持主键回填\n" +
                "   ✅ 批量更新 (batchUpdate) - 支持ignore null策略\n" +
                "   ✅ 数据库特定SQL优化\n" +
                "   ✅ 逻辑删除字段自动过滤\n" +
                "   📍 测试端点: /family/v1/batch-insert, /family/v1/batch-update\n\n" +

                "6️⃣ KeySequence 支持:\n" +
                "   ✅ PostgreSQL/Oracle 序列主键支持\n" +
                "   ✅ 自动处理序列值生成\n" +
                "   ✅ 兼容不同版本的MyBatis Plus\n" +
                "   📍 测试端点: /family/v1/debug-keysequence\n\n" +

                "📋 使用建议:\n" +
                "   • 分页查询使用Page<T>对象\n" +
                "   • ID字段使用IdType.ASSIGN_ID让自定义生成器工作\n" +
                "   • 批量操作优先使用batchInsert/batchUpdate方法\n" +
                "   • 乐观锁字段添加@Version注解\n" +
                "   • 生产环境注意分页大小限制\n" +
                "   • 序列主键使用@KeySequence注解";
    }

    /**
     * 调试 KeySequence 问题
     * 分析为什么 TableInfo 中找不到 KeySequence
     */
    @GetMapping("/debug-keysequence")
    public String debugKeySequence() {
        try {
            StringBuilder result = new StringBuilder();
            result.append("🔍 KeySequence 调试报告\n\n");

            // 测试不同实体类的 KeySequence 情况
            result.append("📋 实体类分析:\n\n");

            // 1. 分析 FamilyDO
            result.append("1️⃣ FamilyDO 分析:\n");
            result.append(analyzeEntityKeySequence(FamilyDO.class));
            result.append("\n");

            // 2. 分析 FamilyAddressDO
            result.append("2️⃣ FamilyAddressDO 分析:\n");
            result.append(analyzeEntityKeySequence(com.artframework.sample.entities.FamilyAddressDO.class));
            result.append("\n");

            // 3. 提供解决方案
            result.append("🔧 解决方案:\n\n");
            result.append("方案1: 直接从实体类获取注解\n");
            result.append("```java\n");
            result.append("// 获取 KeySequence 注解\n");
            result.append("KeySequence keySeq = entityClass.getAnnotation(KeySequence.class);\n");
            result.append("String sequenceName = keySeq != null ? keySeq.value() : null;\n");
            result.append("```\n\n");

            result.append("方案2: 检查 MyBatis Plus 版本\n");
            result.append("如果 TableInfo.getKeySequence() 方法不存在，说明版本较老\n");
            result.append("建议升级到 3.5.3+ 版本\n\n");

            result.append("方案3: 使用反射工具类\n");
            result.append("项目中已提供 TableInfoUtils 工具类解决此问题\n\n");

            result.append("🎯 当前项目状态:\n");
            result.append("- PostgreSQL 增强批量操作已兼容 KeySequence\n");
            result.append("- 自动从实体类注解获取序列信息\n");
            result.append("- 支持序列主键的批量插入和更新\n");

            return result.toString();

        } catch (Exception e) {
            return "❌ KeySequence 调试失败: " + e.getMessage();
        }
    }

    /**
     * 分析实体类的 KeySequence 配置
     */
    private String analyzeEntityKeySequence(Class<?> entityClass) {
        StringBuilder info = new StringBuilder();

        try {
            info.append("  类名: ").append(entityClass.getSimpleName()).append("\n");

            // 检查 @KeySequence 注解
            com.baomidou.mybatisplus.annotation.KeySequence keySeq = entityClass
                    .getAnnotation(com.baomidou.mybatisplus.annotation.KeySequence.class);

            if (keySeq != null) {
                info.append("  ✅ KeySequence: ").append(keySeq.value()).append("\n");
                info.append("  ✅ 序列类型: ").append(keySeq.getClass().getSimpleName()).append("\n");
            } else {
                info.append("  ❌ KeySequence: 未找到注解\n");
            }

            // 检查主键字段配置
            java.lang.reflect.Field[] fields = entityClass.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                com.baomidou.mybatisplus.annotation.TableId tableId = field
                        .getAnnotation(com.baomidou.mybatisplus.annotation.TableId.class);
                if (tableId != null) {
                    info.append("  主键字段: ").append(field.getName()).append("\n");
                    info.append("  主键类型: ").append(tableId.type()).append("\n");
                    break;
                }
            }

        } catch (Exception e) {
            info.append("  ❌ 分析失败: ").append(e.getMessage()).append("\n");
        }

        return info.toString();
    }
}
