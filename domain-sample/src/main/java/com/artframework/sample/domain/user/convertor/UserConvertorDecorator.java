package com.artframework.sample.domain.user.convertor;

import cn.hutool.core.collection.CollectionUtil;
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
        if (null != dtoRequest.getLoadFlag() && dtoRequest.getLoadFlag().getLoadUserFamilyMember()) {
            if (CollectionUtil.isNotEmpty(dtoRequest.getUserFamilyMember())) {
                dtoRequest.setFamilyMemberCount(dtoRequest.getUserFamilyMember().size());
                dtoRequest.setChanged(true);
            } else {
                dtoRequest.setFamilyMemberCount(0);
                dtoRequest.setChanged(true);
            }
        }
    }

    @AfterMapping
    public void after(UserDTO dtoRequest, @MappingTarget UserInfoDO doRequest) {
    }
}
