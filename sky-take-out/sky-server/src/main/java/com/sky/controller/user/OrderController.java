package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理
 * @author can dong
 */
@RestController
@RequestMapping("/user/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户提交订单
     * @param ordersSubmitDTO 订单信息
     * @return 订单提交结果
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户提交订单，订单信息：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 订单支付信息
     * @return 订单支付结果
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<String> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success();
    }

    /**
     * 历史订单分页查询
     *
     * @param page 当前页
     * @param pageSize 每页大小
     * @param status 订单状态
     * @return 订单分页查询结果
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> historyPage(Integer page, Integer pageSize, Integer status){
        log.info("历史订单分页查询");
        PageResult pageResult = orderService.historyPage(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * 订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> detail(@PathVariable Long id){
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.detail(id);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     *
     * @param id 订单ID
     * @return 取消结果
     */
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id){
        log.info("取消订单：{}", id);
        orderService.cancel(id);
        return Result.success();
    }

    /**
     * 再来一单
     *
     * @param id 订单ID
     * @return 再来一单结果
     */
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id){
        log.info("再来一单：{}", id);
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 催单
     * @param id 订单id
     * @return 催单结果
     */
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable Long id){
        log.info("客户催单：{}", id);
        orderService.reminder(id);
        return Result.success();
    }
}
