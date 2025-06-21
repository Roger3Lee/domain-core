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
@ToString
@TableName(value = "family_address", autoResultMap = true)
@KeySequence("seq_family_address_id")
public class FamilyAddressDO {

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;
    /**
     * 关联用户
     */
    @TableField("family_id")
    private Integer familyId;
    /**
     * 家庭名称
     */
    @TableField("family_name")
    private String familyName;
    /**
     * 地址
     */
    @TableField("address_name")
    private String addressName;
}
