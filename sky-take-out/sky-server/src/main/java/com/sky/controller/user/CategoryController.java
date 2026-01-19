package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author can dong
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @RequestMapping("list")
    public Result<List<Category>> list(Integer type){
        log.info("查询分类：{}", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
