package com.huizhong.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author shkstart
 * @create 2019-09-09 14:20
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class HzAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(HzAuthApplication.class, args);
    }
}
