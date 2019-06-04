package com.raven.appserver.upload;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.raven.appserver.common.RestResult;
import com.raven.appserver.upload.pojos.OutputFileInfo;
import com.raven.appserver.user.service.UserService;
import com.raven.appserver.utils.RestResultCode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: bbpatience
 * @date: 2019/6/4
 * @description: FastDFSClientWrapper
 **/
@Slf4j
@Component
public class FastDFSClientWrapper {

    @Autowired
    private UserService userService;

    @Autowired
    private FastFileStorageClient storageClient;

    @Value("${fdfs.storage-port}")
    private String storagePort;

    @Value("${fdfs.res-host}")
    private String serverUrl;

    public RestResult uploadFile(MultipartFile file) throws IOException {
        if (!userService.isUserLogin()) {
            return RestResult.generate(RestResultCode.USER_USER_NOT_LOGIN);
        }
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils
            .getExtension(file.getOriginalFilename()), null);
        log.info("storePath:" + storePath);
        OutputFileInfo fileInfo = new OutputFileInfo(getResAccessUrl(storePath));
        return RestResult.success().setData(fileInfo);
    }

    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = serverUrl + ":" + storagePort + "/" + storePath.getFullPath();
        log.info("fileUrl:" + fileUrl);
        return fileUrl;
    }

    public void deleteFile(String fileUrl) {

        log.info("File to delete :" + fileUrl);
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            log.info("groupName:" + storePath.getGroup() + "------" + " path："+storePath.getPath());
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            log.warn(e.getMessage());
        }
    }

    public InputStream downloadFile(String fileUrl) {
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            byte[] fileByte = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (Exception e) {
            log.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

}
