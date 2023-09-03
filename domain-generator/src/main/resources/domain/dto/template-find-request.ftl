package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;

@Getter
@Setter
@ToString
public class ${NameUtils.getName(source.name)}FindRequest {
    private ${source.keyType} key;
    private ${NameUtils.dataTOName(source.name)}.LoadFlag loadFlag;
}
