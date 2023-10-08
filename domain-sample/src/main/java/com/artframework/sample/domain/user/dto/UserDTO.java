package com.artframework.sample.domain.user.dto;

import com.artframework.domain.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.sample.entities.*;

/**
* user
*
* @author auto
* @version v1.0
* @date 2023-10-8 16:51:25
*/
@Getter
@Setter
@ToString
public class UserDTO extends BaseDTO {
    /**
    * 主鍵
    */
    private java.lang.Long id;
    /**
    * 名字
    */
    private java.lang.String name;
    /**
    * 手机
    */
    private java.lang.String phone;
    /**
    * 家庭成員總數
    */
    private java.lang.Integer familyMemberCount;
    /**
    * 創建時間
    */
    private java.util.Date createdTime;
    /**
    * 創建用戶
    */
    private java.lang.String createdBy;
    /**
    * 創建時間
    */
    private java.util.Date updatedTime;
    /**
    * 創建用戶
    */
    private java.lang.String updatedBy;

    /**
    * RELATE user_address
    */
    private UserAddressDTO userAddress;
    /**
    * RELATE user_family_member
    */
    private java.util.List<UserFamilyMemberDTO>  userFamilyMember;

    /**
    * 加载数据对象
    */
    private LoadFlag loadFlag;

    @Getter
    @Setter
    @ToString
    public static class UserAddressDTO extends BaseDTO{
        /**
        * 是否有变化
        */
        private Boolean changed = false;

        /**
        * 主鍵
        */
        private java.lang.Long id;
        /**
        * 关联用户
        */
        private java.lang.Long userId;
        /**
        * 地址
        */
        private java.lang.String addressName;
        /**
        * 創建時間
        */
        private java.util.Date createdTime;
        /**
        * 創建用戶
        */
        private java.lang.String createdBy;
        /**
        * 創建時間
        */
        private java.util.Date updatedTime;
        /**
        * 創建用戶
        */
        private java.lang.String updatedBy;
    }
    @Getter
    @Setter
    @ToString
    public static class UserFamilyMemberDTO extends BaseDTO{
        /**
        * 是否有变化
        */
        private Boolean changed = false;

        /**
        * 主鍵
        */
        private java.lang.Long id;
        /**
        * 唯一键
        */
        private java.lang.Long userId;
        /**
        * 姓名
        */
        private java.lang.String name;
        /**
        * 家属关系
        */
        private java.lang.String relType;
        /**
        * 創建時間
        */
        private java.util.Date createdTime;
        /**
        * 創建用戶
        */
        private java.lang.String createdBy;
        /**
        * 創建時間
        */
        private java.util.Date updatedTime;
        /**
        * 創建用戶
        */
        private java.lang.String updatedBy;
    }

    @Getter
    @Setter
    @ToString
    public static class LoadFlag{

        /**
        *
        */
        private Boolean loadUserAddress = true;

        /**
        *
        */
        private Boolean loadUserFamilyMember = true;
    }
}
