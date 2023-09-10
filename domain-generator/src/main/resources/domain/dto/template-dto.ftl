package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto;

import com.artframework.domain.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import ${basePackage!''}.entities.*;

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
    /**
    * 是否有变化
    */
    private Boolean changed = false;

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
    /**
    * RELATE ${relateTable.name}
    */
    private <#if relateTable.many>java.util.List<${relateDtoClassName}> <#else>${relateDtoClassName}</#if> ${fieldName};
    </#list>

    /**
    * 加载数据对象
    */
    private LoadFlag loadFlag = new LoadFlag();

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

    @Getter
    @Setter
    @ToString
    public static class LoadFlag{
    <#list source.relatedTable as relateTable>

        /**
        *
        */
        private Boolean ${NameUtils.getFieldWithPrefix(relateTable.name,"load")} = true;
        </#list>
    }
</#if>
}
