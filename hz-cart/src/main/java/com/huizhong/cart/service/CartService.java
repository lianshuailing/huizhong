package com.huizhong.cart.service;

import com.huizhong.cart.pojo.Cart;

import java.util.List;

/**
 * @author shkstart
 * @create 2019-09-12 17:58
 */
public interface CartService {
    void addCart(Cart cart);

    void updateNum(Long skuId, Integer num);

    List<Cart> queryCartList();

    void deleteCart(Long skuId);
}
