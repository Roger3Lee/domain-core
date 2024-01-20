package ${mapperPackage!''};

import org.apache.ibatis.annotations.Mapper;

import ${tablePackage!''}.*;

/**
* ${source.name}
*
* @author auto
* @version v1.0
*/
@Mapper
public interface ${NameUtils.mapperName(source.name)} extends ${baseMapperClass}<${NameUtils.dataObjectName(source.name)}> {
}