package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author can dong
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 营业额统计结果
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        // 1.获取日期列表数据，格式: "2026-1-1,2026-1-2,2026-1-3"
        List<LocalDate> dateList = getDateList(begin, end);
        // 2.获取营业额列表数据，格式: "100.0,200.0,300.0"
        // 调用Mapper方法获取营业额列表数据（当天已完成的订单数据）
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            // 首先进行日期数据转换 LocalDate->LocalDateTime
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            // 封装查询条件
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = ordersMapper.getByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        // 3.封装结果返回
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 用户统计结果
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        // 1.获取日期列表数据，格式: "2026-1-1,2026-1-2,2026-1-3"
        List<LocalDate> dateList = getDateList(begin, end);

        // 2.获取用户处理
        // 新增用户
        List<Integer> newUserList = new ArrayList<>();
        // 全部用户
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            // 首先进行日期数据转换 LocalDate->LocalDateTime
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            // 获取全部用户
            Integer totalUser = getUserCount(null, endTime);
            // 获取新增用户
            Integer newUser = getUserCount(beginTime, endTime);
            newUserList.add(newUser);
            totalUserList.add(totalUser);
        }

        // 3.封装返回结果
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    /**
     * 订单统计
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 订单统计结果
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        // 1.获取日期列表数据，格式: "2026-1-1,2026-1-2,2026-1-3"
        List<LocalDate> dateList = getDateList(begin, end);

        // 2.处理订单数据
        List<Integer> totalOrderList = new ArrayList<>();
        List<Integer> validOrderList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            // 2.1获取全部订单数量
            Integer totalOrder = getOrdersCount(beginTime, endTime, null);
            // 2.2获取有效订单数量
            Integer validOrder = getOrdersCount(beginTime, endTime, Orders.COMPLETED);
            totalOrderList.add(totalOrder);
            validOrderList.add(validOrder);
        }
        // 3.封装返回结果
        // 3.1获取总订单数
        Integer totalOrderCount = totalOrderList.stream().mapToInt(Integer::intValue).sum();
        // 3.2获取有效订单数
        Integer validOrderCount = validOrderList.stream().mapToInt(Integer::intValue).sum();
        // 3.3获取订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = (double) validOrderCount / totalOrderCount;
        }
        // 4.返回结果
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(totalOrderList, ","))
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 获取日期列表
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 日期列表
     */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            // 日期加1
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }

    /**
     * 获取用户数量
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 用户数量
     */
    private Integer getUserCount(LocalDateTime begin, LocalDateTime end){
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        return userMapper.countByMap(map);
    }

    /**
     * 获取订单数量
     *
     * @param begin   开始时间
     * @param end     结束时间
     * @param status  订单状态
     * @return 订单数量
     */
    private Integer getOrdersCount(LocalDateTime begin, LocalDateTime end, Integer status){
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return ordersMapper.countByMap(map);
    }
}
