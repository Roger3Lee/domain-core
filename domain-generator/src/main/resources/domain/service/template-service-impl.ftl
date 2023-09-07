package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.service.impl;

import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.service.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.repository.*;
import com.artframework.domain.core.service.impl.*;

import com.artframework.domain.core.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

<#assign serviceClassName=NameUtils.serviceName(source.name)/>
<#assign serviceImplClassName=NameUtils.serviceImplName(source.name)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
@Service
public class ${serviceImplClassName} extends BaseDomainServiceImpl implements ${serviceClassName} {
@Autowired
private ${repositoryClassName} ${NameUtils.getFieldName(repositoryClassName)};
/**
* 分页查询
* @param request 请求体
* @return
*/
@Override
public IPage<${dtoClassName}> page(${domainName}PageRequest request){
return null;
}

/**
* 查找
* @param request 请求体
* @return
*/
@Override
public ${dtoClassName} find(${domainName}FindRequest request){
return null;
}

/**
* 新增
* @param request 请求体
* @return
*/
@Override
public ${source.keyType} insert(${domainName}CreateRequest request){
return null;
}

/**
* 修改
* @param request 请求体
* @return 成功OR失败
*/
@Override
public Boolean update(${domainName}UpdateRequest request){
return null;
}

/**
* 删除
* @param id 数据ID
* @return 成功OR失败
*/
@Override
public Boolean delete(${source.keyType} id){
return null;
}
}
