package com.huizhong.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author shkstart
 * @create 2019-08-28 16:20
 */
@Table(name = "tb_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Length(min = 4,max = 15,message = "用户名只能在4~15位之间")
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    @Length(min = 6,max = 25,message = "密码只能在6~25位之间")
    private String password;

    /**
     * 电话
     */
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 盐
     */
    @JsonIgnore
    private String salt;

    /**
     * 创建时间
     */
    private Date created;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", created=" + created +
                '}';
    }
}