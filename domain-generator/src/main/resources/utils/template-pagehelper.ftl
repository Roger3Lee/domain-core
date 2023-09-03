package ${basePackage!''}.utils;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageHelper {

    public static <T, F> IPage<F> Covert(IPage<T> pageResult, Function<T, F> converter){
        IPage<F> page=new Page<>();
        page.setPages(pageResult.getPages());
        page.setSize(pageResult.getSize());
        page.setTotal(pageResult.getTotal());
        if(CollUtil.isNotEmpty(pageResult.getRecords())){
            page.setRecords(pageResult.getRecords().stream().map(converter).collect(Collectors.toList()));
        }
        return page;
    }
}
