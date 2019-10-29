package com.huizhong.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author shkstart
 * @create 2019-08-28 16:40
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.huizhong.user.mapper")
public class HzUserService {
    public static void main(String[] args) {
        SpringApplication.run(HzUserService.class, args);
    }
}
