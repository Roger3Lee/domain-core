package com.artframework.domain.core.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;
import mo.gov.dsaj.domain.core.constants.Op;
import mo.gov.dsaj.domain.core.domain.BaseLoadFlag;
import mo.gov.dsaj.domain.core.repository.BaseRepository;
import mo.gov.dsaj.domain.core.uitls.FiltersUtils;
import mo.gov.dsaj.parent.core.mybatis.CustomBaseMapper;
import mo.gov.dsaj.parent.core.mybatis.dataobject.ContainsId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseRepositoryImpl<D, DO extends ContainsId> implements BaseRepository<D, DO> {
    @Autowired
    protected BaseMapper<DO> baseMapper;

    @PostConstruct
    public void init(){
        log.info(this.getDOClass().getSimpleName() +" repository init successfully.");
    }

    @Override
    public D query(Serializable id, SFunction<DO, Serializable> idWrap) {
        return query(id, idWrap, null);
    }

    @Override
    public D query(Serializable id, SFunction<DO, Serializable> idWrap, List<BaseLoadFlag.DOFilter> filters){
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<DO>()
                .eq(idWrap, id);
        //額外的filter
        if (ObjectUtil.isNotNull(filters)) {
            //儅不需要查詢出數據時，返回空
            if (filters.stream().anyMatch(x -> Op.NIL.getCode().equals(x.getOp()))) {
                return null;
            }

            for (BaseLoadFlag.DOFilter filter : filters) {
                wrapper = FiltersUtils.buildWrapper(wrapper,filter, this.getDOClass());
            }
        }
        wrapper = wrapper.last("limit 1");
        List<D> list = convert2DTO(this.baseMapper.selectList(wrapper));
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
    @Override
    public List<D> queryList(Serializable id, SFunction<DO, Serializable> wrap) {
        return queryList(id, wrap, null);
    }

    @Override
    public List<D> queryList(Serializable id, SFunction<DO, Serializable> wrap, List<BaseLoadFlag.DOFilter> filters){
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<DO>()
                .eq(wrap, id);

        //額外的filter
        if (ObjectUtil.isNotNull(filters)) {
            //儅不需要查詢出數據時，返回空列表
            if (filters.stream().anyMatch(x -> Op.NIL.getCode().equals(x.getOp()))) {
                return ListUtil.empty();
            }

            for (BaseLoadFlag.DOFilter filter : filters) {
                FiltersUtils.buildWrapper(wrapper, filter, this.getDOClass());
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
    public D insert(D item) {
        List<D> insertResponse = insert(CollUtil.newArrayList(item));
        return insertResponse.get(0);
    }

    /**
     * 新增列表
     *
     * @param list
     * @return 新增后数据列表
     */
    @Override
    public List<D> insert(List<D> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<DO> tList = convert2DO(list);
        if (list.size()== 1) {
            this.baseMapper.insert(tList.get(0));
        } else {
            if(CustomBaseMapper.class.isAssignableFrom(this.baseMapper.getClass())){
                ((CustomBaseMapper)this.baseMapper).insertBatch(tList);
            }else{
                for (DO d : tList) {
                    this.baseMapper.insert(d);
                }
            }
        }
        //覆蓋列表， 目的是將自賦值的字段的值返回
        for (int i = 0; i < list.size(); i++) {
            convert2DTO(tList.get(i), list.get(i));
        }
        return list;
    }


    /**
     *  批量删除
     * @param list
     * @return 受影响行数
     */
    @Override
    public int delete(List<D> list) {
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        List<Serializable> keyList = list.stream().map(x -> keyLambda().apply(x)).collect(Collectors.toList());
        return this.baseMapper.deleteBatchIds(keyList);
    }

    /**
     * 通過實體的過濾條件刪除數據
     * @param filters
     * @return
     */
    @Override
    public int deleteByFilter(List<BaseLoadFlag.DOFilter> filters){
        //額外的filter
        if (ObjectUtil.isNotNull(filters)) {
            LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<DO>();
            for (BaseLoadFlag.DOFilter filter : filters) {
                FiltersUtils.buildWrapper(wrapper, filter, this.getDOClass());
            }

            return this.baseMapper.delete(wrapper);
        } else {
            log.error("無過濾條件刪除數據，系統忽略此刪除操作");
            return 0;
        }
    }

    /**
     * 删除一行数据BYid
     * @param id
     * @return 受影响行数
     */
    @Override
    public int deleteById(Serializable id){
        return this.baseMapper.deleteById(id);
    }

    /**
     * 更新一条数据
     * @param item
     * @return 更新后数据
     */
    @Override
    public int update(D item) {
        return update(CollUtil.newArrayList(item));
    }

    /**
     * 更新列表
     *
     * @param list
     * @return 更新后数据列表
     */
    @Override
    public int update(List<D> list) {
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        List<DO> tList = convert2DO(list);
        if (list.size() == 1) {
            return this.baseMapper.updateById(tList.get(0));
        } else {
            if(CustomBaseMapper.class.isAssignableFrom(this.baseMapper.getClass())){
                return ((CustomBaseMapper) this.baseMapper).updateSomeColumnBatchById(tList);
            }else{
                for (DO d : tList) {
                    this.baseMapper.updateById(d);
                }
                return tList.size();
            }
        }
    }

    public abstract List<DO> convert2DO(List<D> list);

    public abstract List<D> convert2DTO(List<DO> list);

    public abstract void convert2DTO(DO item ,D targetItem);

    public abstract Function<D, Serializable> keyLambda();

    public abstract Class<DO> getDOClass();
}
