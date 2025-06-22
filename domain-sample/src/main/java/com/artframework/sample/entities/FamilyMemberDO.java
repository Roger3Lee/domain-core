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
@ToString
@TableName(value = "family_member", autoResultMap = true)
@KeySequence("seq_family_member_id")
public class FamilyMemberDO {
    /**
     * 序列主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    /**
     * 家庭ID
     */
    @TableField("family_id")
    private Integer familyId;
    /**
     * 家庭名称
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
