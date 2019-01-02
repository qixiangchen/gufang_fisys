package com.gf.acl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class ShiroExceptionResolver implements HandlerExceptionResolver{  
	  
    public ModelAndView resolveException(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex)
    {
    	String url = request.getRequestURI();
        if(ex instanceof UnauthorizedException){  
            ModelAndView mv = new ModelAndView("nopriv");
            mv.addObject("url", url);
            return mv;
        }
        ex.printStackTrace();  
        ModelAndView mv = new ModelAndView("nopriv");
        mv.addObject("url", url);
        mv.addObject("exception", ex.toString().replaceAll("\n", "<br/>"));  
        return mv;
    }
}
