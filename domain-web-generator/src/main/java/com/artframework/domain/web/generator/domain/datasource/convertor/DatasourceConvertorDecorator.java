package com.artframework.domain.web.generator.domain.datasource.convertor;

import com.artframework.domain.web.generator.domain.datasource.domain.*;
import com.artframework.domain.web.generator.dataobject.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Named("DatasourceConvertorDecorator")
public class DatasourceConvertorDecorator {

    @BeforeMapping
    public void before(DatasourceDomain dtoRequest) {
    }

    @AfterMapping
    public void after(DatasourceDomain dtoRequest, @MappingTarget DatasourceConfigDO doRequest) {
    }
}
