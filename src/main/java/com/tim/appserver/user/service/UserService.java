package com.tim.appserver.user.service;

import com.tim.appserver.common.RestResult;
import com.tim.appserver.user.bean.UserBean;
import com.tim.appserver.user.pojos.InputUserCreate;
import com.tim.appserver.utils.RestResultCode;
import java.util.List;

public interface UserService {
    RestResult login(String username, String password);

    RestResultCode logout();

    String createUser(InputUserCreate data);

    RestResultCode updateUser(String uid, InputUserCreate data);

    RestResultCode updateUserState(String uid, Integer state);

    RestResultCode deleteUser(String uid);

    UserBean getUser(String uid);

    List<UserBean> getUserList(Integer type, Integer state);

    UserBean getUserByUsername(String username);
}
