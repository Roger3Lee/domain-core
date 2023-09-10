package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* user_address
*
* @author auto
* @version v1.0
* @date 2023-9-10 10:32:39
*/
@Getter
@Setter
@ToString
@TableName("user_address")
public class UserAddressDO  extends BaseDO {


    /**
    * 主鍵
    */
    @TableId(value = "id", type = IdType.AUTO)
    private java.lang.Long id;
    /**
    * 关联用户
    */
    @TableField("user_id")
    private java.lang.Long userId;
    /**
    * 地址
    */
    @TableField("address_name")
    private java.lang.String addressName;
}
