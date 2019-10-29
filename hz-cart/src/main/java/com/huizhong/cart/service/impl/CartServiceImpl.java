package com.huizhong.cart.service.impl;

import com.huizhong.auth.entity.UserInfo;
import com.huizhong.cart.filter.LoginInterceptor;
import com.huizhong.cart.pojo.Cart;
import com.huizhong.cart.service.CartService;
import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2019-09-12 17:59
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String CART_PREFIX = "cart:uid:";

/*
    @Override
    public void addCart(Cart cart) {
        Gson gson = new Gson();
        // 获取登录用户
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // key
        String key = CART_PREFIX + userInfo.getId();
        // hashKey
        String hashKey = cart.getSkuId().toString();

        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(key);
        // 判断当前cart中商品，是否在redis中存在
        if (!operations.hasKey(hashKey)) {
            // 新增
            operations.put(hashKey, cart.toString());
        }else {
            // 缓存中的购物车数据 与 cart 中sku数据合并。注意：价格不一致，则不合并，直接新增这次的cart数据；若是一致，则num+
            Cart cartCache = gson.fromJson(operations.get(hashKey).toString(), Cart.class);
            if (cartCache.getPrice() != cart.getPrice()) {
                operations.delete(hashKey);
            }
            cart.setNum(cart.getNum() + cartCache.getNum());
            operations.put(hashKey, cart.toString());
        }
    }

    @Override
    public List<Cart> queryCartList() {
        // 获取登录用户
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // key
        String key = CART_PREFIX + userInfo.getId();
        if (!stringRedisTemplate.hasKey(key)) {
            throw new HzException(ExceptionEnum.USER_CART_NOT_FOUND);
        }
//        // 方式1
//        String json = stringRedisTemplate.opsForValue().get(key);
//        List<Cart> cartList = JsonUtils.parseList(json, Cart.class);

        // 方式2
        if (!stringRedisTemplate.hasKey(key)) {
            throw new HzException(ExceptionEnum.USER_CART_NOT_FOUND);
        }
        BoundHashOperations<String, Object, Object> boundHashOps = stringRedisTemplate.boundHashOps(key);
        List<Cart> cartList = boundHashOps.values().stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());

        return cartList;
    }

    @Override
    public void deleteCart(String skuId) {
        // 获取登录用户
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        // key
        String key = CART_PREFIX + userInfo.getId();

        // 方式1
//        BoundHashOperations<String, Object, Object> boundHashOps = stringRedisTemplate.boundHashOps(key);
//        boundHashOps.delete(skuId);
        // 方式2
        stringRedisTemplate.opsForHash().delete(key, skuId);
    }
*/


    public void addCart(Cart cart) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getUserInfo();
        String key = CART_PREFIX + user.getId();
        String hashKey = cart.getSkuId().toString();
        // 原来的数量
        Integer num = cart.getNum();
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(key);
        // 判断当前购物车商品是否存在
        if (operations.hasKey(hashKey)) {
            // 是, 修改数量
            String json = operations.get(hashKey).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + num);
        }
        // 写回redis
        operations.put(hashKey, JsonUtils.toString(cart));
    }

    public List<Cart> queryCartList() {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getUserInfo();
        String key = CART_PREFIX + user.getId();
        if (!stringRedisTemplate.hasKey(key)) {
            // key不存在, 返回404
            throw new HzException(ExceptionEnum.USER_CART_NOT_FOUND);
        }
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(key);
        List<Object> values = operations.values();
        List<Cart> carts = values.stream()
                .map(o -> JsonUtils.parse(o.toString(), Cart.class))
                .collect(Collectors.toList());
        return carts;
    }

    public void updateNum(Long skuId, Integer num) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getUserInfo();
        String key = CART_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps(key);
        String hashKey = skuId.toString();
        if (!operations.hasKey(hashKey)) {
            throw new HzException(ExceptionEnum.USER_CART_NOT_FOUND);
        }
        String json = operations.get(hashKey).toString();
        Cart cart = JsonUtils.parse(json, Cart.class);
        cart.setNum(num);
        // 写回redis
        operations.put(hashKey, JsonUtils.toString(cart));
    }

    public void deleteCart(Long skuId) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getUserInfo();
        String key = CART_PREFIX + user.getId();

        // 删除
        stringRedisTemplate.opsForHash().delete(key, skuId.toString());
    }
}
