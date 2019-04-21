package com.tim.appserver.user.controller;

import com.tim.appserver.common.RestResult;
import com.tim.appserver.user.pojos.InputLogin;
import com.tim.appserver.user.pojos.InputUserCreate;
import com.tim.appserver.user.service.UserApplication;

import com.tim.appserver.utils.Constants;
import com.tim.appserver.utils.RestResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping(path="/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private UserApplication userApplication;

    @Autowired
    public UserController(UserApplication userApplication) {
        this.userApplication = userApplication;
    }

	@PostMapping(path="/login")
	public @ResponseBody RestResult userLogin(@RequestBody InputLogin data) {
        logger.info("User login called. username:{}", data.getUsername());

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
        logger.info("User logout called.");
        return userApplication.logout();
    }

    @PutMapping("/create")
    public RestResult createUser(@RequestBody InputUserCreate data) {
        return userApplication.createUser(data);
    }

    @PostMapping("/{uid}")
    public RestResult updateUser(@RequestBody InputUserCreate data, @PathVariable("uid") String uid) {
        return userApplication.updateUser(uid, data);
    }

    @PostMapping("/{uid}/state")
    public RestResult updateUserState(
        @RequestParam(value = "state", defaultValue = "0") Integer state
        , @PathVariable("uid") String uid) {
        return userApplication.updateUserState(uid, state);
    }

    @DeleteMapping("/{uid}")
    public RestResult deleteUser(@PathVariable("uid") String uid) {
        return userApplication.deleteUser(uid);
    }

    @GetMapping("/{uid}")
    public RestResult getUser(@PathVariable("uid") String uid) {
        return userApplication.getUser(uid);
    }

    @GetMapping("/list")
    public RestResult getUserList(@RequestParam(value = "type", required = false) Integer type,
        @RequestParam(value = "state", required = false) Integer state) {
        return userApplication.getUserList(type, state);
    }
}
