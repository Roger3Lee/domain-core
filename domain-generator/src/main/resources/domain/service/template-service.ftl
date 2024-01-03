package ${domainPackage!''}.${NameUtils.packageName(source.name)}.service;

import ${domainPackage!''}.${NameUtils.packageName(source.name)}.domain.*;
import mo.gov.dsaj.domain.core.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

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

    <#if source.aggregate??>
    /**
     * 查找
     * @param request 请求体
     * @return
     */
    ${dtoClassName} find(${domainName}FindDomain request, Boolean loadAggregate);
    </#if>

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    ${source.mainTable.keyType} insert(${domainName}CreateDomain request);

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    Boolean update(${domainName}UpdateDomain request);

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    Boolean delete(${source.mainTable.keyType} key);
}
