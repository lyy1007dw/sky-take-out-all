package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author can dong
 */
@Mapper
public interface AddressBookMapper {
    /**
     * 动态查询地址列表
     * @param addressBook 查询条件
     * @return 地址列表
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 插入地址数据
     * @param addressBook 插入的数据
     */
    @Insert("insert into address_book (user_id, consignee, sex, phone, province_code, province_name, " +
            "city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "values (#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}," +
            "#{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void insert(AddressBook addressBook);

    /**
     * 根据id删除地址数据
     * @param id 要删除的地址id
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Integer id);
}
