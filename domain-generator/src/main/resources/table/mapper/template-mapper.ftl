package ${mapperPackage!''};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import ${tablePackage!''}.*;

/**
* ${source.name}
*
* @author auto
* @version v1.0
*/
public interface ${NameUtils.mapperName(source.name)} extends BaseMapper<${NameUtils.dataObjectName(source.name)}> {
}