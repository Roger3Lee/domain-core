package com.artframework.domain.web.generator.domain.project.repository.impl;

import com.artframework.domain.web.generator.domain.project.convertor.*;
import com.artframework.domain.web.generator.domain.project.lambdaexp.*;
import com.artframework.domain.web.generator.domain.project.domain.*;
import com.artframework.domain.web.generator.domain.project.repository.*;
import com.artframework.domain.web.generator.dataobject.*;
import com.artframework.domain.core.repository.impl.*;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

@Repository(value="project-DomainConfigRepositoryImpl")
public class DomainConfigRepositoryImpl extends BaseRepositoryImpl<ProjectDomain.DomainConfigDomain,DomainConfigDO>  implements DomainConfigRepository {

    @Override
    public List<DomainConfigDO> convert2DO(List<ProjectDomain.DomainConfigDomain> list) {
        return ProjectConvertor.INSTANCE.convert2DomainConfigDO(list);
    }

    @Override
    public List<ProjectDomain.DomainConfigDomain> convert2DTO(List<DomainConfigDO> list) {
        return ProjectConvertor.INSTANCE.convert2DomainConfigDTO(list);
    }

    @Override
    public void convert2DTO(DomainConfigDO item ,ProjectDomain.DomainConfigDomain targetItem){
        ProjectConvertor.INSTANCE.convert2DomainConfigDTO(item,targetItem);
    }

    @Override
    public SFunction<ProjectDomain.DomainConfigDomain, Serializable> keyLambda() {
        return ProjectLambdaExp.domainConfigDomainKeyLambda;
    }
}
