package ${mapperPackage!''};

import org.apache.ibatis.annotations.Mapper;
import com.artframework.domain.core.mapper.BatchBaseMapper;
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