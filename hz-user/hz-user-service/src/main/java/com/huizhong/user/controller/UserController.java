package com.huizhong.user.controller;

import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.user.pojo.User;
import com.huizhong.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author shkstart
 * @create 2019-08-28 16:27
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        // 1：
//        Boolean boo = userService.checkData(data, type);
//        if (boo == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//        return ResponseEntity.ok(boo);

        // 2: 推荐
        return ResponseEntity.ok(userService.checkData(data, type));
    }

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Boolean> sendVerifyCode(String phone) {
//        Boolean boo = this.userService.sendVerifyCode(phone);
//        if (boo == null || !boo) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);

        // 2 推荐
        return ResponseEntity.ok(userService.sendVerifyCode(phone));
    }

    /**
     *  注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result, @RequestParam("code") String code) {
//        其实这段代码意义不大，因为 前段web做了友好提示(前端校验)  所以错误的参数还能发到controller处说明 非法绕过
//        if (result.hasFieldErrors()) {
//            throw new RuntimeException(result.getFieldErrors().stream()
//                    .map(e -> e.getDefaultMessage()).collect(Collectors.joining("|")));
//        }

        Boolean boo = userService.register(user, code);
        if (!boo) {
            throw new HzException(ExceptionEnum.USER_REGISTRY_INSERT_FAIL);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        return ResponseEntity.ok(userService.queryUser(username, password));
    }


}
