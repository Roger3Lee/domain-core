package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.repository.impl;

import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.repository.*;
import ${basePackage!''}.entities.*;
import ${basePackage!''}.mappers.*;
import ${basePackage!''}.utils.*;

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
public class ${domainName}RepositoryImpl extends ServiceImpl<${mapperClassName}, ${doClassName}> implements ${domainName}Repository {
    @Autowired
    private ${mapperClassName} ${NameUtils.getFieldName(mapperClassName)};

    <#--关联表-->
    <#list source.relatedTable as relateTable>
    <#assign relateMapperName=NameUtils.mapperName(relateTable.name)/>
    @Autowired
    private ${relateMapperName} ${NameUtils.getFieldName(relateMapperName)};
    </#list>

    /**
    * 分页查询
    * @param page
    * @param request
    * @return
    */
    @Override
    public IPage<${dtoClassName}> page(IPage<?> page, ${domainName}PageRequest request){
        IPage<${doClassName}> queryPage = new Page<>(page.getPages(), page.getSize());
        return PageHelper.convert(${NameUtils.getFieldName(mapperClassName)}.selectPage(queryPage, null), Convertor.INSTANCE::convert);
    }

    /**
    * 查找
    * @param id 数据ID
    * @return
    */
    @Override
    public ${dtoClassName} find(${source.keyType} id){
        LambdaQueryWrapper<${doClassName}> wrapper=new LambdaQueryWrapper<${doClassName}>()
            .eq(${doClassName}.keyLambda,id);

        return Convertor.INSTANCE.convert(${NameUtils.getFieldName(mapperClassName)}.selectOne(wrapper));
    }

    /**
    * 新增
    * @param dtoData 数据
    * @return
    */
    @Override
    public ${source.keyType} insert(${dtoClassName} dtoData){
        ${doClassName} doData= Convertor.INSTANCE.convert(dtoData);
        ${NameUtils.getFieldName(mapperClassName)}.insert(doData);
        return ${doClassName}.keyLambda.apply(doData);
    }

    /**
    * 修改
    * @param dtoData 数据
    * @return 成功OR失败
    */
    @Override
    public Boolean update(${dtoClassName} dtoData){
        ${doClassName} doData= Convertor.INSTANCE.convert(dtoData);
        return ${NameUtils.getFieldName(mapperClassName)}.update(doData,null) > 0;
    }

    /**
    * 删除
    * @param id 数据ID
    * @return 成功OR失败
    */
    @Override
    public Boolean delete(${source.keyType} id){
        LambdaQueryWrapper<${doClassName}> wrapper = new LambdaQueryWrapper<${doClassName}>()
            .eq(${doClassName}.keyLambda,id);
        return ${NameUtils.getFieldName(mapperClassName)}.delete(wrapper) > 0;
    }

<#--    关联实体类-->
<#list source.relatedTable as relateTable>
    <#assign relateDOClassName= NameUtils.dataObjectName(relateTable.name)/>
    <#assign relateDTOClassName= NameUtils.dataTOName(relateTable.name)/>
    <#assign relateMapperClassName=NameUtils.mapperName(relateTable.name)/>
    <#assign relateMapperName=NameUtils.getFieldName(relateMapperClassName)/>
    <#assign relateFieldName=NameUtils.getFieldName(relateTable.name)/>
    /**
    * 新增 ${relateDTOClassName}
    * @param dtoData 数据
    * @return 成功OR失败
    */
    @Override
    public Boolean insert${NameUtils.getName(relateTable.name)}(List<${dtoClassName}.${relateDTOClassName}> dtoData){
        if(CollUtil.isNotEmpty(dtoData)){
            List<${relateDOClassName}> doData=Convertor.INSTANCE.convert2DO(dtoData);
            for (${relateDOClassName} item:doData) {
                ${relateMapperName}.insert(item);
            }
        }
        return true;
    }

    /**
    * 修改
    * @param dtoData 数据
    * @return 成功OR失败
    */
    @Override
    public Boolean update${NameUtils.getName(relateTable.name)}(List<${dtoClassName}.${relateDTOClassName}> dtoData){
        if(CollUtil.isNotEmpty(dtoData)){
            List<${relateDOClassName}> doData=Convertor.INSTANCE.convert2DO(dtoData);
            for (${relateDOClassName} item:doData) {
                ${relateMapperName}.update(item ,null);
            }
        }
        return true;
    }

    /**
    * 删除
    * @param refId 数据ID
    * @return 成功OR失败
    */
    @Override
    public Boolean delete${NameUtils.getName(relateTable.name)}(${relateTable.fkSourceColumnType} refId){
        LambdaQueryWrapper<${relateDOClassName}> wrapper = new LambdaQueryWrapper<${relateDOClassName}>()
            .eq(${dtoClassName}.${NameUtils.fieldTargetLambda(relateFieldName)} ,refId);
        return ${relateMapperName}.delete(wrapper) > 0;
    }
</#list>

<#--MAPPER-->
    @Mapper
    public interface  Convertor{
        Convertor INSTANCE= Mappers.getMapper(Convertor.class);

        ${dtoClassName} convert(${doClassName} request);
        ${doClassName} convert(${dtoClassName} request);

        <#list source.relatedTable as relateTable>
            <#assign relateDTOClassName= NameUtils.dataTOName(relateTable.name)/>
            <#assign relateDOClassName= NameUtils.dataObjectName(relateTable.name)/>
        ${dtoClassName}.${relateDTOClassName} convert2DTO(${relateDOClassName} request);
        List<${dtoClassName}.${relateDTOClassName}> convert2DTO(List<${relateDOClassName}>  request);
        ${relateDOClassName} convert2DO(${dtoClassName}.${relateDTOClassName} request);
        List<${relateDOClassName}> convert2DO(List<${dtoClassName}.${relateDTOClassName}>  request);
        </#list>
    }
}
