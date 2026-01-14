package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 套餐管理
 * @author can dong
 */
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    /**
     * 新增套餐
     * @param setmealDTO 新增的套餐数据
     * @return 新增结果
     */
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐，套餐数据：{}", setmealDTO);
        setMealService.saveWithDish(setmealDTO);
        return Result.success();
    }
}
