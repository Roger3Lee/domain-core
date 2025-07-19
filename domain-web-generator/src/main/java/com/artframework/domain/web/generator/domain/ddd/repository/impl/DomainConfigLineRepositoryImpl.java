package com.artframework.domain.web.generator.domain.ddd.repository.impl;

import com.artframework.domain.web.generator.domain.ddd.convertor.*;
import com.artframework.domain.web.generator.domain.ddd.lambdaexp.*;
import com.artframework.domain.web.generator.domain.ddd.domain.*;
import com.artframework.domain.web.generator.domain.ddd.repository.*;
import com.artframework.domain.web.generator.dataobject.*;
import com.artframework.domain.core.repository.impl.*;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

@Repository(value="DDD-DomainConfigLineRepositoryImpl")
public class DomainConfigLineRepositoryImpl extends BaseRepositoryImpl<DDDDomain.DomainConfigLineDomain,DomainConfigLineDO>  implements DomainConfigLineRepository {

    @Override
    public List<DomainConfigLineDO> convert2DO(List<DDDDomain.DomainConfigLineDomain> list) {
        return DDDConvertor.INSTANCE.convert2DomainConfigLineDO(list);
    }

    @Override
    public List<DDDDomain.DomainConfigLineDomain> convert2DTO(List<DomainConfigLineDO> list) {
        return DDDConvertor.INSTANCE.convert2DomainConfigLineDTO(list);
    }

    @Override
    public void convert2DTO(DomainConfigLineDO item ,DDDDomain.DomainConfigLineDomain targetItem){
        DDDConvertor.INSTANCE.convert2DomainConfigLineDTO(item,targetItem);
    }

    @Override
    public SFunction<DDDDomain.DomainConfigLineDomain, Serializable> keyLambda() {
        return DDDLambdaExp.domainConfigLineDomainKeyLambda;
    }
}
