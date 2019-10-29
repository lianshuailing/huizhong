package com.huizhong.item.service.impl;

import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.item.mapper.CategoryMapper;
import com.huizhong.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2019-07-20 17:45
 */
@Service
public class CategoryServiceImpl {
    @Autowired
    private CategoryMapper categoryMapper;//报错处理 alert+enter 第一个点 再点disable。。。

    public List<Category> queryCategoryListByPid(Long pid) {
        //查询条件
        Category t = new Category();
        t.setParentId(pid);
        //Mapper.select(Object) tk.mybatis里通用Mapper的该方法是将传过来的对象里非空属性作为查询条件
        List<Category> list = categoryMapper.select(t);

        //CollectionUtils.isEmpty(list)底层就是它--->list == null || list.isEmpty()   等价的。
        if(CollectionUtils.isEmpty(list)){
            throw new HzException(ExceptionEnum.CATEGORY_IS_EMPTY);
        }
        return list;
    }

    public List<Category> queryByIds(List<Long> ids) {
        final List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)) {
            throw new HzException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    /**
     * 根据商品分类id查询名称
     * @param ids 要查询的分类id集合
     * @return 多个名称的集合
     */
    public List<String> queryNameByIds(List<Long> ids) {
        return this.categoryMapper.selectByIdList(ids).stream().map(Category::getName).collect(Collectors.toList());
    }
}
