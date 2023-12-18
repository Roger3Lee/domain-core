package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* user_aggregate
*
* @author auto
* @version v1.0
* @date 2023-12-18 15:14:41
*/
@Getter
@Setter
@ToString
@TableName("user_aggregate")
public class UserAggregateDO  {


    /**
    * 自增主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
    * 用户ID
    */
    @TableField("user_id")
    private Integer userId;
    /**
    * 姓名
    */
    @TableField("famliy_member_count")
    private Integer famliyMemberCount;
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
