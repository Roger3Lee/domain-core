package ${applicationPackage!''};

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;

<#assign appServiceClassName=NameUtils.appServiceName(source.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
public interface ${appServiceClassName} {

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    ${dtoClassName} find(${domainName}FindDomain request);

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

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    Boolean delete(${source.mainTable.keyType} key);
}
