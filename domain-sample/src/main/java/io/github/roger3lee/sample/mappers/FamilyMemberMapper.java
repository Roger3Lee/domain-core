package io.github.roger3lee.sample.mappers;

import org.apache.ibatis.annotations.Mapper;
import io.github.roger3lee.domain.core.mapper.BatchBaseMapper;

import io.github.roger3lee.sample.entities.*;

/**
* family_member
*
* @author auto
* @version v1.0
*/
@Mapper
public interface FamilyMemberMapper extends BatchBaseMapper<FamilyMemberDO> {
}
