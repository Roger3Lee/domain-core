package com.artframework.sample.domain.user.dto.request;

import lombok.*;

import java.io.Serializable;

import com.artframework.sample.domain.user.dto.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserFindRequest {
    private Serializable key;

    /**
    * 默认加载所有
    */
    private UserDTO.LoadFlag loadFlag = new UserDTO.LoadFlag();
}
