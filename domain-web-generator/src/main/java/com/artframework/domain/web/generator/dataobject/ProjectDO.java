package com.artframework.domain.web.generator.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* project
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@TableName(value="project", autoResultMap = true)
//@KeySequence("seq_project_id")
public class ProjectDO  {


    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
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
    * 项目名称
    */
    @TableField("name")
    private String name;
    /**
    * 领域package
    */
    @TableField("domain_package")
    private String domainPackage;
    /**
    * 控制器package
    */
    @TableField("controller_package")
    private String controllerPackage;
    /**
    * DO package
    */
    @TableField("do_package")
    private String doPackage;
    /**
    * Mapper package
    */
    @TableField("mapper_package")
    private String mapperPackage;
}
