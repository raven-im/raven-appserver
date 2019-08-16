package com.raven.appserver.user.service.impl;

import com.raven.appserver.pojos.RspTokenParam;
import com.raven.appserver.restapi.RestApi;
import com.raven.appserver.common.RestResult;
import com.raven.appserver.pojos.OutputId;
import com.raven.appserver.user.bean.UserBean;
import com.raven.appserver.user.enums.UserState;
import com.raven.appserver.user.enums.UserType;
import com.raven.appserver.user.pojos.InputUserCreate;
import com.raven.appserver.user.pojos.OutputUserInfo;
import com.raven.appserver.user.service.UserApplication;
import com.raven.appserver.user.service.UserService;
import com.raven.appserver.utils.RestResultCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author: bbpatience
 * @date: 2018/10/22
 * @description: UserApplicationImpl
 **/
@Service
public class UserApplicationImpl implements UserApplication {

    @Value("${app.key}")
    private String key;

    private UserService userService;

    private RestApi restApi;

    @Autowired
    public UserApplicationImpl(UserService userService, RestApi api) {
        this.userService = userService;
        this.restApi = api;
    }


    @Override
    public RestResult login(String username, String password) {
        RestResult result = userService.login(username, password);
        if (RestResultCode.COMMON_SUCCESS.getCode() == result.getRspCode()) {
            RestResult imResult = restApi.getToken(result.getData().toString());
            Map<String, String> map = (Map) imResult.getData();
            return RestResult.success(new RspTokenParam(key, result.getData().toString(), map.get("token")));
        }
        return result;
    }

    @Override
    public RestResult logout() {
        RestResultCode result = userService.logout();
        if (RestResultCode.COMMON_SUCCESS == result) {
            return RestResult.success();
        } else {
            return RestResult.generate(result);
        }
    }

    @Override
    public RestResult createUser(InputUserCreate data) {
        if (StringUtils.isEmpty(data.getUsername()) || StringUtils.isEmpty(data.getName()) ||
            data.getUserType() > UserType.USER.getType() ||
            data.getUserType() < UserType.ADMIN.getType() ) {
            return RestResult.generate(RestResultCode.COMMON_INVALID_PARAMETER);
        }
        String username = data.getUsername();
        UserBean user = userService.getUserByUsername(username);
        if (user != null) {
            return RestResult.generate(RestResultCode.USER_USER_NAME_EXIST);
        }
        return RestResult.success(new OutputId(userService.createUser(data)));
    }

    @Override
    public RestResult updateUser(String uid, InputUserCreate data) {
        RestResultCode result = userService.updateUser(uid, data);
        if (RestResultCode.COMMON_SUCCESS == result) {
            return RestResult.success();
        } else {
            return RestResult.generate(result);
        }
    }

    @Override
    public RestResult updateUserState(String uid, Integer state) {
        RestResultCode result = userService.updateUserState(uid, state);
        if (RestResultCode.COMMON_SUCCESS == result) {
            return RestResult.success();
        } else {
            return RestResult.generate(result);
        }
    }

    @Override
    public RestResult deleteUser(String uid) {
        RestResultCode result = userService.deleteUser(uid);
        if (RestResultCode.COMMON_SUCCESS == result) {
            return RestResult.success();
        } else {
            return RestResult.generate(result);
        }
    }

    @Override
    public RestResult getUser(String uid) {
        OutputUserInfo userInfo = new OutputUserInfo(userService.getUser(uid));
        return RestResult.success().setData(userInfo);
    }

    @Override
    public RestResult getUserList(Integer type, Integer state) {
        if (type != null && (type > UserType.USER.getType() || type < UserType.ADMIN.getType()) ) {
            return RestResult.generate(RestResultCode.COMMON_INVALID_PARAMETER);
        }
        if (state != null && (state > UserState.DELETED.getState() || state < UserState.NORMAL.getState()) ) {
            return RestResult.generate(RestResultCode.COMMON_INVALID_PARAMETER);
        }
//        if (!userService.isUserLogin()) {
//            return RestResult.generate(RestResultCode.USER_USER_NOT_LOGIN);
//        }
        List<OutputUserInfo> userInfos = new ArrayList<>();
        userService.getUserList(type, state).forEach(user -> {
            OutputUserInfo userInfo = new OutputUserInfo(user);
            userInfos.add(userInfo);
        });
        return RestResult.success().setData(userInfos);
    }
}
