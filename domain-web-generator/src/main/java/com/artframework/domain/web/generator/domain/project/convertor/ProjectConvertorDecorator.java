package com.artframework.domain.web.generator.domain.project.convertor;

import com.artframework.domain.web.generator.domain.project.domain.*;
import com.artframework.domain.web.generator.dataobject.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Named("ProjectConvertorDecorator")
public class ProjectConvertorDecorator {

    @BeforeMapping
    public void before(ProjectDomain dtoRequest) {
    }

    @AfterMapping
    public void after(ProjectDomain dtoRequest, @MappingTarget ProjectDO doRequest) {
    }
}
