package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.convertor;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${tablePackage!''}.*;
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
 <#if source.aggregate??>
    <#assign relateDTOClassName= NameUtils.dataTOName(source.aggregate.name)/>
    <#assign relateDOClassName= NameUtils.dataObjectName(source.aggregate.name)/>
    <#assign relateName= NameUtils.getName(source.aggregate.name)/>
    public static void convertAggregate(${dtoClassName} dtoRequest, @MappingTarget ${dtoClassName}.${relateDTOClassName} aggregateDTO){
    }
</#if>
}