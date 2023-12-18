package ${domainPackage!''}.${NameUtils.packageName(source.name)}.repository;

import ${domainPackage!''}.${NameUtils.packageName(source.name)}.dto.request.*;
import ${domainPackage!''}.${NameUtils.packageName(source.name)}.dto.*;
import ${tablePackage!''}.*;
import com.artframework.domain.core.repository.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign doClassName=NameUtils.dataObjectName(source.mainTable.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
public interface ${repositoryClassName} extends BaseRepository<${dtoClassName}, ${doClassName}> {

    /**
    * 分页查询
    * @param request 请求体
    * @return 返回数据
    */
    IPage<${dtoClassName}> page(${domainName}PageRequest request);

<#--    关联实体类-->
<#list source.relatedTable as relateTable>
    <#assign relateRepositoryClassName=NameUtils.repositoryName(relateTable.name)/>
    <#assign relateDtoClassName=NameUtils.dataTOName(relateTable.name)/>
    <#assign relateDoClassName=NameUtils.dataObjectName(relateTable.name)/>
    public interface ${relateRepositoryClassName} extends BaseRepository<${dtoClassName}.${relateDtoClassName}, ${relateDoClassName}> {
    }
</#list>

<#--    聚合-->
<#if source.aggregate??>
    <#assign relateRepositoryClassName=NameUtils.repositoryName(source.aggregate.name)/>
    <#assign relateDtoClassName=NameUtils.dataTOName(source.aggregate.name)/>
    <#assign relateDoClassName=NameUtils.dataObjectName(source.aggregate.name)/>
    public interface ${relateRepositoryClassName} extends BaseRepository<${dtoClassName}.${relateDtoClassName}, ${relateDoClassName}> {
    }
</#if>
}
