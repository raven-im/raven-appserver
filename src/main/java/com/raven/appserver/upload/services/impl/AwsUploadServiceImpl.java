package com.raven.appserver.upload.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.raven.appserver.common.RestResult;
import com.raven.appserver.upload.pojos.OutputFileInfo;
import com.raven.appserver.upload.services.UploadService;
import com.raven.appserver.utils.RestResultCode;
import com.raven.appserver.utils.ShiroUtils;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: bbpatience
 * @date: 2019/9/13
 * @description: upload service.
 **/

@Slf4j
@Service
public class AwsUploadServiceImpl implements UploadService {

    public static final String UPLOAD_PATH = "app";
    private static final String SEPARATOR = "/";
    private static final String META_KEY = "key";
    private static final String META_USER_ID = "uid";
    private static final String META_TIME = "time";
    private static final String META_TYPE = "type";
    private static final String META_FILE_NAME = "name";
    private static final String META_FILE_SIZE = "size";
    private static final String META_FILE_EXT = "ext";

    @Value("${s3.bucket}")
    private String bucket;

    @Value("${app.key}")
    private String key;

    @Value("${s3.download_url}")
    private String url;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public RestResult upload(MultipartFile file) {
        try {
            String uid = (String) ShiroUtils.getAttribute(ShiroUtils.USER_UID);
            if (StringUtils.isEmpty(uid)) {
                throw new AmazonServiceException("uid invalid");
            }
            String path = key + SEPARATOR + uid + SEPARATOR + UPLOAD_PATH + SEPARATOR + file.getOriginalFilename();
            log.info("upload file bucket {}  path {}", bucket, path);
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.addUserMetadata(META_KEY, key);
            metadata.addUserMetadata(META_USER_ID, uid);
            metadata.addUserMetadata(META_TIME, String.valueOf(System.currentTimeMillis() / 1000));
            metadata.addUserMetadata(META_TYPE, FilenameUtils.getExtension(file.getName()));
            metadata.addUserMetadata(META_FILE_NAME, file.getName());
            metadata.setContentLength(file.getSize());

            PutObjectRequest request = new PutObjectRequest(bucket, path, file.getInputStream(), metadata);
            s3Client.putObject(request);

            OutputFileInfo fileInfo = new OutputFileInfo(file.getOriginalFilename(), file.getSize(), getAccessUrl(path));
            return RestResult.success().setData(fileInfo);
        } catch(AmazonServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RestResult.generate(RestResultCode.UPLOAD_FILE_UPLOAD_ERROR);
    }

    private String getAccessUrl(String path) {
        String fileUrl = url  + SEPARATOR + path;
        log.info("fileUrl:" + fileUrl);
        return fileUrl;
    }
}
