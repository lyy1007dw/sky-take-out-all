package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
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
        String dateListStr = getDateListStr(begin, end);

        // 2.获取营业额列表数据，格式: "100.0,200.0,300.0"
        String turnoverListStr = getTurnoverListStr(begin, end);

        // 3.封装结果返回
        return TurnoverReportVO.builder()
                .dateList(dateListStr)
                .turnoverList(turnoverListStr)
                .build();
    }

    /**
     * 获取日期列表数据
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 日期列表数据，格式: "2026-1-1,2026-1-2,2026-1-3"
     */
    private String getDateListStr(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            // 日期加1
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return StringUtils.join(dateList, ",");
    }

    /**
     * 获取营业额列表数据
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 营业额列表数据，格式: "100.0,200.0,300.0"
     */
    private String getTurnoverListStr(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            // 日期加1
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
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
        return StringUtils.join(turnoverList, ",");
    }
}
