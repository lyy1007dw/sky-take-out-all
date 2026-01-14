package com.sky.service;

import com.sky.dto.SetmealDTO;

/**
 * @author can dong
 */
public interface SetMealService {
    /**
     * 新增套餐
     * @param setmealDTO 新增的套餐数据
     */
    void saveWithDish(SetmealDTO setmealDTO);
}
