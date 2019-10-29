package com.huizhong.order.service.impl;

import com.huizhong.auth.entity.UserInfo;
import com.huizhong.common.dto.CartDTO;
import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.common.utils.IdWorker;
import com.huizhong.item.pojo.Sku;
import com.huizhong.order.client.AddressClient;
import com.huizhong.order.client.GoodsClient;
import com.huizhong.order.dto.AddressDTO;
import com.huizhong.order.dto.OrderDTO;
import com.huizhong.order.enums.OrderStatusEnum;
import com.huizhong.order.enums.PayState;
import com.huizhong.order.interceptors.LoginInterceptor;
import com.huizhong.order.mapper.OrderDetailMapper;
import com.huizhong.order.mapper.OrderMapper;
import com.huizhong.order.mapper.OrderStatusMapper;
import com.huizhong.order.pojo.Order;
import com.huizhong.order.pojo.OrderDetail;
import com.huizhong.order.pojo.OrderStatus;
import com.huizhong.order.service.OrderService;
import com.huizhong.order.utils.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2019-09-14 15:54
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private PayHelper payHelper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper detailMapper;

    @Autowired
    private OrderStatusMapper statusMapper;

    /**
     *  新增订单
     * @param orderDTO
     * @return
     */
    @Transactional
    public Long createOrder(OrderDTO orderDTO) {

        Order order = new Order();
        // 新增订单
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDTO.getPaymentType());

        // 购买者信息
        UserInfo user = LoginInterceptor.getUserInfo();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);

        // 物流信息(构建的假数据)
        AddressDTO addr = AddressClient.findById(orderDTO.getAddressId());
        order.setReceiver(addr.getName());
        order.setReceiverAddress(addr.getAddress());
        order.setReceiverCity(addr.getCity());
        order.setReceiverDistrict(addr.getDistrict());
        order.setReceiverMobile(addr.getPhone());
        order.setReceiverState(addr.getState());
        order.setReceiverZip(addr.getZipCode());

        // 订单信息
        Map<Long, Integer> numMap = orderDTO.getCarts().stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
        Set<Long> ids = numMap.keySet();

        List<Sku> skus = goodsClient.querySkuByIds(new ArrayList<>(ids));

        List<OrderDetail> details = new ArrayList<>();
        long totalPay = 0L;

        for (Sku sku : skus) {
            Integer num = numMap.get(sku.getId());
            totalPay += sku.getPrice() * num;

            OrderDetail detail = new OrderDetail();
            detail.setImage(StringUtils.substringBefore(sku.getImages(), ","));
            detail.setNum(num);
            detail.setOrderId(orderId);
            detail.setOwnSpec(sku.getOwnSpec());
            detail.setPrice(sku.getPrice());
            detail.setSkuId(sku.getId());
            detail.setTitle(sku.getTitle());

            details.add(detail);
        }
        order.setTotalPay(totalPay);
        order.setActualPay(totalPay + order.getPostFee() - 0);
        // 把order写入数据库
        int count = orderMapper.insertSelective(order);
        if (count != 1) {
            log.error("[创建订单] 创建订单失败, orderId:{}", orderId);
            throw new HzException(ExceptionEnum.CREATE_ORDER_ERROR);
        }


        // 新增订单详情  OrderDetail
        count = detailMapper.insertList(details);
        if (count != details.size()) {
            log.error("[创建订单] 创建订单详情失败, orderId:{}", orderId);
            throw new HzException(ExceptionEnum.CREATE_ORDER_ERROR);
        }


        // 新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UNPAY.getCode());
        count = statusMapper.insertSelective(orderStatus);
        if (count != 1) {
            log.error("[创建订单] 创建订单状态失败, orderId:{}", orderId);
            throw new HzException(ExceptionEnum.CREATE_ORDER_ERROR);
        }

        // 减库存  取巧方案！注意！！！
        List<CartDTO> cartDTOS = orderDTO.getCarts();
        goodsClient.decreaseStock(cartDTOS);

        return orderId;
    }

    /**
     * 查询订单 订单明细  订单状态
     * @param id
     * @return
     */
    public Order queryOrderById(Long id) {
        // 查询订单
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order == null) {
            throw new HzException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        // 查询订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(id);
        List<OrderDetail> details = detailMapper.select(detail);
        if (CollectionUtils.isEmpty(details)) {
            throw new HzException(ExceptionEnum.ORDER_DETAIL_NOT_FOUND);
        }
        order.setOrderDetails(details);

        // 查询订单状态
        OrderStatus orderStatus = statusMapper.selectByPrimaryKey(id);
        if (orderStatus == null) {
            throw new HzException(ExceptionEnum.ORDER_STATUS_NOT_FOUND);
        }
        order.setOrderStatus(orderStatus);
        return order;
    }

    /**
     * 创建支付链接
     * @param orderId
     * @return
     */
    public String createPayUrl(Long orderId) {
        // 查询订单获取订单金额
        Order order = queryOrderById(orderId);
        // 判断订单状态
        Integer status = order.getOrderStatus().getStatus();
        if (status != OrderStatusEnum.UNPAY.getCode()) {
            throw new HzException(ExceptionEnum.ORDER_STATUS_ERROR);
        }
        // 真实的支付金额： 实际支付
        //Long actualPay = order.getActualPay();
        // 假数据
        Long actualPay = 1L;
        OrderDetail detail = order.getOrderDetails().get(0);
        String desc = detail.getTitle();
        return payHelper.createPayUrl(orderId, actualPay, desc);
    }

    /**
     * 支付回调
     * @param result
     */
    public void handleNotify(Map<String, String> result) {
        // 数据校验
        payHelper.isSuccess(result);
        // 校验签名
        payHelper.isValidSign(result);

        String totalFeeStr = result.get("total_fee");
        String tradeNoStr = result.get("out_trade_no");

        if (StringUtils.isBlank(tradeNoStr) || StringUtils.isBlank(totalFeeStr)) {
            throw new HzException(ExceptionEnum.INVALID_ORDER_PARAM);
        }

        Long totalFee = Long.valueOf(totalFeeStr);
        Long orderId = Long.valueOf(tradeNoStr);
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new HzException(ExceptionEnum.INVALID_ORDER_PARAM);
        }
        // FIXME 这里应该是不等于实际金额
        if (totalFee != 1L) {
            // 金额不符
            throw new HzException(ExceptionEnum.INVALID_ORDER_PARAM);
        }
        // 修改订单状态
        OrderStatus status = new OrderStatus();
        status.setStatus(OrderStatusEnum.PAYED.getCode());
        status.setOrderId(orderId);
        status.setPaymentTime(new Date());
        int count = statusMapper.updateByPrimaryKeySelective(status);
        if (count != 1) {
            throw new HzException(ExceptionEnum.UPDATE_ORDER_STATUS_ERROR);
        }
        log.info("[订单回调, 订单支付成功!], 订单编号:{}", orderId);
    }

    /**
     * 查询订单 支付状态
     * @param orderId
     * @return
     */
    public PayState queryOrderState(Long orderId) {
        OrderStatus orderStatus = statusMapper.selectByPrimaryKey(orderId);
        Integer status = orderStatus.getStatus();
        if (status != OrderStatusEnum.UNPAY.getCode()) {
            // 如果已支付, 真的是已支付
            return PayState.SUCCESS;
        }
        // 如果未支付, 但其实不一定是未支付, 必须去微信查询支付状态
        return payHelper.queryPayState(orderId);
    }
}
