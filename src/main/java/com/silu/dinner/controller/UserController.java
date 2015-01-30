package com.silu.dinner.controller;

import com.silu.dinner.bean.HttpResp;
import com.silu.dinner.bean.User;
import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.exception.ServerException;
import com.silu.dinner.service.UserService;
import com.silu.dinner.util.ObjectMapperFactoryBean;
import com.silu.dinner.util.SessionUtil;
import com.sun.istack.internal.NotNull;
import net.sf.oval.constraint.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by silu on 15-1-15.
 */
@Controller
@RequestMapping(value = "/dinner", produces = "application/json")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private ObjectMapperFactoryBean objectMapperFactoryBean;

    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/user/add",method = RequestMethod.POST)
    @ResponseBody
    public HttpResp register(HttpServletRequest request, @NotNull @NotEmpty @RequestBody User user){
        try {
            user = userService.createUser(user);
        }catch (ServerException e){
            LOGGER.warn("fails to crete user.",e);
            throw e;
        }
        HttpResp resp = HttpResp.newSuccessResp();
        Map<String,String> value = new HashMap<>();
        value.put("user_id",user.getUserId());
        value.put("token_id",user.getTokenId());
        resp.setBody(value);
        return resp;
    }

    @RequestMapping(value = "/user/get",method = RequestMethod.GET)
    @ResponseBody
    public HttpResp get(HttpServletRequest request, @NotNull @NotEmpty String user_id){
        User user = null;
        try {
            user = userService.getUser(user_id);
        }catch (ServerException e){
            LOGGER.warn("fails to crete user.",e);
            throw e;
        }
        
        if(null == user){
            throw new ServerException(StatusCode.USER_NO_EXIST,"use does not exist.");
        }
        
        HttpResp resp = HttpResp.newSuccessResp();
        resp.setBody(user);
        return resp;
    }
}
