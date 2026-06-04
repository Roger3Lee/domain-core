package com.artframework.sample.domains.family.domain;

import io.github.roger3lee.domain.core.domain.*;
import io.github.roger3lee.domain.core.lambda.query.*;
import io.github.roger3lee.domain.core.constants.*;
import io.github.roger3lee.domain.core.utils.LambdaQueryUtils;
import io.github.roger3lee.domain.core.utils.LoadFlagUtils;
import lombok.*;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import com.artframework.sample.domains.family.convertor.*;
import com.artframework.sample.domains.family.service.*;
import com.artframework.sample.domains.family.lambdaexp.*;
import cn.hutool.core.util.*;
import cn.hutool.core.collection.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
* family
*
* @author auto
* @version v1.0
*/
@Schema(description = "家庭领域模型")
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDomain extends BaseAggregateDomain<FamilyDomain,FamilyService> {

    public FamilyDomain(Long key, FamilyService service){
        this.id = key;
        this._service = service;
    }
    /**
    * 自增主键
    */
    @Getter
    @Setter
    @Schema(description = "自增主键")
    private Long id;
    /**
    * 名称
    */
    @Getter
    @Setter
    @Schema(description = "名称")
    private String name;
    /**
    * 家庭成员数量
    */
    @Getter
    @Setter
    @Schema(description = "家庭成员数量")
    private Integer personCount;
    /**
    * 户主姓名
    */
    @Getter
    @Setter
    @Schema(description = "户主姓名")
    private String householder;


    /**
    * RELATE family_member
    */
    @Setter
    @Schema(description = "RELATE family_member")
    private java.util.List<FamilyMemberDomain> familyMemberList;

    public java.util.List<FamilyMemberDomain> getFamilyMemberList(){
        if(ObjectUtil.isNotEmpty(this.familyMemberList)){
            ListUtil.toList(this.familyMemberList).forEach(x -> x.set_thisDomain(this));
        }
        return this.familyMemberList;
    }

    /**
    * RELATE family_address
    */
    @Setter
    @Schema(description = "RELATE family_address")
    private java.util.List<FamilyAddressDomain> familyAddressList;

    public java.util.List<FamilyAddressDomain> getFamilyAddressList(){
        if(ObjectUtil.isNotEmpty(this.familyAddressList)){
            ListUtil.toList(this.familyAddressList).forEach(x -> x.set_thisDomain(this));
        }
        return this.familyAddressList;
    }

    /**
    * 加载数据標識類
    */
    @Getter
    @Setter
    @Schema(description = "加载数据標識類")
    private LoadFlag loadFlag;

    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyMemberDomain extends BaseDomain{
        /**
        * 自增主键
        */
        @Getter
        @Setter
        @Schema(description = "自增主键")
        private Long id;
        /**
        * 家庭ID
        */
        @Getter
        @Setter
        @Schema(description = "家庭ID")
        private Long familyId;
        /**
        * 冗余字段：家庭名称
        */
        @Getter
        @Setter
        @Schema(description = "冗余字段：家庭名称")
        private String familyName;
        /**
        * 姓名
        */
        @Getter
        @Setter
        @Schema(description = "姓名")
        private String name;
        /**
        * 电话
        */
        @Getter
        @Setter
        @Schema(description = "电话")
        private String phone;
        /**
        * 成员关系
        */
        @Getter
        @Setter
        @Schema(description = "成员关系")
        private String type;

        @Schema(description = "Related to family_address")
        @JsonIgnore
        private CacheDomain<FamilyAddressDomain> refAddress;
        /**
        * Related to family_address
        */
        public synchronized FamilyAddressDomain getRefAddress() {
            if(null == this.refAddress){
                FamilyDomain domain = (FamilyDomain)this.get_thisDomain();
                if(null == domain){
                    return null;
                }

                Predicate<FamilyAddressDomain> condition = x -> true;
                condition = condition.and(x ->ObjectUtil.equals(FamilyLambdaExp.familyMemberRefAddress_idSourceLambda.apply(this), FamilyLambdaExp.familyMemberRefAddress_familyMemberIdTargetLambda.apply(x)));
                if (CollUtil.isNotEmpty(domain.getFamilyAddressList())) {
                    this.refAddress= new CacheDomain<>(domain.getFamilyAddressList().stream().filter(condition).findFirst().orElse(null));
                } else {
                    this.refAddress= new CacheDomain<>(null);
                }
            }
            return this.refAddress.getValue();
        }
        /**
        * REF to app_dir_doc_rel set
        */
        public synchronized void setRefAddress(FamilyAddressDomain ref) {
            if (ObjectUtil.isNotEmpty(ref)) {
                ListUtil.toList(ref).forEach(x -> x.set_thisDomain(this.get_thisDomain()));
                this.refAddress = new CacheDomain<>(ref);
            } else {
                this.refAddress = new CacheDomain<>(null);
            }
        }
        @Override
        public void afterSave(SaveState saveState) {
            FamilyDomain domain = (FamilyDomain)this.get_thisDomain();
            // 處理refAddress
            if(ObjectUtil.isNotEmpty(this.refAddress) && ObjectUtil.isNotEmpty(this.refAddress.getValue())){
                java.util.List<FamilyAddressDomain> list = new java.util.ArrayList<>();
                //設置關聯字段值
                for (FamilyAddressDomain refDomain : ListUtil.toList(this.refAddress.getValue())) {
                     FamilyLambdaExp.familyMemberRefAddress_familyMemberIdTargetSetLambda.accept(refDomain, (Long)FamilyLambdaExp.familyMemberRefAddress_idSourceLambda.apply(this));
                     list.add(refDomain);
                }

                //將關聯信息插入到主domain中
                if (CollectionUtil.isNotEmpty(domain.getFamilyAddressList())) {
                    list.forEach(x -> {
                        //引用類型，如果已經存在則不重複加入
                        if (!domain.getFamilyAddressList().contains(x)) {
                            domain.getFamilyAddressList().add(x);
                        }
                    });
                } else {
                    domain.setFamilyAddressList(list);
                }
            }
        }
    }
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyAddressDomain extends BaseDomain{
        /**
        * 自增主键
        */
        @Getter
        @Setter
        @Schema(description = "自增主键")
        private Long id;
        /**
        * 关联家庭ID
        */
        @Getter
        @Setter
        @Schema(description = "关联家庭ID")
        private Long familyId;
        /**
        * 冗余字段：家庭名称
        */
        @Getter
        @Setter
        @Schema(description = "冗余字段：家庭名称")
        private String familyName;
        /**
        * 地址
        */
        @Getter
        @Setter
        @Schema(description = "地址")
        private String addressName;

        @Getter
        @Setter
        @Schema(description = "地址")
        public Long familyMemberId;
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
        if (tClass.equals(FamilyMemberDomain.class)) {
            builder.loadFamilyMemberDomain = true;
        }
        if (tClass.equals(FamilyAddressDomain.class)) {
            builder.loadFamilyAddressDomain = true;
        }
        LoadFlag loadFlag = builder.build();
        LoadFlagUtils.mergeQueryCondition(loadFlag, query, LambdaQueryUtils.getEntityName(tClass));
        this._service.find(this, loadFlag);
        return this;
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
        @Schema(description = "加載所有數據， 謹慎使用")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean loadAll;


        /**
        * 加載FamilyMemberDomain
        */
        @Schema(description = "加載FamilyMemberDomain")
        private Boolean loadFamilyMemberDomain;

        /**
        * 加載FamilyAddressDomain
        */
        @Schema(description = "加載FamilyAddressDomain")
        private Boolean loadFamilyAddressDomain;

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

            // 合併FamilyMemberDomain
            if ((null == loadFlag.loadFamilyMemberDomain || BooleanUtil.isFalse(loadFlag.loadFamilyMemberDomain)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadFamilyMemberDomain)) {
                loadFlag.loadFamilyMemberDomain = true;
                LoadFlagUtils.mergeEntityQuery(loadFlag, loadFlagSource, LambdaQueryUtils.getEntityName(FamilyDomain.FamilyMemberDomain.class));
            }
            // 合併FamilyAddressDomain
            if ((null == loadFlag.loadFamilyAddressDomain || BooleanUtil.isFalse(loadFlag.loadFamilyAddressDomain)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadFamilyAddressDomain)) {
                loadFlag.loadFamilyAddressDomain = true;
                LoadFlagUtils.mergeEntityQuery(loadFlag, loadFlagSource, LambdaQueryUtils.getEntityName(FamilyDomain.FamilyAddressDomain.class));
            }

            return loadFlag;
        }
    }
}
