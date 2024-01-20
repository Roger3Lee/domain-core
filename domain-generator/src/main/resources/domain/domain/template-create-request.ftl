package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ${NameUtils.getName(source.name)}CreateDomain extends ${NameUtils.dataTOName(source.name)} {
}
