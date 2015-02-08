package com.silu.dinner.interceptor;


import com.silu.dinner.bean.User;
import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.constant.SystemConstants;
import com.silu.dinner.entity.CurrentUser;
import com.silu.dinner.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


public class SecInterceptor extends ConfigurableInterceptor {

    @Autowired
    private UserService userService;

    private MappingJacksonJsonView jsonView = new MappingJacksonJsonView();

    private static final Logger log = LoggerFactory.getLogger(SecInterceptor.class);

	@Override
	public boolean internalPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String clientSession = request.getHeader(SystemConstants.REQ_HEAD_SESSION_ID);
		log.debug("token =  {}", clientSession);
        if(null == clientSession) {
            forbidden(clientSession, request, response);
            return false;
        }

 		User loggedUser = userService.authorize(clientSession);
        if(loggedUser == null) {
            forbidden(clientSession, request, response);
            return false;
        }
        CurrentUser.user(loggedUser);
		return true;
	}

    private void forbidden(String token, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("token {} is unauthorized", token);
        Map<String,String> erroResp =  new HashMap<>();
        erroResp.put("status", StatusCode.USER_AUTHORIZ_FAIL);
        erroResp.put("desc", "request head has no token_id or token_id invalid");
        jsonView.setUpdateContentLength(true);
        jsonView.render(erroResp, request, response);
    }

    public void internalPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        response.setHeader("Cache-Control", "no-cache");
        CurrentUser.clearUser();
    }

}
