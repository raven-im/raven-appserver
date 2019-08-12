package com.raven.appserver.utils;

import com.google.common.hash.Hashing;
import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.param.GroupOutParam;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.pojos.ReqTokenParam;
import com.raven.appserver.pojos.RspTokenParam;
import java.nio.charset.StandardCharsets;
import java.util.Map;
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

//    @Value("${app.url}")
    private String serverUrl = "http://114.67.79.183:8080/raven/admin"; // TODO

    @Autowired
    private RestTemplate client;

    public RspTokenParam getToken(String userId) {

        ReqTokenParam param = new ReqTokenParam(key, userId);
        HttpEntity<ReqTokenParam> entity = new HttpEntity<>(param, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "gateway/token", HttpMethod.POST, entity, RestResult.class);
        RestResult resp = respEntity.getBody();
        Map<String, String> map = (Map) resp.getData();
        return new RspTokenParam(key, userId, map.get("token"));
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
    public GroupOutParam createGroup(GroupReqParam reqParam) {
        HttpEntity<GroupReqParam> entity = new HttpEntity<>(reqParam, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "/group/create", HttpMethod.POST, entity, RestResult.class);
        RestResult resp = respEntity.getBody();
        Map<String, String> map = (Map) resp.getData();
        return new GroupOutParam(map.get("groupId"), map.get("converId"), DateTimeUtils.getDate(map.get("time")));
    }

    @Override
    public RestResultCode joinGroup(GroupReqParam reqParam) {
        HttpEntity<GroupReqParam> entity = new HttpEntity<>(reqParam, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "/group/join", HttpMethod.POST, entity, RestResult.class);
        RestResult resp = respEntity.getBody();
        if (RestResultCode.COMMON_SUCCESS.getCode() == resp.getRspCode()) {
            return RestResultCode.COMMON_SUCCESS;
        } else {
            return RestResultCode.newInstance(resp.getRspCode());
        }
    }

    @Override
    public RestResultCode quitGroup(GroupReqParam reqParam) {
        HttpEntity<GroupReqParam> entity = new HttpEntity<>(reqParam, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "/group/quit", HttpMethod.POST, entity, RestResult.class);
        RestResult resp = respEntity.getBody();
        if (RestResultCode.COMMON_SUCCESS.getCode() == resp.getRspCode()) {
            return RestResultCode.COMMON_SUCCESS;
        } else {
            return RestResultCode.newInstance(resp.getRspCode());
        }
    }

    @Override
    public RestResultCode dismissGroup(GroupReqParam reqParam) {
        HttpEntity<GroupReqParam> entity = new HttpEntity<>(reqParam, authHeaders());
        ResponseEntity<RestResult> respEntity = client.exchange(serverUrl + "/group/dismiss", HttpMethod.POST, entity, RestResult.class);
        RestResult resp = respEntity.getBody();
        if (RestResultCode.COMMON_SUCCESS.getCode() == resp.getRspCode()) {
            return RestResultCode.COMMON_SUCCESS;
        } else {
            return RestResultCode.newInstance(resp.getRspCode());
        }
    }
}
