package com.huizhong.gateway.filter;

import com.huizhong.auth.entity.UserInfo;
import com.huizhong.auth.utils.JwtUtils;
import com.huizhong.common.utils.CookieUtils;
import com.huizhong.gateway.config.FilterProperties;
import com.huizhong.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shkstart
 * @create 2019-09-10 14:16
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Autowired
    private JwtProperties JwtProps;

    @Autowired
    private FilterProperties filterProps;

    // pre 是请求执行前  post 是请求执行后
    @Override
    public String filterType() {
//        return "pre"; 或 下面用提供的常量方式
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
//        return 5;
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取Request
        HttpServletRequest request = ctx.getRequest();
        // 获取请求路径（URI）
        String requestURI = request.getRequestURI();
        // 判断是否在 “允许” 列表里
        // 判断白名单
        return !isAllowsPath(requestURI);// true是拦截走下面的run里自定义的拦截逻辑，false放行。
    }

    private boolean isAllowsPath(String requestURI) {
        // 定义一个标记
        Boolean flag = false;
        // 遍历允许访问的路径
        for (String path : filterProps.getAllowPaths()) {
            // 判断是否允许
            if (requestURI.startsWith(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        // 获取token
        String token = CookieUtils.getCookieValue(request, JwtProps.getCookieName());
        // 校验
        try {
            // 校验通过什么都不做，即放行
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, JwtProps.getPublicKey());

            // HttpServletRequest 域 和 ZuulRequest域
            // request.setAttribute("Access-Token", "xxxxxxx");
            // ctx.addZuulRequestHeader("Access-Token", "xxxxxxx");     (ps：请求经过网关时请求头会丢失) 1.可以用这个方法继续往下传 头信息！
            // TODO 接入权限系统 权限校验

        } catch (Exception e) {
            // e.printStackTrace();
            // 校验出现异常，返回403  被禁止
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
            logger.error("[网关服务]：非法访问，未登录，地址：{}", request.getRemoteHost());
        }
        return null;
    }
}
