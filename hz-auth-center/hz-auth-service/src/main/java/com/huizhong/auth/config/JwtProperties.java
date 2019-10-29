package com.huizhong.auth.config;

import com.huizhong.auth.utils.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author shkstart
 * @create 2019-09-09 15:32
 */
@Component
@ConfigurationProperties(prefix = "hz.jwt")
@Data
public class JwtProperties {
    private String secret; // 密钥

    private String pubKeyPath;// 公钥

    private String priKeyPath;// 私钥

    private int expire;// token过期时间

    private int cookieMaxAge;// cookie生存时间
    private String cookieName;// cookie名称

    private PublicKey publicKey; // 公钥

    private PrivateKey privateKey; // 私钥

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    // 对象一旦实例化后，就应该读取 公钥和私钥
    @PostConstruct
    public void init(){
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥和私钥失败！", e);
            // 转化为自定义异常 或 开发、测试时如下
            throw new RuntimeException();
        }
    }

    // getter setter ...

}
