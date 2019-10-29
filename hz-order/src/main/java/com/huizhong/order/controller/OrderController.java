package com.huizhong.order.controller;

import com.huizhong.order.dto.OrderDTO;
import com.huizhong.order.pojo.Order;
import com.huizhong.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author shkstart
 * @create 2019-09-14 15:55
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderDTO orderDTO) {
        Long orderId = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(orderId);
    }

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.queryOrderById(id));
    }

    /**
     * 创建微信支付url
     * @param orderId
     * @return
     */
    @GetMapping("url/{id}")
    public ResponseEntity<String> createPayUrl(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.createPayUrl(orderId));
    }

    /**
     * 查询订单 微信支付-状态
     * @param orderId
     * @return
     */
    @GetMapping("state/{id}")
    public ResponseEntity<Integer> queryOrderState(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.queryOrderState(orderId).getValue());
    }
}
