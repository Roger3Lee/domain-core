package com.artframework.sample.domains.family.service.impl;

import com.artframework.sample.domains.family.service.*;
import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.domains.family.repository.*;
import com.artframework.sample.domains.family.lambdaexp.*;
import com.artframework.domain.core.service.impl.*;
import com.artframework.domain.core.lambda.query.LambdaQuery;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import com.artframework.domain.core.uitls.*;
import com.artframework.domain.core.lambda.*;
import cn.hutool.core.collection.*;
import cn.hutool.core.util.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;



@Service
public class FamilyServiceImpl extends BaseDomainServiceImpl implements FamilyService {
    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private FamilyAddressRepository familyAddressRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @PostConstruct
    public void init(){
        this._DomainRepositoryMap.put(FamilyDomain.class.getCanonicalName(), this.familyRepository);
        this._DomainRepositoryMap.put(FamilyDomain.FamilyAddressDomain.class.getCanonicalName(), this.familyAddressRepository);
        this._DomainRepositoryMap.put(FamilyDomain.FamilyMemberDomain.class.getCanonicalName(), this.familyMemberRepository);
    }

   /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public FamilyDomain find(FamilyFindDomain request){
        FamilyDomain domain = familyRepository.query(request.getKey(), FamilyLambdaExp.dtoKeyLambda);
        if(ObjectUtil.isNull(domain)){
            return domain;
        }
        return find(domain, request.getLoadFlag());
    }

