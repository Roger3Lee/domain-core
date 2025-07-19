package com.artframework.domain.web.generator.domain.project.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectFindDomain {
    private Serializable key;
    /**
    * 默认加载所有
    */
    @Builder.Default
    private ProjectDomain.LoadFlag loadFlag = new ProjectDomain.LoadFlag();
}
