package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author can dong
 */
@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 查询地址列表
     * @return 地址列表
     */
    @Override
    public List<AddressBook> list() {
        // 设置用户id
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        
        // 执行查询并返回结果
        return addressBookMapper.list(addressBook);
    }

    /**
     * 新增地址
     * @param addressBook 新增的参数
     */
    @Override
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * 删除地址
     * @param id 删除的id
     */
    public void delete(Integer id){
        addressBookMapper.deleteById(id);
    }

    /**
     * 根据id查询地址
     * @param id 查询的id
     * @return 地址
     */
    @Override
    public AddressBook getById(Integer id) {
        return addressBookMapper.getById(id);
    }
}
