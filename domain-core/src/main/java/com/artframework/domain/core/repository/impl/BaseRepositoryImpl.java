package com.artframework.domain.core.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.artframework.domain.core.repository.BaseRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseRepositoryImpl<DTO, DO> extends ServiceImpl<BaseMapper<DO>, DO> implements BaseRepository<DTO, DO> {

    @Override
    public DTO query(Serializable id, SFunction<DO, Serializable> idWrap) {
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<DO>()
                .eq(idWrap, id)
                .last("limit 1");

        List<DTO> list = convert2DTO(this.baseMapper.selectList(wrapper));
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
    @Override
    public List<DTO> queryList(Serializable id, SFunction<DO, Serializable> idWrap) {
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<DO>()
                .eq(idWrap, id)
                .last("limit 1");

        return convert2DTO(this.baseMapper.selectList(wrapper));
    }


    /**
     * 插入一条数据
     * @param item
     * @return 新增后数据
     */
    @Override
    public DTO insert(DTO item) {
        List<DTO> insertResponse = insert(CollUtil.newArrayList(item));
        return insertResponse.get(0);
    }

    /**
     * 新增列表
     *
     * @param list
     * @return 新增后数据列表
     */
    @Override
    public List<DTO> insert(List<DTO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<DO> tList = convert2DO(list);
        for (DO item : tList) {
            this.baseMapper.insert(item);
        }
        return convert2DTO(tList);
    }

    /**
     *  批量删除
     * @param list
     * @return 受影响行数
     */
    @Override
    public int delete(List<DTO> list) {
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        List<?> keyList = list.stream().map(x -> keyLambda().apply(x)).collect(Collectors.toList());
        return this.baseMapper.deleteBatchIds(keyList);
    }

    /**
     * 更新一条数据
     * @param item
     * @return 更新后数据
     */
    @Override
    public DTO update(DTO item) {
        List<DTO> insertResponse = update(CollUtil.newArrayList(item));
        return insertResponse.get(0);
    }

    /**
     * 更新列表
     *
     * @param list
     * @return 更新后数据列表
     */
    @Override
    public List<DTO> update(List<DTO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<DO> tList = convert2DO(list);
        for (DO item : tList) {
            this.baseMapper.update(item,null);
        }
        return convert2DTO(tList);
    }

    public abstract List<DO> convert2DO(List<DTO> list);

    public abstract List<DTO> convert2DTO(List<DO> list);

    public abstract Function<DTO,?> keyLambda();
}
