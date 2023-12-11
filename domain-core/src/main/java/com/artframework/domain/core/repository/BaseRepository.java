package com.artframework.domain.core.repository;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseRepository<DTO, DO> {
    DTO query(Serializable id, SFunction<DO, Serializable> idWrap);
    DTO query(Serializable id, SFunction<DO, Serializable> idWrap, Map<String, Object> filters);

    List<DTO> queryList(Serializable id, SFunction<DO, Serializable> wrap);

    List<DTO> queryList(Serializable id, SFunction<DO, Serializable> wrap, Map<String, Object> filters);
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
