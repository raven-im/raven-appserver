package com.raven.appserver.group.validator;

import com.raven.appserver.user.bean.UserBean;
import com.raven.appserver.user.mapper.UserMapper;
import com.raven.appserver.utils.RestResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserMapper mapper;

    @Override
    public boolean isValid(String uid) {
        Example example = new Example(UserBean.class);
        example.createCriteria()
            .andEqualTo("uid", uid)
            .andNotEqualTo("state", 2);
        return mapper.selectCountByExample(example) != 0;
    }

    @Override
    public RestResultCode errorCode() {
        return RestResultCode.USER_USER_NOT_FOUND;
    }
}
