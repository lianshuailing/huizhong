package com.huizhong.item.mapper;

import com.huizhong.item.pojo.Stock;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: cuzz
 * @Date: 2018/11/7 19:18
 * @Description: 必须导入tk.mybatis.mapper.additional.insert.InsertListMapper这个包
 */
public interface StockMapper extends Mapper<Stock>, IdListMapper<Stock, Long>, InsertListMapper<Stock> {
    @Update("UPDATE tb_stock SET stock = stock - #{num} WHERE sku_id = #{skuId} AND stock >= #{num}")
    int decreaseStock(@Param("skuId") Long skuId, @Param("num") Integer num);


    // 注意：这里有个坑, 有两个InsertListMapper,insertList方法限制不一样, 点进去看看！！！



    //IdListMapper 里是 SelectByIdListMapper<T, PK>, DeleteByIdListMapper ，select批量和delete批量
    //InsertListMapper  里是int insertList(List<T> recordList）    批量新增
}
