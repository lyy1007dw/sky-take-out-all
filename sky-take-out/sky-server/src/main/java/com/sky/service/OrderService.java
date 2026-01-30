package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

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

    /**
     * 订单支付
     * @param ordersPaymentDTO 订单支付信息
     * @return 订单支付结果
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo 订单号
     */
    void paySuccess(String outTradeNo);

    /**
     * 订单分页查询
     * @param page 当前页
     * @param pageSize 每页大小
     * @param status 订单状态
     * @return 订单分页查询结果
     */
    PageResult historyPage(Integer page, Integer pageSize, Integer status);

    /**
     * 订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    OrderVO detail(Long id);
}
