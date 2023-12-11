package ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;

@Getter
@Setter
@ToString
public class ${NameUtils.getName(source.name)}UpdateRequest extends ${NameUtils.dataTOName(source.name)}{
}
