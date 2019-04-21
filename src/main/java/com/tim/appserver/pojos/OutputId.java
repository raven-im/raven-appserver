package com.tim.appserver.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: bbpatience
 * @date: 2018/10/21
 * @description: OutputId
 **/
public class OutputId {
    @JsonProperty("id")
    private String uid;

    public OutputId(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
