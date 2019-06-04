package com.raven.appserver.upload.controller;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.upload.FastDFSClientWrapper;
import com.raven.appserver.upload.pojos.OutputFileInfo;
import com.raven.appserver.utils.RestResultCode;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping(path="/upload")
public class UploadController {

    private FastDFSClientWrapper client;

    @Autowired
    public UploadController(FastDFSClientWrapper clientWrapper) {
        this.client = clientWrapper;
    }

    @PostMapping
    public @ResponseBody RestResult userUpload(@RequestParam("file") MultipartFile file) {
        log.info("user upload");

        if (file.isEmpty()) {
            return RestResult.generate(RestResultCode.UPLOAD_FILE_EMPTY);
        }

        try {
            return client.uploadFile(file);
        } catch (IOException e) {
            log.error("upload error:", e.getMessage());
        }
        return RestResult.generate(RestResultCode.UPLOAD_FILE_UPLOAD_ERROR);
    }

}
