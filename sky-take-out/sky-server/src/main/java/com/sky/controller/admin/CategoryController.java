package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 * @author can dong
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 新增分类
     * @param categoryDTO 新增的分类数据
     * @return 新增结果
     */
    @PostMapping
    public Result save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类，分类数据：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分页查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用分类
     * @param status 状态
     * @param id 分类id
     * @return 启用禁用结果
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("启用禁用菜品分类：{}, {}", id, status);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id 分类id
     * @return 删除结果
     */
    @DeleteMapping
    public Result delete(Long id){
        log.info("删除分类：{}", id);
        categoryService.delete(id);
        return Result.success();
    }
}
