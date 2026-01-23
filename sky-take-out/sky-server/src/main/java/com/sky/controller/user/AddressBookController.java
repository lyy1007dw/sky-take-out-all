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

    /**
     * 根据id查询地址
     * @param id 地址id
     * @return 查询结果
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Integer id){
        log.info("根据id查询地址：{}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 修改地址
     * @param addressBook 地址数据
     * @return 修改结果
     */
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook){
        log.info("修改地址：{}", addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     * @param addressBook 地址数据
     * @return 设置结果
     */
     @PutMapping("/default")
     public Result setDefault(@RequestBody AddressBook addressBook) {
         log.info("设置默认地址：{}", addressBook);
         addressBookService.setDefault(addressBook);
         return Result.success();
     }

     /**
      * 获取默认地址
      * @return 默认地址
      */
     @GetMapping("/default")
     public Result<AddressBook> getDefault() {
         log.info("获取默认地址");
         List<AddressBook> addressBookList = addressBookService.getDefault();
         if(addressBookList != null && addressBookList.size() == 1){
             return Result.success(addressBookList.get(0));
         }
         return Result.error("没有识别到默认地址");
     }
 }
