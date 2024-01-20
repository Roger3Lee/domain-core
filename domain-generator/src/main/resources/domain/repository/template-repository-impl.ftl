package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository.impl;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.convertor.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.lambdaexp.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository.*;
import ${tablePackage!''}.*;
import ${corePackage}.repository.impl.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign doClassName=NameUtils.dataObjectName(source.mainTable.name)/>
<#assign mapperClassName=NameUtils.mapperName(source.mainTable.name)/>
<#assign mapperName=NameUtils.getFieldName(mapperClassName)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
<#assign repositoryImplClassName=NameUtils.repositoryImplName(source.name)/>
<#assign covertName=NameUtils.covertName(source.name)/>
<#assign lambdaClassName=NameUtils.lambdaExpName(source.name)/>
@Repository(value="${source.name}-${repositoryImplClassName}")
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
    public void convert2DTO(${doClassName} item ,${dtoClassName} targetItem){
        ${covertName}.INSTANCE.convert2DTO(item,targetItem);
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
    public IPage<${dtoClassName}> page(${domainName}PageDomain request){
        IPage<${doClassName}> page=new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<${doClassName}> wrapper =new LambdaQueryWrapper<>();
        return this.baseMapper.selectPage(page,wrapper).convert(${covertName}.INSTANCE::convert2DTO);
    }
}
