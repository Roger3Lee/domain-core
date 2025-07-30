package com.artframework.domain.web.generator.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* domain_config
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@TableName(value="domain_config", autoResultMap = true)
//@KeySequence("seq_domain_config_id")
public class DomainConfigDO  {


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
    * 乐观锁
    */
    @TableField("revision")
    @Version
    private Integer revision;
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
    * 领域名称
    */
    @TableField("domain_name")
    private String domainName;
    /**
    * 领域模型XML
    */
    @TableField("domain_xml")
    private String domainXml;
    /**
    * 主表
    */
    @TableField("main_table")
    private String mainTable;
    /**
    * 目录
    */
    @TableField("folder")
    private String folder;
}
