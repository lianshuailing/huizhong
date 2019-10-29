package com.huizhong.cart.controller;

import com.huizhong.cart.pojo.Cart;
import com.huizhong.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2019-09-12 18:00
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCard(@RequestBody Cart cart) {
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("list")
    public ResponseEntity<List<Cart>> queryCartList() {
        return ResponseEntity.ok(cartService.queryCartList());
    }

    @PutMapping
    public ResponseEntity<Void> updateCartNum(@RequestParam("id") Long skuId,
                                              @RequestParam("num") Integer num) {
        cartService.updateNum(skuId, num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId) {
        cartService.deleteCart(skuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
