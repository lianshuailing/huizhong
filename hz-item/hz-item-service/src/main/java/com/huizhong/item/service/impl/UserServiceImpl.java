package com.huizhong.item.service.impl;

import com.huizhong.test.entity.User;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author shkstart
 * @create 2019-07-19 14:03
 */

@Service
public class UserServiceImpl {

    /**
     *
     * @param user
     * @return
     */
    public User save(User user){
        //新增用户
        int id = new Random().nextInt(100);
        user.setId(id);
        return user;
    }
}
