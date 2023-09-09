package com.artframework.sample.controller;

import com.artframework.sample.domain.user.dto.*;
import com.artframework.sample.domain.user.dto.request.*;
import com.artframework.sample.domain.user.service.*;
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
    * @return
    */
    @PostMapping("page")
    public IPage<UserDTO> page(@RequestBody UserPageRequest request){
        return userService.page(request);
    }

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    @PostMapping("/query")
    public UserDTO find(@RequestBody UserFindRequest request){
        return userService.find(request);
    }

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    @PutMapping()
    public java.lang.Long insert(@RequestBody UserCreateRequest request){
        return userService.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @PostMapping()
    public Boolean update(@RequestBody UserUpdateRequest request){
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
