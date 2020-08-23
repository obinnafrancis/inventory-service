package com.vlad.tech.inventoryservice.utils;

import com.vlad.tech.inventoryservice.exceptions.CustomAccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CustomAccessDeniedInterface extends AccessDeniedHandler {
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, CustomAccessDeniedException e) throws IOException, ServletException;
}
