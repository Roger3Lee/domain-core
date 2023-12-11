package com.artframework.sample.domain.user.convertor;

import com.artframework.sample.domain.user.dto.*;
import com.artframework.sample.entities.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Named("UserConvertorDecorator")
public class UserConvertorDecorator {

    @BeforeMapping
    public void before(UserDTO dtoRequest) {
    }

    @AfterMapping
    public void after(UserDTO dtoRequest, @MappingTarget UserInfoDO doRequest) {
    }
}
