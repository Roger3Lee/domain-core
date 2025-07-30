package com.artframework.domain.web.generator.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* domain_config_tables
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@TableName(value="domain_config_tables", autoResultMap = true)
//@KeySequence("seq_domain_config_tables_id")
public class DomainConfigTablesDO  {


    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
    * 项目ID
    */
    @TableField("project_id")
    private Integer projectId;
    /**
    * 领域ID
    */
    @TableField("domain_id")
    private Integer domainId;
    /**
    * 创建人
    */
    @TableField("created_by")
    private String createdBy;
    /**
    * 创建时间
    */
    @TableField("created_time")
    private java.time.LocalDateTime createdTime;
    /**
    * 更新人
    */
    @TableField("updated_by")
    private String updatedBy;
    /**
    * 更新时间
    */
    @TableField("updated_time")
    private java.time.LocalDateTime updatedTime;
    /**
    * 表名
    */
    @TableField("table_name")
    private String tableName;
    /**
    * 位置X
    */
    @TableField("x")
    private java.math.BigDecimal x;
    /**
    * 位置Y
    */
    @TableField("y")
    private java.math.BigDecimal y;
    /**
    * 宽度
    */
    @TableField("w")
    private java.math.BigDecimal w;
    /**
    * 高度
    */
    @TableField("h")
    private java.math.BigDecimal h;
}
