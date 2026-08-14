package io.github.roger3lee.sample.test;

import io.github.roger3lee.domain.core.constants.Order;
import io.github.roger3lee.domain.core.lambda.query.LambdaQuery;
import io.github.roger3lee.domain.core.utils.LambdaQueryUtils;
import io.github.roger3lee.domain.core.utils.LoadFlagUtils;
import com.artframework.sample.domains.family.domain.FamilyDomain;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * LoadFlag 基于 Map&lt;String, LambdaQuery&gt; 结构的 JSON 序列化/反序列化测试
 */
public class LoadFlagJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 序列化：lambdaQuery 构建的 LoadFlag 序列化为 {"query":{"实体名":{"filter":...,"order":...}}}
     */
    @Test
    void testSerialize() throws Exception {
        FamilyDomain.LoadFlag loadFlag = FamilyDomain.LoadFlag.builder()
                .loadFamilyMemberDomain(true)
                .build();
        loadFlag.lambdaQuery(FamilyDomain.FamilyMemberDomain.class, q -> q
                .eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
                .orderBy(FamilyDomain.FamilyMemberDomain::getName, Order.DESC));

        String json = objectMapper.writeValueAsString(loadFlag);
        System.out.println("序列化结果: " + json);

        Assertions.assertTrue(json.contains("\"FamilyMemberDomain\""), "应包含实体名 key");
        Assertions.assertTrue(json.contains("\"filter\""), "应包含 filter 属性");
        Assertions.assertTrue(json.contains("\"order\""), "应包含 order 属性");
        Assertions.assertFalse(json.contains("entityClass"), "不应序列化 entityClass");
    }

    /**
     * 反序列化：旧线格式 JSON 应能解析为 LambdaQuery，且条件/排序完整
     */
    @Test
    void testDeserialize() throws Exception {
        String json = "{\"loadFamilyMemberDomain\":true,\"query\":{\"FamilyMemberDomain\":{" +
                "\"filter\":{\"condition\":[" +
                "{\"field\":\"family_id\",\"op\":\"EQ\",\"value\":1}," +
                "{\"logic\":\"OR\",\"condition\":[" +
                "{\"field\":\"type\",\"op\":\"EQ\",\"value\":\"SON\"}," +
                "{\"field\":\"type\",\"op\":\"EQ\",\"value\":\"DAUGHTER\"}]}]}," +
                "\"order\":[{\"field\":\"name\",\"order\":\"DESC\"}]}}}";

        FamilyDomain.LoadFlag loadFlag = objectMapper.readValue(json, FamilyDomain.LoadFlag.class);

        Assertions.assertTrue(loadFlag.getLoadFamilyMemberDomain());
        LambdaQuery<?> entityQuery = LambdaQueryUtils.getEntityLambdaQuery(loadFlag, "FamilyMemberDomain");
        Assertions.assertNotNull(entityQuery, "应解析出实体的 LambdaQuery");
        Assertions.assertTrue(entityQuery.hasFilter(), "应包含过滤条件");
        Assertions.assertEquals(2, entityQuery.getFilter().getCondition().size(), "根组应含2个子元素");
        Assertions.assertEquals(1, entityQuery.getOrderItems().size(), "应含1个排序项");
        Assertions.assertEquals("name", entityQuery.getOrderItems().get(0).getField());
        Assertions.assertEquals(Order.DESC, entityQuery.getOrderItems().get(0).getOrder());

        // 反序列化后的条件应能正常 combine 到目标查询
        LambdaQuery<FamilyDomain.FamilyMemberDomain> targetQuery =
                LambdaQuery.of(FamilyDomain.FamilyMemberDomain.class);
        LambdaQueryUtils.combine(targetQuery, loadFlag, FamilyDomain.FamilyMemberDomain.class);
        Assertions.assertEquals(2, targetQuery.getFilter().getCondition().size(), "combine 后应含2个子元素");
        Assertions.assertEquals(1, targetQuery.getOrderItems().size(), "combine 后应含1个排序项");
    }

    /**
     * 往返：序列化 -> 反序列化 -> 条件结构保持一致
     */
    @Test
    void testRoundTrip() throws Exception {
        FamilyDomain.LoadFlag source = FamilyDomain.LoadFlag.builder().build();
        source.lambdaQuery(FamilyDomain.FamilyMemberDomain.class, q -> q
                .eq(FamilyDomain.FamilyMemberDomain::getFamilyId, 1L)
                .and(sub -> sub.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                        .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER"))
                .orderBy(FamilyDomain.FamilyMemberDomain::getName, Order.DESC));

        String json = objectMapper.writeValueAsString(source);
        FamilyDomain.LoadFlag restored = objectMapper.readValue(json, FamilyDomain.LoadFlag.class);
        String json2 = objectMapper.writeValueAsString(restored);
        System.out.println("往返前: " + json);
        System.out.println("往返后: " + json2);

        Assertions.assertEquals(json, json2, "两次序列化结果应一致");
    }

    /**
     * 合并：mergeEntityQuery / mergeQueryCondition 在新结构下工作正常
     */
    @Test
    void testMerge() {
        FamilyDomain.LoadFlag target = FamilyDomain.LoadFlag.builder().build();
        target.lambdaQuery(FamilyDomain.FamilyMemberDomain.class, q -> q
                .eq(FamilyDomain.FamilyMemberDomain::getFamilyId, 1L)
                .orderBy(FamilyDomain.FamilyMemberDomain::getName, Order.DESC));

        FamilyDomain.LoadFlag sourceFlag = FamilyDomain.LoadFlag.builder().build();
        sourceFlag.lambdaQuery(FamilyDomain.FamilyMemberDomain.class, q -> q
                .eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                .orderBy(FamilyDomain.FamilyMemberDomain::getName, Order.DESC));

        LoadFlagUtils.mergeEntityQuery(target, sourceFlag, "FamilyMemberDomain");

        LambdaQuery<?> merged = LambdaQueryUtils.getEntityLambdaQuery(target, "FamilyMemberDomain");
        Assertions.assertNotNull(merged);
        Assertions.assertEquals(2, merged.getFilter().getCondition().size(), "过滤条件应 AND 合并");
        Assertions.assertEquals(1, merged.getOrderItems().size(), "重复排序项应去重");

        // mergeQueryCondition：目标不存在时直接放入
        FamilyDomain.LoadFlag empty = FamilyDomain.LoadFlag.builder().build();
        LambdaQuery<FamilyDomain.FamilyMemberDomain> query =
                LambdaQuery.of(FamilyDomain.FamilyMemberDomain.class);
        query.eq(FamilyDomain.FamilyMemberDomain::getType, "SON");
        LoadFlagUtils.mergeQueryCondition(empty, query, "FamilyMemberDomain");
        Assertions.assertSame(query,
                LambdaQueryUtils.getEntityLambdaQuery(empty, "FamilyMemberDomain"));
    }
}
