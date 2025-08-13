package com.artframework.domain.web.generator.domain.project.service.impl;

import com.artframework.domain.web.generator.domain.project.service.*;
import com.artframework.domain.web.generator.domain.project.domain.*;
import com.artframework.domain.web.generator.domain.project.repository.*;
import com.artframework.domain.web.generator.domain.project.lambdaexp.*;
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
public class ProjectServiceImpl extends BaseDomainServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DomainConfigRepository domainConfigRepository;

    @PostConstruct
    public void init(){
        this.addRepository(ProjectDomain.class, this.projectRepository);
        this.addRepository(ProjectDomain.DomainConfigDomain.class, this.domainConfigRepository);
    }

   /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public ProjectDomain find(ProjectFindDomain request){
        ProjectDomain domain = projectRepository.query(request.getKey(), ProjectLambdaExp.dtoKeyLambda);
        if(ObjectUtil.isNull(domain)){
            return domain;
        }
        return find(domain, request.getLoadFlag());
    }

    public ProjectDomain find(ProjectDomain response,ProjectDomain.LoadFlag loadFlag){
        final ProjectDomain resp = response;
        if (ObjectUtil.isNotNull(loadFlag)) {
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDomainConfigDomain())){
                LambdaQuery<ProjectDomain.DomainConfigDomain> lambdaQuery = LambdaQuery.of(ProjectDomain.DomainConfigDomain.class);
                lambdaQuery.eq(ProjectLambdaExp.domainConfig_projectIdTargetLambda,ProjectLambdaExp.projectId_RelatedDomainConfig_SourceLambda.apply(resp));
                List<ProjectDomain.DomainConfigDomain> queryList = domainConfigRepository.queryList(
                    LambdaQueryUtils.combine(lambdaQuery, loadFlag, ProjectDomain.DomainConfigDomain.class))
                                            .stream().peek(x -> x.set_thisDomain(resp)).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(resp.getDomainConfigList())){
                    resp.setDomainConfigList(queryList);
                } else {
                    resp.getDomainConfigList().addAll(queryList);
                }
            }
        }
        resp.setLoadFlag(ProjectDomain.LoadFlag.merge(loadFlag, resp.getLoadFlag()));
        return resp;
    }

    /**
     * 查找
     * @param request 请求体
     * @param keyLambda 請求key參數對應的字段的lambda表達式
     * @return
     */
    @Override
    public ProjectDomain findByKey(ProjectFindDomain request, SFunction<ProjectDomain, Serializable> keyLambda){
        return find(projectRepository.query(request.getKey(), keyLambda), request.getLoadFlag());
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insert(ProjectDomain request){
        //插入数据
        ProjectDomain domain = projectRepository.insert(request);

        //插入关联数据domain_config
        if(CollUtil.isNotEmpty(request.getDomainConfigList())){
            request.getDomainConfigList().forEach(x->{
                ProjectLambdaExp.domainConfigProjectIdTargetSetLambda.accept(x, (Integer)ProjectLambdaExp.projectId_RelatedDomainConfig_SourceLambda.apply(domain));
            });
            domainConfigRepository.insert(request.getDomainConfigList());
        }
        return (Integer) ProjectLambdaExp.dtoKeyLambda.apply(domain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ProjectDomain request){
        Serializable keyId = ProjectLambdaExp.dtoKeyLambda.apply(request);
        ProjectDomain old = find(new ProjectFindDomain(keyId, request.getLoadFlag()));
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
    public Boolean update(ProjectDomain request, ProjectDomain domain){
        ProjectDomain old = domain;
        //更新关联数据domain_config
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadDomainConfigDomain()))){
            if(CollUtil.isNotEmpty(request.getDomainConfigList())){
                request.getDomainConfigList().forEach(x->{
                    ProjectLambdaExp.domainConfigProjectIdTargetSetLambda.accept(x, (Integer)ProjectLambdaExp.projectId_RelatedDomainConfig_SourceLambda.apply(request));
                });
            }
            this.merge(old.getDomainConfigList(), request.getDomainConfigList(), ProjectLambdaExp.domainConfigDomainKeyLambda, domainConfigRepository);
        }

        //更新数据
        if(request.getChanged()){
            return projectRepository.update(request) > 0;
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
        return delete(id, ProjectDomain.LoadFlag.builder().loadAll(true).build());
    }
    /**
    * 删除
    * @param id 数据ID
    * @param loadFlag 數據加載參數
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer id, ProjectDomain.LoadFlag loadFlag){
        ProjectDomain old = find(new ProjectFindDomain(id ,ProjectDomain.LoadFlag.builder().build()));
        if (ObjectUtil.isNull(old)) {
            return false;
        }

        if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadDomainConfigDomain())){
            //删除关联数据domain_config
            LambdaQuery<ProjectDomain.DomainConfigDomain> lambdaQuery = LambdaQuery.of(ProjectDomain.DomainConfigDomain.class);
            lambdaQuery.eq(ProjectLambdaExp.domainConfig_projectIdTargetLambda,ProjectLambdaExp.projectId_RelatedDomainConfig_SourceLambda.apply(old));
            domainConfigRepository.deleteByFilter(lambdaQuery);
        }
        return projectRepository.delete(CollUtil.newArrayList(old)) > 0;
    }
}
