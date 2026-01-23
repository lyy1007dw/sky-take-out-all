package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * @author can dong
 */
public interface AddressBookService {
    /**
     * 查询地址列表
     * @return 地址列表
     */
    List<AddressBook> list();

    /**
     * 新增地址
     * @param addressBook 新增的参数
     */
    void add(AddressBook addressBook);

    /**
     * 删除地址
     * @param id 地址id
     */
    void delete(Integer id);

    /**
     * 根据id查询地址
     * @param id 地址id
     * @return 地址
     */
    AddressBook getById(Integer id);

    /**
     * 修改地址
     * @param addressBook 修改的参数
     */
    void update(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param addressBook 地址数据
     */
    void setDefault(AddressBook addressBook);

    /**
     * 获取默认地址
     * @return 默认地址
     */
    List<AddressBook>  getDefault();
}
