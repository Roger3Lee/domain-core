package ${basePackage!''}.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.function.Function;
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
public class ${className}{
<#if source.mainTable??>
    <#list source.mainTable.column as column>
        <#if column.key>
        /**
        * KEY ${relateTable.name} lambda
        */
        public static Function<${className}, ${column.type}> keyLambda= ${className}::${NameUtils.genGetter(column.name)};
        </#if>

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

        /**
        * RELATE ${relateTable.name} lambda
        */
        public static Function<${className}, ${relateTable.fkSourceColumnType}> ${NameUtils.fieldSourceLambda(fieldName)} = ${className}::${NameUtils.genGetter(relateTable.fkSourceColumn)};

        /**
        * RELATE ${relateTable.name} lambda
        */
        public static Function<${relateClassName},${relateTable.fkTargetColumnType}> ${NameUtils.fieldTargetLambda(fieldName)} =${relateClassName}::${NameUtils.genGetter(relateTable.fkTargetColumn)};
    </#list>

<#--    关联实体类-->
    <#list source.relatedTable as relateTable>
        <#assign relateClassName= NameUtils.dataTOName(relateTable.name)/>
        @Getter
        @Setter
        @ToString
        public static class ${relateClassName}{
        <#list relateTable.column as column>
            /**
            * ${column.comment}
            */
            private ${column.type} ${NameUtils.getFieldName(column.name)};
        </#list>
        }
    </#list>
</#if>
}
