package com.raven.appserver.group.bean.param;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupOutParam {

    private String groupId;

    private String converId;

    private Date time;
}
