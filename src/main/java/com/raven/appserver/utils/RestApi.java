package com.raven.appserver.utils;

import com.google.common.hash.Hashing;
import com.raven.appserver.common.RestResult;
import com.raven.appserver.pojos.ReqTokenParam;
import com.raven.appserver.pojos.RspTokenParam;
import java.nio.charset.StandardCharsets;
import java.util.Map;
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
public class RestApi {

    @Value("${app.key}")
    private String key;

    @Value("${app.secret}")
    private String secret;

    @Value("${app.url}")
    private String serverUrl;

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
}
