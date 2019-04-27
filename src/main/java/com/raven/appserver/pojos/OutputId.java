package com.raven.appserver.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: bbpatience
 * @date: 2018/10/21
 * @description: OutputId
 **/
@Data
@AllArgsConstructor
public class OutputId {
    @JsonProperty("id")
    private String uid;
}
