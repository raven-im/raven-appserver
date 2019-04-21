package com.tim.appserver.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tim.appserver.utils.RestResultCode;

@JsonInclude(Include.NON_EMPTY) //added for mock test.
public class RestResult {
    @JsonProperty("code")
    private Integer rspCode;
    @JsonProperty("msg")
    private String rspMsg;
    @JsonProperty("data")
    private Object data;

    public Integer getRspCode() {
        return rspCode;
    }

    public RestResult setRspCode(Integer rspCode) {
        this.rspCode = rspCode;
        return this;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public RestResult setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RestResult setData(Object data) {
        this.data = data;
        return this;
    }

    public static RestResult success() {
        return new RestResult()
            .setRspCode(RestResultCode.COMMON_SUCCESS.getCode())
            .setRspMsg(RestResultCode.COMMON_SUCCESS.getMsg());
    }

    public static RestResult success(Object o) {
        return RestResult.success().setData(o);
    }

    public static RestResult failure() {
        return new RestResult()
            .setRspCode(RestResultCode.COMMON_SERVER_ERROR.getCode())
            .setRspMsg(RestResultCode.COMMON_SERVER_ERROR.getMsg());
    }

    public static RestResult generate(Integer rspCode) {
        return new RestResult().setRspCode(rspCode).setRspMsg(RestResultCode.getMsg(rspCode));
    }

    public static RestResult generate(RestResultCode result) {
        return new RestResult()
            .setRspCode(result.getCode())
            .setRspMsg(result.getMsg());
    }
}
