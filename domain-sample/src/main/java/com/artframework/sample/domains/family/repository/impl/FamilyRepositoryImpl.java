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

    @Override
    public Class<FamilyDO> getDOClass() {
        return FamilyDO.class;
    }
}
