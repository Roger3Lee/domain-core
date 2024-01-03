package com.artframework.sample.domains.user.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserFindDomain {
    private Serializable key;

    /**
    * 默认加载所有
    */
    private UserDomain.LoadFlag loadFlag = new UserDomain.LoadFlag();
}
