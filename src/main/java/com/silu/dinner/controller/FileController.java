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
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Value("#{configproperties['file.download.url.prefix']}")
    private String downPrefix;

    @Autowired
    private FileService fileService;
    
    @Autowired
    private TaskExecutor taskExecutor;

    @RequestMapping(method = RequestMethod.POST, value = "/file/upload")
    @ResponseBody
    public Callable<HttpResp> fileUpload(@NotNull final MultipartFile file) {
        LOGGER.debug("servlet thread is {}", Thread.currentThread().getName());
        return () -> {
            LOGGER.debug("execute thread is {}", Thread.currentThread().getName());
            try {
                String accesskey = fileService.storeFile(file.getInputStream(), file.getOriginalFilename(), file.getSize());
                Map<String, String> value = new HashMap();
                value.put("url", String.format("%s/%s/%s",downPrefix,"file/download",accesskey));
                HttpResp resp = HttpResp.newSuccessResp();
                resp.setBody(value);
                return resp;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new ServerException(StatusCode.FILE_FAIL_TO_UPLOAD, StatusDesc.FILE_FAIL_TO_UPLOAD_DESC);
            }
        };
    }

    /**
     * 根据MessageId获取文件内容
     *
     * @return
     * TODO 待研究jetty的异步
     */
    @RequestMapping(value = "/file/download/{fileId}", method = RequestMethod.GET)
    public void fileContent(HttpServletRequest request, @PathVariable final String fileId) {
        LOGGER.debug("servlet thread is {}", Thread.currentThread().getName());
        final AsyncContext ctx = request.startAsync();
        taskExecutor.execute(() -> {
            LOGGER.debug("execute thread is {}, Async Supported? {}", Thread.currentThread().getName(), ctx.getRequest().isAsyncSupported());
            HttpServletResponse res = (HttpServletResponse) ctx.getResponse();
            HttpServletRequest request1 = (HttpServletRequest) ctx.getRequest();
            try {
                fileService.getfile(res, fileId, getRange(request1));
            } catch (ServerException e) {
                throw  e;
            } finally {
                ctx.complete();
            }
        });
    }

    /**
     * 下载文件时，获取range header
     *
     * @param request
     * @return
     */
    private Long getRange(HttpServletRequest request) {
        Long range = 0l;
        String rangeStr = request.getHeader("range");
        if (!StringUtils.isEmpty(rangeStr) && rangeStr.startsWith("bytes=")) {
            int minus = rangeStr.indexOf('-');
            if (minus > -1) {
                rangeStr = rangeStr.substring(6, minus);
            }
            try {
                range = Long.parseLong(rangeStr);
            } catch (NumberFormatException ignored) {
            }
        }

        return range;
    }


}
