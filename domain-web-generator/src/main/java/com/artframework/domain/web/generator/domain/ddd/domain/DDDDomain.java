package com.artframework.domain.web.generator.domain.ddd.domain;

import com.artframework.domain.core.domain.*;
import com.artframework.domain.core.lambda.query.*;
import com.artframework.domain.core.constants.*;
import com.artframework.domain.core.utils.LambdaQueryUtils;
import com.artframework.domain.core.utils.LoadFlagUtils;
import lombok.*;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.artframework.domain.web.generator.domain.ddd.convertor.*;
import com.artframework.domain.web.generator.domain.ddd.service.*;
import com.artframework.domain.web.generator.domain.ddd.lambdaexp.*;
import cn.hutool.core.util.*;
import cn.hutool.core.collection.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
* DDD
*
* @author auto
* @version v1.0
*/
@ApiModel(value = "项目")
@NoArgsConstructor
@AllArgsConstructor
public class DDDDomain extends BaseAggregateDomain<DDDDomain,DDDService> {

    public DDDDomain(Integer key, DDDService service){
        this.id = key;
        this._service = service;
    }
    /**
    * 主键
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "主键")
    private Integer id;
    /**
    * 项目ID
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "项目ID")
    private Integer projectId;
    /**
    * 乐观锁
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "乐观锁")
    private Integer revision;
    /**
    * 创建人
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "创建人")
    private String createdBy;
    /**
    * 创建时间
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "创建时间")
    private java.time.LocalDateTime createdTime;
    /**
    * 更新人
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "更新人")
    private String updatedBy;
    /**
    * 更新时间
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "更新时间")
    private java.time.LocalDateTime updatedTime;
    /**
    * 领域名称
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "领域名称")
    private String domainName;
    /**
    * 领域模型XML
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "领域模型XML")
    private String domainXml;
    /**
    * 主表
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "主表")
    private String mainTable;
    /**
    * 目录
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "目录")
    private String folder;


    /**
    * RELATE domain_config_line
    */
    @Setter
    @ApiModelProperty(value =  "RELATE domain_config_line")
    private java.util.List<DomainConfigLineDomain> domainConfigLineList;

    public java.util.List<DomainConfigLineDomain> getDomainConfigLineList(){
        if(ObjectUtil.isNotEmpty(this.domainConfigLineList)){
            ListUtil.toList(this.domainConfigLineList).forEach(x -> x.set_thisDomain(this));
        }
        return this.domainConfigLineList;
    }

    /**
    * RELATE domain_config_line_config
    */
    @Setter
    @ApiModelProperty(value =  "RELATE domain_config_line_config")
    private java.util.List<DomainConfigLineConfigDomain> domainConfigLineConfigList;

