package com.huizhong.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author shkstart
 * @create 2019-09-10 14:51
 */
@Data
@ConfigurationProperties(prefix = "hz.filter")
public class FilterProperties {
    private List<String> allowPaths;
}
