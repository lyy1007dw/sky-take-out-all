package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author can dong
 */
@RestController("usrSetMealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    /**
     * 根据分类id查询套餐数据
     * @param categoryId 分类id
     * @return 套餐数据
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "setMealCache", key = "#categoryId")
    public Result<List<Setmeal>> list(Long categoryId){
        log.info("根据分类id{}查询套餐数据", categoryId);
        List<Setmeal> setmealList = setMealService.list(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 根据套餐id查询包含的菜品数据
     * @param id 套餐id
     * @return 菜品数据
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getDishItemById(@PathVariable Long id){
        log.info("根据套餐id{}查询对应的菜品数据", id);
        List<DishItemVO> dishItemVOList = setMealService.getDishItemById(id);
        return Result.success(dishItemVOList);
    }
}
