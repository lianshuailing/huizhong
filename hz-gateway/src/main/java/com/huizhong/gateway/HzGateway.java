package com.huizhong.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author shkstart
 * @create 2019-07-18 11:47
 */
@EnableZuulProxy
@SpringCloudApplication
public class HzGateway {
    public static void main(String[] args) {
        SpringApplication.run(HzGateway.class, args);
    }
}
