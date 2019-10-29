package com.huizhong.order.mapper;


import com.huizhong.order.pojo.Order;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface OrderMapper extends Mapper<Order>, IdListMapper<Order, Long>, InsertListMapper<Order> {
}
