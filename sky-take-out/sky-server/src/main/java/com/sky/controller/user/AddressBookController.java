package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 新增地址
     * @param addressBook 地址数据
     * @return 新增结果
     */
    @PostMapping
    public Result add(@RequestBody AddressBook addressBook){
        log.info("新增地址：{}", addressBook);
        addressBookService.add(addressBook);
        return Result.success();
    }

    /**
     * 删除地址
     * @param id 地址id
     * @return 删除结果
     */
    @DeleteMapping
    public Result delete(Integer id){
        log.info("根据id删除地址：{}", id);
        addressBookService.delete(id);
        return Result.success();
    }
}
