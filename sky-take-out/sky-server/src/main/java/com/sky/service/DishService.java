package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * 菜品
 * @author can dong
 */
public interface DishService {
    /**
     * 新增菜品和对应的口味
     * @param dishDTO 新增的菜品数据
     */
    void saveWithFlavor(DishDTO dishDTO);
}
