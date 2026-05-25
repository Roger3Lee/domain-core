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

@Repository(value="family-FamilyRepositoryImpl")
public class FamilyRepositoryImpl extends BaseRepositoryImpl<FamilyDomain,FamilyDO>  implements FamilyRepository {

    @Override
    public List<FamilyDO> convert2DO(List<FamilyDomain> list) {
        return FamilyConvertor.INSTANCE.convert2DO(list);
    }

    @Override
    public List<FamilyDomain> convert2DTO(List<FamilyDO> list) {
        return FamilyConvertor.INSTANCE.convert2DTO(list);
    }

    @Override
    public void convert2DTO(FamilyDO item ,FamilyDomain targetItem){
        FamilyConvertor.INSTANCE.convert2DTO(item,targetItem);
    }

    @Override
    public SFunction<FamilyDomain, Serializable> keyLambda() {
        return FamilyLambdaExp.dtoKeyLambda;
    }
}
