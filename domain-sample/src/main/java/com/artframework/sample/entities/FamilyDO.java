package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* family
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString(callSuper = true)
@TableName(value="family", autoResultMap = true)
public class FamilyDO  {


    /**
    * 自增主键
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    /**
    * 名称
    */
    @TableField("name")
    private String name;
    /**
    * 家庭成员数量
    */
    @TableField("person_count")
    private Integer personCount;
    /**
    * 户主姓名
    */
    @TableField("householder")
    private String householder;
}
