package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.repository;

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${tablePackage!''}.*;
import ${corePackage}.repository.*;

<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign doClassName=NameUtils.dataObjectName(source.mainTable.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
public interface ${repositoryClassName} extends BaseRepository<${dtoClassName}, ${doClassName}> {
}
