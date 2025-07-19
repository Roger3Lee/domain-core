package com.artframework.domain.web.generator.domain.project.lambdaexp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.domain.web.generator.domain.project.domain.*;
import com.artframework.domain.web.generator.dataobject.*;

import java.util.function.*;
import java.io.Serializable;

/**
* project
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
public class ProjectLambdaExp{
    /**
    * KEY project lambda
    */
    public static SFunction<ProjectDomain, Serializable> dtoKeyLambda= ProjectDomain::getId;

    /**
    * KEY project lambda
    */
    public static SFunction<ProjectDO, Serializable> doKeyLambda= ProjectDO::getId;


    /**
    *  domain_config lambda
    */
    public static SFunction<ProjectDomain.DomainConfigDomain, Serializable> domainConfigDomainKeyLambda = ProjectDomain.DomainConfigDomain::getId;


    /**
    * REF  source lambda
    */
    public static SFunction<ProjectDomain, Serializable> projectId_RelatedDomainConfig_SourceLambda = ProjectDomain::getId;

    /**
    * REF  target lambda
    */
    public static SFunction<ProjectDomain.DomainConfigDomain,Serializable> domainConfig_projectIdTargetLambda =ProjectDomain.DomainConfigDomain::getProjectId;

    /**
    * REF  target lambda
    */
    public static BiConsumer<ProjectDomain.DomainConfigDomain,Integer> domainConfigProjectIdTargetSetLambda =ProjectDomain.DomainConfigDomain::setProjectId;

}
