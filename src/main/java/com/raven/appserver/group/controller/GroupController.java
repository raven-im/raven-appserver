package com.raven.appserver.group.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.param.GroupOutParam;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.group.service.GroupService;
import com.raven.appserver.utils.RestResultCode;
import lombok.extern.slf4j.Slf4j;
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
    public RestResult create(@RequestBody GroupReqParam param) {
        log.info("group create name:{}", param.getName());
        return RestResult.success(service.createGroup(param));
    }

    @PostMapping("/join")
    public RestResult join(@RequestBody GroupReqParam param) {
        log.info("group join:{}", param.getGroupId());
        RestResultCode code = service.joinGroup(param);
        if (RestResultCode.COMMON_SUCCESS.getCode() != code.getCode()) {
            return RestResult.failure(code.getCode());
        }
        return RestResult.success(code);
    }

    @PostMapping("/quit")
    public RestResult quit(@RequestBody GroupReqParam param) {
        log.info("group quit:{}", param.getGroupId());
        RestResultCode code = service.quitGroup(param);
        if (RestResultCode.COMMON_SUCCESS.getCode() != code.getCode()) {
            return RestResult.failure(code.getCode());
        }
        return RestResult.success(code);
    }

    @PostMapping("/dismiss")
    public RestResult dismiss(@RequestBody GroupReqParam param) {
        log.info("group dismiss:{}", param.getGroupId());
        RestResultCode code = service.dismissGroup(param);
        if (RestResultCode.COMMON_SUCCESS.getCode() != code.getCode()) {
            return RestResult.failure(code.getCode());
        }
        return RestResult.success(code);
    }

    @GetMapping("/detail")
    public RestResult detailsGet(@RequestParam("id") String groupId) {
        log.info("group details:{}", groupId);
        return service.groupDetail(groupId);
    }
}
