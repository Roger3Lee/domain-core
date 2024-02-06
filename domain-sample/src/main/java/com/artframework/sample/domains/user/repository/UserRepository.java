package com.artframework.sample.domains.user.repository;

import com.artframework.sample.domains.user.domain.*;
import com.artframework.sample.entities.*;
import com.artframework.domain.core.repository.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface UserRepository extends BaseRepository<UserDomain, UserInfoDO> {

    /**
    * 分页查询
    * @param request 请求体
    * @return 返回数据
    */
    IPage<UserDomain> page(UserPageDomain request);
}
