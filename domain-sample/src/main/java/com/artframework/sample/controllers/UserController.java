package com.artframework.sample.controllers;

import com.artframework.sample.domains.user.dto.*;
import com.artframework.sample.domains.user.dto.request.*;
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
    * @return IPage<UserDTO>
    */
    @PostMapping("page")
    public IPage<UserDTO> page(@RequestBody UserPageRequest request){
        return userService.page(request);
    }

    /**
    * 查找
    * @param request 请求体
    * @return UserDTO
    */
    @PostMapping("/query")
    public UserDTO find(@RequestBody UserFindRequest request){
        return userService.find(request);
    }

    /**
    * 新增
    * @param request 请求体
    * @return Integer
    */
    @PutMapping()
    public Integer insert(@RequestBody UserCreateRequest request){
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
    public Boolean delete(@RequestParam("key") Integer key){
        return userService.delete(key);
    }
}
