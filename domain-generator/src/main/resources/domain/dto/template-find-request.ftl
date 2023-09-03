package ${basePackage!''}.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ${basePackage!''}.domain.dto.*;

@Getter
@Setter
@ToString
public class ${NameUtils.getName(source.name)}FindRequest {
    private ${source.keyType} key;
    private ${NameUtils.dataTOName(source.name)}.LoadFlag loadFlag;
}
