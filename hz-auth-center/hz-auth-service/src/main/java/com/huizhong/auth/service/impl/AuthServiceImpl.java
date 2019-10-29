package com.huizhong.auth.service.impl;

import com.huizhong.auth.client.AuthClient;
import com.huizhong.auth.config.JwtProperties;
import com.huizhong.auth.entity.UserInfo;
import com.huizhong.auth.service.AuthService;
import com.huizhong.auth.utils.JwtUtils;
import com.huizhong.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author shkstart
 * @create 2019-09-09 16:09
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
@EnableFeignClients
public class AuthServiceImpl implements AuthService {
    private static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthClient authClient;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public String authentication(String username, String password) {
        try {

            ResponseEntity<User> resp = authClient.queryUser(username, password);
            if (!resp.hasBody()) {
                logger.info("[授权服务]：用户信息不存在{}", username);
                return null;
            }
            // 获取登录用户
            User user = resp.getBody();
            // 获得token
            String token =JwtUtils.generateToken(new UserInfo(user.getId(),
                    user.getUsername()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());

            return token;
        } catch (Exception e) {
            logger.info("[授权服务]：用户{}授权失败,登录失败", username);
            return null;
        }

    }
}
