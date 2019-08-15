package com.raven.appserver.pojos;

import com.raven.appserver.common.MessageType;
import com.raven.appserver.common.MsgContentType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: bbpatience
 * @date: 2019/8/15
 * @description: ReqMsgParam
 **/
@Data
@AllArgsConstructor
public class ReqMsgParam {
    private String fromUid;
    private String targetUid;
    private MessageType msgType;
    private MsgContentType contentType;
    private String content;
}
