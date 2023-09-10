package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* user_family_member
*
* @author auto
* @version v1.0
* @date 2023-9-10 10:32:39
*/
@Getter
@Setter
@ToString
@TableName("user_family_member")
public class UserFamilyMemberDO  extends BaseDO {


    /**
    * 主鍵
    */
    @TableId(value = "id", type = IdType.AUTO)
    private java.lang.Long id;
    /**
    * 唯一键
    */
    @TableField("user_id")
    private java.lang.Long userId;
    /**
    * 姓名
    */
    @TableField("name")
    private java.lang.String name;
    /**
    * 家属关系
    */
    @TableField("rel_type")
    private java.lang.String relType;
}
