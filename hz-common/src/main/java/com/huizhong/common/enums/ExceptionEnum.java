package com.huizhong.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author shkstart
 * @create 2019-07-19 21:04
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    AGE_CANNOT_BE_NULL(400, "年龄不能为空！"),
    CATEGORY_IS_EMPTY(404, "分类找不到！"),
    BRAND_NOT_FOUND(404, "品牌未找到！"),
    BRAND_XINZENG_FAILD(500, "品牌新增失败！"),
    IMAGE_UPLOAD_FAILD(500, "图片上传失败！"),
    INVALID_FILE_TYPE(400, "图片格式不对！"),
    SPEC_GROUP_NOT_FOUND(404, "商品规格组没有查到"),
    GOODS_SAVE_ERROR(500, "新增商品失败"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数不存在"),
    GOODS_NOT_FOUND(404,"商品不存在"),
    SPU_DETAIL_NOT_FOUND(404, "商品详情不存在"),
    GOODS_SKU_NOT_FOUND(404,"sku没有找到"),
    PRICE_CANNOT_BE_NULL(400, "价格不能为空"),
    CATEGORY_NOT_FOUND(404, "商品分类没有找到"),
    USER_REGISTRY_IS_FREQUENT(300, "您注册的太频繁了，请稍等1分钟之后再操作！"),
    USER_REGISTRY_NO_LEGAL(400, "您输入的参数不合法！"),
    USER_REGISTRY_CODE_ERROR(400, "您输入的验证码不对！"),
    USER_REGISTRY_INSERT_FAIL(500, "新用户注册失败！"),
    USER_IS_NO_EXSIT(400, "该用户名不存在！"),
    USER_PWD_IS_ERROR(400, "您输入的密码有误！"),
    USER_CART_NOT_FOUND(404, "您的购物车为空！"),
    CREATE_ORDER_ERROR(500,"创建订单失败"),
    STOCK_NOT_ENOUGH(500,"库存不足"),
    ORDER_NOT_FOUND(404,"订单不存在"),
    ORDER_DETAIL_NOT_FOUND(404,"订单详情不存在"),
    ORDER_STATUS_NOT_FOUND(404,"订单状态不存在"),
    WX_PAY_ORDER_FAIL(500,"下单失败"),
    ORDER_STATUS_ERROR(400,"订单状态异常"),
    INVALID_SIGN_ERROR(400,"无效的签名"),
    INVALID_ORDER_PARAM(400,"订单参数异常"),
    UPDATE_ORDER_STATUS_ERROR(500,"更新订单状态失败"),
    ;
    private int code;
    private String msg;
}
