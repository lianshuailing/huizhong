package com.huizhong.cart.filter;

import com.huizhong.auth.entity.UserInfo;
import com.huizhong.auth.utils.JwtUtils;
import com.huizhong.cart.config.JwtProperties;
import com.huizhong.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shkstart
 * @create 2019-09-12 17:17
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

//    @Autowired
//    private JwtProperties jwt;

    private JwtProperties jwtProp;

    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public LoginInterceptor(JwtProperties jwtProp) {
        this.jwtProp = jwtProp;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取cookie中有没有token来判断是否登录
        String cookieValue = CookieUtils.getCookieValue(request, jwtProp.getCookieName());
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(cookieValue, jwtProp.getPublicKey());
            //request.setAttribute("userInfo", userInfo);
            // 放入userInfo，传递userInfo信息
            tl.set(userInfo);

            // 放行
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info("[购物车服务]：解析用户身份失败！", e);
            // 拦截
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 最后用完数据要清空
        tl.remove();
    }

    public static UserInfo getUserInfo(){
        return tl.get();
    }
}
