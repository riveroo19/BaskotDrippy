package com.practicaDWS.baskotdrippy.security;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CSRFHandlerInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null){
            CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
            if (token!=null){
                modelAndView.addObject("token", token.getToken());
            }
        }
    }
}
