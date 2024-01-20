package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain;

import ${corePackage}.domain.*;
<#if source.aggregate??>
import com.fasterxml.jackson.annotation.JsonIgnore;
</#if>
import lombok.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
* ${source.name}
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@ApiModel(value = "${source.description}")
<#assign className=NameUtils.dataTOName(source.name)/>
public class ${className} extends BaseDomain<#if (source.implement??) && (source.implement!='')> implements ${source.implement}</#if> {
<#if source.mainTable??>
    <#list source.mainTable.column as column>
    /**
    * ${column.comment}
    */
    @ApiModelProperty(value =  "${column.comment}")
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
    @ApiModelProperty(value =  "RELATE ${relateTable.name}")
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
    @ApiModelProperty(value =  "aggregate ${source.aggregate.name} ,不需要序列化給接口輸出")
    private ${relateDtoClassName} ${fieldName};
    </#if>

    /**
    * 加载数据標識類
    */
    @ApiModelProperty(value =  "加载数据標識類")
    private LoadFlag loadFlag;

<#--    关联实体类-->
    <#list source.relatedTable as relateTable>
    <#assign relateClassName= NameUtils.dataTOName(relateTable.name)/>
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ${relateClassName} extends BaseDomain<#if (relateTable.implement??) && (relateTable.implement!='')> implements ${relateTable.implement}</#if>{
    <#list relateTable.column as column>
        /**
        * ${column.comment}
        */
        @ApiModelProperty(value =  "${column.comment}")
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
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ${relateClassName} extends BaseDomain<#if (source.aggregate.implement??) && (source.aggregate.implement!='')> implements ${source.aggregate.implement}</#if>{
    <#list source.aggregate.column as column>
        /**
        * ${column.comment}
        */
        @ApiModelProperty(value =  "${column.comment}")
        private ${column.type} ${NameUtils.getFieldName(column.name)};
    </#list>
    }
</#if>

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoadFlag extends BaseLoadFlag{
        /**
        * 加載所有數據， 謹慎使用
        */
        @ApiModelProperty(value =  "加載所有數據， 謹慎使用")
        private Boolean loadAll;
    <#list source.relatedTable as relateTable>

        /**
        * 加載${NameUtils.dataTOName(relateTable.name)}
        */
        @ApiModelProperty(value =  "加載${NameUtils.dataTOName(relateTable.name)}")
        private Boolean ${NameUtils.getFieldWithPrefix(relateTable.name,"load")};
    </#list>
    }
</#if>
}
