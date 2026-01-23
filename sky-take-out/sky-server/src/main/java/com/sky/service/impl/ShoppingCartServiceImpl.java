package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author can dong
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车数据
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        // 每个用户只能操作自己的购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        // 查询当前商品在购物车中是否存在
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list != null && !list.isEmpty()){
            // 如果存在数量加一
            shoppingCart = list.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        }else{
            // 不存在则新增对应的数据
            // 判断当前商品是菜品还是套餐数据
            Long dishId = shoppingCart.getDishId();
            if(dishId != null){
                // 当前添加的是菜品数据
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            }else{
                // 当前添加的是套餐数据
                Long setMealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setMealMapper.getById(setMealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            // 插入数据
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return 购物车数据
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        // 根据用户id查询购物车数据
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        return shoppingCartMapper.list(shoppingCart);
    }
}
