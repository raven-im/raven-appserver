package com.raven.appserver.restapi.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.group.service.GroupService;
import com.raven.appserver.pojos.ReqMsgParam;
import com.raven.appserver.restapi.RestApi;
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
@RequestMapping(value = "/misc", produces = APPLICATION_JSON_VALUE)
public class RestApiController {

    @Autowired
    private RestApi api;

    @PostMapping("/notification/user")
    public RestResult notify2User(@RequestBody ReqMsgParam param) {
        log.info("notify2User: from {} to {}", param.getFromUid(), param.getTargetUid());
        return api.sendNotify2User(param);
    }

    @PostMapping("/notification/conversation")
    public RestResult notify2Conversation(@RequestBody ReqMsgParam param) {
        log.info("notify2Conversation: from {} to {}", param.getFromUid(), param.getTargetUid());
        return api.sendNotify2Conversation(param);
    }

    @PostMapping("/message")
    public RestResult sendMessage(@RequestBody ReqMsgParam param) {
        log.info("sendMessage: from {} to {}", param.getFromUid(), param.getTargetUid());
        return api.sendMessage(param);
    }
}
