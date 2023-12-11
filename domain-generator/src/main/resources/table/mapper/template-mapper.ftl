package ${tablePackage!''}.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import ${basePackage!''}.entities.*;

/**
* ${source.name}
*
* @author auto
* @version v1.0
* @date ${.now}
*/
public interface ${NameUtils.mapperName(source.name)} extends BaseMapper<${NameUtils.dataObjectName(source.name)}> {
}