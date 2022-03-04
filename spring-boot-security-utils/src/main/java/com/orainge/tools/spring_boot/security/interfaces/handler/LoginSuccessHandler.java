package com.orainge.tools.spring_boot.security.interfaces.handler;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录成功处理器
 *
 * @author orainge
 * @since 2022/3/2
 */
public interface LoginSuccessHandler {
    /**
     * 登录成功处理方法
     *
     * @param token          登录成功的token
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param authentication 授权信息
     */
    void onSuccess(String token, HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
