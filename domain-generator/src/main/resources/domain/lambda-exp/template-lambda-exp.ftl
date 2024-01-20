package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.lambdaexp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${tablePackage!''}.*;

import java.util.function.*;
import java.io.Serializable;

/**
* ${source.name}
*
* @author auto
* @version v1.0
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
    * RELATE ${relateTable.name} lambda
    */
    public static BiConsumer<${relateDtoClassName},${relateTable.fkTargetColumnType}> ${NameUtils.fieldTargetSetLambda(fieldName)} =${relateDtoClassName}::${NameUtils.genSetter(relateTable.fkTargetColumn)};

  /**
    * RELATE ${relateTable.name} lambda
    */
    public static SFunction<${relateDtoClassName},Serializable> ${NameUtils.fieldTargetDomainLambda(fieldName)} =${relateDtoClassName}::${NameUtils.genGetter(relateTable.fkTargetColumn)};


    /**
    * RELATE ${relateTable.name} lambda
    */
    public static SFunction<${relateClassName},Serializable> ${NameUtils.fieldTargetLambda(fieldName)} =${relateClassName}::${NameUtils.genGetter(relateTable.fkTargetColumn)};
</#list>

<#--    聚合-->
<#if source.aggregate??>
    <#assign relateClassName=NameUtils.dataObjectName(source.aggregate.name)/>
    <#assign relateDtoClassName=dtoClassName+"."+NameUtils.dataTOName(source.aggregate.name)/>
    <#assign fieldName=NameUtils.getFieldName(source.aggregate.name)/>

    /**
    *  ${source.aggregate.name} lambda
    */
    public static SFunction<${relateDtoClassName}, Serializable> ${NameUtils.fieldTargetKeyLambda(fieldName)} = ${relateDtoClassName}::${NameUtils.genGetter(source.aggregate.keyColName)};


    /**
    * RELATE ${source.aggregate.name} lambda
    */
    public static SFunction<${dtoClassName}, Serializable> ${NameUtils.fieldSourceLambda(fieldName)} = ${dtoClassName}::${NameUtils.genGetter(source.aggregate.fkSourceColumn)};


    /**
    * RELATE ${source.aggregate.name} lambda
    */
    public static BiConsumer<${relateDtoClassName},${source.aggregate.fkTargetColumnType}> ${NameUtils.fieldTargetSetLambda(fieldName)} =${relateDtoClassName}::${NameUtils.genSetter(source.aggregate.fkTargetColumn)};

    /**
    * RELATE ${relateTable.name} lambda
    */
    public static SFunction<${relateDtoClassName},Serializable> ${NameUtils.fieldTargetDomainLambda(fieldName)} =${relateDtoClassName}::${NameUtils.genGetter(relateTable.fkTargetColumn)};

    /**
    * RELATE ${source.aggregate.name} lambda
    */
    public static SFunction<${relateClassName},Serializable> ${NameUtils.fieldTargetLambda(fieldName)} =${relateClassName}::${NameUtils.genGetter(source.aggregate.fkTargetColumn)};
</#if>
</#if>
}
