package com.raven.appserver.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: bbpatience
 * @date: 2019/8/15
 * @description: ReqMsgParam
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqMsgParam {
    private String fromUid;
    private String targetUid;
    private String content;
}
