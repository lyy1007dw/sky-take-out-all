package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
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
}
