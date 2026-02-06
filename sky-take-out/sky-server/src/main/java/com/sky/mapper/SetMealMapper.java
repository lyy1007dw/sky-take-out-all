package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 查询条件
     * @return 套餐分页查询结果
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 动态修改套餐
     * @param setmeal 套餐数据
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据id查询套餐数据
     * @param id 套餐id
     * @return 套餐数据
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 批量删除套餐
     * @param ids 套餐id列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 条件查询
     * @param setmeal 筛选条件
     * @return 满足条件的套餐列表
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param id 套餐id
     * @return 菜品选项
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd " +
            "left join dish d " +
            "on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{id}")
    List<DishItemVO> getDishItemById(Long id);

    /**
     * 根据条件查询套餐数量
     * @param map 筛选条件
     * @return 套餐数量
     */
    Integer countByMap(Map<String, Object> map);
}
