package com.artframework.sample.domains.user.repository;

import com.artframework.sample.domains.user.dto.request.*;
import com.artframework.sample.domains.user.dto.*;
import com.artframework.sample.entities.*;
import com.artframework.domain.core.repository.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface UserRepository extends BaseRepository<UserDTO, UserInfoDO> {

    /**
    * 分页查询
    * @param request 请求体
    * @return 返回数据
    */
    IPage<UserDTO> page(UserPageRequest request);

    public interface UserAddressRepository extends BaseRepository<UserDTO.UserAddressDTO, UserAddressDO> {
    }
    public interface UserFamilyMemberRepository extends BaseRepository<UserDTO.UserFamilyMemberDTO, UserFamilyMemberDO> {
    }

}
