package com.huizhong.search.pojo;

import com.huizhong.common.vo.PageResult;
import com.huizhong.item.pojo.Brand;
import com.huizhong.item.pojo.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: cuzz
 * @Date: 2018/11/14 13:34
 * @Description:
 */
@Data
public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;

    private List<Brand> brands;

    private List<Map<String,Object>> specs; // 规格参数过滤条件

    public SearchResult(){};


    //快捷键生成Long total, Long totalPage, List<Goods> items父类的；categories brands子类的。
    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    //优化方案： 构造参数比较多、长的话，该处可以用工厂模式来优化， 用工厂设计模式：用固定的一些方法来构造对象  而不是 依靠构造函数的参数 来生成对象。
    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
