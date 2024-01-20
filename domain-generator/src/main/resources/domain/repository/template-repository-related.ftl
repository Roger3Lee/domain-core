package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${tablePackage!''}.*;
import ${corePackage}.repository.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign relateRepositoryClassName=NameUtils.repositoryName(table.name)/>
<#assign relateDtoClassName=NameUtils.dataTOName(table.name)/>
<#assign relateDoClassName=NameUtils.dataObjectName(table.name)/>
public interface ${relateRepositoryClassName} extends BaseRepository<${dtoClassName}.${relateDtoClassName}, ${relateDoClassName}> {
}
