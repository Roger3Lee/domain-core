package com.artframework.domain.web.generator.domain.project.service;

import com.artframework.domain.web.generator.domain.project.domain.*;

import com.artframework.domain.core.service.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;

public interface ProjectService extends BaseDomainService {

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    ProjectDomain find(ProjectFindDomain request);

   /**
     * 通過已有實體查找
     * @param response 已有實體
     * @param loadFlag 加載參數
     * @return
     */
    ProjectDomain find(ProjectDomain response, ProjectDomain.LoadFlag loadFlag);
    /**
     * 查找
     * @param request 请求体
     * @return
     */
    ProjectDomain findByKey(ProjectFindDomain request, SFunction<ProjectDomain, Serializable> keyLambda);

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    Integer insert(ProjectDomain request);

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    Boolean update(ProjectDomain request);
    /**
    * 修改,此方法不用再加載domain主entity數據
    * @param request 请求体
    * @param domain 原始domain
    * @return 成功OR失败
    */
    Boolean update(ProjectDomain request, ProjectDomain domain);
    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    Boolean delete(Integer key);
    /**
    * 删除
    * @param key 数据ID
    * @param loadFlag 數據加載參數
    * @return 成功OR失败
    */
    Boolean delete(Integer key, ProjectDomain.LoadFlag loadFlag);
}
