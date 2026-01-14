package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author can dong
 */
@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;
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
}
