package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author can dong
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单明细数据
     *
     * @param orderDetailList 订单明细数据列表
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * 根据订单ID查询订单明细
     *
     * @param id 订单ID
     * @return 订单明细列表
     */
    @Select("SELECT * FROM order_detail WHERE order_id = #{id}")
    List<OrderDetail> listByOrderId(Long id);
}
