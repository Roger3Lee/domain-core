package com.artframework.sample.controllers;

import com.artframework.domain.core.constants.Order;
import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.domains.family.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController()
@RequestMapping("/family/v1")
public class FamilyController {
    @Autowired
    private FamilyService familyService;

    /**
    * 查找
    * @param request 请求体
    * @return FamilyDomain
    */
    @PostMapping("/query")
    public FamilyDomain find(@RequestBody FamilyFindDomain request){
        return FamilyDomain.load(1, familyService)
                .loadRelated(FamilyDomain.FamilyAddressDomain.class)
                .loadRelated(FamilyDomain.FamilyMemberDomain.class,
                        x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                                .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
                                .orderBy(FamilyDomain.FamilyMemberDomain::getId, Order.DESC));
    }

    /**
    * 新增
    * @param request 请求体
    * @return Long
    */
    @PutMapping()
    public Long insert(@RequestBody FamilyDomain request){
        return familyService.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @PostMapping()
    public Boolean update(@RequestBody FamilyDomain request){
        return familyService.update(request);
    }

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    @DeleteMapping
    public Boolean delete(@RequestParam("key") Long key){
        return familyService.delete(key);
    }

    /**
     * SQL 测试场景 - 用于验证所有查询场景的 SQL 生成
     *
     * 访问: GET /family/v1/test-sql-scenarios?scenario=all
     *
     * @param scenario 场景名称: all, scenario1, scenario2, etc.
     * @return 测试结果描述
     */
    @GetMapping("/test-sql-scenarios")
    public Map<String, Object> testSqlScenarios(@RequestParam(value = "scenario", defaultValue = "all") String scenario) {
        Map<String, Object> result = new HashMap<>();

        try {
            switch (scenario) {
                case "all":
                    // 执行所有场景
                    log.info("\n\n========== 开始执行所有 SQL 测试场景 ==========\n");
                    testScenario1();
                    testScenario2();
                    testScenario3();
                    testScenario4();
                    testScenario5();
                    testScenario6();
                    testScenario7();
                    testScenario8();
                    testScenario9();
                    testScenario10();
                    testScenario11();
                    testScenario12();
                    log.info("\n========== 所有 SQL 测试场景执行完毕 ==========\n\n");
                    result.put("status", "success");
                    result.put("message", "已执行所有 12 个测试场景，请查看日志中的 SQL 输出");
                    break;

                case "scenario1":
                    testScenario1();
                    result.put("status", "success");
                    result.put("message", "场景1执行完成");
                    break;

                case "scenario2":
                    testScenario2();
                    result.put("status", "success");
                    result.put("message", "场景2执行完成");
                    break;

                case "scenario3":
                    testScenario3();
                    result.put("status", "success");
                    result.put("message", "场景3执行完成");
                    break;

                case "scenario4":
                    testScenario4();
                    result.put("status", "success");
                    result.put("message", "场景4执行完成");
                    break;

                case "scenario5":
                    testScenario5();
                    result.put("status", "success");
                    result.put("message", "场景5执行完成");
                    break;

                case "scenario6":
                    testScenario6();
                    result.put("status", "success");
                    result.put("message", "场景6执行完成");
                    break;

                case "scenario7":
                    testScenario7();
                    result.put("status", "success");
                    result.put("message", "场景7执行完成");
                    break;

                case "scenario8":
                    testScenario8();
                    result.put("status", "success");
                    result.put("message", "场景8执行完成");
                    break;

                case "scenario9":
                    testScenario9();
                    result.put("status", "success");
                    result.put("message", "场景9执行完成");
                    break;

                case "scenario10":
                    testScenario10();
                    result.put("status", "success");
                    result.put("message", "场景10执行完成");
                    break;

                case "scenario11":
                    testScenario11();
                    result.put("status", "success");
                    result.put("message", "场景11执行完成");
                    break;

                case "scenario12":
                    testScenario12();
                    result.put("status", "success");
                    result.put("message", "场景12执行完成");
                    break;

                default:
                    result.put("status", "error");
                    result.put("message", "未知的场景: " + scenario + "。支持的场景: all, scenario1-scenario12");
            }
        } catch (Exception e) {
            log.error("执行测试场景失败: " + scenario, e);
            result.put("status", "error");
            result.put("message", "执行失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 场景1: 基本 loadRelated + 单个条件
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ?
     */
    private void testScenario1() {
        log.info("\n========== 场景1: 基本 loadRelated + 单个条件 ==========");
        log.info("代码: family.loadRelated(FamilyMemberDomain.class, x -> x.eq(FamilyMemberDomain::getType, \"SON\"))");
        log.info("期望 SQL: WHERE family_id = ? AND type = ?");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
        );
    }

    /**
     * 场景2: loadRelated + 链式 OR（2个条件）
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND (type = ? OR type = ?)
     */
    private void testScenario2() {
        log.info("\n========== 场景2: loadRelated + 链式 OR（2个条件）==========");
        log.info("代码: x -> x.eq(type, \"SON\").or().eq(type, \"DAUGHTER\")");
        log.info("期望 SQL: WHERE family_id = ? AND (type = ? OR type = ?)");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                  .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
        );
    }

    /**
     * 场景3: loadRelated + 链式 OR（3个条件）
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND (type = ? OR type = ? OR type = ?)
     */
    private void testScenario3() {
        log.info("\n========== 场景3: loadRelated + 链式 OR（3个条件）==========");
        log.info("代码: x -> x.eq(type, \"SON\").or().eq(type, \"DAUGHTER\").or().eq(type, \"FATHER\")");
        log.info("期望 SQL: WHERE family_id = ? AND (type = ? OR type = ? OR type = ?)");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                  .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
                  .or().eq(FamilyDomain.FamilyMemberDomain::getType, "FATHER")
        );
    }

    /**
     * 场景4: loadRelated + in() 查询（替代链式 OR）
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND type IN (?, ?, ?)
     */
    private void testScenario4() {
        log.info("\n========== 场景4: loadRelated + in() 查询 ==========");
        log.info("代码: x -> x.in(type, Arrays.asList(\"SON\", \"DAUGHTER\", \"FATHER\"))");
        log.info("期望 SQL: WHERE family_id = ? AND type IN (?, ?, ?)");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.in(FamilyDomain.FamilyMemberDomain::getType,
                     Arrays.asList("SON", "DAUGHTER", "FATHER"))
        );
    }

