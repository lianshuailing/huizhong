package com.huizhong.upload.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @author shkstart
 * @create 2019-08-02 17:55
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastClientImporter {
    // 解决jmx重复注册bean的问题
}
