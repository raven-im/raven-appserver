package com.raven.appserver.user.controller;

import com.raven.appserver.upload.FastDFSClientWrapper;
import com.raven.appserver.upload.pojos.OutputFileInfo;
import com.raven.appserver.user.pojos.InputLogin;
import com.raven.appserver.utils.Constants;
import com.raven.appserver.common.RestResult;
import com.raven.appserver.user.pojos.InputUserCreate;
import com.raven.appserver.user.service.UserApplication;

import com.raven.appserver.utils.RestResultCode;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping(path="/user")
public class UserController {

    private UserApplication userApplication;

    private FastDFSClientWrapper client;

    @Autowired
    public UserController(UserApplication userApplication, FastDFSClientWrapper clientWrapper) {
        this.userApplication = userApplication;
        this.client = clientWrapper;
    }

    @PostMapping(path="/login")
    public @ResponseBody RestResult userLogin(@RequestBody InputLogin data) {
        log.info("User login called. username:{}", data.getUsername());

        if (StringUtils.isEmpty(data.getUsername())) {
            return RestResult.generate(RestResultCode.USER_INVALID_USERNAME);
        }

        if (StringUtils.isEmpty(data.getPassword())) {
            return RestResult.generate(RestResultCode.USER_INVALID_PASSWORD);
        }
        return userApplication.login(data.getUsername(), data.getPassword());
    }

    @PostMapping(path="/logout")
    public @ResponseBody RestResult userLogout() {
        log.info("User logout called.");
        return userApplication.logout();
    }

    @PutMapping("/create")
    @RequiresRoles(Constants.USER_SUPER_ADMIN)
    public RestResult createUser(@RequestBody InputUserCreate data) {
        return userApplication.createUser(data);
    }

    @PostMapping("/{uid}")
//    @RequiresRoles(Constants.USER_SUPER_ADMIN)
    public RestResult updateUser(@RequestBody InputUserCreate data, @PathVariable("uid") String uid) {
        return userApplication.updateUser(uid, data);
    }

    @PostMapping("/{uid}/state")
    @RequiresRoles(Constants.USER_SUPER_ADMIN)
    public RestResult updateUserState(
        @RequestParam(value = "state", defaultValue = "0") Integer state
        , @PathVariable("uid") String uid) {
        return userApplication.updateUserState(uid, state);
    }

    @DeleteMapping("/{uid}")
    @RequiresRoles(Constants.USER_SUPER_ADMIN)
    public RestResult deleteUser(@PathVariable("uid") String uid) {
        return userApplication.deleteUser(uid);
    }

    @GetMapping("/{uid}")
//    @RequiresRoles(Constants.USER_SUPER_ADMIN)
    public RestResult getUser(@PathVariable("uid") String uid) {
        return userApplication.getUser(uid);
    }

    @GetMapping("/list")
//    @RequiresRoles(Constants.USER_SUPER_ADMIN)
    public RestResult getUserList(@RequestParam(value = "type", required = false) Integer type,
        @RequestParam(value = "state", required = false) Integer state) {
        return userApplication.getUserList(type, state);
    }

    @PostMapping("/{uid}/portrait")
    public @ResponseBody RestResult uploadFile(@RequestParam("file") MultipartFile file,
        @PathVariable("uid") String uid) {
        log.info("user update portrait");

        if (file.isEmpty()) {
            return RestResult.generate(RestResultCode.UPLOAD_FILE_EMPTY);
        }

        try {
            RestResult result = client.uploadFile(file);
            if (result.getRspCode() == RestResultCode.COMMON_SUCCESS.getCode()) {
                // update the portrait.
                InputUserCreate data = new InputUserCreate();
                OutputFileInfo fileInfo = (OutputFileInfo)result.getData();
                data.setPortrait(fileInfo.getUrl());
                userApplication.updateUser(uid, data);
            }
            return result;
        } catch (IOException e) {
            log.error("upload error:", e.getMessage());
        }
        return RestResult.generate(RestResultCode.UPLOAD_FILE_UPLOAD_ERROR);
    }
}
