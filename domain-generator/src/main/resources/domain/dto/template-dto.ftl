package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto;

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
public class ${className}{
<#if source.mainTable??>
        /**
        * 是否有变化
        */
        private Boolean changed;

    <#if column.key>
        /**
        * KEY ${relateTable.name} lambda
        */
        public static Function<${className}, ${column.type}> keyLambda= ${className}::${NameUtils.genGetter(column.name)};
    </#if>

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

        /**
        * RELATE ${relateTable.name} lambda
        */
        public static SFunction<${className}, ${relateTable.fkSourceColumnType}> ${NameUtils.fieldSourceLambda(fieldName)} = ${className}::${NameUtils.genGetter(relateTable.fkSourceColumn)};

        /**
        * RELATE ${relateTable.name} lambda
        */
        public static SFunction<${relateClassName},${relateTable.fkTargetColumnType}> ${NameUtils.fieldTargetLambda(fieldName)} =${relateClassName}::${NameUtils.genGetter(relateTable.fkTargetColumn)};
    </#list>

<#--    关联实体类-->
    <#list source.relatedTable as relateTable>
        <#assign relateClassName= NameUtils.dataTOName(relateTable.name)/>
        @Getter
        @Setter
        @ToString
        public static class ${relateClassName}{
            /**
            * 是否有变化
            */
            private Boolean changed;

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
            private Boolean ${NameUtils.getFieldWithPrefix(relateTable.name,"load")};
        </#list>
        }
</#if>
}
