package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 菜品
 * @author can dong
 */
@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     * @param categoryId 分类id
     * @return 菜品数量
     */
    @Select("select count(*) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     * @param dish 菜品数据
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询参数
     * @return 菜品分页结果
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品数据
     * @param id 菜品id
     * @return 菜品数据
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 批量删除菜品
     * @param ids 菜品id
     */
    void deleteByIds(List<Long> ids);

    /**
     * 动态的修改菜品数据
     * @param dish 菜品数据
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param dish  菜品数据
     * @return 菜品列表
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param setMealId 套餐id
     * @return 菜品列表
     */
    @Select("select d.* from dish d left join setmeal_dish s on d.id = s.dish_id where s.setmeal_id = #{setMealId}")
    List<Dish> getBySetMaelId(Long setMealId);

    /**
     * 根据条件查询菜品数量
     * @param map 查询条件
     * @return 菜品数量
     */
    Integer countByMap(Map<String, Object> map);
}
