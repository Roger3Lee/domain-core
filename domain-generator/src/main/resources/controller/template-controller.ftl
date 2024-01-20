package ${controllerPackage!''};

import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain.*;
import ${domainPackage!''}.${NameUtils.packageName(source.folder)}.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

<#assign controllerClassName=NameUtils.controllerName(source.name)/>
<#assign serviceClassName=NameUtils.serviceName(source.name)/>
<#assign serviceFieldName=NameUtils.getFieldName(serviceClassName)/>
<#assign dtoClassName=NameUtils.dataTOName(source.name)/>

@RestController()
@RequestMapping("/${source.name}/v1")
public class ${controllerClassName} {
    @Autowired
    private ${serviceClassName} ${serviceFieldName};

    /**
    * 分页查询
    * @param request 请求体
    * @return IPage<${dtoClassName}>
    */
    @PostMapping("page")
    public IPage<${dtoClassName}> page(@RequestBody ${NameUtils.getName(source.name)}PageDomain request){
        return ${serviceFieldName}.page(request);
    }

    /**
    * 查找
    * @param request 请求体
    * @return ${dtoClassName}
    */
    @PostMapping("/query")
    public ${dtoClassName} find(@RequestBody ${NameUtils.getName(source.name)}FindDomain request){
        return ${serviceFieldName}.find(request);
    }

    /**
    * 新增
    * @param request 请求体
    * @return ${source.mainTable.keyType}
    */
    @PutMapping()
    public ${source.mainTable.keyType} insert(@RequestBody ${NameUtils.getName(source.name)}CreateDomain request){
        return ${serviceFieldName}.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @PostMapping()
    public Boolean update(@RequestBody ${NameUtils.getName(source.name)}UpdateDomain request){
        return ${serviceFieldName}.update(request);
    }

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    @DeleteMapping
    public Boolean delete(@RequestParam("key") ${source.mainTable.keyType} key){
        return ${serviceFieldName}.delete(key);
    }
}
