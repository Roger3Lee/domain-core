package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.repository.impl;

import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.repository.*;
import ${basePackage!''}.entities.*;
import ${basePackage!''}.mappers.*;
import ${basePackage!''}.entities.*;
import com.artframework.domain.core.repository.impl.*;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign doClassName=NameUtils.dataObjectName(source.mainTable.name)/>
<#assign mapperClassName=NameUtils.mapperName(source.mainTable.name)/>
<#assign mapperName=NameUtils.getFieldName(mapperClassName)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
<#assign repositoryImplClassName=NameUtils.repositoryImplName(source.name)/>
public class ${repositoryImplClassName} extends BaseRepositoryImpl<${dtoClassName},${doClassName}>  implements ${repositoryClassName} {
@Override
public List<${doClassName}> convert2DO(List<${dtoClassName}> list) {
return null;
}

@Override
public List<${dtoClassName}> convert2DTO(List<${doClassName}> list) {
return null;
}

@Override
public Function<${dtoClassName}, ?> keyLambda() {
return null;
}
}


<#list source.relatedTable as relateTable>
    <#assign relateDOClassName= NameUtils.dataObjectName(relateTable.name)/>
    <#assign relateDTOClassName= NameUtils.dataTOName(relateTable.name)/>
    <#assign relateMapperClassName=NameUtils.mapperName(relateTable.name)/>
    <#assign relateMapperName=NameUtils.getFieldName(relateMapperClassName)/>
    <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
    <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
    <#assign relateRepositoryImplClassName=NameUtils.repositoryImplName(relateTable.name)/>
@Repository
public class ${relateRepositoryImplClassName} extends BaseRepositoryImpl<${dtoClassName}.${relateDTOClassName},${relateDOClassName}>  implements ${relateRepositoryClassName} {
}
</#list>
