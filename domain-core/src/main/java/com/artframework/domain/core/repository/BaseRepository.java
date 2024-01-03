package com.artframework.domain.core.repository;

import com.artframework.domain.core.domain.BaseLoadFlag;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<DTO, DO> {
    DTO query(Serializable id, SFunction<DO, Serializable> idWrap);
    DTO query(Serializable id, SFunction<DO, Serializable> idWrap, List<BaseLoadFlag.Filter> filters);

    List<DTO> queryList(Serializable id, SFunction<DO, Serializable> wrap);

    List<DTO> queryList(Serializable id, SFunction<DO, Serializable> wrap, List<BaseLoadFlag.Filter> filters);
    /**
     * 插入一条数据
     *
     * @param item
     * @return 新增后数据
     */
    DTO insert(DTO item);

    /**
     * 新增列表
     *
     * @param list
     * @return 新增后数据列表
     */
    List<DTO> insert(List<DTO> list);

    /**
     * 批量删除
     *
     * @param list
     * @return 受影响行数
     */
    int delete(List<DTO> list);

    int deleteById(Serializable id);

    /**
     * 更新一条数据
     *
     * @param item
     * @return 更新后数据
     */
    DTO update(DTO item);

    /**
     * 更新列表
     *
     * @param list
     * @return 更新后数据列表
     */
    List<DTO> update(List<DTO> list);
}
