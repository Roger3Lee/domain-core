package ${basePackage!''}.entities;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class ${NameUtils.dataObjectName(source.name)} <#if source.inherit??> extends ${NameUtils.dataObjectName(source.inherit)}</#if> {

<#list source.column as column>
    /**
    * ${column.comment}
    */
    <#if source.key>
    @TableId(value = "${column.name}", type = IdType.<#if source.keyGenerator>AUTO<#else>INPUT</#if>)
    <#else>
    @TableField("${column.name}")
    </#if>
    private ${column.type} ${column.nameFormat};
</#list>
}
