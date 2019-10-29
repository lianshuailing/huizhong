package com.huizhong.order.config;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants.SignType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WXPayConfiguration {

    // ConfigurationProperties两种使用方式：1 之前一直在类上面使用 2 在Bean标签下面使用，spring注入PayConfig对象，及后注入Payconfig里属性对象的值

    // WXPay wxPay(PayConfig payConfig) 通过微信支付 官方给的demo，看到WXPay wxPay(PayConfig payConfig)需要一个PayConfig对象，所以先要构造PayConfig
    @Bean
    @ConfigurationProperties(prefix = "hz.pay")
    public PayConfig payConfig() {
        return new PayConfig();
    }

    @Bean
    public WXPay wxPay(PayConfig payConfig) {
        return new WXPay(payConfig, SignType.HMACSHA256);
    }
}
