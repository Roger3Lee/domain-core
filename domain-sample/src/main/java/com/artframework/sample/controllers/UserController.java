package com.artframework.sample.controller;

import com.artframework.sample.domains.user.domain.*;
import com.artframework.sample.domains.user.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/user/v1")
public class UserController {
    @Autowired
    private UserService userService;

    /**
    * 分页查询
    * @param request 请求体
    * @return IPage<UserDomain>
    */
    @PostMapping("page")
    public IPage<UserDomain> page(@RequestBody UserPageDomain request){
        return userService.page(request);
    }

    /**
    * 查找
    * @param request 请求体
    * @return UserDomain
    */
    @PostMapping("/query")
    public UserDomain find(@RequestBody UserFindDomain request){
        return userService.find(request);
    }

    /**
    * 新增
    * @param request 请求体
    * @return java.lang.Long
    */
    @PutMapping()
    public java.lang.Long insert(@RequestBody UserDomain request){
        return userService.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @PostMapping()
    public Boolean update(@RequestBody UserDomain request){
        return userService.update(request);
    }

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    @DeleteMapping
    public Boolean delete(@RequestParam("key") java.lang.Long key){
        return userService.delete(key);
    }
}
