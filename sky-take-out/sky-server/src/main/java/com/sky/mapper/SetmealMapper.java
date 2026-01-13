package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 套餐
 * @author can dong
 */
@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id查询套餐的数量
     * @param categoryId 分类id
     * @return 套餐数量
     */
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
}
