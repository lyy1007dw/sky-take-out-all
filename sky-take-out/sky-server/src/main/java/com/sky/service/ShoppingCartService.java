package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author can dong
 */
public interface ShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车数据
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return 购物车数据
     */
    List<ShoppingCart> showShoppingCart();
}
