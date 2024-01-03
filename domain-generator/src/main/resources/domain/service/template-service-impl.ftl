package ${domainPackage!''}.${NameUtils.packageName(source.name)}.service.impl;

<#if source.aggregate??>
import ${domainPackage!''}.${NameUtils.packageName(source.name)}.convertor.*;
</#if>
import ${domainPackage!''}.${NameUtils.packageName(source.name)}.service.*;
import ${domainPackage!''}.${NameUtils.packageName(source.name)}.domain.*;
import ${domainPackage!''}.${NameUtils.packageName(source.name)}.repository.*;
import com.artframework.domain.core.service.impl.*;
import com.artframework.domain.core.uitls.*;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

<#assign serviceClassName=NameUtils.serviceName(source.name)/>
<#assign serviceImplClassName=NameUtils.serviceImplName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
<#assign repositoryName=NameUtils.getFieldName(repositoryClassName)/>
<#assign lambdaClassName=NameUtils.lambdaExpName(source.name)/>
<#assign sourceLambda=NameUtils.fieldSourceLambda(source.name)/>

import java.io.Serializable;
import ${domainPackage!''}.${NameUtils.packageName(source.name)}.lambdaexp.*;

@Service
public class ${serviceImplClassName} extends BaseDomainServiceImpl implements ${serviceClassName} {
<#--    注入Repository-->
    @Autowired
    private ${repositoryClassName} ${repositoryName};

<#list source.relatedTable as relateTable>
    <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
    @Autowired
    private ${repositoryClassName}.${relateRepositoryClassName} ${NameUtils.getFieldName(relateRepositoryClassName)};

</#list>
<#if source.aggregate??>
  <#assign relateRepositoryClassName=NameUtils.repositoryName(source.aggregate.name)/>
    @Autowired
    private ${repositoryClassName}.${relateRepositoryClassName} ${NameUtils.getFieldName(relateRepositoryClassName)};
</#if>
    /**
    * 分页查询
    * @param request 请求体
    * @return
    */
    @Override
    public IPage<${dtoClassName}> page(${domainName}PageDomain request){
        return ${repositoryName}.page(request);
    }

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public ${dtoClassName} find(${domainName}FindDomain request){
<#if source.aggregate??>
    return this.find(request,false);
<#else>
<#if (source.relatedTable?size>0)>
        ${dtoClassName} response = ${repositoryName}.query(request.getKey(), ${lambdaClassName}.doKeyLambda);
        if(ObjectUtil.isNull(response)){
            return response;
        }

        if (ObjectUtil.isNotNull(request.getLoadFlag())) {
            <#list source.relatedTable as relateTable>
                <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
                <#assign loadProperty=NameUtils.getFieldWithPrefix(relateTable.name,"getLoad")/>
                <#assign relateName=NameUtils.getName(relateTable.name)/>
                <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
                <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
                <#assign relatetargetLambda=NameUtils.fieldTargetLambda(relateFieldName)/>
                <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>
                <#assign setRelatedPropertyList=NameUtils.genListSetter(relateTable.name)/>
                <#assign relatedDtoClassName=NameUtils.dataTOName(relateTable.name)/>
            if(request.getLoadFlag().${loadProperty}()){
                Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(response);
                <#if relateTable.many>
                response.${setRelatedPropertyList}(${NameUtils.getFieldName(relateRepositoryClassName)}.queryList(key, ${lambdaClassName}.${relatetargetLambda},
                                FiltersUtils.getEntityFilters(request.getLoadFlag().getFilters(), this.getEntityName(${dtoClassName}.${relatedDtoClassName}.class))));
                <#else>
                response.${setRelatedProperty}(${NameUtils.getFieldName(relateRepositoryClassName)}.query(key, ${lambdaClassName}.${relatetargetLambda}));
                </#if>
            }
            </#list>
        }
        response.setLoadFlag(request.getLoadFlag());
        return response;
    <#else>
        return ${repositoryName}.query(request.getKey(), ${lambdaClassName}.doKeyLambda);
    </#if>
</#if>
    }

