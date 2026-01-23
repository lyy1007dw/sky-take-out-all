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
}
