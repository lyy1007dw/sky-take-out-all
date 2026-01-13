package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author can dong
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors 口味数据
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味数据（批量删除）
     * @param dishIds 菜品id
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id查询口味数据
     * @param id 菜品id
     * @return 菜品口味数据
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long id);
}
