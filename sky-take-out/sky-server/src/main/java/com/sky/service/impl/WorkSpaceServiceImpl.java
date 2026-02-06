package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author can dong
 */
@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DishMapper dishMapper;
    /**
     * 获取今日数据
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return BusinessDataVO 业务数据
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        // 1.获取营业额
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        Double turnover = ordersMapper.getByMap(map);
        turnover = turnover == null ? 0.0 : turnover;

        // 2.获取全部订单数
        Integer orderCount = ordersMapper.countByMap(map);

        // 3.获取有效订单数
        map.put("status", Orders.COMPLETED);
        Integer validOrderCount = ordersMapper.countByMap(map);

        // 4.订单完成率
        Double orderCompletionRate = orderCount == 0 ? 0.0 : validOrderCount.doubleValue() / orderCount;

        // 5.平均客单价
        Double unitPrice = validOrderCount == 0 ? 0.0 : turnover / validOrderCount;

        // 6.新增用户
        Integer newUsers = userMapper.countByMap(map);

        // 7.返回结果
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }

    /**
     * 获取订单概览数据
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return OrderOverViewVO 订单概览数据
     */
    @Override
    public OrderOverViewVO getOrderOverView(LocalDateTime begin, LocalDateTime end) {
        // 1.获取全部订单数据
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        Integer allOrders = ordersMapper.countByMap(map);

        // 2.获取待接订单数
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = ordersMapper.countByMap(map);

        // 3.获取派送中订单数
        map.put("status", Orders.DELIVERY_IN_PROGRESS);
        Integer deliveredOrders = ordersMapper.countByMap(map);

        // 4.获取完成订单数
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = ordersMapper.countByMap(map);

        // 5.获取取消订单数
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = ordersMapper.countByMap(map);

        // 6.返回结果
        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .build();
    }

    /**
     * 获取菜品概览数据
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return DishOverViewVO 菜品概览数据
     */
    @Override
    public DishOverViewVO getDishOverView(LocalDateTime begin, LocalDateTime end) {
        // 1.获取起售状态菜品
        Map<String, Object> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        // 2.获取停售状态菜品
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        // 3.返回结果
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
