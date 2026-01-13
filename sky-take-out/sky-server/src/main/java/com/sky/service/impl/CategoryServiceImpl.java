package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author can dong
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增分类
     * @param categoryDTO 新增的分类数据
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        // 属性拷贝
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 设置分类状态
        category.setStatus(StatusConstant.DISABLE);

        // 设置创建时间和修改时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        // 创建人和修改人id
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        // 进行创建
        categoryMapper.insert(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类分页查询参数
     * @return 分类分页结果
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        // 开启分页查询
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        // 执行查询
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);

        // 返回结果
        long total = page.getTotal();
        List<Category> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 启用禁用分类
     * @param status 分类状态
     * @param id 分类id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = new Category();
        category.setStatus(status);
        category.setId(id);

        categoryMapper.update(category);
    }

    /**
     * 删除分类
     * @param id 分类id
     */
    @Override
    public void delete(Long id) {
        categoryMapper.delete(id);
    }
}
