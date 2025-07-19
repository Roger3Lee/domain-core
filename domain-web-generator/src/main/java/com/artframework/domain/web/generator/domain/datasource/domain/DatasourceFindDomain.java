package com.artframework.domain.web.generator.domain.datasource.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatasourceFindDomain {
    private Serializable key;
    /**
    * 默认加载所有
    */
    @Builder.Default
    private DatasourceDomain.LoadFlag loadFlag = new DatasourceDomain.LoadFlag();
}
