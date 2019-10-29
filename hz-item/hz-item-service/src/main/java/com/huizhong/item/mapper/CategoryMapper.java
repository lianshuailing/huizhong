package com.huizhong.item.mapper;

import com.huizhong.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author shkstart
 * @create 2019-07-20 17:42
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {
}
