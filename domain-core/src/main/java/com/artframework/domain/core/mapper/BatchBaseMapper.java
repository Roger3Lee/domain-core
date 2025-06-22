package com.artframework.domain.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 增强的批量操作 Mapper 接口
 * 提供批量插入和批量更新支持，包括主键回填和ignore null策略
 *
 * @param <DO> 数据对象类型
 */
public interface BatchBaseMapper<DO> extends BaseMapper<DO> {

    /**
     * 批量插入
     * 支持主键自动回填和ignore null策略
     * 
     * @param tList 要插入的数据列表
     * @return 受影响的行数
     */
    int batchInsert(@Param("list") List<DO> tList);

    /**
     * 批量更新
     * 支持ignore null策略，只更新非空字段
     *
     * @param tList 要更新的数据列表
     * @return 受影响的行数
     */
    int batchUpdate(@Param("list") List<DO> tList);
}
