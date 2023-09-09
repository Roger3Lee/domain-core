package com.artframework.sample.domain.user.service.impl;

import com.artframework.sample.domain.user.service.*;
import com.artframework.sample.domain.user.dto.request.*;
import com.artframework.sample.domain.user.dto.*;
import com.artframework.sample.domain.user.repository.*;
import com.artframework.domain.core.service.impl.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import com.artframework.sample.domain.user.lambdaexp.*;

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
    public IPage<UserDTO> page(UserPageRequest request){
        return userRepository.page(request);
    }

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public UserDTO find(UserFindRequest request){
        UserDTO response = userRepository.query(request.getKey(), UserLambdaExp.doKeyLambda);
        if (ObjectUtil.isNotNull(request.getLoadFlag())) {
            if(request.getLoadFlag().getLoadUserAddress()){
                Serializable key = UserLambdaExp.userAddressSourceLambda.apply(response);
                response.setUserAddress(userAddressRepository.query(key, UserLambdaExp.userAddressTargetLambda));
            }

            if(request.getLoadFlag().getLoadUserFamilyMember()){
                Serializable key = UserLambdaExp.userFamilyMemberSourceLambda.apply(response);
                response.setUserFamilyMember(userFamilyMemberRepository.queryList(key, UserLambdaExp.userFamilyMemberTargetLambda));
            }

        }
        return response;
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public java.lang.Long insert(UserCreateRequest request){
        //插入数据
        userRepository.insert(request);

        //插入关联数据user_address
        if(ObjectUtil.isNotNull(request.getUserAddress())){
            Serializable key = UserLambdaExp.userAddressSourceLambda.apply(request);
            UserLambdaExp.userAddressTargetSetLambda.accept(request.getUserAddress(),(java.lang.Long)key);
            userAddressRepository.insert(request.getUserAddress());
        }
        //插入关联数据user_family_member
        if(CollUtil.isNotEmpty(request.getUserFamilyMember())){
            Serializable key = UserLambdaExp.userFamilyMemberSourceLambda.apply(request);
            request.getUserFamilyMember().forEach(x->UserLambdaExp.userFamilyMemberTargetSetLambda.accept(x,(java.lang.Long)key));
            userFamilyMemberRepository.insert(request.getUserFamilyMember());
        }
        return (java.lang.Long) UserLambdaExp.dtoKeyLambda.apply(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(UserUpdateRequest request){
        Serializable keyId = UserLambdaExp.dtoKeyLambda.apply(request);
        UserDTO old = find(new UserFindRequest(keyId, request.getLoadFlag()));
        if(request.getChanged()){
            //更新数据
            userRepository.update(request);
        }
        if(ObjectUtil.isNotNull(request.getUserAddress())){
            Serializable key = UserLambdaExp.userAddressSourceLambda.apply(request);
            UserLambdaExp.userAddressTargetSetLambda.accept(request.getUserAddress(),(java.lang.Long)key);
            if(request.getUserAddress().getChanged()){
                userAddressRepository.update(request.getUserAddress());
            }
        }
        //更新关联数据user_family_member
        if(CollUtil.isNotEmpty(request.getUserFamilyMember())){
            Serializable key = UserLambdaExp.userFamilyMemberSourceLambda.apply(request);
            request.getUserFamilyMember().forEach(x->UserLambdaExp.userFamilyMemberTargetSetLambda.accept(x,(java.lang.Long)key));
            this.merge(old.getUserFamilyMember(), request.getUserFamilyMember(), UserLambdaExp.userFamilyMemberKeyLambda, userFamilyMemberRepository);
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
        UserDTO old = find(new UserFindRequest(id ,new UserDTO.LoadFlag()));

        //删除关联数据user_address
        if(ObjectUtil.isNotNull(old.getUserAddress())){
            userAddressRepository.delete(CollUtil.newArrayList(old.getUserAddress()));
        }
        //删除关联数据user_family_member
        if(CollUtil.isNotEmpty(old.getUserFamilyMember())){
            userFamilyMemberRepository.delete(old.getUserFamilyMember());
        }

        return userRepository.delete(CollUtil.newArrayList(old)) > 0;
    }
}
