package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author can dong
 */
@Mapper
public interface OrdersMapper {
    /**
     * 新增订单
     *
     * @param orders 订单信息
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber 订单号
     * @return 订单信息
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders 订单信息
     */
    void update(Orders orders);

    /**
     * 订单分页查询
     * @param ordersPageQueryDTO 订单分页查询条件
     * @return 订单信息
     */
    Page<Orders> historyPage(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id 订单id
     * @return 订单信息
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 根据状态统计订单数量
     * @param status 订单状态
     * @return 订单数量
     */
    @Select("select count(*) from orders where status = #{status}")
    Integer countByStatus(Integer status);
}
