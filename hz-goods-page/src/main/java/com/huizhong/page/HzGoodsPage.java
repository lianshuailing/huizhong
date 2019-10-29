package com.huizhong.page;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author shkstart
 * @create 2019-08-16 10:20
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class HzGoodsPage {
    public static void main(String[] args) {
        SpringApplication.run(HzGoodsPage.class, args);
    }
}
