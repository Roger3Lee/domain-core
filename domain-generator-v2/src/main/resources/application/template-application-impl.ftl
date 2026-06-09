package ${applicationPackage!''};

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<#assign appServiceClassName=NameUtils.appServiceName(source.name)/>
<#assign appServiceImplClassName=NameUtils.appServiceImplName(source.name)/>
<#assign serviceClassName=NameUtils.serviceName(source.name)/>
<#assign serviceFieldName=NameUtils.getFieldName(serviceClassName)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
@Service
public class ${appServiceImplClassName} implements ${appServiceClassName} {

    @Autowired
    private ${serviceClassName} ${serviceFieldName};

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    @Override
    public ${dtoClassName} find(${domainName}FindDomain request){
        return ${serviceFieldName}.find(request);
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @Override
    public ${source.mainTable.keyType} insert(${dtoClassName} request){
        return ${serviceFieldName}.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @Override
    public Boolean update(${dtoClassName} request){
        return ${serviceFieldName}.update(request);
    }

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    @Override
    public Boolean delete(${source.mainTable.keyType} key){
        return ${serviceFieldName}.delete(key);
    }
}
