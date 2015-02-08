package com.silu.dinner.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;


public class LogInterceptor extends ConfigurableInterceptor {
    private static Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    public  void printRequest(HttpServletRequest request){
        if(log.isDebugEnabled()){
            String newLine =" ==== ";// System.getProperty("line.separator");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(newLine);

            stringBuilder.append(request.getMethod()+" "+request.getRequestURI()).append(newLine);
            stringBuilder.append("thread id : "+Thread.currentThread().getName()+"|"+request.getMethod()).append("");

            Enumeration enumeration = request.getHeaderNames();
            stringBuilder.append("Header:{");
            while(enumeration.hasMoreElements()){
                Object obj = enumeration.nextElement();
                stringBuilder.append(obj+" = "+request.getHeader(obj.toString())+",");
            }
            stringBuilder.append("}").append(newLine);
            stringBuilder.append("Parameter:{");
            for(Map.Entry<String, String[]> sub : (request.getParameterMap()).entrySet()){
                stringBuilder.append(sub.getKey()+" = "+Arrays.toString(sub.getValue())+",");
            }
            stringBuilder.append("}").append(newLine);

            log.debug(stringBuilder.toString());
        }
    }

    @Override
    public boolean internalPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        printRequest(request);
        return true;
    }
}
