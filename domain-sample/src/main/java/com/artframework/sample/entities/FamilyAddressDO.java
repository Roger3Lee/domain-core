package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* family_address
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString(callSuper = true)
@TableName(value="family_address", autoResultMap = true)
public class FamilyAddressDO  {


    /**
    * 自增主键
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    /**
    * 关联家庭ID
    */
    @TableField("family_id")
    private Long familyId;
    /**
    * 冗余字段：家庭名称
    */
    @TableField("family_name")
    private String familyName;
    /**
    * 地址
    */
    @TableField("address_name")
    private String addressName;
}
