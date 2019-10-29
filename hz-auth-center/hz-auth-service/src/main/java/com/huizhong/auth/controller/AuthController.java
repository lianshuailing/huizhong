package com.huizhong.auth.controller;

import com.huizhong.auth.config.JwtProperties;
import com.huizhong.auth.entity.UserInfo;
import com.huizhong.auth.service.AuthService;
import com.huizhong.auth.utils.JwtUtils;
import com.huizhong.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shkstart
 * @create 2019-09-09 16:07
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * ===授权===
     *
     * 登录授权
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username, @RequestParam("password") String password,
            HttpServletRequest request, HttpServletResponse response) {

        // 登录校验
        String token = authService.authentication(username, password);
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getCookieMaxAge(), true);

        return ResponseEntity.ok().build();
    }


    /**
     * ===鉴权===
     *
     * 验证用户信息
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("HZ_TOKEN") String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.从token中解析token信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            //2.解析成功要重新刷新token
            String tokenNew = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            //3.更新Cookie中的token
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), tokenNew, jwtProperties.getCookieMaxAge());
            //4.解析成功返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            //5.出现异常,相应401   抛出异常，证明token无效，直接返回401
            e.printStackTrace();
            logger.info("[授权服务]：验证用户信息异常：", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
