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

@Repository(value="datasource-DatasourceTableColumnRepositoryImpl")
public class DatasourceTableColumnRepositoryImpl extends BaseRepositoryImpl<DatasourceDomain.DatasourceTableColumnDomain,DatasourceTableColumnDO>  implements DatasourceTableColumnRepository {

    @Override
    public List<DatasourceTableColumnDO> convert2DO(List<DatasourceDomain.DatasourceTableColumnDomain> list) {
        return DatasourceConvertor.INSTANCE.convert2DatasourceTableColumnDO(list);
    }

    @Override
    public List<DatasourceDomain.DatasourceTableColumnDomain> convert2DTO(List<DatasourceTableColumnDO> list) {
        return DatasourceConvertor.INSTANCE.convert2DatasourceTableColumnDTO(list);
    }

    @Override
    public void convert2DTO(DatasourceTableColumnDO item ,DatasourceDomain.DatasourceTableColumnDomain targetItem){
        DatasourceConvertor.INSTANCE.convert2DatasourceTableColumnDTO(item,targetItem);
    }

    @Override
    public SFunction<DatasourceDomain.DatasourceTableColumnDomain, Serializable> keyLambda() {
        return DatasourceLambdaExp.datasourceTableColumnDomainKeyLambda;
    }
}
