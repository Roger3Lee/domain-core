package com.artframework.sample.domains.user.service;

import com.artframework.sample.domains.user.dto.request.*;
import com.artframework.sample.domains.user.dto.*;
import com.artframework.domain.core.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface UserService extends BaseDomainService {

    /**
    * 分页查询
    * @param request 请求体
    * @return
    */
    IPage<UserDTO> page(UserPageRequest request);

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    UserDTO find(UserFindRequest request);


    /**
    * 新增
    * @param request 请求体
    * @return
    */
    Integer insert(UserCreateRequest request);

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    Boolean update(UserUpdateRequest request);

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    Boolean delete(Integer key);
}
