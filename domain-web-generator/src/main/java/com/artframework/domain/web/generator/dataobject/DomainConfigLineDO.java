package com.artframework.domain.web.generator.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* domain_config_line
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@TableName(value="domain_config_line", autoResultMap = true)
@KeySequence("seq_domain_config_line_id")
public class DomainConfigLineDO  {


    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.INPUT)
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
    * 线条的类型;FK ：外键  REDUNDANCY:冗余
    */
    @TableField("line_type")
    private String lineType;
    /**
    * 源表
    */
    @TableField("source_table")
    private String sourceTable;
    /**
    * 源表列
    */
    @TableField("source_colunm")
    private String sourceColunm;
    /**
    * 目标表
    */
    @TableField("target_table")
    private String targetTable;
    /**
    * 目标列
    */
    @TableField("target_colunm")
    private String targetColunm;
    /**
    * 是否一对多
    */
    @TableField("many")
    private String many;
}
