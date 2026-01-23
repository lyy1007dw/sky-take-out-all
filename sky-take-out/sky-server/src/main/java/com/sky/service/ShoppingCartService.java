package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * @author can dong
 */
public interface ShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车数据
     */
    void add(ShoppingCartDTO shoppingCartDTO);
}
