package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.lambdaexp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;
import ${basePackage!''}.entities.*;

import java.util.function.*;
import java.io.Serializable;

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
<#assign className=NameUtils.lambdaExpName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign doClassName=NameUtils.dataObjectName(source.mainTable.name)/>
public class ${className}{
<#if source.mainTable??>
    <#if source.mainTable.keyColName??>
        /**
        * KEY ${relateTable.name} lambda
        */
        public static SFunction<${dtoClassName}, Serializable> dtoKeyLambda= ${dtoClassName}::${NameUtils.genGetter(source.mainTable.keyColName)};

        /**
        * KEY ${relateTable.name} lambda
        */
        public static SFunction<${doClassName}, Serializable> doKeyLambda= ${doClassName}::${NameUtils.genGetter(source.mainTable.keyColName)};
    </#if>

<#--    关联实体属性-->
    <#list source.relatedTable as relateTable>
        <#assign relateClassName=NameUtils.dataObjectName(relateTable.name)/>
        <#assign relateDtoClassName=dtoClassName+"."+NameUtils.dataTOName(relateTable.name)/>
        <#assign fieldName=NameUtils.getFieldName(relateTable.name)/>

        /**
        *  ${relateTable.name} lambda
        */
        public static SFunction<${relateDtoClassName}, Serializable> ${NameUtils.fieldTargetKeyLambda(fieldName)} = ${relateDtoClassName}::${NameUtils.genGetter(relateTable.keyColName)};


        /**
        * RELATE ${relateTable.name} lambda
        */
        public static SFunction<${dtoClassName}, Serializable> ${NameUtils.fieldSourceLambda(fieldName)} = ${dtoClassName}::${NameUtils.genGetter(relateTable.fkSourceColumn)};


        /**
        * RELATE user_address lambda
        */
        public static BiConsumer<${relateDtoClassName},${relateTable.fkTargetColumnType}> ${NameUtils.fieldTargetSetLambda(fieldName)} =${relateDtoClassName}::${NameUtils.genSetter(relateTable.fkTargetColumn)};

        /**
        * RELATE ${relateTable.name} lambda
        */
        public static SFunction<${relateClassName},Serializable> ${NameUtils.fieldTargetLambda(fieldName)} =${relateClassName}::${NameUtils.genGetter(relateTable.fkTargetColumn)};
    </#list>
</#if>
}
