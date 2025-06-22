package ${tablePackage!''};

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* ${source.name}
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
<#if !source.basic>
@TableName(value="${source.name}", autoResultMap = true)
</#if>
<#if (source.keyGenerator==false)>
@KeySequence("seq_${source.name}_id")
</#if>
<#assign className=NameUtils.dataObjectName(source.name)/>
public class ${className} <#if source.inheritBaseEntity>extends ${source.baseEntity}<#else><#if source.inherit??> extends ${NameUtils.dataObjectName(source.inherit)}</#if></#if> {

<#--<#if !source.basic>-->
<#--    /**-->
<#--    * KEY ${source.name} lambda-->
<#--    */-->
<#--    public static SFunction<${className}, ${source.mainTable.keyType}> keyLambda= ${className}::${NameUtils.genGetter(source.keyName)};-->
<#--</#if>-->

<#list source.column as column>
    <#if !(column.inherit)>
    /**
    * ${column.comment}
    */
    <#if column.key>
    @TableId(value = "${column.name}", type = IdType.<#if source.keyGenerator>AUTO<#else>INPUT</#if>)
    <#else>
    @TableField("${column.name}")
    </#if>
    private ${column.type} ${NameUtils.getFieldName(column.name)};
    </#if>
</#list>
}
