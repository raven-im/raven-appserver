package com.raven.appserver.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: bbpatience
 * @date: 2019/4/21
 * @description: RspTokenParam
 **/
@AllArgsConstructor
@Data
public class RspTokenParam {
    @JsonProperty
    private String appKey;
    @JsonProperty
    private String uid;
    @JsonProperty
    private String token;
}