    public java.util.List<DomainConfigLineConfigDomain> getDomainConfigLineConfigList(){
        if(ObjectUtil.isNotEmpty(this.domainConfigLineConfigList)){
            ListUtil.toList(this.domainConfigLineConfigList).forEach(x -> x.set_thisDomain(this));
        }
        return this.domainConfigLineConfigList;
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
    public static class DomainConfigLineDomain extends BaseDomain{
        /**
        * 主键
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "主键")
        private Integer id;
        /**
        * 项目ID
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "项目ID")
        private Integer projectId;
        /**
        * 领域ID
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "领域ID")
        private Integer domainId;
        /**
        * 创建人
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "创建人")
        private String createdBy;
        /**
        * 创建时间
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "创建时间")
        private java.time.LocalDateTime createdTime;
        /**
        * 更新人
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "更新人")
        private String updatedBy;
        /**
        * 更新时间
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "更新时间")
        private java.time.LocalDateTime updatedTime;
        /**
        * 连线的编码
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "连线的编码")
        private String lineCode;
        /**
        * 线条的类型;FK ：外键  REDUNDANCY:冗余
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "线条的类型;FK ：外键  REDUNDANCY:冗余")
        private String lineType;
        /**
        * 源表
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "源表")
        private String sourceTable;
        /**
        * 源表列
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "源表列")
        private String sourceColunm;
        /**
        * 目标表
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "目标表")
        private String targetTable;
        /**
        * 目标列
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "目标列")
        private String targetColunm;
        /**
        * 是否一对多
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "是否一对多")
        private String many;

        @ApiModelProperty(value =  "Related to domain_config_line_config")
        @JsonIgnore
        private CacheDomain<java.util.List<DomainConfigLineConfigDomain>> refLineList;
        /**
        * Related to domain_config_line_config
        */
        public synchronized java.util.List<DomainConfigLineConfigDomain> getRefLineList() {
            if(null == this.refLineList){
                DDDDomain domain = (DDDDomain)this.get_thisDomain();
                if(null == domain){
                    return null;
                }

                Predicate<DomainConfigLineConfigDomain> condition = x -> true;
                condition = condition.and(x ->ObjectUtil.equals(DDDLambdaExp.domainConfigLineRefLine_lineCodeSourceLambda.apply(this), DDDLambdaExp.domainConfigLineRefLine_lineCodeTargetLambda.apply(x)));
                if (CollUtil.isNotEmpty(domain.getDomainConfigLineConfigList())) {
                    this.refLineList= new CacheDomain<>(domain.getDomainConfigLineConfigList().stream().filter(condition).collect(Collectors.toList()));
                } else {
                    this.refLineList= new CacheDomain<>(ListUtil.empty());
                }
            }
            return this.refLineList.getValue();
        }
        /**
        * REF to app_dir_doc_rel set
        */
        public synchronized void setRefLineList(java.util.List<DomainConfigLineConfigDomain> ref) {
            if (ObjectUtil.isNotEmpty(ref)) {
                ListUtil.toList(ref).forEach(x -> x.set_thisDomain(this.get_thisDomain()));
                this.refLineList = new CacheDomain<>(ref);
            } else {
                this.refLineList = new CacheDomain<>(ListUtil.empty());
            }
        }
        @Override
        public void afterSave(SaveState saveState) {
            DDDDomain domain = (DDDDomain)this.get_thisDomain();
            // 處理refLineList
            if(ObjectUtil.isNotEmpty(this.refLineList) && ObjectUtil.isNotEmpty(this.refLineList.getValue())){
                java.util.List<DomainConfigLineConfigDomain> list = new java.util.ArrayList<>();
                //設置關聯字段值
                for (DomainConfigLineConfigDomain refDomain : ListUtil.toList(this.refLineList.getValue())) {
                     DDDLambdaExp.domainConfigLineRefLine_lineCodeTargetSetLambda.accept(refDomain, (String)DDDLambdaExp.domainConfigLineRefLine_lineCodeSourceLambda.apply(this));
                     list.add(refDomain);
                }

                //將關聯信息插入到主domain中
                if (CollectionUtil.isNotEmpty(domain.getDomainConfigLineConfigList())) {
                    list.forEach(x -> {
                        //引用類型，如果已經存在則不重複加入
                        if (!domain.getDomainConfigLineConfigList().contains(x)) {
                            domain.getDomainConfigLineConfigList().add(x);
                        }
                    });
                } else {
                    domain.setDomainConfigLineConfigList(list);
                }
            }
        }
    }
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DomainConfigLineConfigDomain extends BaseDomain{
        /**
        * 主键
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "主键")
        private Integer id;
        /**
        * 项目ID
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "项目ID")
        private Integer projectId;
        /**
        * 领域ID
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "领域ID")
        private Integer domainId;
        /**
        * 创建人
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "创建人")
        private String createdBy;
        /**
        * 创建时间
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "创建时间")
        private java.time.LocalDateTime createdTime;
        /**
        * 更新人
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "更新人")
        private String updatedBy;
        /**
        * 更新时间
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "更新时间")
        private java.time.LocalDateTime updatedTime;
        /**
        * 连线的编码
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "连线的编码")
        private String lineCode;
        /**
        * 源表列
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "源表列")
        private String sourceColunm;
        /**
        * 源表列的值（在常量关联时使用）
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "源表列的值（在常量关联时使用）")
        private String sourceColumnValue;
        /**
        * 源表列值的类型（在常量关联时使用）
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "源表列值的类型（在常量关联时使用）")
        private String sourceColumnValueDataType;
        /**
        * 目标列
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "目标列")
        private String targetColunm;
        /**
        * 目标表列的值（在常量关联时使用）
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "目标表列的值（在常量关联时使用）")
        private String targetColumnValue;
        /**
        * 目标列值的类型（在常量关联时使用）
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "目标列值的类型（在常量关联时使用）")
        private String targetColumnValueDataType;
    }
    /**
     * 加載實體數據
     * @param key
     * @param service
     * @return
     */
    public static DDDDomain load(Serializable key, DDDService service) {
        DDDDomain domain = service.find(DDDFindDomain.builder().key(key).build());
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
    public static DDDDomain loadByKey(Serializable key, SFunction<DDDDomain, Serializable> keyLambda, DDDService service) {
        DDDDomain domain = service.findByKey(DDDFindDomain.builder().key(key).build(), keyLambda);
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
    public <T> void loadRelated(Class<T> tClass, LambdaQuery<T> query) {
        LoadFlag.LoadFlagBuilder builder = LoadFlag.builder();
        if (tClass.equals(DomainConfigLineDomain.class)) {
            builder.loadDomainConfigLineDomain = true;
        }
        if (tClass.equals(DomainConfigLineConfigDomain.class)) {
            builder.loadDomainConfigLineConfigDomain = true;
        }
        LoadFlag loadFlag = builder.build();
        LoadFlagUtils.addFilters(loadFlag, LambdaQueryUtils.toFilters(query), LambdaQueryUtils.getEntityName(tClass));
        LoadFlagUtils.addOrders(loadFlag, LambdaQueryUtils.toOrders(query));
        this._service.find(this, loadFlag);
    }

     /**
     * 淺拷貝領域模型
     * @param sourceDomain 源模型
     * @return 返回的模型
     */
    public static DDDDomain copy(DDDDomain sourceDomain){
        return DDDConvertor.INSTANCE.copy(sourceDomain);
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
        * 加載DomainConfigLineDomain
        */
        @ApiModelProperty(value =  "加載DomainConfigLineDomain")
        private Boolean loadDomainConfigLineDomain;

        /**
        * 加載DomainConfigLineConfigDomain
        */
        @ApiModelProperty(value =  "加載DomainConfigLineConfigDomain")
        private Boolean loadDomainConfigLineConfigDomain;

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

            // 合併DomainConfigLineDomain
            if ((null == loadFlag.loadDomainConfigLineDomain || BooleanUtil.isFalse(loadFlag.loadDomainConfigLineDomain)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadDomainConfigLineDomain)) {
                loadFlag.loadDomainConfigLineDomain = true;
                LoadFlagUtils.addFilters(loadFlag, LambdaQueryUtils.getEntityFilters(loadFlagSource.getFilters(), DDDDomain.DomainConfigLineDomain.class), LambdaQueryUtils.getEntityName(DDDDomain.DomainConfigLineDomain.class));
            }
            // 合併DomainConfigLineConfigDomain
            if ((null == loadFlag.loadDomainConfigLineConfigDomain || BooleanUtil.isFalse(loadFlag.loadDomainConfigLineConfigDomain)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadDomainConfigLineConfigDomain)) {
                loadFlag.loadDomainConfigLineConfigDomain = true;
                LoadFlagUtils.addFilters(loadFlag, LambdaQueryUtils.getEntityFilters(loadFlagSource.getFilters(), DDDDomain.DomainConfigLineConfigDomain.class), LambdaQueryUtils.getEntityName(DDDDomain.DomainConfigLineConfigDomain.class));
            }
            LoadFlagUtils.addOrders(loadFlag, loadFlagSource.getOrders());
            return loadFlag;
        }
    }
}
