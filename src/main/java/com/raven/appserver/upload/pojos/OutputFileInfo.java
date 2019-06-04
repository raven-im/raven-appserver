package com.raven.appserver.upload.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raven.appserver.user.bean.UserBean;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputFileInfo {

    @JsonProperty("url")
    private String url;

}