    /**
     * 场景5: loadRelated + 混合 AND 和链式 OR
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND name LIKE ? AND (type = ? OR type = ?)
     */
    private void testScenario5() {
        log.info("\n========== 场景5: loadRelated + 混合 AND 和链式 OR ==========");
        log.info("代码: x -> x.like(name, \"张\").eq(type, \"SON\").or().eq(type, \"DAUGHTER\")");
        log.info("期望 SQL: WHERE family_id = ? AND name LIKE ? AND (type = ? OR type = ?)");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.like(FamilyDomain.FamilyMemberDomain::getName, "张")
                  .eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                  .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
        );
    }

    /**
     * 场景6: loadRelated + or(Consumer)（OR 一个 AND 组）
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND (type = ? OR (type = ? AND name LIKE ?))
     */
    private void testScenario6() {
        log.info("\n========== 场景6: loadRelated + or(Consumer)（OR 一个 AND 组）==========");
        log.info("代码: x -> x.eq(type, \"SON\").or(q -> q.eq(type, \"DAUGHTER\").like(name, \"李\"))");
        log.info("期望 SQL: WHERE family_id = ? AND (type = ? OR (type = ? AND name LIKE ?))");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                  .or(q -> q.eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
                            .like(FamilyDomain.FamilyMemberDomain::getName, "李"))
        );
    }

    /**
     * 场景7: loadRelated + and() 嵌套
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND (type = ? AND name LIKE ?)
     */
    private void testScenario7() {
        log.info("\n========== 场景7: loadRelated + and() 嵌套 ==========");
        log.info("代码: x -> x.and(q -> q.eq(type, \"SON\").like(name, \"张\"))");
        log.info("期望 SQL: WHERE family_id = ? AND (type = ? AND name LIKE ?)");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.and(q -> q.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                             .like(FamilyDomain.FamilyMemberDomain::getName, "张"))
        );
    }

    /**
     * 场景8: loadRelated + 复杂嵌套（AND 包含 OR）
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND name LIKE ? AND (type = ? OR type = ?)
     */
    private void testScenario8() {
        log.info("\n========== 场景8: loadRelated + 复杂嵌套（AND 包含 OR）==========");
        log.info("代码: x -> x.like(name, \"张\").and(q -> q.eq(type, \"SON\").or().eq(type, \"DAUGHTER\"))");
        log.info("期望 SQL: WHERE family_id = ? AND name LIKE ? AND (type = ? OR type = ?)");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.like(FamilyDomain.FamilyMemberDomain::getName, "张")
                  .and(q -> q.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                             .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER"))
        );
    }

    /**
     * 场景9: loadRelated + 多个 or(Consumer)
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND (type = ? OR (type = ? AND name LIKE ?) OR (type = ? AND name LIKE ?))
     */
    private void testScenario9() {
        log.info("\n========== 场景9: loadRelated + 多个 or(Consumer) ==========");
        log.info("代码: x -> x.eq(type, \"SON\")");
        log.info("          .or(q -> q.eq(type, \"DAUGHTER\").like(name, \"李\"))");
        log.info("          .or(q -> q.eq(type, \"FATHER\").like(name, \"王\"))");
        log.info("期望 SQL: WHERE family_id = ? AND (type = ? OR (type = ? AND name LIKE ?) OR (type = ? AND name LIKE ?))");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                  .or(q -> q.eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
                            .like(FamilyDomain.FamilyMemberDomain::getName, "李"))
                  .or(q -> q.eq(FamilyDomain.FamilyMemberDomain::getType, "FATHER")
                            .like(FamilyDomain.FamilyMemberDomain::getName, "王"))
        );
    }

    /**
     * 场景10: loadRelated + 链式 OR + 排序
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND (type = ? OR type = ?) ORDER BY id DESC
     */
    private void testScenario10() {
        log.info("\n========== 场景10: loadRelated + 链式 OR + 排序 ==========");
        log.info("代码: x -> x.eq(type, \"SON\").or().eq(type, \"DAUGHTER\").orderBy(id, DESC)");
        log.info("期望 SQL: WHERE family_id = ? AND (type = ? OR type = ?) ORDER BY id DESC");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                  .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
                  .orderBy(FamilyDomain.FamilyMemberDomain::getId, Order.DESC)
        );
    }

    /**
     * 场景11: 不同字段的链式 OR
     * 期望 SQL: SELECT * FROM family_member WHERE family_id = ? AND (type = ? OR name LIKE ? OR phone = ?)
     */
    private void testScenario11() {
        log.info("\n========== 场景11: 不同字段的链式 OR ==========");
        log.info("代码: x -> x.eq(type, \"SON\").or().like(name, \"张\").or().eq(phone, \"13800138000\")");
        log.info("期望 SQL: WHERE family_id = ? AND (type = ? OR name LIKE ? OR phone = ?)");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyMemberDomain.class,
            x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                  .or().like(FamilyDomain.FamilyMemberDomain::getName, "张")
                  .or().eq(FamilyDomain.FamilyMemberDomain::getPhone, "13800138000")
        );
    }

    /**
     * 场景12: 链式加载多个关联（测试链式调用）
     * 期望 SQL:
     *   1) SELECT * FROM family_address WHERE family_id = ?
     *   2) SELECT * FROM family_member WHERE family_id = ? AND (type = ? OR type = ?)
     */
    private void testScenario12() {
        log.info("\n========== 场景12: 链式加载多个关联 ==========");
        log.info("代码: family.loadRelated(FamilyAddressDomain.class)");
        log.info("            .loadRelated(FamilyMemberDomain.class, x -> x.eq(type, \"SON\").or().eq(type, \"DAUGHTER\"))");
        log.info("期望 SQL 1: WHERE family_id = ?");
        log.info("期望 SQL 2: WHERE family_id = ? AND (type = ? OR type = ?)");
        log.info("实际 SQL:");

        FamilyDomain family = FamilyDomain.load(1, familyService);
        family.loadRelated(FamilyDomain.FamilyAddressDomain.class)
              .loadRelated(FamilyDomain.FamilyMemberDomain.class,
                  x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                        .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
              );
    }
}
