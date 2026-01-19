package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author can dong
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealMapper setmealMapper;
    /**
     * 新增菜品和对应的口味
     * @param dishDTO 菜品数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithFlavor(DishDTO dishDTO) {
        // 属性拷贝
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 保存菜品数据
        dishMapper.insert(dish);

        // 获取菜品id
        Long dishId = dish.getId();

        // 为菜品口味插入菜品id
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(df ->{
                df.setDishId(dishId);
            });

            // 批量保存菜品口味数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询参数
     * @return 菜品分页查询结果
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        // 开启分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        // 执行查询
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        // 返回结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品
     * @param ids 菜品id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<Long> ids) {
        // 起售中的菜品不能删除
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == 1){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 关联了套餐的菜品不能删除
        List<Long> setMealIds = setmealMapper.getSetMealIdsByDishIds(ids);
        if(setMealIds != null && !setMealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 批量删除菜品
        dishMapper.deleteByIds(ids);

        // 批量删除菜品关联的口味
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查询菜品和对应的口味
     * @param id 菜品id
     * @return 菜品数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        // 根据id查询菜品数据
        Dish dish = dishMapper.getById(id);

        // 根据dishId查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

        // 将数据进行封装后返回
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO 菜品数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DishDTO dishDTO) {
        // 属性拷贝
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 先修改菜品数据
        dishMapper.update(dish);

        // 删除原有的口味数据
        List<Long> dishIds = new ArrayList<>();
        dishIds.add(dishDTO.getId());
        dishFlavorMapper.deleteByDishIds(dishIds);

        // 为菜品口味插入菜品id
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(df ->{
                df.setDishId(dishDTO.getId());
            });

            // 批量保存菜品口味数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 启用、禁用菜品
     * @param status 菜品状态
     * @param id 菜品id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.update(dish);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类id
     * @return 菜品列表
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        return dishMapper.list(dish);
    }
}
