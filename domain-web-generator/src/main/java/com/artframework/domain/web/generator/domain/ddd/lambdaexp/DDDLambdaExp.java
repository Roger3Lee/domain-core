package com.artframework.domain.web.generator.domain.ddd.lambdaexp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.domain.web.generator.domain.ddd.domain.*;
import com.artframework.domain.web.generator.dataobject.*;

import java.util.function.*;
import java.io.Serializable;

/**
* DDD
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
public class DDDLambdaExp{
    /**
    * KEY domain_config lambda
    */
    public static SFunction<DDDDomain, Serializable> dtoKeyLambda= DDDDomain::getId;

    /**
    * KEY domain_config lambda
    */
    public static SFunction<DomainConfigDO, Serializable> doKeyLambda= DomainConfigDO::getId;


    /**
    *  domain_config_tables lambda
    */
    public static SFunction<DDDDomain.DomainConfigTablesDomain, Serializable> domainConfigTablesDomainKeyLambda = DDDDomain.DomainConfigTablesDomain::getId;


    /**
    * REF  source lambda
    */
    public static SFunction<DDDDomain, Serializable> domainConfigId_RelatedDomainConfigTables_SourceLambda = DDDDomain::getId;

    /**
    * REF  target lambda
    */
    public static SFunction<DDDDomain.DomainConfigTablesDomain,Serializable> domainConfigTables_domainIdTargetLambda =DDDDomain.DomainConfigTablesDomain::getDomainId;

    /**
    * REF  target lambda
    */
    public static BiConsumer<DDDDomain.DomainConfigTablesDomain,Integer> domainConfigTablesDomainIdTargetSetLambda =DDDDomain.DomainConfigTablesDomain::setDomainId;

     /**
     * REF  source lambda
     */
     public static SFunction<DDDDomain, Serializable> domainConfigProjectId_RelatedDomainConfigTables_SourceLambda = DDDDomain::getProjectId;

     /**
     * REF  target lambda
     */
     public static SFunction<DDDDomain.DomainConfigTablesDomain,Serializable> domainConfigTables_projectIdTargetLambda =DDDDomain.DomainConfigTablesDomain::getProjectId;

     /**
     * REF  target lambda
     */
     public static BiConsumer<DDDDomain.DomainConfigTablesDomain,Integer> domainConfigTablesProjectIdTargetSetLambda =DDDDomain.DomainConfigTablesDomain::setProjectId;

    /**
    *  domain_config_line lambda
    */
    public static SFunction<DDDDomain.DomainConfigLineDomain, Serializable> domainConfigLineDomainKeyLambda = DDDDomain.DomainConfigLineDomain::getId;


    /**
    * REF  source lambda
    */
    public static SFunction<DDDDomain, Serializable> domainConfigId_RelatedDomainConfigLine_SourceLambda = DDDDomain::getId;

    /**
    * REF  target lambda
    */
    public static SFunction<DDDDomain.DomainConfigLineDomain,Serializable> domainConfigLine_domainIdTargetLambda =DDDDomain.DomainConfigLineDomain::getDomainId;

    /**
    * REF  target lambda
    */
    public static BiConsumer<DDDDomain.DomainConfigLineDomain,Integer> domainConfigLineDomainIdTargetSetLambda =DDDDomain.DomainConfigLineDomain::setDomainId;

     /**
     * REF  source lambda
     */
     public static SFunction<DDDDomain, Serializable> domainConfigProjectId_RelatedDomainConfigLine_SourceLambda = DDDDomain::getProjectId;

     /**
     * REF  target lambda
     */
     public static SFunction<DDDDomain.DomainConfigLineDomain,Serializable> domainConfigLine_projectIdTargetLambda =DDDDomain.DomainConfigLineDomain::getProjectId;

     /**
     * REF  target lambda
     */
     public static BiConsumer<DDDDomain.DomainConfigLineDomain,Integer> domainConfigLineProjectIdTargetSetLambda =DDDDomain.DomainConfigLineDomain::setProjectId;
    /**
    * REF line source lambda
    */
    public static SFunction<DDDDomain.DomainConfigLineDomain, Serializable> domainConfigLineRefLine_lineCodeSourceLambda = DDDDomain.DomainConfigLineDomain::getLineCode;

    /**
    * REF line target lambda
    */
    public static SFunction<DDDDomain.DomainConfigLineConfigDomain,Serializable> domainConfigLineRefLine_lineCodeTargetLambda =DDDDomain.DomainConfigLineConfigDomain::getLineCode;

    /**
    * REF line target lambda
    */
    public static BiConsumer<DDDDomain.DomainConfigLineConfigDomain,String> domainConfigLineRefLine_lineCodeTargetSetLambda =DDDDomain.DomainConfigLineConfigDomain::setLineCode;

    /**
    *  domain_config_line_config lambda
    */
    public static SFunction<DDDDomain.DomainConfigLineConfigDomain, Serializable> domainConfigLineConfigDomainKeyLambda = DDDDomain.DomainConfigLineConfigDomain::getId;


    /**
    * REF  source lambda
    */
    public static SFunction<DDDDomain, Serializable> domainConfigId_RelatedDomainConfigLineConfig_SourceLambda = DDDDomain::getId;

    /**
    * REF  target lambda
    */
    public static SFunction<DDDDomain.DomainConfigLineConfigDomain,Serializable> domainConfigLineConfig_domainIdTargetLambda =DDDDomain.DomainConfigLineConfigDomain::getDomainId;

    /**
    * REF  target lambda
    */
    public static BiConsumer<DDDDomain.DomainConfigLineConfigDomain,Integer> domainConfigLineConfigDomainIdTargetSetLambda =DDDDomain.DomainConfigLineConfigDomain::setDomainId;

     /**
     * REF  source lambda
     */
     public static SFunction<DDDDomain, Serializable> domainConfigProjectId_RelatedDomainConfigLineConfig_SourceLambda = DDDDomain::getProjectId;

     /**
     * REF  target lambda
     */
     public static SFunction<DDDDomain.DomainConfigLineConfigDomain,Serializable> domainConfigLineConfig_projectIdTargetLambda =DDDDomain.DomainConfigLineConfigDomain::getProjectId;

     /**
     * REF  target lambda
     */
     public static BiConsumer<DDDDomain.DomainConfigLineConfigDomain,Integer> domainConfigLineConfigProjectIdTargetSetLambda =DDDDomain.DomainConfigLineConfigDomain::setProjectId;
}
