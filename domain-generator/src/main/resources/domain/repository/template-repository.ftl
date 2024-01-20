package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${tablePackage!''}.*;
import ${corePackage}.repository.*;
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
    IPage<${dtoClassName}> page(${domainName}PageDomain request);
}
