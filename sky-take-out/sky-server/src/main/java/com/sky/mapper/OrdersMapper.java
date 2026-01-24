package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author can dong
 */
@Mapper
public interface OrdersMapper {
    /**
     * 新增订单
     *
     * @param orders 订单信息
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber 订单号
     * @return 订单信息
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders 订单信息
     */
    void update(Orders orders);

}
