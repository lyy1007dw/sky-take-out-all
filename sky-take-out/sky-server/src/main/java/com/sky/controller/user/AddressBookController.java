package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地址簿管理
 * @author can dong
 */
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询地址列表
     * @return 查询结果
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        log.info("获取当前用户的所有地址：{}", BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list();
        return Result.success(list);
    }
}
