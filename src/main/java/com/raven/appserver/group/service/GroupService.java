package com.raven.appserver.group.service;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.model.GroupModel;
import com.raven.appserver.group.bean.param.GroupOutParam;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.utils.RestResultCode;

public interface GroupService {

    RestResult createGroup(GroupReqParam reqParam);

    RestResultCode joinGroup(GroupReqParam reqParam);

    RestResultCode quitGroup(GroupReqParam reqParam);

    RestResultCode dismissGroup(GroupReqParam reqParam);

    RestResult groupDetail(String groupId);
}
