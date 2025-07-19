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

@Repository(value="datasource-DatasourceTableRepositoryImpl")
public class DatasourceTableRepositoryImpl extends BaseRepositoryImpl<DatasourceDomain.DatasourceTableDomain,DatasourceTableDO>  implements DatasourceTableRepository {

    @Override
    public List<DatasourceTableDO> convert2DO(List<DatasourceDomain.DatasourceTableDomain> list) {
        return DatasourceConvertor.INSTANCE.convert2DatasourceTableDO(list);
    }

    @Override
    public List<DatasourceDomain.DatasourceTableDomain> convert2DTO(List<DatasourceTableDO> list) {
        return DatasourceConvertor.INSTANCE.convert2DatasourceTableDTO(list);
    }

    @Override
    public void convert2DTO(DatasourceTableDO item ,DatasourceDomain.DatasourceTableDomain targetItem){
        DatasourceConvertor.INSTANCE.convert2DatasourceTableDTO(item,targetItem);
    }

    @Override
    public SFunction<DatasourceDomain.DatasourceTableDomain, Serializable> keyLambda() {
        return DatasourceLambdaExp.datasourceTableDomainKeyLambda;
    }
}
