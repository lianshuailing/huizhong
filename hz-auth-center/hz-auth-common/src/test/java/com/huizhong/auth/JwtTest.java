//package com.huizhong.auth;
//
//import com.huizhong.auth.entity.UserInfo;
//import com.huizhong.auth.utils.JwtUtils;
//import com.huizhong.auth.utils.RsaUtils;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.security.PrivateKey;
//import java.security.PublicKey;
//
///**
// * @author shkstart
// * @create 2019-09-09 14:39
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class JwtTest {
//
//    private static final String pubKeyPath = "F:\\Test\\rsa\\rsa.pub";
//
//    private static final String priKeyPath = "F:\\Test\\rsa\\rsa.pri";
//
//    private PublicKey publicKey;
//
//    private PrivateKey privateKey;
//
//    @Test
//    public void testRsa() throws Exception {
//        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
//    }
//
//    @Before
//    public void testGetRsa() throws Exception {
//        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
//        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
//    }
//
//    @Test
//    public void testGenerateToken() throws Exception {
//        // 生成token
//        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
//        System.out.println("token = " + token);
//    }
//
//    @Test
//    public void testParseToken() throws Exception {
////        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU2ODAxMzEzOH0.UfZqaebgnajwdR2zaYJDcYx_nRYQTzy3RKAjbZ0Q0w-FsMblfAozRqf_EdP77hZ4NI_MnHnoBB23QdVECXhg1PQ6ZTnYxpUxc_8Kq6BfoOYfPe78fVGLQP8_BGojOjtEJX0VhLnOhSEQVWO658EgY1s81jG8-8dRDPJHZtAQuCs";
//        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MzEsInVzZXJuYW1lIjoidGVzdCIsImV4cCI6MTU2ODU5NzQ3M30.FTPOrb1TdQ158Mm4_DT4nhXJAAYpmzYExrIRfA_BdKwEny9OTMyKzgI4s8hSjz3wmCD0yvNmgI0c_v2B150kuT6Ojht9J0QT97mXFYJH0i1DSsTa058i8ZTkluC9zq_RPzKMC46Ryyn4m-9REaWQEhL2oHHzup99-uNDSgptUU8";
//        // 解析token
//        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
//        System.out.println("id: " + user.getId());
//        System.out.println("userName: " + user.getUsername());
//    }
//}
