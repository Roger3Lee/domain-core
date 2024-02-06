package com.artframework.sample.domains.user.repository.impl;

import com.artframework.sample.domains.user.convertor.*;
import com.artframework.sample.domains.user.lambdaexp.*;
import com.artframework.sample.domains.user.domain.*;
import com.artframework.sample.domains.user.repository.*;
import com.artframework.sample.entities.*;
import com.artframework.domain.core.repository.impl.*;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

@Repository(value="user-UserAddressRepositoryImpl")
public class UserAddressRepositoryImpl extends BaseRepositoryImpl<UserDomain.UserAddressDomain,UserAddressDO>  implements UserAddressRepository {

    @Override
    public List<UserAddressDO> convert2DO(List<UserDomain.UserAddressDomain> list) {
        return UserConvertor.INSTANCE.convert2UserAddressDO(list);
    }

    @Override
    public List<UserDomain.UserAddressDomain> convert2DTO(List<UserAddressDO> list) {
        return UserConvertor.INSTANCE.convert2UserAddressDTO(list);
    }

    @Override
    public void convert2DTO(UserAddressDO item ,UserDomain.UserAddressDomain targetItem){
        UserConvertor.INSTANCE.convert2UserAddressDTO(item,targetItem);
    }

    @Override
    public SFunction<UserDomain.UserAddressDomain, Serializable> keyLambda() {
        return UserLambdaExp.userAddressDomainKeyLambda;
    }

    @Override
    public Class<UserAddressDO> getDOClass() {
        return UserAddressDO.class;
    }
}
