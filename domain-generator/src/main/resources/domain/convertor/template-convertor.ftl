
package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.convertor;

import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;
import ${basePackage!''}.entities.*;
import ${basePackage!''}.mappers.*;

import java.util.List;

@Repository
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign doClassName=NameUtils.dataObjectName(source.mainTable.name)/>
<#assign mapperClassName=NameUtils.mapperName(source.mainTable.name)/>
<#assign mapperName=NameUtils.getFieldName(mapperClassName)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
<#assign repositoryImplClassName=NameUtils.repositoryImplName(source.name)/>

<#--MAPPER-->
@Mapper
public interface  ${domainName}Convertor{
${domainName}Convertor INSTANCE= Mappers.getMapper(${domainName}Convertor.class);

    ${dtoClassName} convert2DTO(${doClassName} request);
    ${doClassName} convert2DO(${dtoClassName} request);

    <#list source.relatedTable as relateTable>
        <#assign relateDTOClassName= NameUtils.dataTOName(relateTable.name)/>
        <#assign relateDOClassName= NameUtils.dataObjectName(relateTable.name)/>
        ${dtoClassName}.${relateDTOClassName} convert2DTO(${relateDOClassName} request);
        List<${dtoClassName}.${relateDTOClassName}> convert2DTO(List<${relateDOClassName}>  request);
        ${relateDOClassName} convert2DO(${dtoClassName}.${relateDTOClassName} request);
        List<${relateDOClassName}> convert2DO(List<${dtoClassName}.${relateDTOClassName}>  request);
    </#list>
}