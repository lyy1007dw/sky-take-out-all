package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderOverViewVO;

import java.time.LocalDateTime;

/**
 * @author can dong
 */
public interface WorkSpaceService {
    /**
     * 获取今日数据
     * @param begin 开始时间
     * @param end 结束时间
     * @return BusinessDataVO 业务数据
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 获取订单概览数据
     * @param begin 开始时间
     * @param end 结束时间
     * @return OrderOverViewVO 订单概览数据
     */
    OrderOverViewVO getOrderOverView(LocalDateTime begin, LocalDateTime end);
}
