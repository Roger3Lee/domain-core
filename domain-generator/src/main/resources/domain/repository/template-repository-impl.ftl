package ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.repository.impl;

import ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.convertor.*;
import ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.lambdaexp.*;
import ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;
import ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request.*;
import ${domainPackage!''}.domain.${NameUtils.packageName(source.name)}.repository.*;
import ${tablePackage!''}.entities.*;
import com.artframework.domain.core.repository.impl.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

@Repository
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign doClassName=NameUtils.dataObjectName(source.mainTable.name)/>
<#assign mapperClassName=NameUtils.mapperName(source.mainTable.name)/>
<#assign mapperName=NameUtils.getFieldName(mapperClassName)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
<#assign repositoryImplClassName=NameUtils.repositoryImplName(source.name)/>
<#assign covertName=NameUtils.covertName(source.name)/>
<#assign lambdaClassName=NameUtils.lambdaExpName(source.name)/>
public class ${repositoryImplClassName} extends BaseRepositoryImpl<${dtoClassName},${doClassName}>  implements ${repositoryClassName} {

    @Override
    public List<${doClassName}> convert2DO(List<${dtoClassName}> list) {
        return ${covertName}.INSTANCE.convert2DO(list);
    }

    @Override
    public List<${dtoClassName}> convert2DTO(List<${doClassName}> list) {
        return ${covertName}.INSTANCE.convert2DTO(list);
    }

    @Override
    public SFunction<${dtoClassName}, Serializable> keyLambda() {
        return ${lambdaClassName}.dtoKeyLambda;
    }

    @Override
    public Class<${doClassName}> getDOClass() {
        return ${doClassName}.class;
    }

    @Override
    public IPage<${dtoClassName}> page(${domainName}PageRequest request){
        IPage<${doClassName}> page=new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<${doClassName}> wrapper =new LambdaQueryWrapper<${doClassName}>();
        return this.baseMapper.selectPage(page,wrapper).convert(${covertName}.INSTANCE::convert2DTO);
    }

    <#list source.relatedTable as relateTable>
        <#assign relateDOClassName= NameUtils.dataObjectName(relateTable.name)/>
        <#assign relateDTOClassName= dtoClassName+"."+ NameUtils.dataTOName(relateTable.name)/>
        <#assign relateMapperClassName=NameUtils.mapperName(relateTable.name)/>
        <#assign relateMapperName=NameUtils.getFieldName(relateMapperClassName)/>
        <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
        <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
        <#assign relateRepositoryImplClassName=NameUtils.repositoryImplName(relateTable.name)/>
        <#assign fieldName=NameUtils.getFieldName(relateTable.name)/>
        <#assign relateName=NameUtils.getName(relateTable.name)/>
    @Repository
    public static class ${relateRepositoryImplClassName} extends BaseRepositoryImpl<${relateDTOClassName},${relateDOClassName}>  implements ${relateRepositoryClassName} {

        @Override
        public List<${relateDOClassName}> convert2DO(List<${relateDTOClassName}> list) {
            return ${covertName}.INSTANCE.convert2${relateName}DO(list);
        }

        @Override
        public List<${relateDTOClassName}> convert2DTO(List<${relateDOClassName}> list) {
            return ${covertName}.INSTANCE.convert2${relateName}DTO(list);
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
    </#list>
}
