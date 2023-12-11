package ${tablePackage!''}.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
<#if !source.basic>
@TableName("${source.name}")
</#if>
<#if source.keyGenerator>
@KeySequence("seq_${source.name}_id")
</#if>
<#assign className=NameUtils.dataObjectName(source.name)/>
public class ${className} <#if source.inherit??> extends ${NameUtils.dataObjectName(source.inherit)}</#if> {

<#--<#if !source.basic>-->
<#--    /**-->
<#--    * KEY ${source.name} lambda-->
<#--    */-->
<#--    public static SFunction<${className}, ${source.mainTable.keyType}> keyLambda= ${className}::${NameUtils.genGetter(source.keyName)};-->
<#--</#if>-->

<#list source.column as column>
    /**
    * ${column.comment}
    */
    <#if column.key>
    @TableId(value = "${column.name}", type = IdType.<#if column.keyGenerator>AUTO<#else>INPUT</#if>)
    <#else>
    @TableField("${column.name}")
    </#if>
    private ${column.type} ${NameUtils.getFieldName(column.name)};
</#list>
}
