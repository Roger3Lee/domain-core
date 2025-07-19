package com.artframework.domain.web.generator.domain.datasource.domain;

import com.artframework.domain.core.domain.*;
import com.artframework.domain.core.lambda.query.*;
import com.artframework.domain.core.constants.*;
import com.artframework.domain.core.utils.LambdaQueryUtils;
import com.artframework.domain.core.utils.LoadFlagUtils;
import lombok.*;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.artframework.domain.web.generator.domain.datasource.convertor.*;
import com.artframework.domain.web.generator.domain.datasource.service.*;
import com.artframework.domain.web.generator.domain.datasource.lambdaexp.*;
import cn.hutool.core.util.*;
import cn.hutool.core.collection.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
* datasource
*
* @author auto
* @version v1.0
*/
@ApiModel(value = "数据源")
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceDomain extends BaseAggregateDomain<DatasourceDomain,DatasourceService> {

    public DatasourceDomain(Integer key, DatasourceService service){
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
    * 数据源名称
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "数据源名称")
    private String name;
    /**
    * 编码
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "编码")
    private String code;
    /**
    * 数据库类型
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "数据库类型")
    private String dbType;
    /**
    * 数据库连接
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "数据库连接")
    private String url;
    /**
    * 数据库用户
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "数据库用户")
    private String userName;
    /**
    * 密码
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "密码")
    private String password;
    /**
    * schema
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "schema")
    private String schema;


    /**
    * RELATE datasource_table
    */
    @Setter
    @ApiModelProperty(value =  "RELATE datasource_table")
    private java.util.List<DatasourceTableDomain> datasourceTableList;

    public java.util.List<DatasourceTableDomain> getDatasourceTableList(){
        if(ObjectUtil.isNotEmpty(this.datasourceTableList)){
            ListUtil.toList(this.datasourceTableList).forEach(x -> x.set_thisDomain(this));
        }
        return this.datasourceTableList;
    }

    /**
    * RELATE datasource_table_column
    */
    @Setter
    @ApiModelProperty(value =  "RELATE datasource_table_column")
    private java.util.List<DatasourceTableColumnDomain> datasourceTableColumnList;

    public java.util.List<DatasourceTableColumnDomain> getDatasourceTableColumnList(){
        if(ObjectUtil.isNotEmpty(this.datasourceTableColumnList)){
            ListUtil.toList(this.datasourceTableColumnList).forEach(x -> x.set_thisDomain(this));
        }
        return this.datasourceTableColumnList;
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
    public static class DatasourceTableDomain extends BaseDomain{
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
        * 数据源ID
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "数据源ID")
        private Integer dsId;
        /**
        * 表名
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "表名")
        private String name;
        /**
        * 表描述
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "表描述")
        private String comment;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatasourceTableColumnDomain extends BaseDomain{
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
        * 数据源ID
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "数据源ID")
        private Integer dsId;
        /**
        * 表名
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "表名")
        private String tableName;
        /**
        * 列名
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "列名")
        private String name;
        /**
        * 类型
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "类型")
        private String type;
        /**
        * 表描述
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "表描述")
        private String comment;
        /**
        * 是否是主键
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "是否是主键")
        private String key;
    }
    /**
     * 加載實體數據
     * @param key
     * @param service
     * @return
     */
    public static DatasourceDomain load(Serializable key, DatasourceService service) {
        DatasourceDomain domain = service.find(DatasourceFindDomain.builder().key(key).build());
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
    public static DatasourceDomain loadByKey(Serializable key, SFunction<DatasourceDomain, Serializable> keyLambda, DatasourceService service) {
        DatasourceDomain domain = service.findByKey(DatasourceFindDomain.builder().key(key).build(), keyLambda);
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
        if (tClass.equals(DatasourceTableDomain.class)) {
            builder.loadDatasourceTableDomain = true;
        }
        if (tClass.equals(DatasourceTableColumnDomain.class)) {
            builder.loadDatasourceTableColumnDomain = true;
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
    public static DatasourceDomain copy(DatasourceDomain sourceDomain){
        return DatasourceConvertor.INSTANCE.copy(sourceDomain);
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
        * 加載DatasourceTableDomain
        */
        @ApiModelProperty(value =  "加載DatasourceTableDomain")
        private Boolean loadDatasourceTableDomain;

        /**
        * 加載DatasourceTableColumnDomain
        */
        @ApiModelProperty(value =  "加載DatasourceTableColumnDomain")
        private Boolean loadDatasourceTableColumnDomain;

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

            // 合併DatasourceTableDomain
            if ((null == loadFlag.loadDatasourceTableDomain || BooleanUtil.isFalse(loadFlag.loadDatasourceTableDomain)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadDatasourceTableDomain)) {
                loadFlag.loadDatasourceTableDomain = true;
                LoadFlagUtils.addFilters(loadFlag, LambdaQueryUtils.getEntityFilters(loadFlagSource.getFilters(), DatasourceDomain.DatasourceTableDomain.class), LambdaQueryUtils.getEntityName(DatasourceDomain.DatasourceTableDomain.class));
            }
            // 合併DatasourceTableColumnDomain
            if ((null == loadFlag.loadDatasourceTableColumnDomain || BooleanUtil.isFalse(loadFlag.loadDatasourceTableColumnDomain)) &&
                    BooleanUtil.isTrue(loadFlagSource.loadDatasourceTableColumnDomain)) {
                loadFlag.loadDatasourceTableColumnDomain = true;
                LoadFlagUtils.addFilters(loadFlag, LambdaQueryUtils.getEntityFilters(loadFlagSource.getFilters(), DatasourceDomain.DatasourceTableColumnDomain.class), LambdaQueryUtils.getEntityName(DatasourceDomain.DatasourceTableColumnDomain.class));
            }
            LoadFlagUtils.addOrders(loadFlag, loadFlagSource.getOrders());
            return loadFlag;
        }
    }
}
