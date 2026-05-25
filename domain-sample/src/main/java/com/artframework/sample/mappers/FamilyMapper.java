package com.artframework.sample.mappers;

import org.apache.ibatis.annotations.Mapper;
import io.github.roger3lee.domain.core.mapper.BatchBaseMapper;
import com.artframework.sample.entities.*;

/**
* family
*
* @author auto
* @version v1.0
*/
@Mapper
public interface FamilyMapper extends BatchBaseMapper<FamilyDO> {
}
