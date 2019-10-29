package com.huizhong.order.service;

import com.huizhong.order.dto.OrderDTO;
import com.huizhong.order.enums.PayState;
import com.huizhong.order.pojo.Order;

/**
 * @author shkstart
 * @create 2019-09-14 15:53
 */
public interface OrderService {
    Long createOrder(OrderDTO orderDTO);

    Order queryOrderById(Long id);

    String createPayUrl(Long orderId);

    PayState queryOrderState(Long orderId);
}
