package com.artframework.sample.test;

import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.utils.LambdaQueryUtils;
import com.artframework.sample.domains.family.domain.FamilyDomain;
import com.artframework.sample.entities.FamilyMemberDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;

/**
 * 测试 SQL 生成逻辑
 */
public class SqlGenerationTest {

    @Test
    void testSqlGeneration() {
        // 1. 创建外键查询
        LambdaQuery<FamilyDomain.FamilyMemberDomain> lambdaQuery =
            LambdaQuery.of(FamilyDomain.FamilyMemberDomain.class);
        lambdaQuery.eq(FamilyDomain.FamilyMemberDomain::getFamilyId, 1L);

        System.out.println("=== 外键条件后的结构 ===");
        System.out.println("rootFilter 子元素数量: " + lambdaQuery.getFilter().getCondition().size());

        // 2. 创建用户 OR 查询
        LambdaQuery<FamilyDomain.FamilyMemberDomain> userQuery =
            LambdaQuery.of(FamilyDomain.FamilyMemberDomain.class);
        userQuery.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                 .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER");

        System.out.println("\n=== 用户条件结构 ===");
        System.out.println("userFilter logic: " + userQuery.getFilter().getLogic());
        System.out.println("userFilter 子元素数量: " + userQuery.getFilter().getCondition().size());
        userQuery.getFilter().getCondition().forEach(child -> {
            System.out.println("子元素类型: " + child.getClass().getSimpleName());
        });

        // 3. 模拟 combine
        LambdaQuery.ConditionGroup rootFilter = lambdaQuery.getFilter();
        LambdaQuery.ConditionGroup userFilter = userQuery.getFilter();

        // 展开用户 filter
        if (userFilter.getLogic() == null || "AND".equals(userFilter.getLogic().name())) {
            for (Object child : userFilter.getCondition()) {
                rootFilter.addChild(child);
            }
        } else {
            rootFilter.addChild(userFilter);
        }

        System.out.println("\n=== 合并后的结构 ===");
        System.out.println("rootFilter 子元素数量: " + rootFilter.getCondition().size());
        rootFilter.getCondition().forEach(child -> {
            System.out.println("子元素类型: " + child.getClass().getSimpleName());
        });

        // 4. 构建 MyBatis Plus wrapper
        LambdaQueryWrapper<FamilyMemberDO> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryUtils.buildFilterWrapper(wrapper, lambdaQuery.getFilter(), FamilyMemberDO.class);

        System.out.println("\n=== 生成的 SQL ===");
        System.out.println(wrapper.getTargetSql());
        System.out.println("\n期望: family_id = ? AND (type = ? OR type = ?)");
        System.out.println("实际: " + wrapper.getCustomSqlSegment());
    }
}
