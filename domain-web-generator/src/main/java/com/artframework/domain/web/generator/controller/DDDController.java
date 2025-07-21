package com.artframework.domain.web.controller;

import com.artframework.domain.web.domain.ddd.domain.*;
import com.artframework.domain.web.domain.ddd.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/DDD/v1")
public class DDDController {
    @Autowired
    private DDDService dDDService;

    /**
    * 查找
    * @param request 请求体
    * @return DDDDomain
    */
    @PostMapping("/query")
    public DDDDomain find(@RequestBody DDDFindDomain request){
        return dDDService.find(request);
    }

    /**
    * 新增
    * @param request 请求体
    * @return Integer
    */
    @PutMapping()
    public Integer insert(@RequestBody DDDDomain request){
        return dDDService.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @PostMapping()
    public Boolean update(@RequestBody DDDDomain request){
        return dDDService.update(request);
    }

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    @DeleteMapping
    public Boolean delete(@RequestParam("key") Integer key){
        return dDDService.delete(key);
    }
}
