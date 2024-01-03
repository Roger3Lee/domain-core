package com.artframework.sample.domains.user.convertor;

import com.artframework.sample.domains.user.domain.*;
import com.artframework.sample.entities.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Named("UserConvertorDecorator")
public class UserConvertorDecorator {

    @BeforeMapping
    public void before(UserDomain dtoRequest) {
    }

    @AfterMapping
    public void after(UserDomain dtoRequest, @MappingTarget UserInfoDO doRequest) {
    }
}
