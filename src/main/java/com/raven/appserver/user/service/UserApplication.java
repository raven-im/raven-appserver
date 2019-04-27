package com.raven.appserver.user.service;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.user.pojos.InputUserCreate;

public interface UserApplication {
    RestResult login(String username, String password);

    RestResult logout();

    RestResult createUser(InputUserCreate data);

    RestResult updateUser(String uid, InputUserCreate data);

    RestResult updateUserState(String uid, Integer state);

    RestResult deleteUser(String uid);

    RestResult getUser(String uid);

    RestResult getUserList(Integer type, Integer state);
}
