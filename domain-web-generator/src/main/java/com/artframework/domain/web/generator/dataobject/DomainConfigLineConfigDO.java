package com.artframework.domain.web.generator.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* domain_config_line_config
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@TableName(value="domain_config_line_config", autoResultMap = true)
//@KeySequence("seq_domain_config_line_config_id")
public class DomainConfigLineConfigDO  {


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
    * 连线的编码
    */
    @TableField("line_code")
    private String lineCode;
    /**
    * 源表列
    */
    @TableField("source_colunm")
    private String sourceColunm;
    /**
    * 源表列的值（在常量关联时使用）
    */
    @TableField("source_column_value")
    private String sourceColumnValue;
    /**
    * 源表列值的类型（在常量关联时使用）
    */
    @TableField("source_column_value_data_type")
    private String sourceColumnValueDataType;
    /**
    * 目标列
    */
    @TableField("target_colunm")
    private String targetColunm;
    /**
    * 目标表列的值（在常量关联时使用）
    */
    @TableField("target_column_value")
    private String targetColumnValue;
    /**
    * 目标列值的类型（在常量关联时使用）
    */
    @TableField("target_column_value_data_type")
    private String targetColumnValueDataType;
}
