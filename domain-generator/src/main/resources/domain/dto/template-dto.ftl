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
<#assign className=source.nameSuffix("DTO")/>
public class ${className}{
<#if source.mainTable??>
    <#list source.mainTable.column as column>
        /**
        * ${column.comment}
        */
        private ${column.type} ${column.nameFormat};
    </#list>

<#--    关联实体属性-->
    <#list source.relatedTable as relateTable>
        <#assign relateClassName=relateTable.nameSuffix("DO")/>
        <#assign fieldName=relateTable.nameCamelPrefix("related_")/>
        /**
        * RELATE ${relateTable.name}
        */
        private <#if relateTable.many>java.util.List<${relateClassName}> <#else>${relateClassName}</#if> ${fieldName};

        /**
        * RELATE ${relateTable.name} lambda
        */
        public static Function<${className}, ${relateTable.fkSourceColumnType}> ${fieldName}Source= ${className}::${relateTable.fkSourceColumnGet};

        /**
        * RELATE ${relateTable.name} lambda
        */
        public static Function<${relateClassName},${relateTable.fkTargetColumnType}> ${fieldName}Target=${relateClassName}::${relateTable.fkTargetColumnGet};
    </#list>

<#--    关联实体类-->
    <#list source.relatedTable as relateTable>
        <#assign relateClassName=relateTable.nameSuffix("DTO")/>
        <#assign fieldName=relateTable.nameCamelPrefix("related_")/>
        @Getter
        @Setter
        @ToString
        public static class ${relateClassName}{
        <#list relateTable.column as column>
            /**
            * ${column.comment}
            */
            private ${column.type} ${column.nameFormat};
        </#list>
        }
    </#list>
</#if>
}
