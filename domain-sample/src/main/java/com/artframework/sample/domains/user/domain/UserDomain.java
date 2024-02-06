package com.artframework.sample.domains.user.domain;

import com.artframework.domain.core.domain.*;
import com.artframework.domain.core.lambda.*;
import com.artframework.domain.core.uitls.FiltersUtils;
import com.artframework.domain.core.constants.*;
import lombok.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.artframework.sample.domains.user.lambdaexp.*;
import com.artframework.sample.domains.user.service.*;
import cn.hutool.core.util.*;
import cn.hutool.core.collection.*;
import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
* user
*
* @author auto
* @version v1.0
*/
@ApiModel(value = "用戶域")
@NoArgsConstructor
@AllArgsConstructor
public class UserDomain extends BaseAggregateDomain<UserDomain,UserService>  {

    public UserDomain(java.lang.Long key, UserService service){
        this.id = key;
        this._service = service;
    }
    /**
    * 主鍵
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "主鍵")
    private java.lang.Long id;
    /**
    * 名字
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "名字")
    private java.lang.String name;
    /**
    * 手机
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "手机")
    private java.lang.String phone;
    /**
    * 創建時間
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "創建時間")
    private java.util.Date createdTime;
    /**
    * 創建用戶
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "創建用戶")
    private java.lang.String createdBy;
    /**
    * 創建時間
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "創建時間")
    private java.util.Date updatedTime;
    /**
    * 創建用戶
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "創建用戶")
    private java.lang.String updatedBy;


    /**
    * RELATE user_address
    */
    @Setter
    @ApiModelProperty(value =  "RELATE user_address")
    private UserAddressDomain userAddress;

    public UserAddressDomain getUserAddress(){
        if(ObjectUtil.isNotEmpty(this.userAddress)){
            ListUtil.toList(this.userAddress).forEach(x -> x.set_thisDomain(this));
        }
        return this.userAddress;
    }

    /**
    * RELATE user_family_member
    */
    @Setter
    @ApiModelProperty(value =  "RELATE user_family_member")
    private java.util.List<UserFamilyMemberDomain> userFamilyMemberList;

    public java.util.List<UserFamilyMemberDomain> getUserFamilyMemberList(){
        if(ObjectUtil.isNotEmpty(this.userFamilyMemberList)){
            ListUtil.toList(this.userFamilyMemberList).forEach(x -> x.set_thisDomain(this));
        }
        return this.userFamilyMemberList;
    }


    /**
    * 加载数据標識類
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "加载数据標識類")
    private LoadFlag loadFlag;

    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAddressDomain extends BaseDomain{
        /**
        * 主鍵
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "主鍵")
        private java.lang.Long id;
        /**
        * 关联用户
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "关联用户")
        private java.lang.Long userId;
        /**
        * 地址
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "地址")
        private java.lang.String addressName;
        /**
        * 創建時間
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "創建時間")
        private java.util.Date createdTime;
        /**
        * 創建用戶
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "創建用戶")
        private java.lang.String createdBy;
        /**
        * 創建時間
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "創建時間")
        private java.util.Date updatedTime;
        /**
        * 創建用戶
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "創建用戶")
        private java.lang.String updatedBy;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserFamilyMemberDomain extends BaseDomain{
        /**
        * 主鍵
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "主鍵")
        private java.lang.Long id;
        /**
        * 唯一键
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "唯一键")
        private java.lang.Long userId;
        /**
        * 姓名
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "姓名")
        private java.lang.String name;
        /**
        * 家属关系
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "家属关系")
        private java.lang.String relType;
        /**
        * 創建時間
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "創建時間")
        private java.util.Date createdTime;
        /**
        * 創建用戶
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "創建用戶")
        private java.lang.String createdBy;
        /**
        * 創建時間
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "創建時間")
        private java.util.Date updatedTime;
        /**
        * 創建用戶
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "創建用戶")
        private java.lang.String updatedBy;
    }


    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoadFlag extends BaseLoadFlag{
        /**
        * 加載所有數據， 謹慎使用
        */
        @ApiModelProperty(value =  "加載所有數據， 謹慎使用")
        private Boolean loadAll;

        /**
        * 加載UserAddressDomain
        */
        @ApiModelProperty(value =  "加載UserAddressDomain")
        private Boolean loadUserAddress;

        /**
        * 加載UserFamilyMemberDomain
        */
        @ApiModelProperty(value =  "加載UserFamilyMemberDomain")
        private Boolean loadUserFamilyMember;

        /**
         * 合併兩個loadFlag數據
         * @param loadFlag target
         * @param loadFlagSource source
         * @return
         */
        public static LoadFlag merge(LoadFlag loadFlag, LoadFlag loadFlagSource) {
            if (ObjectUtil.isNull(loadFlagSource)) {
                return loadFlag;
            }

            if (ObjectUtil.isNull(loadFlag)) {
                return loadFlagSource;
            }

            // 合併UserAddressDomain
            if ((null == loadFlag.loadUserAddress || BooleanUtil.isFalse(loadFlag.loadUserAddress)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadUserAddress)) {
                loadFlag.loadUserAddress = true;
                loadFlag.addFilters(FiltersUtils.getEntityFiltersEx(loadFlagSource.getFilters(), UserDomain.UserAddressDomain.class));
            }
            // 合併UserFamilyMemberDomain
            if ((null == loadFlag.loadUserFamilyMember || BooleanUtil.isFalse(loadFlag.loadUserFamilyMember)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadUserFamilyMember)) {
                loadFlag.loadUserFamilyMember = true;
                loadFlag.addFilters(FiltersUtils.getEntityFiltersEx(loadFlagSource.getFilters(), UserDomain.UserFamilyMemberDomain.class));
            }
            loadFlag.addOrders(loadFlagSource.getOrders());
            return loadFlag;
        }
    }

    /**
     * 加載實體數據
     * @param key
     * @param service
     * @return
     */
    public static UserDomain load(Serializable key, UserService service) {
        UserDomain domain = service.find(UserFindDomain.builder().key(key).build());
        domain._service = service;
        return domain;
    }

    /**
     * 加載實體數據 by key
     * @param key
     * @param keyLambda
     * @param service
     * @return
     */
    public static UserDomain loadByKey(Serializable key, SFunction<UserDomain, Serializable> keyLambda, UserService service) {
        UserDomain domain = service.findByKey(UserFindDomain.builder().key(key).build(), keyLambda);
        domain._service = service;
        return domain;
    }

    /**
     * 加載關聯數據
     * @param tClass
     * @param  filters
     * @return
     * @param <T>
     */
    @Override
    public <T> UserDomain loadRelated(Class<T> tClass, List<LambdaFilter<T>> filters, LambdaOrder<T> orders) {
        LoadFlag.LoadFlagBuilder builder = LoadFlag.builder();
        if (tClass.equals(UserAddressDomain.class)) {
            builder.loadUserAddress = true;
        }
        if (tClass.equals(UserFamilyMemberDomain.class)) {
            builder.loadUserFamilyMember = true;
        }
        LoadFlag loadFlag = builder.build().addFilters(FiltersUtils.buildLambdaFilter(filters));
        loadFlag.setOrder(orders);
        return this._service.find(this, loadFlag);
    }
}
