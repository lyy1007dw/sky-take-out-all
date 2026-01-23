package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author can dong
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据分类id查询菜品和对应的口味
     * @param categoryId 分类id
     * @return 菜品列表
     */
    @GetMapping("/list")
    public Result<List<DishVO>> listWithFlavors(Long categoryId){
        log.info("查询分类id为{}的菜品数据", categoryId);

        // 增加缓存菜品逻辑
        // 1.从缓存中获取对应的key，规则：dish_分类id
        String key = "dish_" + categoryId;
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        // 2.如果存在，则直接返回
        if(list != null && !list.isEmpty()){
            return Result.success(list);
        }
        // 3.不存在则从数据库中查询再加入到缓存中
        list = dishService.listWithFlavors(categoryId);
        redisTemplate.opsForValue().set(key, list);
        return Result.success(list);
    }
}
