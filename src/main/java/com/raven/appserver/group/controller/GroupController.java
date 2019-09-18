package com.raven.appserver.group.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.param.GroupListReqParam;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.group.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/group", produces = APPLICATION_JSON_VALUE)
public class GroupController {

    @Autowired
    private GroupService service;

    @PostMapping("/create")
    @RequiresAuthentication
    public RestResult create(@RequestBody GroupReqParam param) {
        log.info("group create name:{}", param.getName());
        return service.createGroup(param);
    }

    @PostMapping("/join")
    @RequiresAuthentication
    public RestResult join(@RequestBody GroupReqParam param) {
        log.info("group join:{}", param.getGroupId());
        return service.joinGroup(param);
    }

    @PostMapping("/quit")
    @RequiresAuthentication
    public RestResult quit(@RequestBody GroupReqParam param) {
        log.info("group quit:{}", param.getGroupId());
        return service.quitGroup(param);
    }

    @PostMapping("/dismiss")
    @RequiresAuthentication
    public RestResult dismiss(@RequestBody GroupReqParam param) {
        log.info("group dismiss:{}", param.getGroupId());
        return service.dismissGroup(param);
    }

    @PostMapping("/detail")
    @RequiresAuthentication
    public RestResult detailsPost(@RequestBody GroupReqParam param) {
        log.info("group details:{}", param.getGroupId());
        return service.groupDetail(param.getGroupId());
    }

    @GetMapping("/detail")
    @RequiresAuthentication
    public RestResult detailsGet(@RequestParam("id") String groupId) {
        log.info("group details:{}", groupId);
        return service.groupDetail(groupId);
    }

    @PostMapping("/details")
    @RequiresAuthentication
    public RestResult details(@RequestBody GroupListReqParam param) {
        log.info("group details:{}", param.getGroups().size());
        return service.groupDetails(param.getGroups());
    }
}
