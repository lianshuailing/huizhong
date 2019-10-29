package com.huizhong.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.huizhong.common.enums.ExceptionEnum;
import com.huizhong.common.exception.HzException;
import com.huizhong.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author shkstart
 * @create 2019-08-01 9:28
 */
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadServiceImpl {
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Autowired
    private UploadProperties prop;

    // 支持的文件类型
    private static final List<String> ALLOW_TYPES = Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/bmp");

    //1：上传到本地 或 tomcat目录下
    public String uploadImage1(MultipartFile file) {
        String contentType = file.getContentType();

        try {
            // 校验文件类型
            if(!ALLOW_TYPES.contains(contentType)){
                throw new HzException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            // 校验文件内容  ImageIO.read(file.getInputStream()) 正常读出来是一个图片
            BufferedImage image = ImageIO.read(file.getInputStream());
            // 不是一个正常图片的话一般返回的是null
            if(image == null){
                throw new HzException(ExceptionEnum.INVALID_FILE_TYPE);
            }
//            也可通过 看能否获得这些属性来 校验
//            image.getHeight();
//            image.getWidth();

            // 2、保存图片
            // 2.1、生成保存目录
            File dir = new File("D:\\Works\\UploadTest");
            if(!dir.exists()){
                dir.mkdirs();
            }
            // 2.2、保存图片   转移文件到、、、
            file.transferTo(new File(dir, file.getOriginalFilename()));
            // 2.3、拼接图片地址
            String url = "http://image.huizhong.com/upload/" + file.getOriginalFilename();

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            //上传文件失败
            log.error("上传文件失败", e);
            return null;
        }
    }


    //2：上传到FastDFS上
    public String uploadImage2(MultipartFile file) {
        String contentType = file.getContentType();

        try {
            // 校验文件类型
            if(!prop.getAllowTypes().contains(contentType)){
                throw new HzException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            // 校验文件内容  ImageIO.read(file.getInputStream()) 正常读出来是一个图片
            BufferedImage image = ImageIO.read(file.getInputStream());
            // 不是一个正常图片的话一般返回的是null
            if(image == null){
                throw new HzException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            // 2、保存图片
            //String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 上传并且生成缩略图
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);

            // 3、拼接图片地址
            String url = prop.getBaseUrl() + storePath.getFullPath();
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            //上传文件失败
            log.error("[文件上传]  上传文件失败", e);
            return null;
        }
    }
}
