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

@Repository(value="DDD-DomainConfigTablesRepositoryImpl")
public class DomainConfigTablesRepositoryImpl extends BaseRepositoryImpl<DDDDomain.DomainConfigTablesDomain,DomainConfigTablesDO>  implements DomainConfigTablesRepository {

    @Override
    public List<DomainConfigTablesDO> convert2DO(List<DDDDomain.DomainConfigTablesDomain> list) {
        return DDDConvertor.INSTANCE.convert2DomainConfigTablesDO(list);
    }

    @Override
    public List<DDDDomain.DomainConfigTablesDomain> convert2DTO(List<DomainConfigTablesDO> list) {
        return DDDConvertor.INSTANCE.convert2DomainConfigTablesDTO(list);
    }

    @Override
    public void convert2DTO(DomainConfigTablesDO item ,DDDDomain.DomainConfigTablesDomain targetItem){
        DDDConvertor.INSTANCE.convert2DomainConfigTablesDTO(item,targetItem);
    }

    @Override
    public SFunction<DDDDomain.DomainConfigTablesDomain, Serializable> keyLambda() {
        return DDDLambdaExp.domainConfigTablesDomainKeyLambda;
    }
}
