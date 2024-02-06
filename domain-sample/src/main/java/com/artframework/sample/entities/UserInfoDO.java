package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* user_info
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@TableName(value="user_info", autoResultMap = true)
public class UserInfoDO  extends BaseDO {


    /**
    * 主鍵
    */
    @TableId(value = "id", type = IdType.AUTO)
    private java.lang.Long id;
    /**
    * 名字
    */
    @TableField("name")
    private java.lang.String name;
    /**
    * 手机
    */
    @TableField("phone")
    private java.lang.String phone;
}
