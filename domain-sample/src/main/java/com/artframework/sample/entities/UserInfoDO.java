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
* @date 2023-12-11 16:12:26
*/
@Getter
@Setter
@ToString
@TableName("user_info")
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
    /**
    * 家庭成員總數
    */
    @TableField("family_member_count")
    private java.lang.Integer familyMemberCount;
}
