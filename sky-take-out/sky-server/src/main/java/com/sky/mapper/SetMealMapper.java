package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 套餐
 * @author can dong
 */
@Mapper
public interface SetMealMapper {
    /**
     * 根据分类id查询套餐的数量
     * @param categoryId 分类id
     * @return 套餐数量
     */
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 根据菜品id查询关联的套餐id
     * @param ids 菜品id
     * @return 套餐id
     */
    List<Long> getSetMealIdsByDishIds(List<Long> ids);

    /**
     * 新增套餐
     * @param setmeal 套餐数据
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);
}
