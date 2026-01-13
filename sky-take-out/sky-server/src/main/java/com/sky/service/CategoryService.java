package com.sky.service;

/**
 * @author can dong
 */

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 启用禁用分类
     * @param status 状态
     * @param id 分类id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 删除分类
     * @param id 分类id
     */
    void delete(Long id);

    /**
     * 修改分类
     * @param categoryDTO 分类数据
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 查询分类
     * @param type 分类类型
     * @return 分类列表
     */
    List<Category> list(Integer type);
}
