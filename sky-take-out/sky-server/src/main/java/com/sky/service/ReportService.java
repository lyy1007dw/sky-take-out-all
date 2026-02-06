package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @author can dong
 */
public interface ReportService {
    /**
     * 营业额统计
     * @param begin 开始时间
     * @param end 结束时间
     * @return 营业额统计结果
     */
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     * @param begin 开始时间
     * @param end 结束时间
     * @return 用户统计结果
     */
    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     * @param begin 开始时间
     * @param end 结束时间
     * @return 订单统计结果
     */
    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取Top10菜品
     * @param begin 开始时间
     * @param end 结束时间
     * @return Top10菜品结果
     */
    SalesTop10ReportVO top10(LocalDate begin, LocalDate end);

    /**
     * 导出近30天运营数据
     */
    void getBusinessDataExport(HttpServletResponse response);
}
