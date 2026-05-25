package io.github.roger3lee.sample.domains.family.repository.impl;

import io.github.roger3lee.sample.domains.family.convertor.*;
import io.github.roger3lee.sample.domains.family.lambdaexp.*;
import io.github.roger3lee.sample.domains.family.domain.*;
import io.github.roger3lee.sample.domains.family.repository.*;
import io.github.roger3lee.sample.entities.*;
import io.github.roger3lee.domain.core.repository.impl.*;

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
}
