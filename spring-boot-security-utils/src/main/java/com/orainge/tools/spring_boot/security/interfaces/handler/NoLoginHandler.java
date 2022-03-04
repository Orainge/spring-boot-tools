package com.orainge.tools.spring_boot.security.interfaces.handler;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 未登录处理器
 *
 * @author orainge
 * @since 2022/3/2
 */
public interface NoLoginHandler {
    /**
     * 未登录处理方法
     *
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @param exception 异常信息
     */
    void onNoLogin(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception);
}
