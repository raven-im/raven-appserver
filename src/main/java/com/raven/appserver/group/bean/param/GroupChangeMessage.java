package com.raven.appserver.group.bean.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author: bbpatience
 * @date: 2019/9/30
 * @description: GroupChangeMessage
 **/
@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class GroupChangeMessage {
//    NORMAL(0),
//    CREATE(1),
//    JOIN(2),
//    QUIT(3),
//    KICK(4),
//    DISMISS(5);
    private int type;
    private String notification;
    private List<String> members;
}
