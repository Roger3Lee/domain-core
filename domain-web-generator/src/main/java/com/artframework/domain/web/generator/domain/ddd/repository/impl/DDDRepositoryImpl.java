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

@Repository(value="DDD-DDDRepositoryImpl")
public class DDDRepositoryImpl extends BaseRepositoryImpl<DDDDomain,DomainConfigDO>  implements DDDRepository {

    @Override
    public List<DomainConfigDO> convert2DO(List<DDDDomain> list) {
        return DDDConvertor.INSTANCE.convert2DO(list);
    }

    @Override
    public List<DDDDomain> convert2DTO(List<DomainConfigDO> list) {
        return DDDConvertor.INSTANCE.convert2DTO(list);
    }

    @Override
    public void convert2DTO(DomainConfigDO item ,DDDDomain targetItem){
        DDDConvertor.INSTANCE.convert2DTO(item,targetItem);
    }

    @Override
    public SFunction<DDDDomain, Serializable> keyLambda() {
        return DDDLambdaExp.dtoKeyLambda;
    }
}
