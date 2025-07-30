package com.artframework.domain.web.generator.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* datasource_config
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@TableName(value="datasource_config", autoResultMap = true)
//@KeySequence("seq_datasource_config_id")
public class DatasourceConfigDO  {


    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
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
    * 数据源名称
    */
    @TableField("name")
    private String name;
    /**
    * 编码
    */
    @TableField("code")
    private String code;
    /**
    * 数据库类型
    */
    @TableField("db_type")
    private String dbType;
    /**
    * 数据库连接
    */
    @TableField("url")
    private String url;
    /**
    * 数据库用户
    */
    @TableField("user_name")
    private String userName;
    /**
    * 密码
    */
    @TableField("password")
    private String password;
    /**
    * schema
    */
    @TableField("schema")
    private String schema;
}
