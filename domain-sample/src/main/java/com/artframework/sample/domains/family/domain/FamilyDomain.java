package com.artframework.sample.domains.family.domain;

import com.artframework.domain.core.domain.*;
import com.artframework.domain.core.lambda.query.*;
import com.artframework.domain.core.constants.*;
import com.artframework.domain.core.uitls.LambdaQueryUtils;
import com.artframework.domain.core.uitls.LoadFlagUtils;
import lombok.*;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.artframework.sample.domains.family.convertor.*;
import com.artframework.sample.domains.family.service.*;
import com.artframework.sample.domains.family.lambdaexp.*;
import cn.hutool.core.util.*;
import cn.hutool.core.collection.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
* family
*
* @author auto
* @version v1.0
*/
@ApiModel(value = "家庭领域模型")
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDomain extends BaseAggregateDomain<FamilyDomain,FamilyService> {

    public FamilyDomain(Integer key, FamilyService service){
        this.id = key;
        this._service = service;
    }
    /**
    * 自增主键
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "自增主键")
    private Integer id;
    /**
    * 名称
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "名称")
    private String name;
    /**
    * 家庭成员数量
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "家庭成员数量")
    private Integer personCount;


    /**
    * RELATE family_address
    */
    @Setter
    @ApiModelProperty(value =  "RELATE family_address")
    private FamilyAddressDomain familyAddress;

    public FamilyAddressDomain getFamilyAddress(){
        if(ObjectUtil.isNotEmpty(this.familyAddress)){
            ListUtil.toList(this.familyAddress).forEach(x -> x.set_thisDomain(this));
        }
        return this.familyAddress;
    }

    /**
    * RELATE family_member
    */
    @Setter
    @ApiModelProperty(value =  "RELATE family_member")
    private java.util.List<FamilyMemberDomain> familyMemberList;

    public java.util.List<FamilyMemberDomain> getFamilyMemberList(){
        if(ObjectUtil.isNotEmpty(this.familyMemberList)){
            ListUtil.toList(this.familyMemberList).forEach(x -> x.set_thisDomain(this));
        }
        return this.familyMemberList;
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
    public static class FamilyAddressDomain extends BaseDomain{
        /**
        * 自增主键
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "自增主键")
        private Integer id;
        /**
        * 关联用户
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "关联用户")
        private Integer familyId;
        /**
        * 家庭名称
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "家庭名称")
        private String familyName;
        /**
        * 地址
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "地址")
        private String addressName;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyMemberDomain extends BaseDomain{
        /**
        * 自增主键
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "自增主键")
        private Integer id;
        /**
        * 家庭ID
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "家庭ID")
        private Integer familyId;
        /**
        * 家庭名称
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "家庭名称")
        private String familyName;
        /**
        * 姓名
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "姓名")
        private String name;
        /**
        * 电话
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "电话")
        private String phone;
        /**
        * 成员关系
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "成员关系")
        private String type;
    }
    /**
     * 加載實體數據
     * @param key
     * @param service
     * @return
     */
    public static FamilyDomain load(Serializable key, FamilyService service) {
        FamilyDomain domain = service.find(FamilyFindDomain.builder().key(key).build());
        if(ObjectUtil.isNotNull(domain)){
            domain._service = service;
        }
        return domain;
    }

    /**
     * 加載實體數據 by key
     * @param key
     * @param keyLambda
     * @param service
     * @return
     */
    public static FamilyDomain loadByKey(Serializable key, SFunction<FamilyDomain, Serializable> keyLambda, FamilyService service) {
        FamilyDomain domain = service.findByKey(FamilyFindDomain.builder().key(key).build(), keyLambda);
        if(ObjectUtil.isNotNull(domain)){
            domain._service = service;
        }
        return domain;
    }
    /**
     * 加載關聯數據
     * @param tClass
     * @param query
     * @return
     * @param <T>
     */
    @Override
    public <T> FamilyDomain loadRelated(Class<T> tClass, LambdaQuery<T> query) {
        LoadFlag.LoadFlagBuilder builder = LoadFlag.builder();
        if (tClass.equals(FamilyAddressDomain.class)) {
            builder.loadFamilyAddressDomain = true;
        }
        if (tClass.equals(FamilyMemberDomain.class)) {
            builder.loadFamilyMemberDomain = true;
        }
        LoadFlag loadFlag = builder.build();
        LoadFlagUtils.addFilters(loadFlag, LambdaQueryUtils.toFilters(query), LambdaQueryUtils.getEntityName(tClass));
        LoadFlagUtils.addOrders(loadFlag, LambdaQueryUtils.toOrders(query));
        return this._service.find(this, loadFlag);
    }

     /**
     * 淺拷貝領域模型
     * @param sourceDomain 源模型
     * @return 返回的模型
     */
    public static FamilyDomain copy(FamilyDomain sourceDomain){
        return FamilyConvertor.INSTANCE.copy(sourceDomain);
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
        * 加載FamilyAddressDomain
        */
        @ApiModelProperty(value =  "加載FamilyAddressDomain")
        private Boolean loadFamilyAddressDomain;

        /**
        * 加載FamilyMemberDomain
        */
        @ApiModelProperty(value =  "加載FamilyMemberDomain")
        private Boolean loadFamilyMemberDomain;

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

            // 合併FamilyAddressDomain
            if ((null == loadFlag.loadFamilyAddressDomain || BooleanUtil.isFalse(loadFlag.loadFamilyAddressDomain)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadFamilyAddressDomain)) {
                loadFlag.loadFamilyAddressDomain = true;
                LoadFlagUtils.addFilters(loadFlag, LambdaQueryUtils.getEntityFiltersEx(loadFlagSource.getFilters(), FamilyDomain.FamilyAddressDomain.class), LambdaQueryUtils.getEntityName(FamilyDomain.FamilyAddressDomain.class));
            }
            // 合併FamilyMemberDomain
            if ((null == loadFlag.loadFamilyMemberDomain || BooleanUtil.isFalse(loadFlag.loadFamilyMemberDomain)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadFamilyMemberDomain)) {
                loadFlag.loadFamilyMemberDomain = true;
                LoadFlagUtils.addFilters(loadFlag, LambdaQueryUtils.getEntityFiltersEx(loadFlagSource.getFilters(), FamilyDomain.FamilyMemberDomain.class), LambdaQueryUtils.getEntityName(FamilyDomain.FamilyMemberDomain.class));
            }
            LoadFlagUtils.addOrders(loadFlag, loadFlagSource.getOrders());
            return loadFlag;
        }
    }
}
