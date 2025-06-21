package com.artframework.sample.controllers;

import cn.hutool.core.collection.ListUtil;
import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.domains.family.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
