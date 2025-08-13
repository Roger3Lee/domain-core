package com.artframework.domain.web.generator.domain.datasource.service.impl;

import com.artframework.domain.web.generator.domain.datasource.service.*;
import com.artframework.domain.web.generator.domain.datasource.domain.*;
import com.artframework.domain.web.generator.domain.datasource.repository.*;
import com.artframework.domain.web.generator.domain.datasource.lambdaexp.*;
import com.artframework.domain.core.service.impl.*;
import com.artframework.domain.core.lambda.query.LambdaQuery;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import com.artframework.domain.core.utils.*;
import com.artframework.domain.core.lambda.*;
import cn.hutool.core.collection.*;
import cn.hutool.core.util.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;



@Service
public class DatasourceServiceImpl extends BaseDomainServiceImpl implements DatasourceService {
    @Autowired
    private DatasourceRepository datasourceRepository;

    @Autowired
    private DatasourceTableRepository datasourceTableRepository;

    @Autowired
    private DatasourceTableColumnRepository datasourceTableColumnRepository;

    @PostConstruct
    public void init(){
        this.addRepository(DatasourceDomain.class, this.datasourceRepository);
        this.addRepository(DatasourceDomain.DatasourceTableDomain.class, this.datasourceTableRepository);
        this.addRepository(DatasourceDomain.DatasourceTableColumnDomain.class, this.datasourceTableColumnRepository);
    }

   /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public DatasourceDomain find(DatasourceFindDomain request){
        DatasourceDomain domain = datasourceRepository.query(request.getKey(), DatasourceLambdaExp.dtoKeyLambda);
        if(ObjectUtil.isNull(domain)){
            return domain;
        }
        return find(domain, request.getLoadFlag());
    }

