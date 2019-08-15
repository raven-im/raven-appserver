package com.raven.appserver.group.service;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.model.GroupModel;
import com.raven.appserver.group.bean.param.GroupOutParam;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.utils.RestResultCode;

public interface GroupService {

    RestResult createGroup(GroupReqParam reqParam);

    RestResult joinGroup(GroupReqParam reqParam);

    RestResult quitGroup(GroupReqParam reqParam);

    RestResult dismissGroup(GroupReqParam reqParam);

    RestResult groupDetail(String groupId);
}
