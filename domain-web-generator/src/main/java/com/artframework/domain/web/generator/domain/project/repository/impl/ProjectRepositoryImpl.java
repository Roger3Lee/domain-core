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

@Repository(value="project-ProjectRepositoryImpl")
public class ProjectRepositoryImpl extends BaseRepositoryImpl<ProjectDomain,ProjectDO>  implements ProjectRepository {

    @Override
    public List<ProjectDO> convert2DO(List<ProjectDomain> list) {
        return ProjectConvertor.INSTANCE.convert2DO(list);
    }

    @Override
    public List<ProjectDomain> convert2DTO(List<ProjectDO> list) {
        return ProjectConvertor.INSTANCE.convert2DTO(list);
    }

    @Override
    public void convert2DTO(ProjectDO item ,ProjectDomain targetItem){
        ProjectConvertor.INSTANCE.convert2DTO(item,targetItem);
    }

    @Override
    public SFunction<ProjectDomain, Serializable> keyLambda() {
        return ProjectLambdaExp.dtoKeyLambda;
    }
}
