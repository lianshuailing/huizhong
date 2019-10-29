package com.huizhong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author shkstart
 * @create 2019-07-18 17:28
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.huizhong.item.mapper")
public class HzItemService {
    public static void main(String[] args) {
        SpringApplication.run(HzItemService.class, args);
    }
}
