package com.vlad.tech.inventoryservice.utils;

import com.google.gson.Gson;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age","*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
        if (auth != null) {
            log.warn("User: " + auth.getName()
                    + " attempted to access the protected URL: "
                    + httpServletRequest.getRequestURI());
            BaseResponse response =  BaseResponse.builder()
                    .code(ResponseMapper.FORBIDDEN_ACCESS.getCode())
                    .description(ResponseMapper.FORBIDDEN_ACCESS.getDescription()+ " '"+httpServletRequest.getRequestURI()+"'")
                    .build();
            String resp= new Gson().toJson(response);
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.getWriter().write(resp);
        }
    }
}
