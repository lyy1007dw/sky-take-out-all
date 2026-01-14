package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author can dong
 */
@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Autowired
    private DishMapper dishMapper;
    /**
     * 新增套餐
     * @param setmealDTO 新增的套餐数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithDish(SetmealDTO setmealDTO) {
        // 进行属性拷贝
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 保存套餐数据
        setMealMapper.insert(setmeal);

        // 得到套餐id
        Long setMealId = setmeal.getId();

        // 得到套餐的菜品数据
        List<SetmealDish> setMealDishes = setmealDTO.getSetmealDishes();
        if(setMealDishes != null && !setMealDishes.isEmpty()){
            setMealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setMealId);
            });
            // 批量保存套餐菜品数据
            setMealDishMapper.insertBatch(setMealDishes);
        }
    }

    /**
     * 分页查询套餐数据
     * @param setmealPageQueryDTO 分页查询条件
     * @return 分页查询结果
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 开启分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        // 执行查询
        Page<SetmealVO> page = setMealMapper.pageQuery(setmealPageQueryDTO);

        // 返回结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 套餐起售停售
     * @param status 状态
     * @param id 套餐id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // 套餐如果存在未启售菜品时无法启售
        if(status == StatusConstant.ENABLE){
            List<Dish> dishList = dishMapper.getBySetMaelId(id);
            if(dishList != null && !dishList.isEmpty()){
                dishList.forEach(dish ->{
                    if(dish.getStatus() == StatusConstant.DISABLE){
                        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setMealMapper.update(setmeal);
    }

    /**
     * 批量删除套餐
     * @param ids 套餐id列表
     */
    @Override
    public void delete(List<Long> ids) {
        // 起售中的套餐不能删除
        for (Long id : ids) {
            Setmeal setmeal = setMealMapper.getById(id);
            if(setmeal.getStatus() == 1){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        // 批量删除套餐
        setMealMapper.deleteByIds(ids);
    }

    /**
     * 修改套餐
     * @param setmealDTO 修改的套餐数据
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        // 属性拷贝
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 修改套餐数据
        setMealMapper.update(setmeal);

        // 删除原有的套餐和菜品的关联数据
        setMealDishMapper.deleteBySetmealId(setmealDTO.getId());

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDTO.getId());
            });
            // 批量保存套餐菜品数据
            setMealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 根据id查询套餐数据
     * @param id 套餐id
     * @return 套餐数据
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        // 首先得到套餐数据
        Setmeal setmeal = setMealMapper.getById(id);

        // 得到套餐的菜品数据
        List<SetmealDish> setMealDishes = setMealDishMapper.getBySetMaelId(id);

        // 数据封装
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setMealDishes);

        return setmealVO;
    }
}
