package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.repository;

import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request.*;
import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

<#assign dtoClassName=NameUtils.dataTOName(source.name)/>
<#assign domainName=NameUtils.getName(source.name)/>
<#assign repositoryClassName=NameUtils.repositoryName(source.name)/>
public interface ${repositoryClassName} {

     /**
     * 分页查询
     * @param page
     * @param request
     * @return
     */
     IPage<${dtoClassName}> page(IPage<?> page, ${domainName}PageRequest request);

     /**
     * 查找
     * @param id 数据ID
     * @return
     */
     ${dtoClassName} find(${source.keyType} id);

     /**
     * 新增
     * @param dtoData 数据
     * @return
     */
     ${source.keyType} insert(${dtoClassName} dtoData);

     /**
     * 修改
     * @param dtoData 数据
     * @return 成功OR失败
     */
     Boolean update(${dtoClassName} dtoData);

     /**
     * 删除
     * @param id 数据ID
     * @return 成功OR失败
     */
     Boolean delete(${source.keyType} id);

<#--    关联实体类-->
<#list source.relatedTable as relateTable>
     <#assign relateClassName= NameUtils.dataTOName(relateTable.name)/>
     /**
     * 新增 ${relateClassName}
     * @param dtoData 数据
     * @return 成功OR失败
     */
     Boolean insert${NameUtils.getName(relateTable.name)}(List<${dtoClassName}.${relateClassName}> dtoData);

     /**
     * 修改
     * @param dtoData 数据
     * @return 成功OR失败
     */
     Boolean update${NameUtils.getName(relateTable.name)}(List<${dtoClassName}.${relateClassName}> dtoData);

     /**
     * 删除
     * @param id 数据ID
     * @return 成功OR失败
     */
     Boolean delete${NameUtils.getName(relateTable.name)}(${relateTable.fkSourceColumnType} refId);
</#list>
}
