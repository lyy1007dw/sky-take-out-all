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
}
