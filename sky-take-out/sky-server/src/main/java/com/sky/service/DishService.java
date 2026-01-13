package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询参数
     * @return 菜品分页结果
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids 菜品id
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询菜品和对应的口味
     * @param id 菜品id
     * @return 菜品数据
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品
     * @param dishDTO 修改的菜品数据
     */
    void update(DishDTO dishDTO);
}
