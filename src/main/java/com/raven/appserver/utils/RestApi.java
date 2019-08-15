package com.raven.appserver.utils;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.pojos.ReqMsgParam;

public interface RestApi {
    RestResult getToken(String userId);

    RestResult createGroup(GroupReqParam reqParam);

    RestResult joinGroup(GroupReqParam reqParam);

    RestResult quitGroup(GroupReqParam reqParam);

    RestResult dismissGroup(GroupReqParam reqParam);

    RestResult sendMessage(ReqMsgParam reqParam);

    RestResult sendNotify2Conversation(ReqMsgParam reqParam);

    RestResult sendNotify2User(ReqMsgParam reqParam);
}
