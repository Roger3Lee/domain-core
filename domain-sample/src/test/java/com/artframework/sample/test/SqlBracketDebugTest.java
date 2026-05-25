//package com.artframework.sample.test;
//
//import com.artframework.domain.core.lambda.query.LambdaQuery;
//import com.artframework.domain.core.lambda.query.LogicalOperator;
//import com.artframework.sample.domains.family.domain.FamilyDomain;
//import org.junit.jupiter.api.Test;
//
///**
// * 调试 SQL 括号问题
// */
//public class SqlBracketDebugTest {
//
//    @Test
//    void debugQueryStructure() {
//        // 模拟外键条件
//        LambdaQuery<FamilyDomain.FamilyMemberDomain> query =
//            LambdaQuery.of(FamilyDomain.FamilyMemberDomain.class);
//        query.eq(FamilyDomain.FamilyMemberDomain::getFamilyId, 1L);
//
//        System.out.println("=== 添加外键条件后 ===");
//        printQueryStructure(query.getFilter(), 0);
//
//        // 模拟用户条件（链式 OR）
//        LambdaQuery<FamilyDomain.FamilyMemberDomain> userQuery =
//            LambdaQuery.of(FamilyDomain.FamilyMemberDomain.class);
//        userQuery.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
//                 .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER");
//
//        System.out.println("\n=== 用户条件结构 ===");
//        printQueryStructure(userQuery.getFilter(), 0);
//
//        // 模拟 combineFilter 合并
//        LambdaQuery.ConditionGroup userFilter = userQuery.getFilter();
//        LambdaQuery.ConditionGroup rootFilter = query.getFilter();
//
//        // 检查用户条件结构
//        System.out.println("\n=== 分析用户条件 ===");
//        System.out.println("用户 filter logic: " + userFilter.getLogic());
//        System.out.println("用户 filter 子元素数量: " + userFilter.getCondition().size());
//
//        if (userFilter.getCondition().size() == 1) {
//            Object onlyChild = userFilter.getCondition().get(0);
//            System.out.println("唯一子元素类型: " + onlyChild.getClass().getSimpleName());
//            if (onlyChild instanceof LambdaQuery.ConditionGroup) {
//                LambdaQuery.ConditionGroup childGroup = (LambdaQuery.ConditionGroup) onlyChild;
//                System.out.println("子元素 logic: " + childGroup.getLogic());
//                System.out.println("子元素条件数量: " + childGroup.getCondition().size());
//            }
//        }
//
//        // 合并后的结构
//        rootFilter.addChild(userFilter);
//
//        System.out.println("\n=== 合并后结构（当前实现）===");
//        printQueryStructure(rootFilter, 0);
//
//        // 期望的结构
//        System.out.println("\n=== 期望结构 ===");
//        System.out.println("rootFilter (AND)");
//        System.out.println("  ├─ family_id = ?");
//        System.out.println("  └─ OR group");
//        System.out.println("     ├─ type = ?");
//        System.out.println("     └─ type = ?");
//    }
//
//    private void printQueryStructure(LambdaQuery.ConditionGroup group, int indent) {
//        String prefix = "  ".repeat(indent);
//        System.out.println(prefix + "ConditionGroup (logic=" + group.getLogic() + ")");
//
//        for (Object child : group.getCondition()) {
//            if (child instanceof LambdaQuery.Condition) {
//                LambdaQuery.Condition condition = (LambdaQuery.Condition) child;
//                System.out.println(prefix + "  ├─ Condition: " + condition.getField() +
//                                 " " + condition.getOp() + " " + condition.getValue());
//            } else if (child instanceof LambdaQuery.ConditionGroup) {
//                System.out.println(prefix + "  └─ ");
//                printQueryStructure((LambdaQuery.ConditionGroup) child, indent + 2);
//            }
//        }
//    }
//}
