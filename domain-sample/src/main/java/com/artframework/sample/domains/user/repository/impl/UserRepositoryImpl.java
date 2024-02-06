package com.artframework.sample.domains.user.repository.impl;

import com.artframework.sample.domains.user.convertor.*;
import com.artframework.sample.domains.user.lambdaexp.*;
import com.artframework.sample.domains.user.domain.*;
import com.artframework.sample.domains.user.repository.*;
import com.artframework.sample.entities.*;
import com.artframework.domain.core.repository.impl.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

@Repository(value="user-UserRepositoryImpl")
public class UserRepositoryImpl extends BaseRepositoryImpl<UserDomain,UserInfoDO>  implements UserRepository {

    @Override
    public List<UserInfoDO> convert2DO(List<UserDomain> list) {
        return UserConvertor.INSTANCE.convert2DO(list);
    }

    @Override
    public List<UserDomain> convert2DTO(List<UserInfoDO> list) {
        return UserConvertor.INSTANCE.convert2DTO(list);
    }

    @Override
    public void convert2DTO(UserInfoDO item ,UserDomain targetItem){
        UserConvertor.INSTANCE.convert2DTO(item,targetItem);
    }

    @Override
    public SFunction<UserDomain, Serializable> keyLambda() {
        return UserLambdaExp.dtoKeyLambda;
    }

    @Override
    public Class<UserInfoDO> getDOClass() {
        return UserInfoDO.class;
    }

    @Override
    public IPage<UserDomain> page(UserPageDomain request){
        IPage<UserInfoDO> page=new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<UserInfoDO> wrapper =new LambdaQueryWrapper<>();
        return this.baseMapper.selectPage(page,wrapper).convert(UserConvertor.INSTANCE::convert2DTO);
    }
}
