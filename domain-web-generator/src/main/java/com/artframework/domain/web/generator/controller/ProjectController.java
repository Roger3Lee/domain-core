package com.artframework.domain.web.controller;

import com.artframework.domain.web.domain.project.domain.*;
import com.artframework.domain.web.domain.project.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/project/v1")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    /**
    * 查找
    * @param request 请求体
    * @return ProjectDomain
    */
    @PostMapping("/query")
    public ProjectDomain find(@RequestBody ProjectFindDomain request){
        return projectService.find(request);
    }

    /**
    * 新增
    * @param request 请求体
    * @return Integer
    */
    @PutMapping()
    public Integer insert(@RequestBody ProjectDomain request){
        return projectService.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @PostMapping()
    public Boolean update(@RequestBody ProjectDomain request){
        return projectService.update(request);
    }

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    @DeleteMapping
    public Boolean delete(@RequestParam("key") Integer key){
        return projectService.delete(key);
    }
}
