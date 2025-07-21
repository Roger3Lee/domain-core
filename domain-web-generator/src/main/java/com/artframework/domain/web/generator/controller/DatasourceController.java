package com.artframework.domain.web.controller;

import com.artframework.domain.web.domain.datasource.domain.*;
import com.artframework.domain.web.domain.datasource.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/datasource/v1")
public class DatasourceController {
    @Autowired
    private DatasourceService datasourceService;

    /**
    * 查找
    * @param request 请求体
    * @return DatasourceDomain
    */
    @PostMapping("/query")
    public DatasourceDomain find(@RequestBody DatasourceFindDomain request){
        return datasourceService.find(request);
    }

    /**
    * 新增
    * @param request 请求体
    * @return Integer
    */
    @PutMapping()
    public Integer insert(@RequestBody DatasourceDomain request){
        return datasourceService.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @PostMapping()
    public Boolean update(@RequestBody DatasourceDomain request){
        return datasourceService.update(request);
    }

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    @DeleteMapping
    public Boolean delete(@RequestParam("key") Integer key){
        return datasourceService.delete(key);
    }
}
