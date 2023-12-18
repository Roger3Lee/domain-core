package com.artframework.sample.domains.user.dto;

import com.artframework.domain.core.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;


/**
* user
*
* @author auto
* @version v1.0
* @date 2023-12-18 12:46:32
*/
@Getter
@Setter
@ToString
public class UserDTO extends BaseDTO {
    /**
    * 自增主键
    */
    private Integer id;
    /**
    * 名称
    */
    private String name;
    /**
    * 手机
    */
    private String phone;
    /**
    * 创建人
    */
    private String createdBy;
    /**
    * 创建时间
    */
    private java.util.Date createdTime;
    /**
    * 更新人
    */
    private String updatedBy;
    /**
    * 更新时间
    */
    private java.util.Date updatedTime;

    /**
    * RELATE user_address
    */
    private UserAddressDTO userAddress;
    /**
    * RELATE user_family_member
    */
    private java.util.List<UserFamilyMemberDTO> userFamilyMemberList;


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
        * 自增主键
        */
        private Integer id;
        /**
        * 关联用户
        */
        private Integer userId;
        /**
        * 地址
        */
        private String addressName;
        /**
        * 创建人
        */
        private String createdBy;
        /**
        * 创建时间
        */
        private java.util.Date createdTime;
        /**
        * 更新人
        */
        private String updatedBy;
        /**
        * 更新时间
        */
        private java.util.Date updatedTime;
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
        * 自增主键
        */
        private Integer id;
        /**
        * 用户ID
        */
        private Integer userId;
        /**
        * 姓名
        */
        private String name;
        /**
        * 家属关系
        */
        private String relType;
        /**
        * 创建人
        */
        private String createdBy;
        /**
        * 创建时间
        */
        private java.util.Date createdTime;
        /**
        * 更新人
        */
        private String updatedBy;
        /**
        * 更新时间
        */
        private java.util.Date updatedTime;
    }


    @Getter
    @Setter
    @ToString
    public static class LoadFlag{

        /**
        *
        */
        private Boolean loadUserAddress = false;

        /**
        *
        */
        private Boolean loadUserFamilyMember = false;

        /**
         * 過濾條件
         */
        private Map<String, Object> filters = new HashMap<>();
    }
}
