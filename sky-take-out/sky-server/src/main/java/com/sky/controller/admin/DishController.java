package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 * @author can dong
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO 新增的菜品数据
     * @return 新增结果
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品，{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询参数
     * @return 菜品分页结果
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品，{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids 菜品id
     * @return 删除结果
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品，{}", ids);
        dishService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品和对应的口味
     * @param id 菜品id
     * @return 菜品数据
     */
    @GetMapping("/{id}")
    public Result<DishVO> getByIdWithFlavor(@PathVariable Long id){
        log.info("根据id查询菜品和对应的口味：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * @param dishDTO 修改的菜品数据
     * @return 修改结果
     */
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品信息：{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }
}
