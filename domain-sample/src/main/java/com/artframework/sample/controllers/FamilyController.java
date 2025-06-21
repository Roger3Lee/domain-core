package com.artframework.sample.controllers;

import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.domains.family.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/family/v1")
public class FamilyController {
    @Autowired
    private FamilyService familyService;

    /**
    * 查找
    * @param request 请求体
    * @return FamilyDomain
    */
    @PostMapping("/query")
    public FamilyDomain find(@RequestBody FamilyFindDomain request){
        return familyService.find(request);
    }

    /**
    * 新增
    * @param request 请求体
    * @return Integer
    */
    @PutMapping()
    public Integer insert(@RequestBody FamilyDomain request){
        return familyService.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @PostMapping()
    public Boolean update(@RequestBody FamilyDomain request){
        return familyService.update(request);
    }

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    @DeleteMapping
    public Boolean delete(@RequestParam("key") Integer key){
        return familyService.delete(key);
    }
}
