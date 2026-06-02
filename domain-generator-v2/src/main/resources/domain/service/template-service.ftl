package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;

import ${corePackage}.service.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;

<#assign serviceClassName=NameUtils.serviceName(source.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
public interface ${serviceClassName} extends <#if (source.relatedTable?size>0)>BaseDomainService<#else>BaseSimpleDomainService<${dtoClassName}></#if> {

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    ${dtoClassName} find(${domainName}FindDomain request);

<#if (source.relatedTable?size>0)>
   /**
     * 通過已有實體查找
     * @param response 已有實體
     * @param loadFlag 加載參數
     * @return
     */
    ${dtoClassName} find(${dtoClassName} response, ${dtoClassName}.LoadFlag loadFlag);
</#if>
    /**
     * 查找
     * @param request 请求体
     * @return
     */
    ${dtoClassName} findByKey(${domainName}FindDomain request, SFunction<${dtoClassName}, Serializable> keyLambda);

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    ${source.mainTable.keyType} insert(${dtoClassName} request);

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    Boolean update(${dtoClassName} request);
<#if (source.relatedTable?size>0)>
    /**
    * 修改,此方法不用再加載domain主entity數據
    * @param request 请求体
    * @param domain 原始domain
    * @return 成功OR失败
    */
    Boolean update(${dtoClassName} request, ${dtoClassName} domain);
</#if>
    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    Boolean delete(${source.mainTable.keyType} key);
    <#if (source.relatedTable?size>0)>
    /**
    * 删除
    * @param key 数据ID
    * @param loadFlag 數據加載參數
    * @return 成功OR失败
    */
    Boolean delete(${source.mainTable.keyType} key, ${dtoClassName}.LoadFlag loadFlag);
    </#if>
}
