package com.artframework.sample.domains.family.repository.impl;

import com.artframework.sample.domains.family.convertor.*;
import com.artframework.sample.domains.family.lambdaexp.*;
import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.domains.family.repository.*;
import com.artframework.sample.entities.*;
import com.artframework.domain.core.repository.impl.*;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

@Repository(value="family-FamilyMemberRepositoryImpl")
public class FamilyMemberRepositoryImpl extends BaseRepositoryImpl<FamilyDomain.FamilyMemberDomain,FamilyMemberDO>  implements FamilyMemberRepository {

    @Override
    public List<FamilyMemberDO> convert2DO(List<FamilyDomain.FamilyMemberDomain> list) {
        return FamilyConvertor.INSTANCE.convert2FamilyMemberDO(list);
    }

    @Override
    public List<FamilyDomain.FamilyMemberDomain> convert2DTO(List<FamilyMemberDO> list) {
        return FamilyConvertor.INSTANCE.convert2FamilyMemberDTO(list);
    }

    @Override
    public void convert2DTO(FamilyMemberDO item ,FamilyDomain.FamilyMemberDomain targetItem){
        FamilyConvertor.INSTANCE.convert2FamilyMemberDTO(item,targetItem);
    }

    @Override
    public SFunction<FamilyDomain.FamilyMemberDomain, Serializable> keyLambda() {
        return FamilyLambdaExp.familyMemberDomainKeyLambda;
    }

    @Override
    public Class<FamilyMemberDO> getDOClass() {
        return FamilyMemberDO.class;
    }
}
