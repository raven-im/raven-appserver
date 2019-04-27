package com.raven.appserver.user.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raven.appserver.user.bean.UserBean;
import lombok.Data;

@Data
public class OutputUserInfo {

    @JsonProperty("id")
    private String uid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("state")
    private Integer state;

    @JsonProperty("type")
    private Integer type;

    public OutputUserInfo(UserBean bean) {
        this.name = bean.getName();
        this.state = bean.getState();
        this.type = bean.getType();
        this.uid = bean.getUid();
    }
}
