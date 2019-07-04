package com.raven.appserver.upload.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputQiniuFileInfo {

    @JsonProperty("token")
    private String token;

    @JsonProperty("url")
    private String url;

}
