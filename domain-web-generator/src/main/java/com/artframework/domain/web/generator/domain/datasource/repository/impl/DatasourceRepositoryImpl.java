package com.artframework.domain.web.generator.domain.datasource.repository.impl;

import com.artframework.domain.web.generator.domain.datasource.convertor.*;
import com.artframework.domain.web.generator.domain.datasource.lambdaexp.*;
import com.artframework.domain.web.generator.domain.datasource.domain.*;
import com.artframework.domain.web.generator.domain.datasource.repository.*;
import com.artframework.domain.web.generator.dataobject.*;
import com.artframework.domain.core.repository.impl.*;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

@Repository(value="datasource-DatasourceRepositoryImpl")
public class DatasourceRepositoryImpl extends BaseRepositoryImpl<DatasourceDomain,DatasourceConfigDO>  implements DatasourceRepository {

    @Override
    public List<DatasourceConfigDO> convert2DO(List<DatasourceDomain> list) {
        return DatasourceConvertor.INSTANCE.convert2DO(list);
    }

    @Override
    public List<DatasourceDomain> convert2DTO(List<DatasourceConfigDO> list) {
        return DatasourceConvertor.INSTANCE.convert2DTO(list);
    }

    @Override
    public void convert2DTO(DatasourceConfigDO item ,DatasourceDomain targetItem){
        DatasourceConvertor.INSTANCE.convert2DTO(item,targetItem);
    }

    @Override
    public SFunction<DatasourceDomain, Serializable> keyLambda() {
        return DatasourceLambdaExp.dtoKeyLambda;
    }
}
