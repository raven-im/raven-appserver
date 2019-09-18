package com.raven.appserver.upload.controller;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.upload.services.UploadService;
import com.raven.appserver.utils.RestResultCode;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping(path="/aws_upload")
public class AwsUploadController {

    private UploadService service;

    @Autowired
    public AwsUploadController(UploadService service) {
        this.service = service;
    }

    @PostMapping
    @RequiresAuthentication
    public @ResponseBody RestResult uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("file upload");

        if (file.isEmpty()) {
            return RestResult.generate(RestResultCode.UPLOAD_FILE_EMPTY);
        }
        return service.upload(file);
    }

//    @GetMapping(path="/meta")
//    public @ResponseBody RestResult getFileMeta(@RequestParam("group") String group,
//        @RequestParam("path") String path) {
//        log.info("get file meta data.");
//
//        if (StringUtils.isEmpty(group) || StringUtils.isEmpty(path)) {
//            return RestResult.generate(RestResultCode.UPLOAD_FILE_UPLOAD_PARAMETER_ERROR);
//        }
//        return client.getFileMetaData(group, path);
//    }

}
