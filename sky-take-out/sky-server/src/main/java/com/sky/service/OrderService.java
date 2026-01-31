package com.sky.service;

import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.web.bind.annotation.GetMapping;

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

    /**
     * 取消订单
     * @param id 订单ID
     */
    void cancel(Long id);

    /**
     * 再来一单
     * @param id 订单ID
     */
    void repetition(Long id);

    /**
     * 按条件进行订单查询
     * @param ordersPageQueryDTO 订单查询参数
     * @return 订单查询结果
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 获取各个状态的订单统计信息
     *
     * @return 订单统计信息
     */
    OrderStatisticsVO statistics();

    /**
     * 确认订单
     * @param ordersConfirmDTO 确认订单参数
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);
}
