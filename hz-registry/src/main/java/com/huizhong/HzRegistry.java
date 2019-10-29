package com.huizhong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author shkstart
 * @create 2019-07-18 11:11
 */
@EnableEurekaServer
@SpringBootApplication
public class HzRegistry {
    public static void main(String[] args) {
        SpringApplication.run(HzRegistry.class, args);
    }
}
