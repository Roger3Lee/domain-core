package com.artframework.sample.domains.family.convertor;

import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.entities.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Named("FamilyConvertorDecorator")
public class FamilyConvertorDecorator {

    @BeforeMapping
    public void before(FamilyDomain dtoRequest) {
    }

    @AfterMapping
    public void after(FamilyDomain dtoRequest, @MappingTarget FamilyDO doRequest) {
    }
}
