package com.artframework.domain.web.generator.domain.project.domain;

import com.artframework.domain.core.domain.*;
import com.artframework.domain.core.lambda.query.*;
import com.artframework.domain.core.constants.*;
import com.artframework.domain.core.utils.LambdaQueryUtils;
import com.artframework.domain.core.utils.LoadFlagUtils;
import lombok.*;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.artframework.domain.web.generator.domain.project.convertor.*;
import com.artframework.domain.web.generator.domain.project.service.*;
import com.artframework.domain.web.generator.domain.project.lambdaexp.*;
import cn.hutool.core.util.*;
import cn.hutool.core.collection.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
* project
*
* @author auto
* @version v1.0
*/
@ApiModel(value = "项目")
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDomain extends BaseAggregateDomain<ProjectDomain,ProjectService> {

    public ProjectDomain(Integer key, ProjectService service){
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
    * 项目名称
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "项目名称")
    private String name;
    /**
    * 领域package
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "领域package")
    private String domainPackage;
    /**
    * 控制器package
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "控制器package")
    private String controllerPackage;
    /**
    * DO package
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "DO package")
    private String doPackage;
    /**
    * Mapper package
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "Mapper package")
    private String mapperPackage;


    /**
    * RELATE domain_config
    */
    @Setter
    @ApiModelProperty(value =  "RELATE domain_config")
    private java.util.List<DomainConfigDomain> domainConfigList;

    public java.util.List<DomainConfigDomain> getDomainConfigList(){
        if(ObjectUtil.isNotEmpty(this.domainConfigList)){
            ListUtil.toList(this.domainConfigList).forEach(x -> x.set_thisDomain(this));
        }
        return this.domainConfigList;
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
    public static class DomainConfigDomain extends BaseDomain{
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
    }
    /**
     * 加載實體數據
     * @param key
     * @param service
     * @return
     */
    public static ProjectDomain load(Serializable key, ProjectService service) {
        ProjectDomain domain = service.find(ProjectFindDomain.builder().key(key).build());
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
    public static ProjectDomain loadByKey(Serializable key, SFunction<ProjectDomain, Serializable> keyLambda, ProjectService service) {
        ProjectDomain domain = service.findByKey(ProjectFindDomain.builder().key(key).build(), keyLambda);
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
        if (tClass.equals(DomainConfigDomain.class)) {
            builder.loadDomainConfigDomain = true;
        }
        LoadFlag loadFlag = builder.build();
        LoadFlagUtils.mergeQueryCondition(loadFlag, query, LambdaQueryUtils.getEntityName(tClass));
        this._service.find(this, loadFlag);
    }

     /**
     * 淺拷貝領域模型
     * @param sourceDomain 源模型
     * @return 返回的模型
     */
    public static ProjectDomain copy(ProjectDomain sourceDomain){
        return ProjectConvertor.INSTANCE.copy(sourceDomain);
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
        * 加載DomainConfigDomain
        */
        @ApiModelProperty(value =  "加載DomainConfigDomain")
        private Boolean loadDomainConfigDomain;

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

            // 合併DomainConfigDomain
            if ((null == loadFlag.loadDomainConfigDomain || BooleanUtil.isFalse(loadFlag.loadDomainConfigDomain)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadDomainConfigDomain)) {
                loadFlag.loadDomainConfigDomain = true;
                LoadFlagUtils.mergeEntityQuery(loadFlag, loadFlagSource, LambdaQueryUtils.getEntityName(ProjectDomain.DomainConfigDomain.class));
            }

            return loadFlag;
        }
    }
}
