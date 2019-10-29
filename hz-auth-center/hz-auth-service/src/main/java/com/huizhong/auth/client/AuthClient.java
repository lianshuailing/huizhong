package com.huizhong.auth.client;

import com.huizhong.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shkstart
 * @create 2019-09-09 19:17
 */
@FeignClient("user-service")
public interface AuthClient extends UserApi {
}
