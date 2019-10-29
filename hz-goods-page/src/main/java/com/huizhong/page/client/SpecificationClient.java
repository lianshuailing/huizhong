package com.huizhong.page.client;

import com.huizhong.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: cuzz
 * @Date: 2018/11/9 16:20
 * @Description:
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi{
}
