package io.github.roger3lee.sample.domains.family.convertor;

import io.github.roger3lee.sample.domains.family.domain.*;
import io.github.roger3lee.sample.entities.*;
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
