package com.artframework.domain.core.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.MPFieldLambda;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseRepositoryImpl<DTO, DO> implements BaseRepository<DTO, DO> {
    @Autowired
    protected BaseMapper<DO> baseMapper;

    @Override
    public DTO query(Serializable id, SFunction<DO, Serializable> idWrap) {
        return query(id, idWrap, null);
    }

    @Override
    public DTO query(Serializable id, SFunction<DO, Serializable> idWrap, Map<String, Object> filters){
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<DO>()
                .eq(idWrap, id);
        //額外的filter
        if (ObjectUtil.isNotNull(filters)) {
            for (Map.Entry<String, Object> filter : filters.entrySet()) {
                wrapper = wrapper.eq(new MPFieldLambda(this.getDOClass(), filter.getKey()).fieldLambda(), filter.getValue());
            }
        }
        wrapper = wrapper.last("limit 1");
        List<DTO> list = convert2DTO(this.baseMapper.selectList(wrapper));
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
    @Override
    public List<DTO> queryList(Serializable id, SFunction<DO, Serializable> wrap) {
        return queryList(id, wrap, null);
    }

    @Override
    public List<DTO> queryList(Serializable id, SFunction<DO, Serializable> wrap, Map<String, Object> filters){
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<DO>()
                .eq(wrap, id);

        //額外的filter
        if (ObjectUtil.isNotNull(filters)) {
            for (Map.Entry<String, Object> filter : filters.entrySet()) {
                wrapper = wrapper.eq(new MPFieldLambda(this.getDOClass(),filter.getKey()).fieldLambda(), filter.getValue());
            }
        }

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
        List<Serializable> keyList = list.stream().map(x -> keyLambda().apply(x)).collect(Collectors.toList());
        return this.baseMapper.deleteBatchIds(keyList);
    }

    /**
     * 更新一条数据
     * @param item
     * @return 更新后数据
     */
    @Override
    public DTO update(DTO item) {
        List<DTO> response = update(CollUtil.newArrayList(item));
        return response.get(0);
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
            this.baseMapper.updateById(item);
        }
        return convert2DTO(tList);
    }

    public abstract List<DO> convert2DO(List<DTO> list);

    public abstract List<DTO> convert2DTO(List<DO> list);

    public abstract Function<DTO, Serializable> keyLambda();

    public abstract Class<DO> getDOClass();
}
