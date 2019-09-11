package com.raven.appserver.group.bean.param;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupDetailParam {

    private String groupId;

    private String name;

    private String portrait;

    private String conversationId;

    private String ownerUid;

    private int status;

    private List<String> members;

    private Date time;
}
