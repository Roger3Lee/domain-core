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

@Repository(value="user-UserFamilyMemberRepositoryImpl")
public class UserFamilyMemberRepositoryImpl extends BaseRepositoryImpl<UserDomain.UserFamilyMemberDomain,UserFamilyMemberDO>  implements UserFamilyMemberRepository {

    @Override
    public List<UserFamilyMemberDO> convert2DO(List<UserDomain.UserFamilyMemberDomain> list) {
        return UserConvertor.INSTANCE.convert2UserFamilyMemberDO(list);
    }

    @Override
    public List<UserDomain.UserFamilyMemberDomain> convert2DTO(List<UserFamilyMemberDO> list) {
        return UserConvertor.INSTANCE.convert2UserFamilyMemberDTO(list);
    }

    @Override
    public void convert2DTO(UserFamilyMemberDO item ,UserDomain.UserFamilyMemberDomain targetItem){
        UserConvertor.INSTANCE.convert2UserFamilyMemberDTO(item,targetItem);
    }

    @Override
    public SFunction<UserDomain.UserFamilyMemberDomain, Serializable> keyLambda() {
        return UserLambdaExp.userFamilyMemberDomainKeyLambda;
    }

    @Override
    public Class<UserFamilyMemberDO> getDOClass() {
        return UserFamilyMemberDO.class;
    }
}
