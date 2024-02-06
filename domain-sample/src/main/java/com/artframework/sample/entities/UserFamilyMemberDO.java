package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* user_family_member
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@TableName(value="user_family_member", autoResultMap = true)
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
