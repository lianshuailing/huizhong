package com.huizhong.item.web;

import com.huizhong.item.pojo.Category;
import com.huizhong.item.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shkstart
 * @create 2019-07-20 17:52
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 分类管理
     * @param pid
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid")Long pid){
        //下面比ResponseEntity.status(HTTP.status).body().....省事，它也可已。
        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }

    /**
     * 根据商品分类id查询名称
     * @param ids 要查询的分类id集合
     * @return 多个名称的集合
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<Long> ids){
        List<String > list = this.categoryService.queryNameByIds(ids);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据id查询商品分类
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryListByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }
}
