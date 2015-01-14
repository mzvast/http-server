package com.silu.dinner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/test", produces = "application/json")

public class HelloController {

	@RequestMapping(method = RequestMethod.GET,value = "/1")
    @ResponseBody
    public Map<String,String> test(){
        Map<String,String> result = new HashMap<>();
        result.put("status","2000");
        return result;
    }
}