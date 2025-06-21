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

@Repository(value="family-FamilyAddressRepositoryImpl")
public class FamilyAddressRepositoryImpl extends BaseRepositoryImpl<FamilyDomain.FamilyAddressDomain,FamilyAddressDO>  implements FamilyAddressRepository {

    @Override
    public List<FamilyAddressDO> convert2DO(List<FamilyDomain.FamilyAddressDomain> list) {
        return FamilyConvertor.INSTANCE.convert2FamilyAddressDO(list);
    }

    @Override
    public List<FamilyDomain.FamilyAddressDomain> convert2DTO(List<FamilyAddressDO> list) {
        return FamilyConvertor.INSTANCE.convert2FamilyAddressDTO(list);
    }

    @Override
    public void convert2DTO(FamilyAddressDO item ,FamilyDomain.FamilyAddressDomain targetItem){
        FamilyConvertor.INSTANCE.convert2FamilyAddressDTO(item,targetItem);
    }

    @Override
    public SFunction<FamilyDomain.FamilyAddressDomain, Serializable> keyLambda() {
        return FamilyLambdaExp.familyAddressDomainKeyLambda;
    }
}
