package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
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
            // 封装查询条件
            Map<String, Object> map = new HashMap<>();
            map.put("end", endTime);
            // 获取全部用户
            Integer totalUser = userMapper.countByMap(map);
            // 获取新增用户
            map.put("begin", beginTime);
            Integer newUser = userMapper.countByMap(map);
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
}
