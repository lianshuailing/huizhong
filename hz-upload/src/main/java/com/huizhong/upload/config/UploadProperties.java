package com.huizhong.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author shkstart
 * @create 2019-08-04 18:23
 */
@Data
@ConfigurationProperties(prefix = "hz.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;
}
