package com.huizhong.order.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "tb_order")
public class Order {
//    `order_id` bigint(20) NOT NULL COMMENT '订单id',
//    `total_pay` bigint(20) NOT NULL COMMENT '总金额，单位为分',
//    `actual_pay` bigint(20) NOT NULL COMMENT '实付金额。单位:分。如:20007，表示:200元7分',
//    `promotion_ids` varchar(256) COLLATE utf8_bin DEFAULT '',
//    `payment_type` tinyint(1) unsigned zerofill NOT NULL COMMENT '支付类型，1、在线支付，2、货到付款',
//    `post_fee` bigint(20) NOT NULL COMMENT '邮费。单位:分。如:20007，表示:200元7分',
//    `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
//    `shipping_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流名称',
//    `shipping_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流单号',
//    `user_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户id',
//    `buyer_message` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '买家留言',
//    `buyer_nick` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '买家昵称',
//    `buyer_rate` tinyint(1) DEFAULT NULL COMMENT '买家是否已经评价,0未评价，1已评价',
//    `receiver_state` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '收获地址（省）',
//    `receiver_city` varchar(256) COLLATE utf8_bin DEFAULT '' COMMENT '收获地址（市）',
//    `receiver_district` varchar(256) COLLATE utf8_bin DEFAULT '' COMMENT '收获地址（区/县）',
//    `receiver_address` varchar(256) COLLATE utf8_bin DEFAULT '' COMMENT '收获地址（街道、住址等详细地址）',
//    `receiver_mobile` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人手机',
//    `receiver_zip` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人邮编',
//    `receiver` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
//    `invoice_type` int(1) DEFAULT '0' COMMENT '发票类型(0无发票1普通发票，2电子发票，3增值税发票)',
//    `source_type` int(1) DEFAULT '2' COMMENT '订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端',
    @Id
    private Long orderId;
    private Long totalPay;
    private Long actualPay;
    private Integer paymentType;
    private String promotionIds;
    private Long postFee = 0L;
    private Date createTime;
    private String shippingName;
    private String shippingCode;
    private Long userId;
    private String buyerMessage;
    private String buyerNick;
    private Boolean buyerRate;
    private String receiver;
    private String receiverMobile;
    private String receiverState;
    private String receiverCity;
    private String receiverDistrict;
    private String receiverAddress;
    private String receiverZip;
    private Integer invoiceType = 0; // 发票类型
    private Integer sourceType = 1;

    @Transient
    private OrderStatus orderStatus;

    @Transient
    private List<OrderDetail> orderDetails;
}
