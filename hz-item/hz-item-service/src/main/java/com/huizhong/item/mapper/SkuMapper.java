package com.huizhong.item.mapper;

import com.huizhong.item.pojo.Sku;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: cuzz
 * @Date: 2018/11/7 19:17
 * @Description:
 */
public interface SkuMapper extends Mapper<Sku>, IdListMapper<Sku, Long>, InsertListMapper<Sku> {
}
