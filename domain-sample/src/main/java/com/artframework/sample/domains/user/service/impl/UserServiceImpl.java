package com.artframework.sample.domains.user.service.impl;

import com.artframework.sample.domains.user.service.*;
import com.artframework.sample.domains.user.domain.*;
import com.artframework.sample.domains.user.repository.*;
import com.artframework.domain.core.service.impl.*;
import com.artframework.domain.core.uitls.*;

import cn.hutool.core.collection.*;
import cn.hutool.core.util.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.artframework.sample.domains.user.lambdaexp.*;

import javax.annotation.PostConstruct;


@Service
public class UserServiceImpl extends BaseDomainServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserFamilyMemberRepository userFamilyMemberRepository;

    @PostConstruct
    public void init(){
        this._DomainRepositoryMap.put(UserDomain.UserAddressDomain.class.getCanonicalName(), this.userAddressRepository);
        this._DomainRepositoryMap.put(UserDomain.UserFamilyMemberDomain.class.getCanonicalName(), this.userFamilyMemberRepository);
    }
    /**
    * 分页查询
    * @param request 请求体
    * @return
    */
    @Override
    public IPage<UserDomain> page(UserPageDomain request){
        return userRepository.page(request);
    }

   /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public UserDomain find(UserFindDomain request){
        return find(request, null);
    }

    /**
    * 查找
    * @param request 请求体
    * @param response 原始數據，避免重新查詢主表
    * @return
    */
    @Override
    public UserDomain find(UserFindDomain request, UserDomain response){
        if(ObjectUtil.isNull(response)){
            response = userRepository.query(request.getKey(), UserLambdaExp.doKeyLambda);
            if(ObjectUtil.isNull(response)){
                return response;
            }
        }
        return find(response, request.getLoadFlag());
    }


    public UserDomain find(UserDomain response,UserDomain.LoadFlag loadFlag){
        final UserDomain resp = response;
        if (ObjectUtil.isNotNull(loadFlag)) {
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadUserAddress())){
                Serializable key = UserLambdaExp.userAddressDomainEntitySourceLambda.apply(resp);
                if(ObjectUtil.isNotNull(key)){
                    UserDomain.UserAddressDomain item= userAddressRepository.query(key, UserLambdaExp.userAddressDOTargetLambda);
                    if(ObjectUtil.isNotNull(item)){
                        item.set_thisDomain(resp);
                    }
                    resp.setUserAddress(item);
                }
            }
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.getLoadUserFamilyMember())){
                Serializable key = UserLambdaExp.userFamilyMemberDomainEntitySourceLambda.apply(resp);
                if(ObjectUtil.isNotNull(key)){
                    List<UserDomain.UserFamilyMemberDomain> queryList = userFamilyMemberRepository.queryList(key, UserLambdaExp.userFamilyMemberDOTargetLambda,
                                     FiltersUtils.getEntityFilters(loadFlag.getFilters(), UserDomain.UserFamilyMemberDomain.class),
                                     OrdersUtils.getEntityOrders(loadFlag.getOrders(), UserDomain.UserFamilyMemberDomain.class))
                                                .stream().peek(x -> x.set_thisDomain(resp)).collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(resp.getUserFamilyMemberList())){
                        resp.setUserFamilyMemberList(queryList);
                    } else {
                        resp.getUserFamilyMemberList().addAll(queryList);
                    }
                }
            }
        }
        resp.setLoadFlag(UserDomain.LoadFlag.merge(loadFlag, resp.getLoadFlag()));
        return resp;
    }


    /**
     * 查找
     * @param request 请求体
     * @param keyLambda 請求key參數對應的字段的lambda表達式
     * @return
     */
    @Override
    public UserDomain findByKey(UserFindDomain request, SFunction<UserDomain, Serializable> keyLambda){
        return find(request, userRepository.queryByKey(request.getKey(), keyLambda));
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public java.lang.Long insert(UserDomain request){
        //插入数据
        UserDomain domain = userRepository.insert(request);

        //插入关联数据user_address
        if(ObjectUtil.isNotNull(request.getUserAddress())){
            Serializable key = UserLambdaExp.userAddressDomainEntitySourceLambda.apply(domain);
            UserLambdaExp.userAddressDomainTargetSetLambda.accept(request.getUserAddress(),(java.lang.Long)key);
            userAddressRepository.insert(request.getUserAddress());
        }

        //插入关联数据user_family_member
        if(CollUtil.isNotEmpty(request.getUserFamilyMemberList())){
            Serializable key = UserLambdaExp.userFamilyMemberDomainEntitySourceLambda.apply(domain);
            request.getUserFamilyMemberList().forEach(x->UserLambdaExp.userFamilyMemberDomainTargetSetLambda.accept(x,(java.lang.Long)key));
            userFamilyMemberRepository.insert(request.getUserFamilyMemberList());
        }
        return (java.lang.Long) UserLambdaExp.dtoKeyLambda.apply(domain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(UserDomain request){
        Serializable keyId = UserLambdaExp.dtoKeyLambda.apply(request);
        UserDomain old = find(new UserFindDomain(keyId, null));
        return update(request,old);
    }
    /**
    * 修改 此方法不用再加載domain主entity數據
    * @param request 请求体
    * @param domain 原始數據，避免重新查詢主表
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(UserDomain request, UserDomain domain){
        return update(request, domain, true);
    }

   /**
    * 修改,此方法不用再加載domain主entity數據
    * reload參數True將重新加載數據， False將直接對request和domain進行比較， 適用於模型比較複雜，需要以來加載後的數據再進行下一層數據加載的情況層數據加載的情況

    * @param request 请求体
    * @param domain 原始domain
    * @param reload 是否使用request的loadFlag重新加載數據
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(UserDomain request, UserDomain domain, Boolean reload){
        UserDomain old = domain;
        if (reload) {
            Serializable keyId = UserLambdaExp.dtoKeyLambda.apply(request);
            old = find(new UserFindDomain(keyId, request.getLoadFlag()), domain);
        }
        //更新关联数据user_address
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadUserAddress()))){
            if(ObjectUtil.isNotNull(request.getUserAddress())){
                Serializable key = UserLambdaExp.userAddressDomainEntitySourceLambda.apply(request);
                UserLambdaExp.userAddressDomainTargetSetLambda.accept(request.getUserAddress(),(java.lang.Long)key);
            }
            this.merge(ObjectUtil.isNotNull(old.getUserAddress())? CollUtil.toList(old.getUserAddress()):ListUtil.empty(),
                    ObjectUtil.isNotNull(request.getUserAddress())? CollUtil.toList(request.getUserAddress()):ListUtil.empty(),
                    UserLambdaExp.userAddressDomainKeyLambda, userAddressRepository);
        }
        //更新关联数据user_family_member
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().getLoadUserFamilyMember()))){
            if(CollUtil.isNotEmpty(request.getUserFamilyMemberList())){
                Serializable key = UserLambdaExp.userFamilyMemberDomainEntitySourceLambda.apply(request);
                request.getUserFamilyMemberList().forEach(x->UserLambdaExp.userFamilyMemberDomainTargetSetLambda.accept(x,(java.lang.Long)key));
            }
            this.merge(old.getUserFamilyMemberList(), request.getUserFamilyMemberList(), UserLambdaExp.userFamilyMemberDomainKeyLambda, userFamilyMemberRepository);
        }


        //更新数据
        if(request.getChanged()){
            userRepository.update(request);
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
    public Boolean delete(java.lang.Long id){
        UserDomain old = find(new UserFindDomain(id ,UserDomain.LoadFlag.builder().build()));
        if (ObjectUtil.isNull(old)) {
            return false;
        }

        //删除关联数据user_address
        userAddressRepository.deleteByFilter(ListUtil.toList(FiltersUtils.build(UserLambdaExp.userAddressDomainTargetLambda,
               UserLambdaExp.userAddressDomainEntitySourceLambda.apply(old))));
        //删除关联数据user_family_member
        userFamilyMemberRepository.deleteByFilter(ListUtil.toList(FiltersUtils.build(UserLambdaExp.userFamilyMemberDomainTargetLambda,
               UserLambdaExp.userFamilyMemberDomainEntitySourceLambda.apply(old))));
        return userRepository.delete(CollUtil.newArrayList(old)) > 0;
    }
}
