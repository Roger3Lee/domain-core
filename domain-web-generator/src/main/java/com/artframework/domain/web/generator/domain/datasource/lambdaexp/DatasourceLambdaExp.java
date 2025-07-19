package com.artframework.domain.web.generator.domain.datasource.lambdaexp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.domain.web.generator.domain.datasource.domain.*;
import com.artframework.domain.web.generator.dataobject.*;

import java.util.function.*;
import java.io.Serializable;

/**
* datasource
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
public class DatasourceLambdaExp{
    /**
    * KEY datasource_config lambda
    */
    public static SFunction<DatasourceDomain, Serializable> dtoKeyLambda= DatasourceDomain::getId;

    /**
    * KEY datasource_config lambda
    */
    public static SFunction<DatasourceConfigDO, Serializable> doKeyLambda= DatasourceConfigDO::getId;


    /**
    *  datasource_table lambda
    */
    public static SFunction<DatasourceDomain.DatasourceTableDomain, Serializable> datasourceTableDomainKeyLambda = DatasourceDomain.DatasourceTableDomain::getId;


    /**
    * REF  source lambda
    */
    public static SFunction<DatasourceDomain, Serializable> datasourceConfigId_RelatedDatasourceTable_SourceLambda = DatasourceDomain::getId;

    /**
    * REF  target lambda
    */
    public static SFunction<DatasourceDomain.DatasourceTableDomain,Serializable> datasourceTable_dsIdTargetLambda =DatasourceDomain.DatasourceTableDomain::getDsId;

    /**
    * REF  target lambda
    */
    public static BiConsumer<DatasourceDomain.DatasourceTableDomain,Integer> datasourceTableDsIdTargetSetLambda =DatasourceDomain.DatasourceTableDomain::setDsId;


    /**
    *  datasource_table_column lambda
    */
    public static SFunction<DatasourceDomain.DatasourceTableColumnDomain, Serializable> datasourceTableColumnDomainKeyLambda = DatasourceDomain.DatasourceTableColumnDomain::getId;


    /**
    * REF  source lambda
    */
    public static SFunction<DatasourceDomain, Serializable> datasourceConfigId_RelatedDatasourceTableColumn_SourceLambda = DatasourceDomain::getId;

    /**
    * REF  target lambda
    */
    public static SFunction<DatasourceDomain.DatasourceTableColumnDomain,Serializable> datasourceTableColumn_dsIdTargetLambda =DatasourceDomain.DatasourceTableColumnDomain::getDsId;

    /**
    * REF  target lambda
    */
    public static BiConsumer<DatasourceDomain.DatasourceTableColumnDomain,Integer> datasourceTableColumnDsIdTargetSetLambda =DatasourceDomain.DatasourceTableColumnDomain::setDsId;

}
