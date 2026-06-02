package ${mapperPackage!''};

import org.apache.ibatis.annotations.Mapper;
import io.github.roger3lee.domain.core.mapper.BatchBaseMapper;
import ${tablePackage!''}.*;

/**
* ${source.name}
*
* @author auto
* @version v1.0
*/
@Mapper
public interface ${NameUtils.mapperName(source.name)} extends BatchBaseMapper<${NameUtils.dataObjectName(source.name)}> {
}