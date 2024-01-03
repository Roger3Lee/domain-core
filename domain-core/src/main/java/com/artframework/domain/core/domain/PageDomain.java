package com.artframework.domain.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageDomain {
    private Long pageSize;
    private Long pageNum;
}
