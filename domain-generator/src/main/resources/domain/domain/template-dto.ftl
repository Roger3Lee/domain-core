package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain;

import ${corePackage}.domain.*;
<#if (source.relatedTable?size>0)>
import ${corePackage}.lambda.query.*;
import ${corePackage}.constants.*;
import ${corePackage}.utils.LambdaQueryUtils;
import ${corePackage}.utils.LoadFlagUtils;
</#if>
import lombok.*;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.convertor.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service.*;
<#if (source.relatedTable?size>0)>
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.lambdaexp.*;
import cn.hutool.core.util.*;
import cn.hutool.core.collection.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
</#if>
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

<#assign covertName=NameUtils.covertName(source.name)/>
<#assign className=NameUtils.dataTOName(source.name)/>
<#assign lambdaClassName=NameUtils.lambdaExpName(source.name)/>
<#assign serviceClassName=NameUtils.serviceName(source.name)/>
/**
* ${source.name}
*
* @author auto
* @version v1.0
*/
@ApiModel(value = "${source.description}")
@NoArgsConstructor
@AllArgsConstructor
public class ${className} extends <#if (source.relatedTable?size>0)>BaseAggregateDomain<${className},${serviceClassName}><#else>BaseDomain</#if><#if (source.implement??) && (source.implement!='')> implements ${source.implement}</#if> {

<#if (source.relatedTable?size>0)>
    public ${className}(${source.mainTable.keyType} key, ${serviceClassName} service){
        this.${source.mainTable.keyColName} = key;
        this._service = service;
    }
</#if>
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

<#if (source.relatedTable?size>0)>
    /**
    * 加载数据標識類
    */
    @Getter
    @Setter
    @ApiModelProperty(value =  "加载数据標識類")
    private LoadFlag loadFlag;
</#if>

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
            if(null == this.${refFieldName}){
                ${className} domain = (${className})this.get_thisDomain();
                if(null == domain){
                    return null;
                }

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
            if(ObjectUtil.isNotEmpty(this.${refFieldName}) && ObjectUtil.isNotEmpty(this.${refFieldName}.getValue())){
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
                 <#list refTable.redundancyList as fk>
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

                //將關聯信息插入到主domain中
                if (CollectionUtil.isNotEmpty(domain.${sourceDomainGetterList}())) {
                    list.forEach(x -> {
                        //引用類型，如果已經存在則不重複加入
                        if (!domain.${sourceDomainGetterList}().contains(x)) {
                            domain.${sourceDomainGetterList}().add(x);
                        }
                    });
                } else {
                    domain.${sourceDomainSetterList}(list);
                }
            }
            </#list>
        }
    </#if>
    }
    </#list>
    /**
     * 加載實體數據
     * @param key
     * @param service
     * @return
     */
    public static ${className} load(Serializable key, ${serviceClassName} service) {
    <#if (source.relatedTable?size>0)>
        ${className} domain = service.find(${NameUtils.getName(source.name)}FindDomain.builder().key(key).build());
        if(ObjectUtil.isNotNull(domain)){
            domain._service = service;
        }
        return domain;
    <#else>
        return service.find(${NameUtils.getName(source.name)}FindDomain.builder().key(key).build());
    </#if>
    }

    /**
     * 加載實體數據 by key
     * @param key
     * @param keyLambda
     * @param service
     * @return
     */
    public static ${className} loadByKey(Serializable key, SFunction<${className}, Serializable> keyLambda, ${serviceClassName} service) {
     <#if (source.relatedTable?size>0)>
        ${className} domain = service.findByKey(${NameUtils.getName(source.name)}FindDomain.builder().key(key).build(), keyLambda);
        if(ObjectUtil.isNotNull(domain)){
            domain._service = service;
        }
        return domain;
     <#else>
        return service.findByKey(${NameUtils.getName(source.name)}FindDomain.builder().key(key).build(), keyLambda);
     </#if>
    }
<#if (source.relatedTable?size>0)>
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
    <#list source.relatedTable as relateTable>
        <#assign relateDtoClassName=NameUtils.dataTOName(relateTable.name)/>
        <#assign relatedLoadPropertyName=NameUtils.getFieldWithPrefix(relateDtoClassName,"load")/>
        if (tClass.equals(${relateDtoClassName}.class)) {
            builder.${relatedLoadPropertyName} = true;
        }
    </#list>
        LoadFlag loadFlag = builder.build();
        LoadFlagUtils.mergeQueryCondition(loadFlag, query, LambdaQueryUtils.getEntityName(tClass));
        this._service.find(this, loadFlag);
    }
</#if>
</#if>

     /**
     * 淺拷貝領域模型
     * @param sourceDomain 源模型
     * @return 返回的模型
     */
    public static ${className} copy(${className} sourceDomain){
        return ${covertName}.INSTANCE.copy(sourceDomain);
    }


<#if (source.relatedTable?size>0)>
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoadFlag extends BaseLoadFlag{
    <#list source.relatedTable as relateTable>
    <#assign relateClassName= NameUtils.dataTOName(relateTable.name)/>
    <#assign relatedLoadPropertyName=NameUtils.getFieldWithPrefix(relateClassName,"load")/>

        /**
        * 加載${NameUtils.dataTOName(relateTable.name)}
        */
        @ApiModelProperty(value =  "加載${relateClassName}")
        private Boolean ${relatedLoadPropertyName};
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
    <#assign relatedLoadPropertyName=NameUtils.getFieldWithPrefix(relateClassName,"load")/>
            // 合併${NameUtils.dataTOName(relateTable.name)}
            if ((null == loadFlag.${relatedLoadPropertyName} || BooleanUtil.isFalse(loadFlag.${relatedLoadPropertyName})) &&
                    BooleanUtil.isTrue(loadFlagSource.${relatedLoadPropertyName})) {
                loadFlag.${relatedLoadPropertyName} = true;
                LoadFlagUtils.mergeEntityQuery(loadFlag, loadFlagSource, LambdaQueryUtils.getEntityName(${className}.${relateClassName}.class));
            }
    </#list>

            return loadFlag;
        }
    }
</#if>
}
