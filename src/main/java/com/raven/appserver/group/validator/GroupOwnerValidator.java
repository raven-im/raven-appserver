package com.raven.appserver.group.validator;


import com.raven.appserver.group.bean.model.GroupModel;
import com.raven.appserver.group.mapper.GroupMapper;
import com.raven.appserver.utils.RestResultCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

@Component
public class GroupOwnerValidator implements Validator {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public boolean isValid(String operator, String groupId) {
        Example example = new Example(GroupModel.class);
        example.createCriteria()
            .andEqualTo("uid", groupId)
            .andNotEqualTo("status", 2);
        List<GroupModel> model = groupMapper.selectByExample(example);
        return model != null && model.get(0).getOwner().equals(operator);
    }

    @Override
    public RestResultCode errorCode() {
        return RestResultCode.GROUP_ERROR_NO_AUTH_OPERATE;
    }
}
