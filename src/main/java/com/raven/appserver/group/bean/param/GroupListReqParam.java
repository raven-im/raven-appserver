package com.raven.appserver.group.bean.param;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupListReqParam {
    private List<String> groups;
}
