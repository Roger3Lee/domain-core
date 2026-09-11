package io.github.roger3lee.sample.test;

import io.github.roger3lee.domain.core.domain.BaseAggregateDomain;
import io.github.roger3lee.domain.core.domain.BaseDomain;
import io.github.roger3lee.domain.core.domain.PageDomain;
import io.github.roger3lee.domain.core.lambda.query.LambdaQuery;
import io.github.roger3lee.domain.core.repository.BaseRepository;
import io.github.roger3lee.domain.core.service.BaseDomainService;
import io.github.roger3lee.domain.core.service.impl.BaseDomainServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 验证 queryOne/queryList/queryPage 返回的聚合根已自动绑定领域服务，
 * 后续调用 loadRelated 不会再出现 _service 为 null 的 NPE。
 */
public class ServiceBindingTest {

    private interface TestService extends BaseDomainService {
    }

    private static class TestServiceImpl extends BaseDomainServiceImpl implements TestService {
    }

    /**
     * 测试用聚合根，模拟生成代码的行为：
     * loadRelated 内部依赖 _service，未绑定时会 NPE
     */
    private static class TestAggregateDomain extends BaseAggregateDomain<TestAggregateDomain, TestService> {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public <T> TestAggregateDomain loadRelated(Class<T> tClass, LambdaQuery<T> query) {
            if (this._service == null) {
                throw new NullPointerException("_service is null");
            }
            return this;
        }

        Object boundService() {
            return this._service;
        }
    }

    /** 测试用普通实体（非聚合根，无 _service 字段） */
    private static class SimpleDomain extends BaseDomain {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    /**
     * 假仓储：不走数据库，直接返回预置数据
     */
    private static class FakeRepository<D extends BaseDomain> implements BaseRepository<D, Object> {
        private final List<D> data;

        FakeRepository(List<D> data) {
            this.data = data;
        }

        @Override
        public D query(Serializable value, SFunction<D, Serializable> valueWarp) {
            return query(LambdaQuery.of(getDomainClass()).eq(valueWarp, value));
        }

        @Override
        public D query(LambdaQuery<D> lambdaQuery) {
            return data.isEmpty() ? null : data.get(0);
        }

        @Override
        public List<D> queryList(Serializable value, SFunction<D, Serializable> valueWarp) {
            return queryList(LambdaQuery.of(getDomainClass()).eq(valueWarp, value));
        }

        @Override
        public List<D> queryList(LambdaQuery<D> lambdaQuery) {
            return new ArrayList<>(data);
        }

        @Override
        public IPage<D> queryPage(PageDomain pageDomain, LambdaQuery<D> lambdaQuery) {
            Page<D> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());
            page.setRecords(new ArrayList<>(data));
            return page;
        }

        @Override
        public Long queryCount(LambdaQuery<D> lambdaQuery) {
            return (long) data.size();
        }

        @Override
        public D insert(D item) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<D> insert(List<D> list) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int delete(List<D> list) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int deleteByFilter(LambdaQuery<D> lambdaQuery) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int deleteById(Serializable id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int update(D item) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int update(List<D> list) {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unchecked")
        private Class<D> getDomainClass() {
            return (Class<D>) data.stream().findFirst().map(Object::getClass)
                    .orElse((Class<?>) Object.class);
        }
    }

    private static TestServiceImpl newService() {
        return new TestServiceImpl();
    }

    @Test
    void testQueryOneBindsService() {
        TestServiceImpl service = newService();
        TestAggregateDomain domain = new TestAggregateDomain();
        domain.setId(1L);
        service.addRepository(TestAggregateDomain.class, new FakeRepository<>(List.of(domain)));

        LambdaQuery<TestAggregateDomain> query = LambdaQuery.of(TestAggregateDomain.class)
                .eq(TestAggregateDomain::getId, 1L);

        TestAggregateDomain result = service.queryOne(TestAggregateDomain.class, query);

        Assertions.assertNotNull(result);
        // 返回数据已自动绑定服务
        Assertions.assertSame(service, result.boundService());
        // loadRelated 不再 NPE
        Assertions.assertDoesNotThrow(() -> result.loadRelated(Object.class));
    }

    @Test
    void testQueryListBindsService() {
        TestServiceImpl service = newService();
        TestAggregateDomain first = new TestAggregateDomain();
        TestAggregateDomain second = new TestAggregateDomain();
        service.addRepository(TestAggregateDomain.class, new FakeRepository<>(List.of(first, second)));

        LambdaQuery<TestAggregateDomain> query = LambdaQuery.of(TestAggregateDomain.class)
                .eq(TestAggregateDomain::getId, 1L);

        List<TestAggregateDomain> results = service.queryList(TestAggregateDomain.class, query);

        Assertions.assertEquals(2, results.size());
        results.forEach(item -> {
            Assertions.assertSame(service, item.boundService());
            Assertions.assertDoesNotThrow(() -> item.loadRelated(Object.class));
        });
    }

    @Test
    void testQueryPageBindsService() {
        TestServiceImpl service = newService();
        TestAggregateDomain first = new TestAggregateDomain();
        TestAggregateDomain second = new TestAggregateDomain();
        service.addRepository(TestAggregateDomain.class, new FakeRepository<>(List.of(first, second)));

        LambdaQuery<TestAggregateDomain> query = LambdaQuery.of(TestAggregateDomain.class)
                .eq(TestAggregateDomain::getId, 1L);

        IPage<TestAggregateDomain> page = service.queryPage(TestAggregateDomain.class,
                PageDomain.builder().pageNum(1L).pageSize(10L).build(), query);

        Assertions.assertEquals(2, page.getRecords().size());
        page.getRecords().forEach(item -> {
            Assertions.assertSame(service, item.boundService());
            Assertions.assertDoesNotThrow(() -> item.loadRelated(Object.class));
        });
    }

    @Test
    void testQueryOneNullResult() {
        TestServiceImpl service = newService();
        service.addRepository(TestAggregateDomain.class, new FakeRepository<>(Collections.emptyList()));

        LambdaQuery<TestAggregateDomain> query = LambdaQuery.of(TestAggregateDomain.class)
                .eq(TestAggregateDomain::getId, 1L);

        // 查询无结果时不抛异常
        Assertions.assertDoesNotThrow(() ->
                Assertions.assertNull(service.queryOne(TestAggregateDomain.class, query)));
    }

    @Test
    void testSimpleDomainPassThrough() {
        TestServiceImpl service = newService();
        SimpleDomain domain = new SimpleDomain();
        domain.setId(1L);
        service.addRepository(SimpleDomain.class, new FakeRepository<>(List.of(domain)));

        LambdaQuery<SimpleDomain> query = LambdaQuery.of(SimpleDomain.class)
                .eq(SimpleDomain::getId, 1L);

        // 非聚合根实体不参与服务绑定，查询行为不受影响
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertSame(domain, service.queryOne(SimpleDomain.class, query));
            Assertions.assertEquals(1, service.queryList(SimpleDomain.class, query).size());
        });
    }
}
