package ${basePackage!''}.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageRequest {
    private Long pageSize;
    private Long pageNum;
}
