package com.raven.appserver.upload.controller;

import com.raven.appserver.common.RestResult;
import com.raven.appserver.upload.FastDFSClientWrapper;
import com.raven.appserver.upload.bean.TokenBean;
import com.raven.appserver.upload.pojos.OutputQiniuFileInfo;
import com.raven.appserver.utils.RestResultCode;
import java.io.IOException;
import java.util.UUID;
import javax.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(path="/qiniu_upload")
public class QiniuUploadController {

    private TokenBean tokenBean;

    @Autowired
    public QiniuUploadController(TokenBean bean) {
        this.tokenBean = bean;
    }

    @GetMapping
    public @ResponseBody RestResult getFileMeta(@RequestParam(value = "suffix") String suffix) {
        log.info("get file meta data.");

        String[] imgTypes = {"jpg","jpeg","bmp","gif","png"};
        boolean isSuffixValid = false;
        for(String fileSuffix : imgTypes) {
            if (suffix.equalsIgnoreCase(fileSuffix)) {
                isSuffixValid = true;
                break;
            }
        }
        if (!isSuffixValid) {
            return RestResult.generate(RestResultCode.UPLOAD_FILE_UPLOAD_FORMAT_ERROR);
        }
        String token = tokenBean.getToken();
        String imageUrl = UUID.randomUUID().toString() + "." + suffix;
        return RestResult.success().setData(new OutputQiniuFileInfo(token, imageUrl));
    }

}
