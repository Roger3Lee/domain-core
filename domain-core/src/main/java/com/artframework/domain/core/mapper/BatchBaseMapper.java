package com.artframework.domain.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批量操作基础 Mapper 接口
 * 提供通用的批量插入和批量更新方法
 *
 * @param <DO> 数据对象类型
 */
public interface BatchBaseMapper<DO> extends BaseMapper<DO> {

    /**
     * 批量更新
     *
     * @param tList 要更新的数据列表
     * @return 受影响的行数
     */
    int batchUpdate(@Param("list") List<DO> tList);

    /**
     * 批量插入
     * 支持返回生成的主键（如果数据库支持）
     *
     * @param tList 要插入的数据列表
     * @return 受影响的行数
     */
    int insertBatch(@Param("list") List<DO> tList);
}
