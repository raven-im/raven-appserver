package com.tim.appserver.user.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * @author: bbpatience
 * @date: 2018/10/20
 * @description: InputUserCreate
 **/
@Data
public class InputUserCreate {

    @JsonProperty("name")
    private String name;

    @JsonProperty("username")
    private String username;

    @JsonProperty("type")
    private Integer userType;

    @JsonProperty("password")
    private String password;
}
