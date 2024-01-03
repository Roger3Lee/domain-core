package com.artframework.sample.domains.user.service.impl;

import com.artframework.sample.domains.user.service.*;
import com.artframework.sample.domains.user.domain.*;
import com.artframework.sample.domains.user.repository.*;
import com.artframework.domain.core.service.impl.*;
import com.artframework.domain.core.uitls.*;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import com.artframework.sample.domains.user.lambdaexp.*;

@Service
public class UserServiceImpl extends BaseDomainServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepository.UserAddressRepository userAddressRepository;

    @Autowired
    private UserRepository.UserFamilyMemberRepository userFamilyMemberRepository;

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
        UserDomain response = userRepository.query(request.getKey(), UserLambdaExp.doKeyLambda);
        if(ObjectUtil.isNull(response)){
            return response;
        }

        if (ObjectUtil.isNotNull(request.getLoadFlag())) {
            if(request.getLoadFlag().getLoadUserAddress()){
                Serializable key = UserLambdaExp.userAddressSourceLambda.apply(response);
                response.setUserAddress(userAddressRepository.query(key, UserLambdaExp.userAddressTargetLambda));
            }
            if(request.getLoadFlag().getLoadUserFamilyMember()){
                Serializable key = UserLambdaExp.userFamilyMemberSourceLambda.apply(response);
                response.setUserFamilyMemberList(userFamilyMemberRepository.queryList(key, UserLambdaExp.userFamilyMemberTargetLambda,
                                FiltersUtils.getEntityFilters(request.getLoadFlag().getFilters(), this.getEntityName(UserDomain.UserFamilyMemberDomain.class))));
            }
        }
        response.setLoadFlag(request.getLoadFlag());
        return response;
    }


    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public java.lang.Long insert(UserCreateDomain request){
        //插入关联数据user_address
        if(ObjectUtil.isNotNull(request.getUserAddress())){
            Serializable key = UserLambdaExp.userAddressSourceLambda.apply(request);
            UserLambdaExp.userAddressTargetSetLambda.accept(request.getUserAddress(),(java.lang.Long)key);
            userAddressRepository.insert(request.getUserAddress());
        }
        //插入关联数据user_family_member
        if(CollUtil.isNotEmpty(request.getUserFamilyMemberList())){
            Serializable key = UserLambdaExp.userFamilyMemberSourceLambda.apply(request);
            request.getUserFamilyMemberList().forEach(x->UserLambdaExp.userFamilyMemberTargetSetLambda.accept(x,(java.lang.Long)key));
            userFamilyMemberRepository.insert(request.getUserFamilyMemberList());
        }
        //插入数据
        UserDomain dto = userRepository.insert(request);
        return (java.lang.Long) UserLambdaExp.dtoKeyLambda.apply(dto);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(UserUpdateDomain request){
        Serializable keyId = UserLambdaExp.dtoKeyLambda.apply(request);
        UserDomain old = find(new UserFindDomain(keyId, request.getLoadFlag()));
        if(ObjectUtil.isNotNull(request.getLoadFlag()) && request.getLoadFlag().getLoadUserAddress()
            && ObjectUtil.isNotNull(request.getUserAddress())){
            Serializable key = UserLambdaExp.userAddressSourceLambda.apply(request);
            UserLambdaExp.userAddressTargetSetLambda.accept(request.getUserAddress(),(java.lang.Long)key);
            if(BooleanUtil.isTrue(request.getUserAddress().getChanged())){
                userAddressRepository.update(request.getUserAddress());
            }
        }
        //更新关联数据user_family_member
        if(CollUtil.isNotEmpty(request.getUserFamilyMemberList())){
            Serializable key = UserLambdaExp.userFamilyMemberSourceLambda.apply(request);
            request.getUserFamilyMemberList().forEach(x->UserLambdaExp.userFamilyMemberTargetSetLambda.accept(x,(java.lang.Long)key));
            this.merge(old.getUserFamilyMemberList(), request.getUserFamilyMemberList(), UserLambdaExp.userFamilyMemberKeyLambda, userFamilyMemberRepository);
        }


        //更新数据
        userRepository.update(request);
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
        UserDomain old = find(new UserFindDomain(id ,new UserDomain.LoadFlag()));

        //删除关联数据user_address
        if(ObjectUtil.isNotNull(old.getUserAddress())){
            userAddressRepository.delete(CollUtil.newArrayList(old.getUserAddress()));
        }
        //删除关联数据user_family_member
        if(CollUtil.isNotEmpty(old.getUserFamilyMemberList())){
            userFamilyMemberRepository.delete(old.getUserFamilyMemberList());
        }
        return userRepository.delete(CollUtil.newArrayList(old)) > 0;
    }
}
