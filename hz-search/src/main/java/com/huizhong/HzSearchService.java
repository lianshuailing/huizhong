package com.huizhong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author shkstart
 * @create 2019-08-09 8:47
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class HzSearchService {
    public static void main(String[] args) {
        SpringApplication.run(HzSearchService.class, args);
    }
}
