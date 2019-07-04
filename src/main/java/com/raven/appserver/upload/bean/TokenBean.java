package com.raven.appserver.upload.bean;

import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;

/**
 * @author: bbpatience
 * @date: 2018/12/5
 * @description: TokenBean
 **/
@AllArgsConstructor
public class TokenBean {
    private String key;
    private String secret;
    private String bucket;

    public String getToken() {
        Auth auth = Auth.create(key, secret);
        return auth.uploadToken(bucket);
    }
}
