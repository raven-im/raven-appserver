package com.raven.appserver.group.service;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.model.GroupModel;
import com.raven.appserver.group.bean.param.GroupOutParam;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.utils.RestResultCode;
import java.util.List;

public interface GroupService {

    RestResult createGroup(GroupReqParam reqParam);

    RestResult joinGroup(GroupReqParam reqParam);

    RestResult quitGroup(GroupReqParam reqParam);

    RestResult dismissGroup(GroupReqParam reqParam);

    RestResult groupDetail(String groupId);

    RestResult groupDetails(List<String> groups);

    enum GroupOperationType {
        CREATE(0),
        JOIN(1),
        QUIT(2),
        DISMISS(3);

        private int type;

        GroupOperationType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
