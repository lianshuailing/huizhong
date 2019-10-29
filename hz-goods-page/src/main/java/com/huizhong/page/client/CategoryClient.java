package com.huizhong.page.client;

import com.huizhong.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: cuzz
 * @Date: 2018/11/9 15:50
 * @Description:
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
