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

@Repository(value="DDD-DomainConfigLineConfigRepositoryImpl")
public class DomainConfigLineConfigRepositoryImpl extends BaseRepositoryImpl<DDDDomain.DomainConfigLineConfigDomain,DomainConfigLineConfigDO>  implements DomainConfigLineConfigRepository {

    @Override
    public List<DomainConfigLineConfigDO> convert2DO(List<DDDDomain.DomainConfigLineConfigDomain> list) {
        return DDDConvertor.INSTANCE.convert2DomainConfigLineConfigDO(list);
    }

    @Override
    public List<DDDDomain.DomainConfigLineConfigDomain> convert2DTO(List<DomainConfigLineConfigDO> list) {
        return DDDConvertor.INSTANCE.convert2DomainConfigLineConfigDTO(list);
    }

    @Override
    public void convert2DTO(DomainConfigLineConfigDO item ,DDDDomain.DomainConfigLineConfigDomain targetItem){
        DDDConvertor.INSTANCE.convert2DomainConfigLineConfigDTO(item,targetItem);
    }

    @Override
    public SFunction<DDDDomain.DomainConfigLineConfigDomain, Serializable> keyLambda() {
        return DDDLambdaExp.domainConfigLineConfigDomainKeyLambda;
    }
}
