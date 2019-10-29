package com.huizhong.gateway.config;

import com.huizhong.auth.utils.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author shkstart
 * @create 2019-09-10 14:11
 */
@Data
@ConfigurationProperties(prefix = "hz.jwt")
public class JwtProperties {

    private String pubKeyPath;// 公钥

    private PublicKey publicKey; // 公钥

    private String cookieName;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init(){
        try {
            // 获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            logger.error("[网关服务]：初始化公钥失败！", e);
            throw new RuntimeException();
        }
    }

}
