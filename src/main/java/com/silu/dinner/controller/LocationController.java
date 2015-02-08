package com.silu.dinner.controller;

/**
 * Created by silu on 15-2-8.
 */

import com.silu.dinner.bean.HttpResp;
import com.silu.dinner.bean.Location;
import com.silu.dinner.bean.User;
import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.entity.CurrentUser;
import com.silu.dinner.exception.ServerException;
import com.silu.dinner.service.LocationService;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by silu on 15-1-15.
 */
@Controller
@RequestMapping(value = "/dinner", produces = "application/json")
public class LocationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);
    
    @Autowired
    private LocationService locationService;

    @RequestMapping(value = "/location",method = RequestMethod.POST)
    @ResponseBody
    public HttpResp upload(@NotNull @NotEmpty @RequestBody Location location){
        User user = CurrentUser.user();
        try {
            locationService.uploadLocation(location,user.getUserId());
        }
        catch (Exception e){
            LOGGER.error(String.format("fails to save user location.userId:%s",user.getUserId()),e);
            throw new ServerException(StatusCode.INNER_ERROR,"fails to save user location");
        }
        return  HttpResp.newSuccessResp();
    }
}
