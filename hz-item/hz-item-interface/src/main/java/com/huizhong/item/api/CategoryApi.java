package com.huizhong.item.api;

import com.huizhong.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: cuzz
 * @Date: 2018/11/9 16:03
 * @Description:
 */
@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据id查询商品分类
     * @param ids
     */
    @GetMapping("list/ids")
    List<Category> queryCategoryListByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("names")
    List<String> queryNameByIds(@RequestParam("ids") List<Long> ids);

}
