package com.silu.dinner.controller;

import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.constant.StatusDesc;
import com.silu.dinner.bean.HttpResp;
import com.silu.dinner.exception.ServerException;
import com.silu.dinner.service.FileService;
import com.sun.istack.internal.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by silu on 15-1-14.
 */
@Controller
@RequestMapping(value = "/dinner", produces = "application/json")
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    
    @Value("${file.download.url.prefix}")
    private String downPrefix;
    
    @Autowired
    private FileService fileService;

    @RequestMapping(method = RequestMethod.POST, value = "/file/upload")
    @ResponseBody
    public Callable<HttpResp> fileUpload(@NotNull final MultipartFile file) {
        LOGGER.debug("servlet thread is {}", Thread.currentThread().getName());
        return () -> {
            LOGGER.debug("execute thread is {}", Thread.currentThread().getName());
            try {
                String accesskey = fileService.storeFile(file.getInputStream(), file.getOriginalFilename(), file.getSize());
                Map<String, String> value = new HashMap();
                value.put("url", String.format("%s/%s/%s",downPrefix,"/file/download",accesskey));
                HttpResp resp = HttpResp.newSuccessResp();
                resp.setBody(value);
                return resp;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new ServerException(StatusCode.FILE_FAIL_TO_UPLOAD, StatusDesc.FILE_FAIL_TO_UPLOAD_DESC);
            }
        };
    }
}
