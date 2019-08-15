package com.raven.appserver.utils;

import com.google.common.hash.Hashing;
import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.pojos.ReqMsgParam;
import com.raven.appserver.pojos.ReqTokenParam;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author: bbpatience
 * @date: 2019/4/21
 * @description: RestUtils
 **/
@Component
@Slf4j
public class RestApiImpl implements RestApi {

    @Value("${app.key}")
    private String key;

    @Value("${app.secret}")
    private String secret;

    @Value("${app.url}")
    private String serverUrl;

    @Autowired
    private RestTemplate client;

    @Override
    public RestResult getToken(String userId) {
        ReqTokenParam param = new ReqTokenParam(key, userId);
        HttpEntity<ReqTokenParam> entity = new HttpEntity<>(param, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "gateway/token", HttpMethod.POST, entity, RestResult.class);
        return respEntity.getBody();
    }

    private HttpHeaders authHeaders() {
        String nonce = new ShortUuid.Builder().build().toString();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String toSign = secret + nonce + timestamp;
        String sign = Hashing.sha1()
            .hashString(toSign, StandardCharsets.UTF_8)
            .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("AppKey", key);
        headers.set("Nonce", nonce);
        headers.set("Timestamp", timestamp);
        headers.set("Sign", sign);

        return headers;
    }

    @Override
    public RestResult createGroup(GroupReqParam reqParam) {
        HttpEntity<GroupReqParam> entity = new HttpEntity<>(reqParam, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "/group/create", HttpMethod.POST, entity, RestResult.class);
        return respEntity.getBody();
    }

    @Override
    public RestResult joinGroup(GroupReqParam reqParam) {
        HttpEntity<GroupReqParam> entity = new HttpEntity<>(reqParam, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "/group/join", HttpMethod.POST, entity, RestResult.class);
        return respEntity.getBody();
    }

    @Override
    public RestResult quitGroup(GroupReqParam reqParam) {
        HttpEntity<GroupReqParam> entity = new HttpEntity<>(reqParam, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "/group/quit", HttpMethod.POST, entity, RestResult.class);
        return respEntity.getBody();
    }

    @Override
    public RestResult dismissGroup(GroupReqParam reqParam) {
        HttpEntity<GroupReqParam> entity = new HttpEntity<>(reqParam, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "/group/dismiss", HttpMethod.POST, entity, RestResult.class);
        return respEntity.getBody();
    }

    @Override
    public RestResult sendMessage(ReqMsgParam reqParam) {
        HttpEntity<ReqMsgParam> entity = new HttpEntity<>(reqParam, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "/msg/send", HttpMethod.POST, entity, RestResult.class);
        return respEntity.getBody();
    }
}
