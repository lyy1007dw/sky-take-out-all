package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author can dong
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;


    /**
     * 提交订单
     *
     * @param ordersSubmitDTO 订单信息
     * @return 订单提交结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 1.处理业务异常信息
        // 地址为空的情况
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 购物车为空的情况
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 2.保存订单信息
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());

        // 3.向订单表插入一条数据
        ordersMapper.insert(orders);

        // 4.向订单详情表插入多条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        // 5.清空购物车
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());

        // 6.封装返回结果
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 订单支付信息
     * @return 订单支付结果
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                //商户订单号
                ordersPaymentDTO.getOrderNumber(),
                //支付金额，单位 元
                new BigDecimal("0.01"),
                //商品描述
                "苍穹外卖订单",
                //微信用户的openid
                user.getOpenid()
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo 订单号
     */
    @Override
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = ordersMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);
    }

    /**
     * 订单分页查询
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param status   订单状态
     * @return 订单分页查询结果
     */
    @Override
    public PageResult historyPage(Integer page, Integer pageSize, Integer status) {
        // 开启分页查询
        PageHelper.startPage(page, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setStatus(status);
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());

        // 分页查询条件
        Page<Orders> ordersList = ordersMapper.historyPage(ordersPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();

        // 封装订单VO
        if (ordersList != null && ordersList.getTotal() > 0) {
            for (Orders order : ordersList) {
                Long id = order.getId(); // 订单id
                // 根据订单id查询订单详情
                List<OrderDetail> orderDetails = orderDetailMapper.listByOrderId(id);
                // 判断订单详情是否为空
                if (orderDetails != null && !orderDetails.isEmpty()) {
                    OrderVO orderVO = new OrderVO();
                    BeanUtils.copyProperties(order, orderVO);
                    orderVO.setOrderDetailList(orderDetails);
                    orderVOList.add(orderVO);
                }
            }
        }
        return new PageResult(ordersList.getTotal(), orderVOList);
    }

    /**
     * 订单详情
     *
     * @param id 订单id
     * @return 订单详情
     */
    @Override
    public OrderVO detail(Long id) {
        OrderVO orderVO = new OrderVO();
        // 查询订单信息
        Orders orders = ordersMapper.getById(id);
        BeanUtils.copyProperties(orders, orderVO);

        // 查询订单详情
        List<OrderDetail> orderDetails = orderDetailMapper.listByOrderId(id);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    /**
     * 取消订单
     *
     * @param id 订单id
     */
    @Override
    public void cancel(Long id) {
        // 根据id查询订单
        Orders orders = ordersMapper.getById(id);

        // 查看订单是否存在
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 待支付和待接单状态下，可以直接取消订单
        if (orders.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders updateOrders = new Orders();
        updateOrders.setId(orders.getId());

        // 如果待接单状态下取消订单，需要退款
        if (orders.getStatus() == 2) {
            // 调用微信支付接口，进行退款。由于没有商户号，这里的逻辑删除
            // 更改订单状态
            updateOrders.setStatus(Orders.CANCELLED);
        }

        // 更新订单状态
        updateOrders.setStatus(Orders.CANCELLED);
        updateOrders.setCancelReason("用户取消");
        updateOrders.setCancelTime(LocalDateTime.now());
        ordersMapper.update(updateOrders);
    }

    /**
     * 再来一单
     *
     * @param id 订单id
     */
    @Override
    public void repetition(Long id) {
        // 获取用户id
        Long userId = BaseContext.getCurrentId();

        // 获取订单详情
        List<OrderDetail> orderDetails = orderDetailMapper.listByOrderId(id);

        // 将订单详情添加到购物车对象列表中
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartList.add(shoppingCart);
        }
        // 批量插入购物车数据
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 订单条件查询
     *
     * @param ordersPageQueryDTO 订单查询条件
     * @return 订单分页查询结果
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 开启分页查询
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        // 执行分页查询
        Page<Orders> ordersList = ordersMapper.historyPage(ordersPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();
        String orderDishes;

        // 封装结果
        if (ordersList != null && ordersList.getTotal() > 0) {
            for (Orders orders : ordersList) {
                Long id = orders.getId();
                // 根据id查询订单详情
                List<OrderDetail> orderDetails = orderDetailMapper.listByOrderId(id);
                if (orderDetails != null && !orderDetails.isEmpty()){
                    // 获取订单详情中的菜名，用字符串展示
                    orderDishes = getOrderDishStr(orderDetails);
                    OrderVO orderVO = new OrderVO();
                    BeanUtils.copyProperties(orders, orderVO);
                    orderVO.setOrderDishes(orderDishes);
                    orderVOList.add(orderVO);
                }
            }
        } else {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        return new PageResult(ordersList.getTotal(), orderVOList);
    }

    /**
     * 获取订单菜品字符串
     *
     * @param orderDetails 订单详情列表
     * @return 订单菜品字符串
     */
    private String getOrderDishStr(List<OrderDetail> orderDetails){
        StringBuilder orderDishesSb = new StringBuilder();
        // 将每条订单详情中的菜名用字符串展示（格式：宫保鸡丁*3；）
        for (OrderDetail orderDetail : orderDetails) {
            orderDishesSb.append(orderDetail.getName()).append("*").append(orderDetail.getNumber()).append("；");
        }
        return orderDishesSb.toString();
    }
}
