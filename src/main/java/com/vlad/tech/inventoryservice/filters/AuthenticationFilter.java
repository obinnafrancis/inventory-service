package com.vlad.tech.inventoryservice.filters;

import com.google.gson.Gson;
import com.vlad.tech.inventoryservice.daos.UserRepository;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.services.AuthenticateService;
import com.vlad.tech.inventoryservice.services.UserService;
import com.vlad.tech.inventoryservice.utils.JwtUtil;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    AuthenticateService authenticateService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        if(StringUtils.hasText(authHeader)){
            token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
            try {
            String username = jwtUtil.extractUsername(token);
            if(StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() ==null){
                UserDetails userDetails = authenticateService.loadUserByUsername(username);
                if(jwtUtil.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken uspass = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    uspass.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(uspass);
                    }
                }
            }catch (Exception e){
                handleExpiredToken(httpServletRequest,httpServletResponse,e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    public void handleExpiredToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age","*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
        log.warn("Token Expired: {}", e.getMessage());
        BaseResponse response =  BaseResponse.builder()
                    .code("40301")
                    .description(ResponseMapper.FORBIDDEN_ACCESS.getDescription()+ ". Access Token Expired")
                    .build();
        String resp= new Gson().toJson(response);
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        httpServletResponse.getWriter().write(resp);
    }
}
