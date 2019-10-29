package com.huizhong.user.api;

import com.huizhong.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author shkstart
 * @create 2019-08-28 16:21
 */
public interface UserApi {

    @GetMapping("query")
    ResponseEntity<User> queryUser(@RequestParam("username") String username, @RequestParam("password") String password);
}
