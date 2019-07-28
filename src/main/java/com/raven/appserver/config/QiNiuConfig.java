package com.raven.appserver.config;

import com.raven.appserver.upload.bean.TokenBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: bbpatience
 * @date: 2018/12/5
 * @description: QiNiuConfig
 **/
@Configuration
@RefreshScope
public class QiNiuConfig {
    @Value("${qiniu.appKey}")
    private String appKey;

    @Value("${qiniu.secret}")
    private String appSecret;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Bean
    public TokenBean getTokenBean() {
        return new TokenBean(appKey, appSecret, bucket);
    }
}
