package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service.impl;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.lambdaexp.*;
import ${corePackage}.service.impl.*;
import ${corePackage}.lambda.query.LambdaQuery;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
<#if (source.relatedTable?size>0)>
import ${corePackage}.uitls.*;
import ${corePackage}.lambda.*;
import cn.hutool.core.collection.*;
import cn.hutool.core.util.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
<#else>
import ${corePackage}.repository.*;
</#if>


<#assign serviceClassName=NameUtils.serviceName(source.name)/>
<#assign serviceImplClassName=NameUtils.serviceImplName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
<#assign repositoryName=NameUtils.getFieldName(repositoryClassName)/>
<#assign lambdaClassName=NameUtils.lambdaExpName(source.name)/>
<#assign sourceLambda=NameUtils.fieldSourceLambda(source.name)/>

@Service
public class ${serviceImplClassName} extends <#if (source.relatedTable?size>0)>BaseDomainServiceImpl<#else>BaseSimpleDomainServiceImpl<${dtoClassName}></#if> implements ${serviceClassName} {
<#--    注入Repository-->
    @Autowired
    private ${repositoryClassName} ${repositoryName};

<#list source.relatedTableDistinct as relateTable>
    <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.tableName)/>
    @Autowired
    private ${relateRepositoryClassName} ${NameUtils.getFieldName(relateRepositoryClassName)};

</#list>
<#if (source.relatedTable?size>0)>
    @PostConstruct
    public void init(){
        this._DomainRepositoryMap.put(${dtoClassName}.class.getCanonicalName(), this.${repositoryName});
    <#list source.relatedTable as relateTable>
        <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.tableName)/>
        <#assign relateDtoClassName=NameUtils.dataTOName(relateTable.name)/>
        this._DomainRepositoryMap.put(${dtoClassName}.${relateDtoClassName}.class.getCanonicalName(), this.${NameUtils.getFieldName(relateRepositoryClassName)});
    </#list>
    }
<#else>
    public BaseRepository getRepository() {
        return ${repositoryName};
    }
</#if>

   /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public ${dtoClassName} find(${domainName}FindDomain request){
    <#if (source.relatedTable?size>0)>
        ${dtoClassName} domain = ${repositoryName}.query(request.getKey(), ${lambdaClassName}.dtoKeyLambda);
        if(ObjectUtil.isNull(domain)){
            return domain;
        }
        return find(domain, request.getLoadFlag());
    <#else>
        return ${repositoryName}.query(request.getKey(), ${lambdaClassName}.dtoKeyLambda);
    </#if>
    }

<#if (source.relatedTable?size>0)>
    public ${dtoClassName} find(${dtoClassName} response,${dtoClassName}.LoadFlag loadFlag){
        final ${dtoClassName} resp = response;
        if (ObjectUtil.isNotNull(loadFlag)) {
            <#list source.relatedTable as relateTable>
                <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
                <#assign relatedDtoClassName=NameUtils.dataTOName(relateTable.name)/>
                <#assign loadProperty=NameUtils.getFieldWithPrefix(relatedDtoClassName,"getLoad")/>
                <#assign relateName=NameUtils.getName(relateTable.name)/>
                <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
                <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
                <#assign relatetargetLambda=NameUtils.fieldTargetLambda(relateFieldName)/>
                <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>
                <#assign setRelatedPropertyList=NameUtils.genListSetter(relateTable.name)/>
                <#assign getRelatedPropertyList=NameUtils.genListGetter(relateTable.name)/>
            if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.${loadProperty}())){
                 <#if (relateTable.fkList?size>0)>
                LambdaQuery<${dtoClassName}.${relatedDtoClassName}> lambdaQuery = LambdaQuery.of(${dtoClassName}.${relatedDtoClassName}.class);
                    <#list relateTable.fkList as fk>
                     <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                     <#assign relatedTargetLambda=NameUtils.fieldRelatedTargetLambda(relateTable.name,fk.fkTargetColumn)/>
                     <#if fk.fkSourceColumn??>
                lambdaQuery.eq(${lambdaClassName}.${relatedTargetLambda},<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(this))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(resp)</#if>);
                     <#else>
                lambdaQuery.eq(${lambdaClassName}.${relatedTargetLambda},${fk.sourceValue});
                     </#if>
                    </#list>
                 </#if>
                <#if relateTable.many>
                List<${dtoClassName}.${relatedDtoClassName}> queryList = ${NameUtils.getFieldName(relateRepositoryClassName)}.queryList(
                    LambdaQueryUtils.combine(lambdaQuery, FiltersUtils.getEntityFilters(loadFlag.getFilters(), ${dtoClassName}.${relatedDtoClassName}.class),
                                 OrdersUtils.getEntityOrders(loadFlag.getOrders(), ${dtoClassName}.${relatedDtoClassName}.class)))
                                            .stream().peek(x -> x.set_thisDomain(resp)).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(resp.${getRelatedPropertyList}())){
                    resp.${setRelatedPropertyList}(queryList);
                } else {
                    resp.${getRelatedPropertyList}().addAll(queryList);
                }
                <#else>
                ${dtoClassName}.${relatedDtoClassName} item= ${NameUtils.getFieldName(relateRepositoryClassName)}.query(
                    LambdaQueryUtils.combine(lambdaQuery, FiltersUtils.getEntityFilters(loadFlag.getFilters(), ${dtoClassName}.${relatedDtoClassName}.class),
                                 OrdersUtils.getEntityOrders(loadFlag.getOrders(), ${dtoClassName}.${relatedDtoClassName}.class)));
                if(ObjectUtil.isNotNull(item)){
                    item.set_thisDomain(resp);
                }
                resp.${setRelatedProperty}(item);
                </#if>
            }
            </#list>
        }
        resp.setLoadFlag(${dtoClassName}.LoadFlag.merge(loadFlag, resp.getLoadFlag()));
        return resp;
    }
