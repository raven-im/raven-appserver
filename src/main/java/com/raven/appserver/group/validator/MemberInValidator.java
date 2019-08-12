package com.raven.appserver.group.validator;

import com.raven.appserver.group.bean.model.GroupMemberModel;
import com.raven.appserver.group.mapper.GroupMemberMapper;
import com.raven.appserver.utils.RestResultCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

@Component
public class MemberInValidator implements Validator {

    @Autowired
    private GroupMemberMapper memberMapper;

    @Override
    public boolean isValid(String groupId, List<String> members) {
        //members 中有一个成员在群组中，就算失败。 members需要是一个净添加列表
        Example example = new Example(GroupMemberModel.class);
        example.createCriteria()
            .andNotEqualTo("status", 2)
            .andEqualTo("groupId", groupId)
            .andIn("memberUid", members);
        return memberMapper.selectCountByExample(example) == 0;
    }

    @Override
    public RestResultCode errorCode() {
        return RestResultCode.GROUP_ERROR_MEMBER_ALREADY_IN;
    }
}
