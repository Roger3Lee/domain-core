package io.github.roger3lee.domain.core.domain;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDomain {
    private Long pageSize;
    private Long pageNum;
}
