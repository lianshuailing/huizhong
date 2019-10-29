package com.huizhong.order.config;

import com.huizhong.order.interceptors.LoginInterceptor;
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
        //改成拦截/order服务下所有，这样就不拦截暴露出去的该地址http://ueizdi.natappfree.cc/wxpay/notify，微信可以调该地址了。
        registry.addInterceptor(new LoginInterceptor(jwtProp)).addPathPatterns("/order/**");
    }
}