    public FamilyDomain find(FamilyDomain response,FamilyDomain.LoadFlag loadFlag){
        final FamilyDomain resp = response;
        if (ObjectUtil.isNotNull(loadFlag)) {
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadFamilyAddressDomain())){
                LambdaQuery<FamilyDomain.FamilyAddressDomain> lambdaQuery = LambdaQuery.of(FamilyDomain.FamilyAddressDomain.class);
                lambdaQuery.eq(FamilyLambdaExp.familyAddress_familyIdTargetLambda,FamilyLambdaExp.familyId_RelatedFamilyAddress_SourceLambda.apply(resp));
                FamilyDomain.FamilyAddressDomain item= familyAddressRepository.query(
                    LambdaQueryUtils.combine(lambdaQuery, LambdaQueryUtils.getEntityFilters(loadFlag.getFilters(), FamilyDomain.FamilyAddressDomain.class),
                    LambdaQueryUtils.getEntityOrders(loadFlag.getOrders(), FamilyDomain.FamilyAddressDomain.class)));
                if(ObjectUtil.isNotNull(item)){
                    item.set_thisDomain(resp);
                }
                resp.setFamilyAddress(item);
            }
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadFamilyMemberDomain())){
                LambdaQuery<FamilyDomain.FamilyMemberDomain> lambdaQuery = LambdaQuery.of(FamilyDomain.FamilyMemberDomain.class);
                lambdaQuery.eq(FamilyLambdaExp.familyMember_familyIdTargetLambda,FamilyLambdaExp.familyId_RelatedFamilyMember_SourceLambda.apply(resp));
                List<FamilyDomain.FamilyMemberDomain> queryList = familyMemberRepository.queryList(
                    LambdaQueryUtils.combine(lambdaQuery, LambdaQueryUtils.getEntityFilters(loadFlag.getFilters(), FamilyDomain.FamilyMemberDomain.class),
                    LambdaQueryUtils.getEntityOrders(loadFlag.getOrders(), FamilyDomain.FamilyMemberDomain.class)))
                                            .stream().peek(x -> x.set_thisDomain(resp)).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(resp.getFamilyMemberList())){
                    resp.setFamilyMemberList(queryList);
                } else {
                    resp.getFamilyMemberList().addAll(queryList);
                }
            }
        }
        resp.setLoadFlag(FamilyDomain.LoadFlag.merge(loadFlag, resp.getLoadFlag()));
        return resp;
    }

    /**
     * 查找
     * @param request 请求体
     * @param keyLambda 請求key參數對應的字段的lambda表達式
     * @return
     */
    @Override
    public FamilyDomain findByKey(FamilyFindDomain request, SFunction<FamilyDomain, Serializable> keyLambda){
        return find(familyRepository.query(request.getKey(), keyLambda), request.getLoadFlag());
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insert(FamilyDomain request){
        //插入数据
        FamilyDomain domain = familyRepository.insert(request);

        //插入关联数据family_address
        if(ObjectUtil.isNotNull(request.getFamilyAddress())){
            FamilyLambdaExp.familyAddressFamilyIdTargetSetLambda.accept(request.getFamilyAddress(), (Integer)FamilyLambdaExp.familyId_RelatedFamilyAddress_SourceLambda.apply(domain));
            FamilyLambdaExp.familyAddressFamilyNameTargetSetLambda.accept(request.getFamilyAddress(), (String)FamilyLambdaExp.familyName_RelatedFamilyAddress_SourceLambda.apply(domain));
            familyAddressRepository.insert(request.getFamilyAddress());
        }

        //插入关联数据family_member
        if(CollUtil.isNotEmpty(request.getFamilyMemberList())){
            request.getFamilyMemberList().forEach(x->{
                FamilyLambdaExp.familyMemberFamilyIdTargetSetLambda.accept(x, (Integer)FamilyLambdaExp.familyId_RelatedFamilyMember_SourceLambda.apply(domain));
                FamilyLambdaExp.familyMemberFamilyNameTargetSetLambda.accept(x, (String)FamilyLambdaExp.familyName_RelatedFamilyMember_SourceLambda.apply(domain));
            });
            familyMemberRepository.insert(request.getFamilyMemberList());
        }
        return (Integer) FamilyLambdaExp.dtoKeyLambda.apply(domain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(FamilyDomain request){
        Serializable keyId = FamilyLambdaExp.dtoKeyLambda.apply(request);
        FamilyDomain old = find(new FamilyFindDomain(keyId, request.getLoadFlag()));
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
    public Boolean update(FamilyDomain request, FamilyDomain domain){
        FamilyDomain old = domain;
        //更新关联数据family_address
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadFamilyAddressDomain()))){
            if(ObjectUtil.isNotNull(request.getFamilyAddress())){
                FamilyLambdaExp.familyAddressFamilyIdTargetSetLambda.accept(request.getFamilyAddress(), (Integer)FamilyLambdaExp.familyId_RelatedFamilyAddress_SourceLambda.apply(request));
                FamilyLambdaExp.familyAddressFamilyNameTargetSetLambda.accept(request.getFamilyAddress(), (String)FamilyLambdaExp.familyName_RelatedFamilyAddress_SourceLambda.apply(request));
            }
            this.merge(ObjectUtil.isNotNull(old.getFamilyAddress())? CollUtil.toList(old.getFamilyAddress()):ListUtil.empty(),
                    ObjectUtil.isNotNull(request.getFamilyAddress())? CollUtil.toList(request.getFamilyAddress()):ListUtil.empty(),
                    FamilyLambdaExp.familyAddressDomainKeyLambda, familyAddressRepository);
        }
        //更新关联数据family_member
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadFamilyMemberDomain()))){
            if(CollUtil.isNotEmpty(request.getFamilyMemberList())){
                request.getFamilyMemberList().forEach(x->{
                    FamilyLambdaExp.familyMemberFamilyIdTargetSetLambda.accept(x, (Integer)FamilyLambdaExp.familyId_RelatedFamilyMember_SourceLambda.apply(request));
                    FamilyLambdaExp.familyMemberFamilyNameTargetSetLambda.accept(x, (String)FamilyLambdaExp.familyName_RelatedFamilyMember_SourceLambda.apply(request));
                });
            }
            this.merge(old.getFamilyMemberList(), request.getFamilyMemberList(), FamilyLambdaExp.familyMemberDomainKeyLambda, familyMemberRepository);
        }

        //更新数据
        if(request.getChanged()){
            return familyRepository.update(request) > 0;
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
        return delete(id, FamilyDomain.LoadFlag.builder().loadAll(true).build());
    }
    /**
    * 删除
    * @param id 数据ID
    * @param loadFlag 數據加載參數
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer id, FamilyDomain.LoadFlag loadFlag){
        FamilyDomain old = find(new FamilyFindDomain(id ,FamilyDomain.LoadFlag.builder().build()));
        if (ObjectUtil.isNull(old)) {
            return false;
        }

        if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadFamilyAddressDomain())){
            //删除关联数据family_address
            LambdaQuery<FamilyDomain.FamilyAddressDomain> lambdaQuery = LambdaQuery.of(FamilyDomain.FamilyAddressDomain.class);
            lambdaQuery.eq(FamilyLambdaExp.familyAddress_familyIdTargetLambda,FamilyLambdaExp.familyId_RelatedFamilyAddress_SourceLambda.apply(old));
            familyAddressRepository.deleteByFilter(lambdaQuery);
        }
        if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadFamilyMemberDomain())){
            //删除关联数据family_member
            LambdaQuery<FamilyDomain.FamilyMemberDomain> lambdaQuery = LambdaQuery.of(FamilyDomain.FamilyMemberDomain.class);
            lambdaQuery.eq(FamilyLambdaExp.familyMember_familyIdTargetLambda,FamilyLambdaExp.familyId_RelatedFamilyMember_SourceLambda.apply(old));
            familyMemberRepository.deleteByFilter(lambdaQuery);
        }
        return familyRepository.delete(CollUtil.newArrayList(old)) > 0;
    }
}
