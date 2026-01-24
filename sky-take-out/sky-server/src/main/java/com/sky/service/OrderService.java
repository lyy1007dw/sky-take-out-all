package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * @author can dong
 */
public interface OrderService {
    /**
     * 提交订单
     * @param ordersSubmitDTO 订单信息
     * @return 订单提交结果
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
