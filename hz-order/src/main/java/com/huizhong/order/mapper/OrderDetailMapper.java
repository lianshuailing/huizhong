package com.huizhong.order.mapper;


import com.huizhong.order.pojo.OrderDetail;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface OrderDetailMapper extends Mapper<OrderDetail>, InsertListMapper<OrderDetail> {
}