</#if>

    /**
     * 查找
     * @param request 请求体
     * @param keyLambda 請求key參數對應的字段的lambda表達式
     * @return
     */
    @Override
    public ${dtoClassName} findByKey(${domainName}FindDomain request, SFunction<${dtoClassName}, Serializable> keyLambda){
    <#if (source.relatedTable?size>0)>
        return find(${repositoryName}.query(request.getKey(), keyLambda), request.getLoadFlag());
    <#else>
        return ${repositoryName}.query(request.getKey(), keyLambda);
    </#if>
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${source.mainTable.keyType} insert(${dtoClassName} request){
        //插入数据
        ${dtoClassName} domain = ${repositoryName}.insert(request);
    <#list source.relatedTable as relateTable>
        <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
        <#assign getter=NameUtils.genGetter(relateTable.name)/>
        <#assign getterList=NameUtils.genListGetter(relateTable.name)/>
        <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
        <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
        <#assign targetSetLambda=NameUtils.fieldTargetSetLambda(relateFieldName)/>
        <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>

        //插入关联数据${relateTable.name}
        <#if relateTable.many>
        if(CollUtil.isNotEmpty(request.${getterList}())){
            <#if (relateTable.fkList?size>0 || relateTable.redundancyList?size>0)>
            request.${getterList}().forEach(x->{
                <#list relateTable.fkList as fk>
                 <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                 <#assign relatedTargetSetLambda=NameUtils.fieldRelatedTargetSetLambda(relateTable.name,fk.fkTargetColumn)/>
                    <#if fk.fkSourceColumn??>
                ${lambdaClassName}.${relatedTargetSetLambda}.accept(x, (${fk.fkTargetColumnType})<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(domain))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(domain)</#if>);
                         <#else>
                ${lambdaClassName}.${relatedTargetSetLambda}.accept(x, ${fk.sourceValue});
                         </#if>
                </#list>
                <#list relateTable.redundancyList as fk>
                 <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                 <#assign relatedTargetSetLambda=NameUtils.fieldRelatedTargetSetLambda(relateTable.name,fk.fkTargetColumn)/>
                    <#if fk.fkSourceColumn??>
                ${lambdaClassName}.${relatedTargetSetLambda}.accept(x, (${fk.fkTargetColumnType})<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(domain))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(domain)</#if>);
                         <#else>
                ${lambdaClassName}.${relatedTargetSetLambda}.accept(x, ${fk.sourceValue});
                         </#if>
                </#list>
            });
             </#if>
            ${NameUtils.getFieldName(relateRepositoryClassName)}.insert(request.${getterList}());
        }
        <#else>
        if(ObjectUtil.isNotNull(request.${getter}())){
            <#if (relateTable.fkList?size>0 || relateTable.redundancyList?size>0)>
                <#list relateTable.fkList as fk>
                 <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                 <#assign relatedTargetSetLambda=NameUtils.fieldRelatedTargetSetLambda(relateTable.name,fk.fkTargetColumn)/>
                    <#if fk.fkSourceColumn??>
            ${lambdaClassName}.${relatedTargetSetLambda}.accept(request.${getter}(), (${fk.fkTargetColumnType})<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(domain))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(domain)</#if>);
                     <#else>
            ${lambdaClassName}.${relatedTargetSetLambda}.accept(request.${getter}(), ${fk.sourceValue});
                     </#if>
                </#list>
                <#list relateTable.redundancyList as fk>
                 <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                 <#assign relatedTargetSetLambda=NameUtils.fieldRelatedTargetSetLambda(relateTable.name,fk.fkTargetColumn)/>
                    <#if fk.fkSourceColumn??>
            ${lambdaClassName}.${relatedTargetSetLambda}.accept(request.${getter}(), (${fk.fkTargetColumnType})<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(domain))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(domain)</#if>);
                     <#else>
            ${lambdaClassName}.${relatedTargetSetLambda}.accept(request.${getter}(), ${fk.sourceValue});
                     </#if>
                </#list>
             </#if>
            ${NameUtils.getFieldName(relateRepositoryClassName)}.insert(request.${getter}());
        }
        </#if>
    </#list>
        return (${source.mainTable.keyType}) ${lambdaClassName}.dtoKeyLambda.apply(domain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(${dtoClassName} request){
<#if (source.relatedTable?size>0)>
        Serializable keyId = ${lambdaClassName}.dtoKeyLambda.apply(request);
        ${dtoClassName} old = find(new ${NameUtils.getName(source.name)}FindDomain(keyId, request.getLoadFlag()));
        return update(request,old);
<#else>
        if(request.getChanged()){
            return ${repositoryName}.update(request) > 0;
        }
        return true;
</#if>
    }
<#if (source.relatedTable?size>0)>
   /**
    * 修改,此方法不用再加載domain主entity數據
    *
    * @param request 请求体
    * @param domain 原始domain
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(${dtoClassName} request, ${dtoClassName} domain){
        ${dtoClassName} old = domain;
<#list source.relatedTable as relateTable>
    <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
    <#assign getter=NameUtils.genGetter(relateTable.name)/>
    <#assign getterList=NameUtils.genListGetter(relateTable.name)/>
    <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
    <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
    <#assign targetSetLambda=NameUtils.fieldTargetSetLambda(relateFieldName)/>
    <#assign targetKeyLambda=NameUtils.fieldTargetKeyLambda(relateFieldName)/>
    <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>
    <#assign relatedDtoClassName=NameUtils.dataTOName(relateTable.name)/>
    <#assign loadProperty=NameUtils.getFieldWithPrefix(relatedDtoClassName,"getLoad")/>
        //更新关联数据${relateTable.name}
        <#if relateTable.many>
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().${loadProperty}()))){
            if(CollUtil.isNotEmpty(request.${getterList}())){
                <#if (relateTable.fkList?size>0 || relateTable.redundancyList?size>0)>
                request.${getterList}().forEach(x->{
                    <#list relateTable.fkList as fk>
                 <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                 <#assign relatedTargetSetLambda=NameUtils.fieldRelatedTargetSetLambda(relateTable.name,fk.fkTargetColumn)/>
                        <#if fk.fkSourceColumn??>
                    ${lambdaClassName}.${relatedTargetSetLambda}.accept(x, (${fk.fkTargetColumnType})<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(request))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(request)</#if>);
                             <#else>
                    ${lambdaClassName}.${relatedTargetSetLambda}.accept(x, ${fk.sourceValue});
                             </#if>
                    </#list>
                    <#list relateTable.redundancyList as fk>
                 <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                 <#assign relatedTargetSetLambda=NameUtils.fieldRelatedTargetSetLambda(relateTable.name,fk.fkTargetColumn)/>
                        <#if fk.fkSourceColumn??>
                    ${lambdaClassName}.${relatedTargetSetLambda}.accept(x, (${fk.fkTargetColumnType})<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(request))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(request)</#if>);
                             <#else>
                    ${lambdaClassName}.${relatedTargetSetLambda}.accept(x, ${fk.sourceValue});
                             </#if>
                    </#list>
                });
                </#if>
            }
            this.merge(old.${getterList}(), request.${getterList}(), ${lambdaClassName}.${targetKeyLambda}, ${NameUtils.getFieldName(relateRepositoryClassName)});
        }
        <#else>
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().${loadProperty}()))){
            if(ObjectUtil.isNotNull(request.${getter}())){
                <#if (relateTable.fkList?size>0 || relateTable.redundancyList?size>0)>
                    <#list relateTable.fkList as fk>
                 <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                 <#assign relatedTargetSetLambda=NameUtils.fieldRelatedTargetSetLambda(relateTable.name,fk.fkTargetColumn)/>
                        <#if fk.fkSourceColumn??>
                ${lambdaClassName}.${relatedTargetSetLambda}.accept(request.${getter}(), (${fk.fkTargetColumnType})<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(request))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(request)</#if>);
                         <#else>
                ${lambdaClassName}.${relatedTargetSetLambda}.accept(request.${getter}(), ${fk.sourceValue});
                         </#if>
                    </#list>
                    <#list relateTable.redundancyList as fk>
                 <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                 <#assign relatedTargetSetLambda=NameUtils.fieldRelatedTargetSetLambda(relateTable.name,fk.fkTargetColumn)/>
                        <#if fk.fkSourceColumn??>
                ${lambdaClassName}.${relatedTargetSetLambda}.accept(request.${getter}(), (${fk.fkTargetColumnType})<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(request))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(request)</#if>);
                         <#else>
                ${lambdaClassName}.${relatedTargetSetLambda}.accept(request.${getter}(), ${fk.sourceValue});
                         </#if>
                    </#list>
                 </#if>
            }
            this.merge(ObjectUtil.isNotNull(old.${getter}())? CollUtil.toList(old.${getter}()):ListUtil.empty(),
                    ObjectUtil.isNotNull(request.${getter}())? CollUtil.toList(request.${getter}()):ListUtil.empty(),
                    ${lambdaClassName}.${targetKeyLambda}, ${NameUtils.getFieldName(relateRepositoryClassName)});
        }
        </#if>
</#list>

        //更新数据
        if(request.getChanged()){
            return ${repositoryName}.update(request) > 0;
        }
        return true;
    }
</#if>

    /**
    * 删除
    * @param id 数据ID
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(${source.mainTable.keyType} id){
        <#if (source.relatedTable?size>0)>
        return delete(id, ${dtoClassName}.LoadFlag.builder().loadAll(true).build());
    }
    /**
    * 删除
    * @param id 数据ID
    * @param loadFlag 數據加載參數
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(${source.mainTable.keyType} id, ${dtoClassName}.LoadFlag loadFlag){
        </#if>
<#if (source.relatedTable?size>0)>
        ${dtoClassName} old = find(new ${NameUtils.getName(source.name)}FindDomain(id ,${dtoClassName}.LoadFlag.builder().build()));
        if (ObjectUtil.isNull(old)) {
            return false;
        }

<#list source.relatedTable as relateTable>
<#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
<#assign getter=NameUtils.genGetter(relateTable.name)/>
<#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
<#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
<#assign relatefieldTargetDomainLambda=NameUtils.fieldTargetDomainLambda(relateFieldName)/>
<#assign relatedDtoClassName=NameUtils.dataTOName(relateTable.name)/>
<#assign loadProperty=NameUtils.getFieldWithPrefix(relatedDtoClassName,"getLoad")/>
<#assign getterList=NameUtils.genListGetter(relateTable.name)/>
<#if relateTable.deletable>
        if(BooleanUtil.isTrue(loadFlag.getLoadAll()) || BooleanUtil.isTrue(loadFlag.${loadProperty}())){
            //删除关联数据${relateTable.name}
            <#if (relateTable.fkList?size>0)>
            LambdaQuery<${dtoClassName}.${relatedDtoClassName}> lambdaQuery = LambdaQuery.of(${dtoClassName}.${relatedDtoClassName}.class);
                <#list relateTable.fkList as fk>
                 <#assign relatedSourceLambda=NameUtils.fieldRelatedSourceLambda(source.mainTable.name,relateTable.name,fk.fkSourceColumn)/>
                 <#assign relatedTargetLambda=NameUtils.fieldRelatedTargetLambda(relateTable.name,fk.fkTargetColumn)/>
                 <#if fk.fkSourceColumn??>
            lambdaQuery.eq(${lambdaClassName}.${relatedTargetLambda},<#if fk.fkSourceConvertMethod?? &&(fk.fkSourceConvertMethod!='')>${fk.fkSourceConvertMethod}(${lambdaClassName}.${relatedSourceLambda}.apply(this))<#else>${lambdaClassName}.${relatedSourceLambda}.apply(old)</#if>);
                 <#else>
            lambdaQuery.eq(${lambdaClassName}.${relatedTargetLambda},${fk.sourceValue});
                 </#if>
                </#list>
             </#if>
            ${NameUtils.getFieldName(relateRepositoryClassName)}.deleteByFilter(lambdaQuery);
        }
</#if>
</#list>
        return ${repositoryName}.delete(CollUtil.newArrayList(old)) > 0;
<#else>
        return ${repositoryName}.deleteById(id) > 0;
</#if>
    }
}
