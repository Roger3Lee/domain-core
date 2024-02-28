package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${tablePackage!''}.*;
import ${corePackage}.repository.*;

<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign relateRepositoryClassName=NameUtils.repositoryName(table.tableName)/>
<#assign relateDtoClassName=NameUtils.dataTOName(table.tableName)/>
<#assign relateDoClassName=NameUtils.dataObjectName(table.tableName)/>
public interface ${relateRepositoryClassName} extends BaseRepository<${dtoClassName}.${relateDtoClassName}, ${relateDoClassName}> {
}
