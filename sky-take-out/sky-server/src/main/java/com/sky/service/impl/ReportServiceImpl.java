package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author can dong
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkSpaceService workSpaceService;

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
     * 销量排名
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 销量排名结果
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        // 时间转换
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSalesDTOList = ordersMapper.getTop10(beginTime, endTime);
        // 获取列表数据
        List<String> nameList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        // 封装返回结果
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    /**
     * 导出近30天运营数据
     * @param response 响应
     */
    @Override
    public void getBusinessDataExport(HttpServletResponse response) {
        // 1.获取概览数据
        LocalDate today = LocalDate.now();
        LocalDate beginDate = today.minusDays(30);
        LocalDate endDate = today.minusDays(1);
        BusinessDataVO businessData = workSpaceService.
                getBusinessData(LocalDateTime.of(beginDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX));
        // 2.写入Excel中
        InputStream in = this.getClass()
                .getClassLoader()
                .getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            // 2.1基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            // 2.2获取模板中的Sheet对象
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 2.3填写时间数据
            sheet.getRow(1).getCell(1).setCellValue(beginDate + "至" + endDate);
            // 2.4填写概览数据
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            // 3.获取明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = beginDate.plusDays(i);
                BusinessDataVO data = workSpaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(data.getTurnover());
                row.getCell(3).setCellValue(data .getValidOrderCount());
                row.getCell(4).setCellValue(data.getNewUsers());
                row.getCell(5).setCellValue(data.getUnitPrice());
                row.getCell(6).setCellValue(data.getNewUsers());
            }
            // 4.将Excel文件写入到响应中
            workbook.write(response.getOutputStream());

            // 5.关闭资源
            workbook.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
