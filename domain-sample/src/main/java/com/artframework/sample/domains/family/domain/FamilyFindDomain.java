package com.artframework.sample.domains.family.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilyFindDomain {
    private Long key;
    /**
    * 默认加载所有
    */
    @Builder.Default
    private FamilyDomain.LoadFlag loadFlag = new FamilyDomain.LoadFlag();
}
