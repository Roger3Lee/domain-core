package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;

import ${corePackage}.domain.*;
import ${corePackage}.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.List;
import java.io.Serializable;

<#assign serviceClassName=NameUtils.serviceName(source.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
public interface ${serviceClassName} extends BaseDomainService {

    /**
    * 分页查询
    * @param request 请求体
    * @return
    */
    IPage<${dtoClassName}> page(${domainName}PageDomain request);

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    ${dtoClassName} find(${domainName}FindDomain request);

    /**
    * 查找
    * @param request 请求体
    * @param domain 源domain,如果此參數不為空則直接使用此參數作為主實體
    * @return
    */
    ${dtoClassName} find(${domainName}FindDomain request, ${dtoClassName} domain);

    <#if source.aggregate??>
    /**
     * 查找
     * @param request 请求体
     * @param domain 源domain,如果此參數不為空則直接使用此參數作為主實體
     * @return
     */
    ${dtoClassName} find(${domainName}FindDomain request, ${dtoClassName} domain, Boolean loadAggregate);
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
     *
     * @param filters
     * @return
     */
    Boolean deleteRelated(List<BaseLoadFlag.Filter> filters);
</#if>
}
