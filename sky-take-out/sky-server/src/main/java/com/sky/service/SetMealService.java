package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

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

    /**
     * 套餐起售、停售
     * @param status 状态
     * @param id 套餐id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 批量删除套餐
     * @param ids 套餐id列表
     */
    void delete(List<Long> ids);


    /**
     * 修改套餐
     * @param setmealDTO 修改的套餐数据
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 根据id查询套餐数据
     * @param id 套餐id
     * @return 套餐数据
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 根据分类id查询套餐
     * @param categoryId 分类id
     * @return 套餐列表
     */
    List<Setmeal> list(Long categoryId);

    /**
     * 根据套餐id查询包含的菜品数据
     * @param id 套餐id
     * @return 根据套餐id查询包含的菜品数据
     */
    List<DishItemVO> getDishItemById(Long id);
}
