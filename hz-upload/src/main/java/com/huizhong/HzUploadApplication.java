package com.huizhong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author shkstart
 * @create 2019-07-26 18:18
 */
@EnableDiscoveryClient
@SpringBootApplication
public class HzUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(HzUploadApplication.class, args);
    }
}
