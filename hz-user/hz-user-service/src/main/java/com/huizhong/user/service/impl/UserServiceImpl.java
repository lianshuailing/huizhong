package com.huizhong.user.service.impl;

import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.common.utils.NumberUtils;
import com.huizhong.user.Utils.CodecUtils;
import com.huizhong.user.mapper.UserMapper;
import com.huizhong.user.pojo.User;
import com.huizhong.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author shkstart
 * @create 2019-08-28 16:37
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:phone";
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new HzException(ExceptionEnum.USER_REGISTRY_NO_LEGAL);//输入参数不合法
        }
        return userMapper.selectCount(record) == 0;
    }

    @Override
    public Boolean sendVerifyCode(String phone) {
        try {
            String key = KEY_PREFIX + phone;

            String code = NumberUtils.generateCode(6);

            Map<String, String> msg = new HashMap<>();
            msg.put("phone", phone);
            msg.put("code", code);

            // 向rabbit中 发送 生成的验证码
            amqpTemplate.convertAndSend("hz.sms.exchange", "sms.verify.code", msg);

            // 保存验证码
            redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("[验证码服务]：user微服务发送验证码异常，phone：{}", phone);
            return false;
        }
    }

    @Override
    public Boolean register(User user, String code) {
        // 从redis中取验证码 code
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        // 验证页面传来的code 和 redis中的code是否一致
        // 不一致，返回自定义异常
        if (!StringUtils.equals(code, cacheCode)) {
            throw new HzException(ExceptionEnum.USER_REGISTRY_CODE_ERROR);
        }
        // 一致，放行
        // 取用户名、密码
        // 密码加密 （明文+salt）再md5加密
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //String md5Hex = CodecUtils.md5Hex(user.getPassword(), salt);
        // 存入库中
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        user.setCreated(new Date());

        return userMapper.insert(user) == 1 ? true : false;
    }

    @Override
    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        //
        User user = userMapper.selectOne(record);
        // 验证user
        if (user == null) {
            throw new HzException(ExceptionEnum.USER_IS_NO_EXSIT);
        }
        // 判断密码
        if (!StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password, user.getSalt()))) {
            throw new HzException(ExceptionEnum.USER_PWD_IS_ERROR);
        }
        return user;
    }
}
