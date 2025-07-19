package com.artframework.domain.web.generator.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* datasource_table_column
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@TableName(value="datasource_table_column", autoResultMap = true)
@KeySequence("seq_datasource_table_column_id")
public class DatasourceTableColumnDO  {


    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.INPUT)
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
    * 数据源ID
    */
    @TableField("ds_id")
    private Integer dsId;
    /**
    * 表名
    */
    @TableField("table_name")
    private String tableName;
    /**
    * 列名
    */
    @TableField("name")
    private String name;
    /**
    * 类型
    */
    @TableField("type")
    private String type;
    /**
    * 表描述
    */
    @TableField("comment")
    private String comment;
    /**
    * 是否是主键
    */
    @TableField("key")
    private String key;
}
