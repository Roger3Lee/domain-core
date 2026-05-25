package com.artframework.sample.domains.family.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilyFindDomain {
    private Serializable key;
    /**
    * 默认加载所有
    */
    @Builder.Default
    private FamilyDomain.LoadFlag loadFlag = new FamilyDomain.LoadFlag();
}
