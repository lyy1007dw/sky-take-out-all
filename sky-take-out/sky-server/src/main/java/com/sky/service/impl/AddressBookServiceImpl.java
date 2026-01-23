package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 修改地址
     * @param addressBook 修改的参数
     */
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook 地址数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(AddressBook addressBook) {
        // 首先将当前用户的所有地址设置为非默认
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        // 再将当前地址设置为默认
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    /**
     * 获取默认地址
     * @return 默认地址
     */
    @Override
    public List<AddressBook> getDefault() {
       AddressBook addressBook = new AddressBook();
       addressBook.setUserId(BaseContext.getCurrentId());
       addressBook.setIsDefault(1);

       List<AddressBook> addressBookList = addressBookMapper.list(addressBook);
       return addressBookList;
    }
}
