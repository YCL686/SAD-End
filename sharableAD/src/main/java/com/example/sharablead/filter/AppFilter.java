package com.example.sharablead.filter;

import cn.hutool.http.HttpStatus;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.common.GlobalException;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.context.AppContext;
import com.example.sharablead.entity.Role;
import com.example.sharablead.entity.Token;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.service.RoleService;
import com.example.sharablead.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
//@WebFilter(urlPatterns = {"/account/*", "/account-entry/*", "/collect/*", "/comment/*", "/focus/*", "/like/*", "/report/*", "/resource/*", "/role/*", "user/*", "/watch/*"},
//           filterName = "appFilter")
@WebFilter(urlPatterns = {"/app"}, filterName = "appFilter")
@Slf4j
public class AppFilter implements Filter {
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private RoleService roleService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String headerToken = request.getHeader("token");
        try {
            Token token= tokenUtil.parseToken(headerToken);
            AppContext context = AppContext.getContext();
            context.setUserId(token.getUserId());
            context.setAddress(token.getAddress());
            LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Role::getUserId, token.getUserId());
            context.setRoleNames(roleService.list(lambdaQueryWrapper).stream().map(r->r.getRoleName()).collect(Collectors.toList()));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        } catch (Exception e) {
//            log.error("parse token error, token = {}, exception = {}", e.getMessage());
//            response.setStatus(HttpStatus.HTTP_OK);
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json; charset=utf-8");
//            try {
//                response.getWriter().write(JSONUtil.toJsonStr(new GlobalResponse(HttpStatus.HTTP_UNAUTHORIZED, "Please Login Again", null)));
//            }catch (Exception ex) {
//                ex.printStackTrace();
//            }
            filterChain.doFilter(servletRequest, servletResponse);
        } finally{
            AppContext.removeContext();
        }
    }
}
