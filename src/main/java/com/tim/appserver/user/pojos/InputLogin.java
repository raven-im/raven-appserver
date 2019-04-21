package com.tim.appserver.user.pojos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class InputLogin {
    @JsonProperty
    private String username;

    @JsonProperty
    private String password;
}
