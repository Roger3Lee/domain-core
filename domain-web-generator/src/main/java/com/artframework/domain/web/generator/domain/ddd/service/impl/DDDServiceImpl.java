package com.artframework.domain.web.generator.domain.ddd.service.impl;

import com.artframework.domain.web.generator.domain.ddd.service.*;
import com.artframework.domain.web.generator.domain.ddd.domain.*;
import com.artframework.domain.web.generator.domain.ddd.repository.*;
import com.artframework.domain.web.generator.domain.ddd.lambdaexp.*;
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
public class DDDServiceImpl extends BaseDomainServiceImpl implements DDDService {
    @Autowired
    private DDDRepository dDDRepository;

    @Autowired
    private DomainConfigTablesRepository domainConfigTablesRepository;

    @Autowired
    private DomainConfigLineRepository domainConfigLineRepository;

    @Autowired
    private DomainConfigLineConfigRepository domainConfigLineConfigRepository;

    @PostConstruct
    public void init(){
        this.addRepository(DDDDomain.class, this.dDDRepository);
        this.addRepository(DDDDomain.DomainConfigTablesDomain.class, this.domainConfigTablesRepository);
        this.addRepository(DDDDomain.DomainConfigLineDomain.class, this.domainConfigLineRepository);
        this.addRepository(DDDDomain.DomainConfigLineConfigDomain.class, this.domainConfigLineConfigRepository);
    }

   /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public DDDDomain find(DDDFindDomain request){
        DDDDomain domain = dDDRepository.query(request.getKey(), DDDLambdaExp.dtoKeyLambda);
        if(ObjectUtil.isNull(domain)){
            return domain;
        }
        return find(domain, request.getLoadFlag());
    }

