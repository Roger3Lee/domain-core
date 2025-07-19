package com.artframework.domain.web.generator.domain.ddd.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DDDFindDomain {
    private Serializable key;
    /**
    * 默认加载所有
    */
    @Builder.Default
    private DDDDomain.LoadFlag loadFlag = new DDDDomain.LoadFlag();
}
