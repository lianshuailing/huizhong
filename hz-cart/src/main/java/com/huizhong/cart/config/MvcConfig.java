package com.huizhong.cart.config;

import com.huizhong.cart.filter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author shkstart
 * @create 2019-09-12 17:46
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProp;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor(jwtProp)).addPathPatterns("/**");
    }
}
