package com.silu.dinner.interceptor;


import com.silu.dinner.util.RequestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ConfigurableInterceptor implements HandlerInterceptor,InitializingBean {
    private final String CACHE_KEY = "hi_" + hashCode() + "_";
    private String[] excludes;
    private String[] includes;
    private UrlPathHelper urlPathHelper;
    protected PathMatcher pathMatcher;
    
    @Autowired
    private RequestUtils requestUtils;

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        urlPathHelper = requestUtils.getUrlPathHelper();
        pathMatcher = requestUtils.getPathMatcher();
    }
    
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	if(needProcess(request))
    		return internalPreHandle(request, response, handler);
    	else 
    		return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (needProcess(request)) {
            internalPostHandle(request, response, handler, modelAndView);
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (needProcess(request)) {
            internalAfterCompletion(request, response, handler, ex);
        }
        request.removeAttribute(getCacheKey(request));
    }

    public boolean internalPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    public void internalPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void internalAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    // TODO: 不能处理Context path？需要检查
    private boolean needProcess(HttpServletRequest request) {
        String key = getCacheKey(request);
        Boolean need = (Boolean) request.getAttribute(key);
        if (need != null) {
            return need;
        }
        //先排除不需要处理的请求
        need = !requestUtils.matchAny(request, urlPathHelper, pathMatcher, excludes);
        if(need){
            need = includes == null || requestUtils.matchAny(request, urlPathHelper, pathMatcher, includes);//includes为空或者匹配的表示需要处理
        }
        request.setAttribute(key, need);
        return need;
    }

    private String getCacheKey(HttpServletRequest request) {
        return CACHE_KEY + request.getRequestURI();
    }


}
