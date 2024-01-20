package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service.impl;

<#if source.aggregate??>
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.convertor.*;
</#if>
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository.*;
import ${corePackage}.service.impl.*;
import ${corePackage}.uitls.*;

import ${corePackage}.domain.*;
import ${corePackage}.repository.BaseRepository;
import cn.hutool.core.collection.*;
import cn.hutool.core.util.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.lambdaexp.*;

import javax.annotation.PostConstruct;

<#assign serviceClassName=NameUtils.serviceName(source.name)/>
<#assign serviceImplClassName=NameUtils.serviceImplName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
<#assign repositoryName=NameUtils.getFieldName(repositoryClassName)/>
<#assign lambdaClassName=NameUtils.lambdaExpName(source.name)/>
<#assign sourceLambda=NameUtils.fieldSourceLambda(source.name)/>

@Service
public class ${serviceImplClassName} extends BaseDomainServiceImpl implements ${serviceClassName} {
<#if (source.relatedTable?size>0)>
    //用於保存實體和repository之間的關係
    private final Map<String, BaseRepository> _DomainRepositoryMap = new HashMap<>();
</#if>
<#--    注入Repository-->
    @Autowired
    private ${repositoryClassName} ${repositoryName};

<#list source.relatedTable as relateTable>
    <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
    @Autowired
    private ${relateRepositoryClassName} ${NameUtils.getFieldName(relateRepositoryClassName)};

</#list>
<#if source.aggregate??>
  <#assign relateRepositoryClassName=NameUtils.repositoryName(source.aggregate.name)/>
    @Autowired
    private ${relateRepositoryClassName} ${NameUtils.getFieldName(relateRepositoryClassName)};
</#if>
<#if (source.relatedTable?size>0)>
    @PostConstruct
    public void init(){
    <#list source.relatedTable as relateTable>
        <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
        <#assign relateDtoClassName=NameUtils.dataTOName(relateTable.name)/>
        _DomainRepositoryMap.put(FiltersUtils.getEntityName(${dtoClassName}.${relateDtoClassName}.class), this.${NameUtils.getFieldName(relateRepositoryClassName)});
    </#list>
    <#if source.aggregate??>
      <#assign relateRepositoryClassName=NameUtils.repositoryName(source.aggregate.name)/>
      <#assign relateDtoClassName=NameUtils.dataTOName(source.aggregate.name)/>
        _DomainRepositoryMap.put(FiltersUtils.getEntityName(${dtoClassName}.${relateDtoClassName}.class), this.${NameUtils.getFieldName(relateRepositoryClassName)});
    </#if>
    }
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
        return find(request, null);
    }

    /**
    * 查找
    * @param request 请求体
    * @param response 原始數據，避免重新查詢主表
    * @return
    */
    @Override
    public ${dtoClassName} find(${domainName}FindDomain request, ${dtoClassName} response){
<#if source.aggregate??>
    return this.find(request, response, true);
<#else>
<#if (source.relatedTable?size>0)>
        if(ObjectUtil.isNull(response)){
            response = ${repositoryName}.query(request.getKey(), ${lambdaClassName}.doKeyLambda);
            if(ObjectUtil.isNull(response)){
                return response;
            }
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
            if(BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().${loadProperty}())){
                Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(response);
                if(ObjectUtil.isNotNull(key)){
                    <#if relateTable.many>
                    response.${setRelatedPropertyList}(${NameUtils.getFieldName(relateRepositoryClassName)}.queryList(key, ${lambdaClassName}.${relatetargetLambda},
                                    FiltersUtils.getEntityFilters(request.getLoadFlag().getFilters(), ${dtoClassName}.${relatedDtoClassName}.class)));
                    <#else>
                    response.${setRelatedProperty}(${NameUtils.getFieldName(relateRepositoryClassName)}.query(key, ${lambdaClassName}.${relatetargetLambda}));
                    </#if>
                }
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
    public ${dtoClassName} find(${domainName}FindDomain request, ${dtoClassName} domain, Boolean loadAggregate){
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
            if(BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().${loadProperty}())){
                Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(response);
                if(ObjectUtil.isNotNull(key)){
                    <#if relateTable.many>
                    response.${setRelatedPropertyList}(${NameUtils.getFieldName(relateRepositoryClassName)}.queryList(key, ${lambdaClassName}.${relatetargetLambda},
                                    LoadFiltersUtils.getEntityFilters(request.getLoadFlag().getFilters(),"${relateName}")));
                    <#else>
                    response.${setRelatedProperty}(${NameUtils.getFieldName(relateRepositoryClassName)}.query(key, ${lambdaClassName}.${relatetargetLambda}));
                    </#if>
                }
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
        <#assign setRelatedTargetProperty=NameUtils.genGetter(relateTable.fkTargetColumn)/>

        //插入关联数据${relateTable.name}
        <#if relateTable.many>
        if(CollUtil.isNotEmpty(request.${getterList}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(domain);
            request.${getterList}().forEach(x->${lambdaClassName}.${targetSetLambda}.accept(x,(${relateTable.fkTargetColumnType})key));
            ${NameUtils.getFieldName(relateRepositoryClassName)}.insert(request.${getterList}());
        }
        <#else>
        if(ObjectUtil.isNotNull(request.${getter}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(domain);
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
        return (${source.mainTable.keyType}) ${lambdaClassName}.dtoKeyLambda.apply(domain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(${dtoClassName} request){
<#if (source.relatedTable?size>0)>
        Serializable keyId = ${lambdaClassName}.dtoKeyLambda.apply(request);
        ${dtoClassName} old = find(new ${NameUtils.getName(source.name)}FindDomain(keyId, null)<#if source.aggregate??>,true</#if>);
        return update(request,old);
<#else>
        if(request.getChanged()){
            ${repositoryName}.update(request);
        }
        return true;
</#if>
    }
<#if (source.relatedTable?size>0)>
    /**
    * 修改
    * @param request 请求体
    * @param domain 原始數據，避免重新查詢主表
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(${dtoClassName} request, ${dtoClassName} domain){
        Serializable keyId = ${lambdaClassName}.dtoKeyLambda.apply(request);
        ${dtoClassName} old = find(new ${NameUtils.getName(source.name)}FindDomain(keyId, request.getLoadFlag()), domain<#if source.aggregate??>,true</#if>);
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
        //更新关联数据${relateTable.name}
        <#if relateTable.many>
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().${loadProperty}()))){
            if(CollUtil.isNotEmpty(request.${getterList}())){
                Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
                request.${getterList}().forEach(x->${lambdaClassName}.${targetSetLambda}.accept(x,(${relateTable.fkTargetColumnType})key));
            }
            this.merge(old.${getterList}(), request.${getterList}(), ${lambdaClassName}.${targetKeyLambda}, ${NameUtils.getFieldName(relateRepositoryClassName)});
        }
        <#else>
        if(ObjectUtil.isNotNull(request.getLoadFlag())
            && (BooleanUtil.isTrue(request.getLoadFlag().getLoadAll()) || BooleanUtil.isTrue(request.getLoadFlag().${loadProperty}()))){
            if(ObjectUtil.isNotNull(request.${getter}())){
                Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
                ${lambdaClassName}.${targetSetLambda}.accept(request.${getter}(),(${relateTable.fkTargetColumnType})key);
            }
            this.merge(ObjectUtil.isNotNull(old.${getter}())? CollUtil.toList(old.${getter}()):ListUtil.empty(),
                    ObjectUtil.isNotNull(request.${getter}())? CollUtil.toList(request.${getter}()):ListUtil.empty(),
                    ${lambdaClassName}.${targetKeyLambda}, ${NameUtils.getFieldName(relateRepositoryClassName)});
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
        if(request.getChanged()){
            ${repositoryName}.update(request);
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
        ${dtoClassName} old = find(new ${NameUtils.getName(source.name)}FindDomain(id ,${dtoClassName}.LoadFlag.builder().build())<#if source.aggregate??>,true</#if>);
        if (ObjectUtil.isNull(old)) {
            return false;
        }

<#list source.relatedTable as relateTable>
<#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
<#assign getter=NameUtils.genGetter(relateTable.name)/>
<#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
<#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
<#assign relatefieldTargetDomainLambda=NameUtils.fieldTargetDomainLambda(relateFieldName)/>
<#assign getterList=NameUtils.genListGetter(relateTable.name)/>
<#if relateTable.deletable>
        //删除关联数据${relateTable.name}
        ${NameUtils.getFieldName(relateRepositoryClassName)}.deleteByFilter(ListUtil.toList(FiltersUtils.build(${lambdaClassName}.${relatefieldTargetDomainLambda},
               ${lambdaClassName}.${relatesourceLambda}.apply(old))));
</#if>
</#list>
<#if source.aggregate??>
<#assign relateRepositoryClassName=NameUtils.repositoryName(source.aggregate.name)/>
<#assign getter=NameUtils.genGetter(source.aggregate.name)/>
<#assign relateFieldName=NameUtils.getFieldName(source.aggregate.name)/>
<#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
<#assign relatefieldTargetDomainLambda=NameUtils.fieldTargetDomainLambda(relateFieldName)/>
<#if source.aggregate.deletable>
        //删除聚合數據
        ${NameUtils.getFieldName(relateRepositoryClassName)}.deleteByFilter(ListUtil.toList(FiltersUtils.build(${lambdaClassName}.${relatefieldTargetDomainLambda},
               ${lambdaClassName}.${relatesourceLambda}.apply(old))));
</#if>
</#if>
        return ${repositoryName}.delete(CollUtil.newArrayList(old)) > 0;
<#else>
        return ${repositoryName}.deleteById(id) > 0;
</#if>
    }
<#if (source.relatedTable?size>0)>

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRelated(List<BaseLoadFlag.Filter> filters) {
        if (CollUtil.isEmpty(filters)) {
            return false;
        }

        int totalEffect = 0;
        Map<String, List<BaseLoadFlag.Filter>> filterMap = filters.stream().collect(Collectors.groupingBy(BaseLoadFlag.Filter::getEntity));
        for (Map.Entry<String, List<BaseLoadFlag.Filter>> item : filterMap.entrySet()) {
            BaseRepository repository = _DomainRepositoryMap.get(item.getKey());
            if (null == repository) {
                throw new UnsupportedOperationException("刪除不支持的實體:" + item.getKey());
            }

            totalEffect += repository.deleteByFilter(item.getValue());
        }

        return totalEffect > 0;
    }
</#if>
}
