package com.huizhong.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author shkstart
 * @create 2019-09-11 14:31
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HzCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(HzCartApplication.class, args);
    }
}
