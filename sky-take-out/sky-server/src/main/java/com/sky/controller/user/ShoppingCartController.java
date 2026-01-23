package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车管理
 * @author can dong
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车数据
     * @return 添加结果
     */
    @PostMapping("add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车：{}", shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     * @return 购物车数据
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> showShoppingCart(){
        log.info("查看购物车信息");
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }
}
