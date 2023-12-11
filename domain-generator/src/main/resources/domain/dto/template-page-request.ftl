package ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request;

import com.artframework.domain.core.dto.request.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ${NameUtils.getName(source.name)}PageRequest extends PageRequest {

}
