package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ${basePackage!''}.common.*;

@Getter
@Setter
@ToString
public class ${NameUtils.getName(source.name)}PageRequest extends PageRequest {

}
