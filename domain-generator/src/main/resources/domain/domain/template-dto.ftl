package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain;

import ${corePackage}.domain.*;
import ${corePackage}.lambda.*;
import ${corePackage}.uitls.FiltersUtils;
import ${corePackage}.constants.*;
<#if source.aggregate??>
import com.fasterxml.jackson.annotation.JsonIgnore;
</#if>
import lombok.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.lambdaexp.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service.*;
import cn.hutool.core.util.*;
import cn.hutool.core.collection.*;
import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
* ${source.name}
*
* @author auto
* @version v1.0
*/
@ApiModel(value = "${source.description}")
@NoArgsConstructor
@AllArgsConstructor
<#assign className=NameUtils.dataTOName(source.name)/>
<#assign lambdaClassName=NameUtils.lambdaExpName(source.name)/>
<#assign serviceClassName=NameUtils.serviceName(source.name)/>
public class ${className} extends BaseAggregateDomain<${className},${serviceClassName}> <#if (source.implement??) && (source.implement!='')> implements ${source.implement}</#if> {

    public ${className}(${source.mainTable.keyType} key, ${serviceClassName} service){
        this.${source.mainTable.keyColName} = key;
        this._service = service;
    }
<#if source.mainTable??>
    <#list source.mainTable.column as column>
    /**
    * ${column.comment}
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "${column.comment}")
    private ${column.type} ${NameUtils.getFieldName(column.name)};
    </#list>

<#--    关联实体属性-->
    <#list source.relatedTable as relateTable>
        <#assign relateClassName=NameUtils.dataObjectName(relateTable.name)/>
        <#assign relateDtoClassName=NameUtils.dataTOName(relateTable.tableName)/>
        <#assign fieldName=NameUtils.getFieldName(relateTable.name)/>
        <#assign fieldNameList=NameUtils.getListFieldName(relateTable.name)/>
        <#assign relatedDomainGetterList=NameUtils.genListGetter(relateTable.name)/>
        <#assign relatedDomainGetter=NameUtils.genGetter(relateTable.name)/>

    /**
    * RELATE ${relateTable.name}
    */
    @Setter
    @ApiModelProperty(value =  "RELATE ${relateTable.name}")
    private <#if relateTable.many>java.util.List<${relateDtoClassName}> ${fieldNameList};<#else>${relateDtoClassName} ${fieldName};</#if>

    public <#if relateTable.many>java.util.List<${relateDtoClassName}> ${relatedDomainGetterList}<#else>${relateDtoClassName} ${relatedDomainGetter}</#if>(){
        if(ObjectUtil.isNotEmpty(this.<#if relateTable.many>${fieldNameList}<#else>${fieldName}</#if>)){
            ListUtil.toList(this.<#if relateTable.many>${fieldNameList}<#else>${fieldName}</#if>).forEach(x -> x.set_thisDomain(this));
        }
        return this.<#if relateTable.many>${fieldNameList}<#else>${fieldName}</#if>;
    }
    </#list>

<#--    聚合-->
    <#if source.aggregate??>
        <#assign relateDtoClassName=NameUtils.dataTOName(source.aggregate.name)/>
        <#assign fieldName=NameUtils.getFieldName(source.aggregate.name)/>
    /**
    * aggregate ${source.aggregate.name} ,不需要序列化給接口輸出
    */
    @Getter
    @Setter
    @JsonIgnore
    @ApiModelProperty(value =  "aggregate ${source.aggregate.name} ,不需要序列化給接口輸出")
    private ${relateDtoClassName} ${fieldName};
    </#if>

    /**
    * 加载数据標識類
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "加载数据標識類")
    private LoadFlag loadFlag;

<#--    关联实体类-->
    <#list source.relatedTable as relateTable>
    <#assign relateClassName= NameUtils.dataTOName(relateTable.name)/>
    <#assign fieldName=NameUtils.getFieldName(relateTable.name)/>
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ${relateClassName} extends BaseDomain<#if (relateTable.implement??) && (relateTable.implement!='')> implements ${relateTable.implement}</#if>{
    <#list relateTable.column as column>
        /**
        * ${column.comment}
        */
        @Getter
        @Setter
        @ApiModelProperty(value =  "${column.comment}")
        private ${column.type} ${NameUtils.getFieldName(column.name)};
    </#list>
    <#--    关联实体關聯其他實體属性-->
    <#if relateTable.refTableList??>
        <#list relateTable.refTableList as refTable>
            <#assign refClassName= NameUtils.dataTOName(refTable.tableName)/>
            <#assign refName=NameUtils.getFieldName(refTable.name)/>
            <#assign refGetterMethodName=NameUtils.getRefGetterName(refTable.name,refTable.many)/>
            <#assign refSetterMethodName=NameUtils.getRefSetterName(refTable.name,refTable.many)/>
            <#assign refFieldName=NameUtils.getRefFieldName(refTable.name,refTable.many)/>
            <#assign sourceDomainGetterList=NameUtils.genListGetter(refTable.tableName)/>

        @ApiModelProperty(value =  "Related to ${refTable.tableName}")
        @JsonIgnore
        private <#if refTable.many>CacheDomain<java.util.List<${refClassName}>><#else>CacheDomain<${refClassName}></#if> ${refFieldName};
        /**
        * Related to ${refTable.tableName}
        */
        <#--   列表 -->
        public synchronized <#if refTable.many>java.util.List<${refClassName}><#else>${refClassName}</#if> ${refGetterMethodName}() {
            ${className} domain = (${className})this.get_thisDomain();
            if(null == domain){
                return null;
            }

            if(null == this.${refFieldName}){
                Predicate<${refClassName}> condition = x -> true;
                <#list refTable.fkList as fk>
                 <#assign refSourceLambda=NameUtils.fieldRefSourceLambda(fieldName,refName,fk.fkSourceColumn)/>
                 <#assign refTargetLambda=NameUtils.fieldRefTargetDomainLambda(fieldName,refName,fk.fkTargetColumn)/>
                 <#if fk.fkSourceColumn??>
                condition = condition.and(x ->ObjectUtil.equals(<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${refSourceLambda}.apply(this))<#else>${lambdaClassName}.${refSourceLambda}.apply(this)</#if>, <#if fk.fkTargetConvertMethod?? &&(fk.fkTargetConvertMethod!='')>${fk.fkTargetConvertMethod}(${lambdaClassName}.${refTargetLambda}.apply(x))<#else>${lambdaClassName}.${refTargetLambda}.apply(x)</#if>));
                 <#else>
                condition = condition.and(x ->ObjectUtil.equals(${fk.sourceValue}, ${lambdaClassName}.${refTargetLambda}.apply(x)));
                 </#if>
                </#list>
                if (CollUtil.isNotEmpty(domain.${sourceDomainGetterList}())) {
                    this.${refFieldName}= new CacheDomain<>(domain.${sourceDomainGetterList}().stream().filter(condition)<#if refTable.many>.collect(Collectors.toList())<#else>.findFirst().orElse(null)</#if>);
                } else {
                    this.${refFieldName}= new CacheDomain<>(<#if refTable.many>ListUtil.empty()<#else>null</#if>);
                }
            }
            return this.${refFieldName}.getValue();
        }
        /**
        * REF to app_dir_doc_rel set
        */
        public synchronized void ${refSetterMethodName}(<#if refTable.many>java.util.List<${refClassName}><#else>${refClassName}</#if> ref) {
            if (ObjectUtil.isNotEmpty(ref)) {
                ListUtil.toList(ref).forEach(x -> x.set_thisDomain(this.get_thisDomain()));
                this.${refFieldName} = new CacheDomain<>(ref);
            } else {
                this.${refFieldName} = new CacheDomain<>(<#if refTable.many>ListUtil.empty()<#else>null</#if>);
            }
        }
        </#list>
        @Override
        public void afterSave(SaveState saveState) {
            ${className} domain = (${className})this.get_thisDomain();
             <#list relateTable.refTableList as refTable>
                        <#assign refClassName= NameUtils.dataTOName(refTable.tableName)/>
                        <#assign refName=NameUtils.getFieldName(refTable.name)/>
                        <#assign refGetterMethodName=NameUtils.getRefGetterName(refTable.name,refTable.many)/>
                        <#assign refSetterMethodName=NameUtils.getRefSetterName(refTable.name,refTable.many)/>
                        <#assign refFieldName=NameUtils.getRefFieldName(refTable.name,refTable.many)/>
                        <#assign sourceDomainGetterList=NameUtils.genListGetter(refTable.tableName)/>
                        <#assign sourceDomainSetterList=NameUtils.genListSetter(refTable.tableName)/>
            // 處理${refFieldName}
            if(ObjectUtil.isNotEmpty(this.${refFieldName})){
                java.util.List<${refClassName}> list = new java.util.ArrayList<>();
                //設置關聯字段值
                for (${refClassName} refDomain : ListUtil.toList(this.${refFieldName}.getValue())) {
                <#list refTable.fkList as fk>
                     <#assign refSourceLambda=NameUtils.fieldRefSourceLambda(fieldName,refName,fk.fkSourceColumn)/>
                     <#assign refTargetLambdaSetter=NameUtils.fieldRefTargetDomainSetLambda(fieldName,refName,fk.fkTargetColumn)/>
                     <#if fk.fkSourceColumn??>
                     ${lambdaClassName}.${refTargetLambdaSetter}.accept(refDomain, <#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${refSourceLambda}.apply(this))<#else>(${fk.fkTargetColumnType})${lambdaClassName}.${refSourceLambda}.apply(this)</#if>);
                     <#else>
                     ${lambdaClassName}.${refTargetLambdaSetter}.accept(refDomain, ${fk.sourceValue});
                     </#if>
                </#list>
                     list.add(refDomain);
                }
                if(saveState.equals(SaveState.INSERT)){
                    //新增關聯數據, 更新loadFlag=true
                    domain.getLoadFlag().${NameUtils.getFieldWithPrefix(refTable.tableName,"load")} = true;
                }

                //將關聯信息插入到主domain中
                if (CollectionUtil.isNotEmpty(domain.${sourceDomainGetterList}())) {
                    domain.${sourceDomainGetterList}().addAll(list);
                } else {
                    domain.${sourceDomainSetterList}(list);
                }
            }
            </#list>
        }
    </#if>
    }
    </#list>

<#--    聚合-->
<#if source.aggregate??>
    <#assign relateClassName= NameUtils.dataTOName(source.aggregate.name)/>
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ${relateClassName} extends BaseDomain<#if (source.aggregate.implement??) && (source.aggregate.implement!='')> implements ${source.aggregate.implement}</#if>{
    <#list source.aggregate.column as column>
        /**
        * ${column.comment}
        */
        @ApiModelProperty(value =  "${column.comment}")
        private ${column.type} ${NameUtils.getFieldName(column.name)};
    </#list>
    }
</#if>

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
    <#list source.relatedTable as relateTable>

        /**
        * 加載${NameUtils.dataTOName(relateTable.name)}
        */
        @ApiModelProperty(value =  "加載${NameUtils.dataTOName(relateTable.name)}")
        private Boolean ${NameUtils.getFieldWithPrefix(relateTable.name,"load")};
    </#list>

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

    <#list source.relatedTable as relateTable>
    <#assign relateClassName= NameUtils.dataTOName(relateTable.name)/>
            // 合併${NameUtils.dataTOName(relateTable.name)}
            if ((null == loadFlag.${NameUtils.getFieldWithPrefix(relateTable.name,"load")} || BooleanUtil.isFalse(loadFlag.${NameUtils.getFieldWithPrefix(relateTable.name,"load")})) &&
                    BooleanUtil.isTrue(loadFlagSource.${NameUtils.getFieldWithPrefix(relateTable.name,"load")})) {
                loadFlag.${NameUtils.getFieldWithPrefix(relateTable.name,"load")} = true;
                loadFlag.addFilters(FiltersUtils.getEntityFiltersEx(loadFlagSource.getFilters(), ${className}.${relateClassName}.class));
            }
    </#list>
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
    public static ${className} load(Serializable key, ${serviceClassName} service) {
        ${className} domain = service.find(${NameUtils.getName(source.name)}FindDomain.builder().key(key).build());
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
    public static ${className} loadByKey(Serializable key, SFunction<${className}, Serializable> keyLambda, ${serviceClassName} service) {
        ${className} domain = service.findByKey(${NameUtils.getName(source.name)}FindDomain.builder().key(key).build(), keyLambda);
        domain._service = service;
        return domain;
    }

    /**
     * 加載關聯數據
     * @param tClass
     * @param  filters
     * @param ignoreDomainFilter 是否忽略模型的外鍵過濾， 用於特殊場景
     * @return
     * @param <T>
     */
    @Override
    public <T> ${className} loadRelated(Class<T> tClass, List<LambdaFilter<T>> filters, LambdaOrder<T> orders, Boolean ignoreDomainFilter) {
        LoadFlag.LoadFlagBuilder builder = LoadFlag.builder();
    <#list source.relatedTable as relateTable>
        <#assign relateDtoClassName=NameUtils.dataTOName(relateTable.name)/>
        <#assign relatedLoadPropertyName=NameUtils.getFieldWithPrefix(relateTable.name,"load")/>
        if (tClass.equals(${relateDtoClassName}.class)) {
            builder.${relatedLoadPropertyName} = true;
        }
    </#list>
        LoadFlag loadFlag = builder.build().addFilters(FiltersUtils.buildLambdaFilter(filters));
        loadFlag.setOrder(orders);
        loadFlag.setIgnoreDomainFilter(ignoreDomainFilter);
        return this._service.find(this, loadFlag);
    }
</#if>
}
