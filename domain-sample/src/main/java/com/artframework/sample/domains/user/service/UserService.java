package com.artframework.sample.domains.user.service;

import com.artframework.sample.domains.user.domain.*;
import mo.gov.dsaj.domain.core.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface UserService extends BaseDomainService {

    /**
    * 分页查询
    * @param request 请求体
    * @return
    */
    IPage<UserDomain> page(UserPageDomain request);

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    UserDomain find(UserFindDomain request);


    /**
    * 新增
    * @param request 请求体
    * @return
    */
    java.lang.Long insert(UserCreateDomain request);

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    Boolean update(UserUpdateDomain request);

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    Boolean delete(java.lang.Long key);
}
