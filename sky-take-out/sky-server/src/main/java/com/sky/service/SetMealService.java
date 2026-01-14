package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

/**
 * @author can dong
 */
public interface SetMealService {
    /**
     * 新增套餐
     * @param setmealDTO 新增的套餐数据
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO 查询条件
     * @return 查询结果
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
