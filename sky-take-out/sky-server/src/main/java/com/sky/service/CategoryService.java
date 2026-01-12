package com.sky.service;

/**
 * @author can dong
 */

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

public interface CategoryService {
    /**
     * 新增分类
     * @param categoryDTO 新增的分类数据
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分类分页
     * @param categoryPageQueryDTO 分类分页查询参数
     * @return 分类分页结果
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
