package com.artframework.sample.mappers;

import org.apache.ibatis.annotations.Mapper;
import com.artframework.domain.core.mapper.BatchBaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.artframework.sample.entities.*;

/**
 * family_member
 *
 * @author auto
 * @version v1.0
 */
@Mapper
public interface FamilyMemberMapper extends BatchBaseMapper<FamilyMemberDO> {
}
