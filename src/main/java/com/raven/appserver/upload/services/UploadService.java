package com.raven.appserver.upload.services;

import com.raven.appserver.common.RestResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    RestResult upload(MultipartFile file);
}
