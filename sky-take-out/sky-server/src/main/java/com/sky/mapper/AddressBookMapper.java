package com.sky.mapper;

import com.sky.entity.AddressBook;
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
}
