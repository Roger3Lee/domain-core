package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository.impl;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.convertor.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.lambdaexp.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository.*;
import ${tablePackage!''}.*;
import ${corePackage}.repository.impl.*;

import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign covertName=NameUtils.covertName(source.name)/>
<#assign lambdaClassName=NameUtils.lambdaExpName(source.name)/>

<#assign relateDOClassName= NameUtils.dataObjectName(table.name)/>
<#assign relateDTOClassName= dtoClassName+"."+ NameUtils.dataTOName(table.name)/>
<#assign relateMapperClassName=NameUtils.mapperName(table.name)/>
<#assign relateMapperName=NameUtils.getFieldName(relateMapperClassName)/>
<#assign relateFieldName=NameUtils.getFieldName(table.name)/>
<#assign relateRepositoryClassName=NameUtils.repositoryName(table.name)/>
<#assign relateRepositoryImplClassName=NameUtils.repositoryImplName(table.name)/>
<#assign fieldName=NameUtils.getFieldName(table.name)/>
<#assign relateName=NameUtils.getName(table.name)/>
@Repository(value="${source.name}-${relateRepositoryImplClassName}")
public class ${relateRepositoryImplClassName} extends BaseRepositoryImpl<${relateDTOClassName},${relateDOClassName}>  implements ${relateRepositoryClassName} {

    @Override
    public List<${relateDOClassName}> convert2DO(List<${relateDTOClassName}> list) {
        return ${covertName}.INSTANCE.convert2${relateName}DO(list);
    }

    @Override
    public List<${relateDTOClassName}> convert2DTO(List<${relateDOClassName}> list) {
        return ${covertName}.INSTANCE.convert2${relateName}DTO(list);
    }

    @Override
    public void convert2DTO(${relateDOClassName} item ,${relateDTOClassName} targetItem){
        ${covertName}.INSTANCE.convert2${relateName}DTO(item,targetItem);
    }

    @Override
    public SFunction<${relateDTOClassName}, Serializable> keyLambda() {
        return ${lambdaClassName}.${NameUtils.fieldTargetKeyLambda(fieldName)};
    }

    @Override
    public Class<${relateDOClassName}> getDOClass() {
        return ${relateDOClassName}.class;
    }
}
