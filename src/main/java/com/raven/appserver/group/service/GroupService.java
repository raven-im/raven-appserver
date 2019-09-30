package com.raven.appserver.group.service;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.param.GroupReqParam;
import java.util.List;

public interface GroupService {

    RestResult createGroup(GroupReqParam reqParam);

    RestResult joinGroup(GroupReqParam reqParam);

    RestResult kickGroup(GroupReqParam reqParam);

    RestResult quitGroup(GroupReqParam reqParam);

    RestResult dismissGroup(GroupReqParam reqParam);

    RestResult groupDetail(String groupId);

    RestResult groupDetails(List<String> groups);

    enum GroupOperationType {
        NORMAL(0),
        CREATE(1),
        JOIN(2),
        QUIT(3),
        KICK(4),
        DISMISS(5);

        private int type;

        GroupOperationType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