    public DatasourceDomain find(DatasourceDomain response,DatasourceDomain.LoadFlag loadFlag){
        final DatasourceDomain resp = response;
        if (ObjectUtil.isNotNull(loadFlag)) {
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDatasourceTableDomain())){
                LambdaQuery<DatasourceDomain.DatasourceTableDomain> lambdaQuery = LambdaQuery.of(DatasourceDomain.DatasourceTableDomain.class);
                lambdaQuery.eq(DatasourceLambdaExp.datasourceTable_dsIdTargetLambda,DatasourceLambdaExp.datasourceConfigId_RelatedDatasourceTable_SourceLambda.apply(resp));
                List<DatasourceDomain.DatasourceTableDomain> queryList = datasourceTableRepository.queryList(
                    LambdaQueryUtils.combine(lambdaQuery, loadFlag, DatasourceDomain.DatasourceTableDomain.class))
                                            .stream().peek(x -> x.set_thisDomain(resp)).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(resp.getDatasourceTableList())){
                    resp.setDatasourceTableList(queryList);
                } else {
                    resp.getDatasourceTableList().addAll(queryList);
                }
            }
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDatasourceTableColumnDomain())){
                LambdaQuery<DatasourceDomain.DatasourceTableColumnDomain> lambdaQuery = LambdaQuery.of(DatasourceDomain.DatasourceTableColumnDomain.class);
                lambdaQuery.eq(DatasourceLambdaExp.datasourceTableColumn_dsIdTargetLambda,DatasourceLambdaExp.datasourceConfigId_RelatedDatasourceTableColumn_SourceLambda.apply(resp));
                List<DatasourceDomain.DatasourceTableColumnDomain> queryList = datasourceTableColumnRepository.queryList(
                    LambdaQueryUtils.combine(lambdaQuery, loadFlag, DatasourceDomain.DatasourceTableColumnDomain.class))
                                            .stream().peek(x -> x.set_thisDomain(resp)).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(resp.getDatasourceTableColumnList())){
                    resp.setDatasourceTableColumnList(queryList);
                } else {
                    resp.getDatasourceTableColumnList().addAll(queryList);
                }
            }
        }
        resp.setLoadFlag(DatasourceDomain.LoadFlag.merge(loadFlag, resp.getLoadFlag()));
        return resp;
    }

    /**
     * 查找
     * @param request 请求体
     * @param keyLambda 請求key參數對應的字段的lambda表達式
     * @return
     */
    @Override
    public DatasourceDomain findByKey(DatasourceFindDomain request, SFunction<DatasourceDomain, Serializable> keyLambda){
        return find(datasourceRepository.query(request.getKey(), keyLambda), request.getLoadFlag());
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insert(DatasourceDomain request){
        //插入数据
        DatasourceDomain domain = datasourceRepository.insert(request);

        //插入关联数据datasource_table
        if(CollUtil.isNotEmpty(request.getDatasourceTableList())){
            request.getDatasourceTableList().forEach(x->{
                DatasourceLambdaExp.datasourceTableDsIdTargetSetLambda.accept(x, (Integer)DatasourceLambdaExp.datasourceConfigId_RelatedDatasourceTable_SourceLambda.apply(domain));
            });
            datasourceTableRepository.insert(request.getDatasourceTableList());
        }

        //插入关联数据datasource_table_column
        if(CollUtil.isNotEmpty(request.getDatasourceTableColumnList())){
            request.getDatasourceTableColumnList().forEach(x->{
                DatasourceLambdaExp.datasourceTableColumnDsIdTargetSetLambda.accept(x, (Integer)DatasourceLambdaExp.datasourceConfigId_RelatedDatasourceTableColumn_SourceLambda.apply(domain));
            });
            datasourceTableColumnRepository.insert(request.getDatasourceTableColumnList());
        }
        return (Integer) DatasourceLambdaExp.dtoKeyLambda.apply(domain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(DatasourceDomain request){
        Serializable keyId = DatasourceLambdaExp.dtoKeyLambda.apply(request);
        DatasourceDomain old = find(new DatasourceFindDomain(keyId, request.getLoadFlag()));
        return update(request,old);
    }
   /**
    * 修改,此方法不用再加載domain主entity數據
    *
    * @param request 请求体
    * @param domain 原始domain
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(DatasourceDomain request, DatasourceDomain domain){
        DatasourceDomain old = domain;
        //更新关联数据datasource_table
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadDatasourceTableDomain()))){
            if(CollUtil.isNotEmpty(request.getDatasourceTableList())){
                request.getDatasourceTableList().forEach(x->{
                    DatasourceLambdaExp.datasourceTableDsIdTargetSetLambda.accept(x, (Integer)DatasourceLambdaExp.datasourceConfigId_RelatedDatasourceTable_SourceLambda.apply(request));
                });
            }
            this.merge(old.getDatasourceTableList(), request.getDatasourceTableList(), DatasourceLambdaExp.datasourceTableDomainKeyLambda, datasourceTableRepository);
        }
        //更新关联数据datasource_table_column
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadDatasourceTableColumnDomain()))){
            if(CollUtil.isNotEmpty(request.getDatasourceTableColumnList())){
                request.getDatasourceTableColumnList().forEach(x->{
                    DatasourceLambdaExp.datasourceTableColumnDsIdTargetSetLambda.accept(x, (Integer)DatasourceLambdaExp.datasourceConfigId_RelatedDatasourceTableColumn_SourceLambda.apply(request));
                });
            }
            this.merge(old.getDatasourceTableColumnList(), request.getDatasourceTableColumnList(), DatasourceLambdaExp.datasourceTableColumnDomainKeyLambda, datasourceTableColumnRepository);
        }

        //更新数据
        if(request.getChanged()){
            return datasourceRepository.update(request) > 0;
        }
        return true;
    }

    /**
    * 删除
    * @param id 数据ID
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer id){
        return delete(id, DatasourceDomain.LoadFlag.builder().loadAll(true).build());
    }
    /**
    * 删除
    * @param id 数据ID
    * @param loadFlag 數據加載參數
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer id, DatasourceDomain.LoadFlag loadFlag){
        DatasourceDomain old = find(new DatasourceFindDomain(id ,DatasourceDomain.LoadFlag.builder().build()));
        if (ObjectUtil.isNull(old)) {
            return false;
        }

        if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDatasourceTableDomain())){
            //删除关联数据datasource_table
            LambdaQuery<DatasourceDomain.DatasourceTableDomain> lambdaQuery = LambdaQuery.of(DatasourceDomain.DatasourceTableDomain.class);
            lambdaQuery.eq(DatasourceLambdaExp.datasourceTable_dsIdTargetLambda,DatasourceLambdaExp.datasourceConfigId_RelatedDatasourceTable_SourceLambda.apply(old));
            datasourceTableRepository.deleteByFilter(lambdaQuery);
        }
        if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDatasourceTableColumnDomain())){
            //删除关联数据datasource_table_column
            LambdaQuery<DatasourceDomain.DatasourceTableColumnDomain> lambdaQuery = LambdaQuery.of(DatasourceDomain.DatasourceTableColumnDomain.class);
            lambdaQuery.eq(DatasourceLambdaExp.datasourceTableColumn_dsIdTargetLambda,DatasourceLambdaExp.datasourceConfigId_RelatedDatasourceTableColumn_SourceLambda.apply(old));
            datasourceTableColumnRepository.deleteByFilter(lambdaQuery);
        }
        return datasourceRepository.delete(CollUtil.newArrayList(old)) > 0;
    }
}
