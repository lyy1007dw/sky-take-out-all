package com.sky.service;

/**
 * @author can dong
 */

import com.sky.dto.CategoryDTO;
import org.springframework.stereotype.Service;

public interface CategoryService {
    /**
     * 新增分类
     * @param categoryDTO 新增的分类数据
     */
    void save(CategoryDTO categoryDTO);
}
