package com.huizhong.item.web;

import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.item.service.impl.UserServiceImpl;
import com.huizhong.test.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author shkstart
 * @create 2019-07-19 14:18
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;


//    /**
//     * 新增用户，演示 统一异常处理
//     * @param user
//     * @return
//     */
//    @PostMapping
//    public ResponseEntity<User> insertUser(User user){
//        if(Objects.isNull(user) || user.getAge() == null){
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //age为null,返回400 参数不合法
//            throw new RuntimeException("age 不能为空！！！");                  //返回500 因为是应用程序抛出异常。springmvc经过辨别后返回前端一状态码
//        }
//        user = userService.save(user);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(user); //成功create的话，返回201
//    }

    /**
     * 新增用户，演示 统一异常处理 升级
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<User> saveUser(User user){
        if(Objects.isNull(user) || user.getAge() == null){
            throw new HzException(ExceptionEnum.AGE_CANNOT_BE_NULL);
        }
        user = userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }






}
