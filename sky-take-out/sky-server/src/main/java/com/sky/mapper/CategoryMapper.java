package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author can dong
 */
@Mapper
public interface CategoryMapper {
    /**
     * 插入分类数据
     * @param category 分类数据
     */
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values" +
            "(#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime},#{createUser},#{updateUser})")
    public void insert(Category category);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类分页查询参数
     * @return 分类分页结果
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 更新员工数据
     * @param category 分类数据
     */
    void update(Category category);

    /**
     * 删除员工数据
     * @param id 分类id
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);

    /**
     * 根据类型查询
     * @param type 分类类型
     * @return 分类列表
     */
    List<Category> list(Integer type);
}
