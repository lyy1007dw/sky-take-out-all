package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author can dong
 */
@Mapper
public interface SetMealDishMapper {
    /**
     * 批量插入套餐菜品数据
     * @param setMealDishes 套餐菜品数据
     */
    void insertBatch(List<SetmealDish> setMealDishes);
}
