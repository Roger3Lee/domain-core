
package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.convertor;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${tablePackage!''}.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

<#assign covertName=NameUtils.covertName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign doClassName=NameUtils.dataObjectName(source.mainTable.name)/>
<#assign mapperClassName=NameUtils.mapperName(source.mainTable.name)/>
<#assign mapperName=NameUtils.getFieldName(mapperClassName)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
<#assign repositoryImplClassName=NameUtils.repositoryImplName(source.name)/>
<#assign decoratorName=NameUtils.covertDecoratorName(source.name)/>
<#--MAPPER-->
@Mapper(uses = ${decoratorName}.class)
public interface  ${covertName}{
    ${domainName}Convertor INSTANCE= Mappers.getMapper(${domainName}Convertor.class);

<#list source.relatedTable as relateTable>
    <#assign fieldName=NameUtils.getFieldName(relateTable.name)/>
    <#assign fieldNameList=NameUtils.getListFieldName(relateTable.name)/>
    @Mapping(target = "<#if relateTable.many>${fieldNameList}<#else>${fieldName}</#if>", ignore = true)
</#list>
    ${dtoClassName} copy(${dtoClassName} request);
    ${dtoClassName} convert2DTO(${doClassName} request);
    void convert2DTO(${doClassName} request, @MappingTarget ${dtoClassName} target);
    List<${dtoClassName}> convert2DTO(List<${doClassName}> request);


    @BeanMapping(qualifiedByName = { "${decoratorName}"})
    ${doClassName} convert2DO(${dtoClassName} request);
    List<${doClassName}> convert2DO(List<${dtoClassName}> request);

    <#list source.relatedTable as relateTable>
    <#assign relateDTOClassName= NameUtils.dataTOName(relateTable.name)/>
    <#assign relateDOClassName= NameUtils.dataObjectName(relateTable.tableName)/>
    <#assign relateName= NameUtils.getName(relateTable.name)/>
    ${dtoClassName}.${relateDTOClassName} convert2${relateName}DTO(${relateDOClassName} request);
    void convert2${relateName}DTO(${relateDOClassName} request, @MappingTarget ${dtoClassName}.${relateDTOClassName} target);
    List<${dtoClassName}.${relateDTOClassName}> convert2${relateName}DTO(List<${relateDOClassName}>  request);
    @BeanMapping(qualifiedByName = { "${decoratorName}"})
    ${relateDOClassName} convert2${relateName}DO(${dtoClassName}.${relateDTOClassName} request);
    List<${relateDOClassName}> convert2${relateName}DO(List<${dtoClassName}.${relateDTOClassName}>  request);
    </#list>
}