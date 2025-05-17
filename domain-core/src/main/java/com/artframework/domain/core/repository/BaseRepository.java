package com.artframework.domain.core.repository;

import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.artframework.domain.core.lambda.LambdaOrder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<D extends BaseDomain, DO> {
    /**
     * 通過字段查詢唯一一條數據
     * @param value
     * @param valueWarp
     * @return
     */
    D query(Serializable value, SFunction<D, Serializable> valueWarp);
    D query(List<LambdaFilter.Filter> filters, List<LambdaOrder.LambdaOrderItem> orders);

    List<D> queryList(Serializable value, SFunction<D, Serializable> valueWarp);
    List<D> queryList(List<LambdaFilter.Filter> filters);
    List<D> queryList(List<LambdaFilter.Filter> filters, List<LambdaOrder.LambdaOrderItem> orders);

    IPage<D> queryPage(PageDomain pageDomain, List<LambdaFilter.Filter> filters);


    IPage<D> queryPage(PageDomain pageDomain, List<LambdaFilter.Filter> filters, List<LambdaOrder.LambdaOrderItem> orders);
    /**
     * 插入一条数据
     *
     * @param item
     * @return 新增后数据
     */
    D insert(D item);

    /**
     * 新增列表
     *
     * @param list
     * @return 新增后数据列表
     */
    List<D> insert(List<D> list);

    /**
     * 批量删除
     *
     * @param list
     * @return 受影响行数
     */
    int delete(List<D> list);

    /**
     * 通過Filter刪除數據
     * @param filters
     * @return
     */
    int deleteByFilter(List<LambdaFilter.Filter> filters);

    int deleteById(Serializable id);

    /**
     * 更新一条数据
     *
     * @param item
     * @return 更新后数据
     */
    int update(D item);

    /**
     * 更新列表
     *
     * @param list
     * @return 更新后数据列表
     */
    int update(List<D> list);
}
