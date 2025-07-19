package com.artframework.domain.web.generator.domain.ddd.convertor;

import com.artframework.domain.web.generator.domain.ddd.domain.*;
import com.artframework.domain.web.generator.dataobject.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Named("DDDConvertorDecorator")
public class DDDConvertorDecorator {

    @BeforeMapping
    public void before(DDDDomain dtoRequest) {
    }

    @AfterMapping
    public void after(DDDDomain dtoRequest, @MappingTarget DomainConfigDO doRequest) {
    }
}