    public DDDDomain find(DDDDomain response,DDDDomain.LoadFlag loadFlag){
        final DDDDomain resp = response;
        if (ObjectUtil.isNotNull(loadFlag)) {
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDomainConfigTablesDomain())){
                LambdaQuery<DDDDomain.DomainConfigTablesDomain> lambdaQuery = LambdaQuery.of(DDDDomain.DomainConfigTablesDomain.class);
                lambdaQuery.eq(DDDLambdaExp.domainConfigTables_domainIdTargetLambda,DDDLambdaExp.domainConfigId_RelatedDomainConfigTables_SourceLambda.apply(resp));
                List<DDDDomain.DomainConfigTablesDomain> queryList = domainConfigTablesRepository.queryList(
                    LambdaQueryUtils.combine(lambdaQuery, loadFlag, DDDDomain.DomainConfigTablesDomain.class))
                                            .stream().peek(x -> x.set_thisDomain(resp)).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(resp.getDomainConfigTablesList())){
                    resp.setDomainConfigTablesList(queryList);
                } else {
                    resp.getDomainConfigTablesList().addAll(queryList);
                }
            }
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDomainConfigLineDomain())){
                LambdaQuery<DDDDomain.DomainConfigLineDomain> lambdaQuery = LambdaQuery.of(DDDDomain.DomainConfigLineDomain.class);
                lambdaQuery.eq(DDDLambdaExp.domainConfigLine_domainIdTargetLambda,DDDLambdaExp.domainConfigId_RelatedDomainConfigLine_SourceLambda.apply(resp));
                List<DDDDomain.DomainConfigLineDomain> queryList = domainConfigLineRepository.queryList(
                    LambdaQueryUtils.combine(lambdaQuery, loadFlag, DDDDomain.DomainConfigLineDomain.class))
                                            .stream().peek(x -> x.set_thisDomain(resp)).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(resp.getDomainConfigLineList())){
                    resp.setDomainConfigLineList(queryList);
                } else {
                    resp.getDomainConfigLineList().addAll(queryList);
                }
            }
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDomainConfigLineConfigDomain())){
                LambdaQuery<DDDDomain.DomainConfigLineConfigDomain> lambdaQuery = LambdaQuery.of(DDDDomain.DomainConfigLineConfigDomain.class);
                lambdaQuery.eq(DDDLambdaExp.domainConfigLineConfig_domainIdTargetLambda,DDDLambdaExp.domainConfigId_RelatedDomainConfigLineConfig_SourceLambda.apply(resp));
                List<DDDDomain.DomainConfigLineConfigDomain> queryList = domainConfigLineConfigRepository.queryList(
                    LambdaQueryUtils.combine(lambdaQuery, loadFlag, DDDDomain.DomainConfigLineConfigDomain.class))
                                            .stream().peek(x -> x.set_thisDomain(resp)).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(resp.getDomainConfigLineConfigList())){
                    resp.setDomainConfigLineConfigList(queryList);
                } else {
                    resp.getDomainConfigLineConfigList().addAll(queryList);
                }
            }
        }
        resp.setLoadFlag(DDDDomain.LoadFlag.merge(loadFlag, resp.getLoadFlag()));
        return resp;
    }

    /**
     * 查找
     * @param request 请求体
     * @param keyLambda 請求key參數對應的字段的lambda表達式
     * @return
     */
    @Override
    public DDDDomain findByKey(DDDFindDomain request, SFunction<DDDDomain, Serializable> keyLambda){
        return find(dDDRepository.query(request.getKey(), keyLambda), request.getLoadFlag());
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insert(DDDDomain request){
        //插入数据
        DDDDomain domain = dDDRepository.insert(request);

        //插入关联数据domain_config_tables
        if(CollUtil.isNotEmpty(request.getDomainConfigTablesList())){
            request.getDomainConfigTablesList().forEach(x->{
                DDDLambdaExp.domainConfigTablesDomainIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigId_RelatedDomainConfigTables_SourceLambda.apply(domain));
                DDDLambdaExp.domainConfigTablesProjectIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigProjectId_RelatedDomainConfigTables_SourceLambda.apply(domain));
            });
            domainConfigTablesRepository.insert(request.getDomainConfigTablesList());
        }

        //插入关联数据domain_config_line
        if(CollUtil.isNotEmpty(request.getDomainConfigLineList())){
            request.getDomainConfigLineList().forEach(x->{
                DDDLambdaExp.domainConfigLineDomainIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigId_RelatedDomainConfigLine_SourceLambda.apply(domain));
                DDDLambdaExp.domainConfigLineProjectIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigProjectId_RelatedDomainConfigLine_SourceLambda.apply(domain));
            });
            domainConfigLineRepository.insert(request.getDomainConfigLineList());
        }

        //插入关联数据domain_config_line_config
        if(CollUtil.isNotEmpty(request.getDomainConfigLineConfigList())){
            request.getDomainConfigLineConfigList().forEach(x->{
                DDDLambdaExp.domainConfigLineConfigDomainIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigId_RelatedDomainConfigLineConfig_SourceLambda.apply(domain));
                DDDLambdaExp.domainConfigLineConfigProjectIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigProjectId_RelatedDomainConfigLineConfig_SourceLambda.apply(domain));
            });
            domainConfigLineConfigRepository.insert(request.getDomainConfigLineConfigList());
        }
        return (Integer) DDDLambdaExp.dtoKeyLambda.apply(domain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(DDDDomain request){
        Serializable keyId = DDDLambdaExp.dtoKeyLambda.apply(request);
        DDDDomain old = find(new DDDFindDomain(keyId, request.getLoadFlag()));
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
    public Boolean update(DDDDomain request, DDDDomain domain){
        DDDDomain old = domain;
        //更新关联数据domain_config_tables
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadDomainConfigTablesDomain()))){
            if(CollUtil.isNotEmpty(request.getDomainConfigTablesList())){
                request.getDomainConfigTablesList().forEach(x->{
                    DDDLambdaExp.domainConfigTablesDomainIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigId_RelatedDomainConfigTables_SourceLambda.apply(request));
                    DDDLambdaExp.domainConfigTablesProjectIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigProjectId_RelatedDomainConfigTables_SourceLambda.apply(request));
                });
            }
            this.merge(old.getDomainConfigTablesList(), request.getDomainConfigTablesList(), DDDLambdaExp.domainConfigTablesDomainKeyLambda, domainConfigTablesRepository);
        }
        //更新关联数据domain_config_line
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadDomainConfigLineDomain()))){
            if(CollUtil.isNotEmpty(request.getDomainConfigLineList())){
                request.getDomainConfigLineList().forEach(x->{
                    DDDLambdaExp.domainConfigLineDomainIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigId_RelatedDomainConfigLine_SourceLambda.apply(request));
                    DDDLambdaExp.domainConfigLineProjectIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigProjectId_RelatedDomainConfigLine_SourceLambda.apply(request));
                });
            }
            this.merge(old.getDomainConfigLineList(), request.getDomainConfigLineList(), DDDLambdaExp.domainConfigLineDomainKeyLambda, domainConfigLineRepository);
        }
        //更新关联数据domain_config_line_config
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadDomainConfigLineConfigDomain()))){
            if(CollUtil.isNotEmpty(request.getDomainConfigLineConfigList())){
                request.getDomainConfigLineConfigList().forEach(x->{
                    DDDLambdaExp.domainConfigLineConfigDomainIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigId_RelatedDomainConfigLineConfig_SourceLambda.apply(request));
                    DDDLambdaExp.domainConfigLineConfigProjectIdTargetSetLambda.accept(x, (Integer)DDDLambdaExp.domainConfigProjectId_RelatedDomainConfigLineConfig_SourceLambda.apply(request));
                });
            }
            this.merge(old.getDomainConfigLineConfigList(), request.getDomainConfigLineConfigList(), DDDLambdaExp.domainConfigLineConfigDomainKeyLambda, domainConfigLineConfigRepository);
        }

        //更新数据
        if(request.getChanged()){
            return dDDRepository.update(request) > 0;
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
        return delete(id, DDDDomain.LoadFlag.builder().loadAll(true).build());
    }
    /**
    * 删除
    * @param id 数据ID
    * @param loadFlag 數據加載參數
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer id, DDDDomain.LoadFlag loadFlag){
        DDDDomain old = find(new DDDFindDomain(id ,DDDDomain.LoadFlag.builder().build()));
        if (ObjectUtil.isNull(old)) {
            return false;
        }

        if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDomainConfigTablesDomain())){
            //删除关联数据domain_config_tables
            LambdaQuery<DDDDomain.DomainConfigTablesDomain> lambdaQuery = LambdaQuery.of(DDDDomain.DomainConfigTablesDomain.class);
            lambdaQuery.eq(DDDLambdaExp.domainConfigTables_domainIdTargetLambda,DDDLambdaExp.domainConfigId_RelatedDomainConfigTables_SourceLambda.apply(old));
            domainConfigTablesRepository.deleteByFilter(lambdaQuery);
        }
        if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDomainConfigLineDomain())){
            //删除关联数据domain_config_line
            LambdaQuery<DDDDomain.DomainConfigLineDomain> lambdaQuery = LambdaQuery.of(DDDDomain.DomainConfigLineDomain.class);
            lambdaQuery.eq(DDDLambdaExp.domainConfigLine_domainIdTargetLambda,DDDLambdaExp.domainConfigId_RelatedDomainConfigLine_SourceLambda.apply(old));
            domainConfigLineRepository.deleteByFilter(lambdaQuery);
        }
        if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDomainConfigLineConfigDomain())){
            //删除关联数据domain_config_line_config
            LambdaQuery<DDDDomain.DomainConfigLineConfigDomain> lambdaQuery = LambdaQuery.of(DDDDomain.DomainConfigLineConfigDomain.class);
            lambdaQuery.eq(DDDLambdaExp.domainConfigLineConfig_domainIdTargetLambda,DDDLambdaExp.domainConfigId_RelatedDomainConfigLineConfig_SourceLambda.apply(old));
            domainConfigLineConfigRepository.deleteByFilter(lambdaQuery);
        }
        return dDDRepository.delete(CollUtil.newArrayList(old)) > 0;
    }
}
