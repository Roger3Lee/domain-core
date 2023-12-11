package ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.convertor;

import ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;
import ${tablePackage!''}.entities.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

<#assign decoratorName=NameUtils.covertDecoratorName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign doClassName=NameUtils.dataObjectName(source.mainTable.name)/>
@Named("${decoratorName}")
public class ${decoratorName} {

    @BeforeMapping
    public void before(${dtoClassName} dtoRequest) {
    }

    @AfterMapping
    public void after(${dtoClassName} dtoRequest, @MappingTarget ${doClassName} doRequest) {
    }
}