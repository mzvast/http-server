package com.silu.dinner.service;

import com.silu.dinner.bean.HttpResp;
import com.silu.dinner.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * User: qgan(qgan@v5.cn)
 * Date: 14-3-11 下午7:17
 */
public class ServerExceptionResolver extends SimpleMappingExceptionResolver {
    private static final Logger log = LoggerFactory.getLogger(ServerExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String newLine = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(newLine);
        stringBuilder.append("-------------------------------------response start").append(newLine);
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        ModelAndView modelAndView = new ModelAndView();


        if(ex instanceof ServerException) {
            ServerException e = (ServerException) ex;
            Map attributes = new HashMap<>();
            attributes.put("status",e.getCode());
            attributes.put("desc", e.getDesc());
            view.setAttributesMap(attributes);
            view.setUpdateContentLength(true);
            modelAndView.setView(view);
            stringBuilder.append("status:").append(e.getCode()).append(newLine);
            stringBuilder.append("desc:").append(e.getDesc()).append(newLine);
        } else {
            log.error("",ex);
            response.setStatus(500);
        }
        stringBuilder.append("-------------------------------------response end").append(newLine);
        log.debug(stringBuilder.toString());
        return modelAndView;
    }
}