    <#if source.aggregate??>
    /**
     * 查找
     * @param request 请求体
     * @return
     */
    @Override
    public ${dtoClassName} find(${domainName}FindDomain request, Boolean loadAggregate){
        ${dtoClassName} response = ${repositoryName}.query(request.getKey(), ${lambdaClassName}.doKeyLambda);
        if(ObjectUtil.isNull(response)){
            return response;
        }

<#if (source.relatedTable?size>0)>
        if (ObjectUtil.isNotNull(request.getLoadFlag())) {
            <#list source.relatedTable as relateTable>
                <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
                <#assign loadProperty=NameUtils.getFieldWithPrefix(relateTable.name,"getLoad")/>
                <#assign relateName=NameUtils.getName(relateTable.name)/>
                <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
                <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
                <#assign relatetargetLambda=NameUtils.fieldTargetLambda(relateFieldName)/>
                <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>
                <#assign setRelatedPropertyList=NameUtils.genListSetter(relateTable.name)/>
            if(request.getLoadFlag().${loadProperty}()){
                Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(response);
                <#if relateTable.many>
                response.${setRelatedPropertyList}(${NameUtils.getFieldName(relateRepositoryClassName)}.queryList(key, ${lambdaClassName}.${relatetargetLambda},
                                LoadFiltersUtils.getEntityFilters(request.getLoadFlag().getFilters(),"${relateName}")));
                <#else>
                response.${setRelatedProperty}(${NameUtils.getFieldName(relateRepositoryClassName)}.query(key, ${lambdaClassName}.${relatetargetLambda}));
                </#if>
            }
            </#list>
        }
        response.setLoadFlag(request.getLoadFlag());
</#if>
<#if source.aggregate??>
        <#assign relateRepositoryClassName=NameUtils.repositoryName(source.aggregate.name)/>
        <#assign relateFieldName=NameUtils.getFieldName(source.aggregate.name)/>
        <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
        <#assign relatetargetLambda=NameUtils.fieldTargetLambda(relateFieldName)/>
        <#assign setRelatedProperty=NameUtils.genSetter(source.aggregate.name)/>
        //聚合
        if(loadAggregate){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(response);
            response.${setRelatedProperty}(${NameUtils.getFieldName(relateRepositoryClassName)}.query(key, ${lambdaClassName}.${relatetargetLambda}));
        }
</#if>
        return response;
    }
    </#if>

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${source.mainTable.keyType} insert(${domainName}CreateDomain request){
    <#list source.relatedTable as relateTable>
        <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
        <#assign getter=NameUtils.genGetter(relateTable.name)/>
        <#assign getterList=NameUtils.genListGetter(relateTable.name)/>
        <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
        <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
        <#assign targetSetLambda=NameUtils.fieldTargetSetLambda(relateFieldName)/>
        <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>
        <#assign setRelatedTargetProperty=NameUtils.genGetter(relateTable.fkTargetColumn)/>
        //插入关联数据${relateTable.name}
        <#if relateTable.many>
        if(CollUtil.isNotEmpty(request.${getterList}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
            request.${getterList}().forEach(x->${lambdaClassName}.${targetSetLambda}.accept(x,(${relateTable.fkTargetColumnType})key));
            ${NameUtils.getFieldName(relateRepositoryClassName)}.insert(request.${getterList}());
        }
        <#else>
        if(ObjectUtil.isNotNull(request.${getter}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
            ${lambdaClassName}.${targetSetLambda}.accept(request.${getter}(),(${relateTable.fkTargetColumnType})key);
            ${NameUtils.getFieldName(relateRepositoryClassName)}.insert(request.${getter}());
        }
        </#if>
    </#list>
     <#if source.aggregate??>
        //聚合數據
        <#assign className=NameUtils.dataTOName(source.name)/>
        <#assign relateClassName= NameUtils.dataTOName(source.aggregate.name)/>
        <#assign decoratorName=NameUtils.covertDecoratorName(source.name)/>
        <#assign relateRepositoryClassName=NameUtils.repositoryName(source.aggregate.name)/>
        ${className}.${relateClassName} aggregateDTO = new ${className}.${relateClassName}();
        ${decoratorName}.convertAggregate(request, aggregateDTO);
        ${NameUtils.getFieldName(relateRepositoryClassName)}.insert(aggregateDTO);
     </#if>
        //插入数据
        ${dtoClassName} dto = ${repositoryName}.insert(request);
        return (${source.mainTable.keyType}) ${lambdaClassName}.dtoKeyLambda.apply(dto);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(${domainName}UpdateDomain request){
<#if (source.relatedTable?size>0)>
        Serializable keyId = ${lambdaClassName}.dtoKeyLambda.apply(request);
        ${dtoClassName} old = find(new ${NameUtils.getName(source.name)}FindDomain(keyId, request.getLoadFlag())<#if source.aggregate??>,true</#if>);
</#if>
<#list source.relatedTable as relateTable>
    <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
    <#assign getter=NameUtils.genGetter(relateTable.name)/>
    <#assign getterList=NameUtils.genListGetter(relateTable.name)/>
    <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
    <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
    <#assign targetSetLambda=NameUtils.fieldTargetSetLambda(relateFieldName)/>
    <#assign targetKeyLambda=NameUtils.fieldTargetKeyLambda(relateFieldName)/>
    <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>
    <#assign loadProperty=NameUtils.getFieldWithPrefix(relateTable.name,"getLoad")/>
    <#assign setRelatedTargetProperty=NameUtils.genGetter(relateTable.fkTargetColumn)/>
        <#if relateTable.many>
        //更新关联数据${relateTable.name}
        if(CollUtil.isNotEmpty(request.${getterList}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
            request.${getterList}().forEach(x->${lambdaClassName}.${targetSetLambda}.accept(x,(${relateTable.fkTargetColumnType})key));
            this.merge(old.${getterList}(), request.${getterList}(), ${lambdaClassName}.${targetKeyLambda}, ${NameUtils.getFieldName(relateRepositoryClassName)});
        }
        <#else>
        if(ObjectUtil.isNotNull(request.getLoadFlag()) && request.getLoadFlag().${loadProperty}()
            && ObjectUtil.isNotNull(request.${getter}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
            ${lambdaClassName}.${targetSetLambda}.accept(request.${getter}(),(${relateTable.fkTargetColumnType})key);
            if(BooleanUtil.isTrue(request.${getter}().getChanged())){
                ${NameUtils.getFieldName(relateRepositoryClassName)}.update(request.${getter}());
            }
        }
        </#if>
</#list>

     <#if source.aggregate??>
        //聚合數據
        <#assign className=NameUtils.dataTOName(source.name)/>
        <#assign relateClassName= NameUtils.dataTOName(source.aggregate.name)/>
        <#assign decoratorName=NameUtils.covertDecoratorName(source.name)/>
        <#assign relateRepositoryClassName=NameUtils.repositoryName(source.aggregate.name)/>
        <#assign fieldName=NameUtils.getFieldName(source.aggregate.name)/>
        ${className}.${relateClassName} aggregateDTO = ObjectUtil.isNull(old.${NameUtils.genGetter(source.aggregate.name)}()) ? new ${className}.${relateClassName}() : old.${NameUtils.genGetter(source.aggregate.name)}();
        ${decoratorName}.convertAggregate(request, aggregateDTO);
        ${NameUtils.getFieldName(relateRepositoryClassName)}.update(aggregateDTO);
     </#if>

        //更新数据
        ${repositoryName}.update(request);
        return true;
    }

    /**
    * 删除
    * @param id 数据ID
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(${source.mainTable.keyType} id){
<#if (source.relatedTable?size>0)>
        ${dtoClassName} old = find(new ${NameUtils.getName(source.name)}FindDomain(id ,new ${dtoClassName}.LoadFlag())<#if source.aggregate??>,true</#if>);

<#list source.relatedTable as relateTable>
<#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
<#assign getter=NameUtils.genGetter(relateTable.name)/>
<#assign getterList=NameUtils.genListGetter(relateTable.name)/>
        //删除关联数据${relateTable.name}
        <#if relateTable.many>
        if(CollUtil.isNotEmpty(old.${getterList}())){
            ${NameUtils.getFieldName(relateRepositoryClassName)}.delete(old.${getterList}());
        }
        <#else>
        if(ObjectUtil.isNotNull(old.${getter}())){
            ${NameUtils.getFieldName(relateRepositoryClassName)}.delete(CollUtil.newArrayList(old.${getter}()));
        }
        </#if>
</#list>
<#if source.aggregate??>
<#assign relateRepositoryClassName=NameUtils.repositoryName(source.aggregate.name)/>
<#assign getter=NameUtils.genGetter(source.aggregate.name)/>
        //删除聚合數據
        if(ObjectUtil.isNotNull(old.${getter}())){
            ${NameUtils.getFieldName(relateRepositoryClassName)}.delete(CollUtil.newArrayList(old.${getter}()));
        }
</#if>
        return ${repositoryName}.delete(CollUtil.newArrayList(old)) > 0;
<#else>
        return ${repositoryName}.deleteById(id) > 0;
</#if>
    }
}
