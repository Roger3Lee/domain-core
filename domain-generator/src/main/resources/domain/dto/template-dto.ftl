package ${domainPackage!''}.${NameUtils.packageName(source.name)}.dto;

import com.artframework.domain.core.dto.*;
<#if source.aggregate??>
import com.fasterxml.jackson.annotation.JsonIgnore;
</#if>
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
* ${source.name}
*
* @author auto
* @version v1.0
* @date ${.now}
*/
@Getter
@Setter
@ToString
<#assign className=NameUtils.dataTOName(source.name)/>
<#assign relateDtoClassName=NameUtils.dataTOName(relateTable.name)/>
public class ${className} extends BaseDTO {
<#if source.mainTable??>
    <#list source.mainTable.column as column>
    /**
    * ${column.comment}
    */
    private ${column.type} ${NameUtils.getFieldName(column.name)};
    </#list>

<#--    关联实体属性-->
    <#list source.relatedTable as relateTable>
        <#assign relateClassName=NameUtils.dataObjectName(relateTable.name)/>
        <#assign relateDtoClassName=NameUtils.dataTOName(relateTable.name)/>
        <#assign fieldName=NameUtils.getFieldName(relateTable.name)/>
        <#assign fieldNameList=NameUtils.getListFieldName(relateTable.name)/>
    /**
    * RELATE ${relateTable.name}
    */
    private <#if relateTable.many>java.util.List<${relateDtoClassName}> ${fieldNameList};<#else>${relateDtoClassName} ${fieldName};</#if>
    </#list>

<#--    聚合-->
    <#if source.aggregate??>
        <#assign relateDtoClassName=NameUtils.dataTOName(source.aggregate.name)/>
        <#assign fieldName=NameUtils.getFieldName(source.aggregate.name)/>
    /**
    * aggregate ${source.aggregate.name} ,不需要序列化給接口輸出
    */
    @JsonIgnore
    private ${relateDtoClassName} ${fieldName};
    </#if>

    /**
    * 加载数据对象
    */
    private LoadFlag loadFlag;

<#--    关联实体类-->
    <#list source.relatedTable as relateTable>
    <#assign relateClassName= NameUtils.dataTOName(relateTable.name)/>
    @Getter
    @Setter
    @ToString
    public static class ${relateClassName} extends BaseDTO{
        /**
        * 是否有变化
        */
        private Boolean changed = false;

    <#list relateTable.column as column>
        /**
        * ${column.comment}
        */
        private ${column.type} ${NameUtils.getFieldName(column.name)};
    </#list>
    }
    </#list>

<#--    聚合-->
<#if source.aggregate??>
    <#assign relateClassName= NameUtils.dataTOName(source.aggregate.name)/>
    @Getter
    @Setter
    @ToString
    public static class ${relateClassName} extends BaseDTO{
    <#list source.aggregate.column as column>
        /**
        * ${column.comment}
        */
        private ${column.type} ${NameUtils.getFieldName(column.name)};
    </#list>
    }
</#if>

    @Getter
    @Setter
    @ToString
    public static class LoadFlag extends BaseLoadFlag{
    <#list source.relatedTable as relateTable>

        /**
        *
        */
        private Boolean ${NameUtils.getFieldWithPrefix(relateTable.name,"load")} = false;
    </#list>
    }
</#if>
}
