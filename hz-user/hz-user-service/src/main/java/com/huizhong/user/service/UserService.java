package com.huizhong.user.service;

import com.huizhong.user.pojo.User;

/**
 * @author shkstart
 * @create 2019-08-28 16:29
 */
public interface UserService {
    Boolean checkData(String data, Integer type);

    Boolean sendVerifyCode(String phone);

    Boolean register(User user, String code);

    User queryUser(String username, String password);
}
