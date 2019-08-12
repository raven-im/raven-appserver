package com.raven.appserver.group.validator;


import com.raven.appserver.group.bean.model.GroupModel;
import com.raven.appserver.group.mapper.GroupMapper;
import com.raven.appserver.utils.RestResultCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

@Component
public class GroupValidator implements Validator {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public boolean isValid(String key) {
        Example example = new Example(GroupModel.class);
        example.createCriteria()
            .andEqualTo("uid", key)
            .andNotEqualTo("status", 2);
        return groupMapper.selectCountByExample(example) != 0;
    }

    @Override
    public RestResultCode errorCode() {
        return RestResultCode.GROUP_ERROR_INVALID_GROUP_ID;
    }
}
