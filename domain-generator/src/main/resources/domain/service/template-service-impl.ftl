package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.service.impl;

import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.service.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.repository.*;
import com.artframework.domain.core.service.impl.*;

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
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.lambdaexp.*;

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
    /**
    * 分页查询
    * @param request 请求体
    * @return
    */
    @Override
    public IPage<${dtoClassName}> page(${domainName}PageRequest request){
        return ${repositoryName}.page(request);
    }

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public ${dtoClassName} find(${domainName}FindRequest request){
        ${dtoClassName} response = ${repositoryName}.query(request.getKey(), ${lambdaClassName}.doKeyLambda);
        if (ObjectUtil.isNotNull(request.getLoadFlag())) {
            <#list source.relatedTable as relateTable>
                <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
                <#assign loadProperty=NameUtils.getFieldWithPrefix(relateTable.name,"getLoad")/>
                <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
                <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
                <#assign relatetargetLambda=NameUtils.fieldTargetLambda(relateFieldName)/>
                <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>
            if(request.getLoadFlag().${loadProperty}()){
                Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(response);
                response.${setRelatedProperty}(${NameUtils.getFieldName(relateRepositoryClassName)}.${relateTable.many? string('queryList',"query")}(key, ${lambdaClassName}.${relatetargetLambda}));
            }

            </#list>
        }
        return response;
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ${source.mainTable.keyType} insert(${domainName}CreateRequest request){
        //插入数据
        ${repositoryName}.insert(request);

    <#list source.relatedTable as relateTable>
        <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
        <#assign getter=NameUtils.genGetter(relateTable.name)/>
        <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
        <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
        <#assign targetSetLambda=NameUtils.fieldTargetSetLambda(relateFieldName)/>
        <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>
        <#assign setRelatedTargetProperty=NameUtils.genGetter(relateTable.fkTargetColumn)/>
        //插入关联数据${relateTable.name}
        <#if relateTable.many>
        if(CollUtil.isNotEmpty(request.${getter}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
            request.${getter}().forEach(x->${lambdaClassName}.${targetSetLambda}.accept(x,(${relateTable.fkTargetColumnType})key));
            ${NameUtils.getFieldName(relateRepositoryClassName)}.insert(request.${getter}());
        }
        <#else>
        if(ObjectUtil.isNotNull(request.${getter}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
            ${lambdaClassName}.${targetSetLambda}.accept(request.${getter}(),(${relateTable.fkTargetColumnType})key);
            ${NameUtils.getFieldName(relateRepositoryClassName)}.insert(request.${getter}());
        }
        </#if>
    </#list>
        return (${source.mainTable.keyType}) UserLambdaExp.dtoKeyLambda.apply(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(${domainName}UpdateRequest request){
        Serializable keyId = ${lambdaClassName}.dtoKeyLambda.apply(request);
        ${dtoClassName} old = find(new ${NameUtils.getName(source.name)}FindRequest(keyId, request.getLoadFlag()));
        if(request.getChanged()){
            //更新数据
            ${repositoryName}.update(request);
        }
<#list source.relatedTable as relateTable>
    <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
    <#assign getter=NameUtils.genGetter(relateTable.name)/>
    <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
    <#assign relatesourceLambda=NameUtils.fieldSourceLambda(relateFieldName)/>
    <#assign targetSetLambda=NameUtils.fieldTargetSetLambda(relateFieldName)/>
    <#assign targetKeyLambda=NameUtils.fieldTargetKeyLambda(relateFieldName)/>
    <#assign setRelatedProperty=NameUtils.genSetter(relateTable.name)/>
    <#assign setRelatedTargetProperty=NameUtils.genGetter(relateTable.fkTargetColumn)/>
        <#if relateTable.many>
        //更新关联数据${relateTable.name}
        if(CollUtil.isNotEmpty(request.${getter}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
            request.${getter}().forEach(x->${lambdaClassName}.${targetSetLambda}.accept(x,(${relateTable.fkTargetColumnType})key));
            this.merge(old.${getter}(), request.${getter}(), UserLambdaExp.${targetKeyLambda}, ${NameUtils.getFieldName(relateRepositoryClassName)});
        }
        <#else>
        if(ObjectUtil.isNotNull(request.${getter}())){
            Serializable key = ${lambdaClassName}.${relatesourceLambda}.apply(request);
            ${lambdaClassName}.${targetSetLambda}.accept(request.${getter}(),(${relateTable.fkTargetColumnType})key);
            if(request.${getter}().getChanged()){
                ${NameUtils.getFieldName(relateRepositoryClassName)}.update(request.${getter}());
            }
        }
        </#if>
</#list>
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
        ${dtoClassName} old = find(new ${NameUtils.getName(source.name)}FindRequest(id ,new ${dtoClassName}.LoadFlag()));

<#list source.relatedTable as relateTable>
<#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
<#assign getter=NameUtils.genGetter(relateTable.name)/>
        //删除关联数据${relateTable.name}
        <#if relateTable.many>
        if(CollUtil.isNotEmpty(old.${getter}())){
            ${NameUtils.getFieldName(relateRepositoryClassName)}.delete(old.${getter}());
        }
        <#else>
        if(ObjectUtil.isNotNull(old.${getter}())){
            ${NameUtils.getFieldName(relateRepositoryClassName)}.delete(CollUtil.newArrayList(old.${getter}()));
        }
        </#if>
</#list>

        return ${repositoryName}.delete(CollUtil.newArrayList(old)) > 0;
    }
}
