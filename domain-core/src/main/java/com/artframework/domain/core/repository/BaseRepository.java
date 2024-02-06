package com.artframework.domain.core.repository;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.domain.BaseLoadFlag;
import com.artframework.domain.core.lambda.LambdaOrder;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<D extends BaseDomain, DO> {
    /**
     * 通過非主鍵字段查詢唯一一條數據
     * @param key
     * @param keyWarp
     * @return
     */
    D queryByKey(Serializable key, SFunction<D, Serializable> keyWarp);
    D query(Serializable id, SFunction<DO, Serializable> idWrap);
    D query(Serializable id, SFunction<DO, Serializable> idWrap, List<BaseLoadFlag.DOFilter> filters);

    List<D> queryList(Serializable id, SFunction<DO, Serializable> wrap);

    List<D> queryList(Serializable id, SFunction<DO, Serializable> wrap, List<BaseLoadFlag.DOFilter> filters);

    List<D> queryList(Serializable id, SFunction<DO, Serializable> wrap, List<BaseLoadFlag.DOFilter> filters, List<LambdaOrder.LambdaOrderItem> orders);
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
    int deleteByFilter(List<BaseLoadFlag.DOFilter> filters);

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
