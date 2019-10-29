package com.huizhong.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author shkstart
 * @create 2019-09-14 15:52
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.huizhong.order.mapper")
public class HzOrderService {
    public static void main(String[] args) {
        SpringApplication.run(HzOrderService.class, args);
    }
}
