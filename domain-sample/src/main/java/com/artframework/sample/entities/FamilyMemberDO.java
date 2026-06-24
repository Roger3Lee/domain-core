package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* family_member
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString(callSuper = true)
@TableName(value="family_member", autoResultMap = true)
public class FamilyMemberDO  {


    /**
    * 自增主键
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    /**
    * 家庭ID
    */
    @TableField("family_id")
    private Long familyId;
    /**
    * 冗余字段：家庭名称
    */
    @TableField("family_name")
    private String familyName;
    /**
    * 姓名
    */
    @TableField("name")
    private String name;
    /**
    * 电话
    */
    @TableField("phone")
    private String phone;
    /**
    * 成员关系
    */
    @TableField("type")
    private String type;
}
