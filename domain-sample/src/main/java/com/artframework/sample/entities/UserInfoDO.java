package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* user_info
*
* @author auto
* @version v1.0
* @date 2023-9-10 0:42:56
*/
@Getter
@Setter
@ToString
@TableName("user_info")
public class UserInfoDO  extends BaseDO {


    /**
    * 主鍵
    */
    @TableId(value = "id", type = IdType.INPUT)
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
