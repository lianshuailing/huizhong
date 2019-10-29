package com.huizhong.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hz.worker")
public class IdWorkerProperties {
    private long workerId;
    private long dataCenterId;
}
