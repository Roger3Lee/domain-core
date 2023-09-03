package ${basePackage!''}.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ${basePackage!''}.domain.dto.*;

@Getter
@Setter
@ToString
public class ${NameUtils.getName(source.name)}UpdateRequest {
    private ${NameUtils.dataTOName(source.name)} entity;
    private ${NameUtils.dataTOName(source.name)}.LoadFlag loadFlag;
}
