package com.artframework.sample.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* base
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
@KeySequence("seq_base_id")
public class BaseDO  {


    /**
    * 創建時間
    */
    @TableField("CREATED_TIME")
    private java.util.Date createdTime;
    /**
    * 創建用戶
    */
    @TableField("CREATED_BY")
    private java.lang.String createdBy;
    /**
    * 創建時間
    */
    @TableField("UPDATED_TIME")
    private java.util.Date updatedTime;
    /**
    * 創建用戶
    */
    @TableField("UPDATED_BY")
    private java.lang.String updatedBy;
}
