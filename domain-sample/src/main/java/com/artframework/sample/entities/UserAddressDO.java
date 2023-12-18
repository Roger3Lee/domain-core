package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* user_address
*
* @author auto
* @version v1.0
* @date 2023-12-18 15:14:41
*/
@Getter
@Setter
@ToString
@TableName("user_address")
public class UserAddressDO  {


    /**
    * 自增主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
    * 关联用户
    */
    @TableField("user_id")
    private Integer userId;
    /**
    * 地址
    */
    @TableField("address_name")
    private String addressName;
    /**
    * 创建人
    */
    @TableField("created_by")
    private String createdBy;
    /**
    * 创建时间
    */
    @TableField("created_time")
    private java.util.Date createdTime;
    /**
    * 更新人
    */
    @TableField("updated_by")
    private String updatedBy;
    /**
    * 更新时间
    */
    @TableField("updated_time")
    private java.util.Date updatedTime;
}
