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
@ToString
@TableName(value = "family", autoResultMap = true)
@KeySequence("seq_family_id")
public class FamilyDO {

    /**
     * 序列主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
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
}
