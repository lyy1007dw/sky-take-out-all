package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

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
}
