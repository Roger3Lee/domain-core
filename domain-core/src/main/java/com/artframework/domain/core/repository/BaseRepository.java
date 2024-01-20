package com.artframework.domain.core.repository;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import mo.gov.dsaj.domain.core.domain.BaseLoadFlag;
import mo.gov.dsaj.parent.core.mybatis.dataobject.ContainsId;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<D, DO extends ContainsId> {
    D query(Serializable id, SFunction<DO, Serializable> idWrap);
    D query(Serializable id, SFunction<DO, Serializable> idWrap, List<BaseLoadFlag.DOFilter> filters);

    List<D> queryList(Serializable id, SFunction<DO, Serializable> wrap);

    List<D> queryList(Serializable id, SFunction<DO, Serializable> wrap, List<BaseLoadFlag.DOFilter> filters);
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
