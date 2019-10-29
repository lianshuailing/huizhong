package com.huizhong.page.client;

import com.huizhong.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: cuzz
 * @Date: 2018/11/9 16:06
 * @Description:
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi{
}
