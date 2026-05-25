package com.artframework.sample.mappers;

import org.apache.ibatis.annotations.Mapper;
import io.github.roger3lee.domain.core.mapper.BatchBaseMapper;
import com.artframework.sample.entities.*;

/**
* family_address
*
* @author auto
* @version v1.0
*/
@Mapper
public interface FamilyAddressMapper extends BatchBaseMapper<FamilyAddressDO> {
}
