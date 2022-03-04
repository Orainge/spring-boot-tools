package com.orainge.tools.spring_boot.security.interfaces.handler;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录失败处理器
 *
 * @author orainge
 * @since 2022/3/3
 */
public interface LogoutFailHandler {
    /**
     * 退出登录失败处理方法
     *
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @param exception 异常信息
     * @param message   错误信息
     */
    void onFail(HttpServletRequest request, HttpServletResponse response, Exception exception, String message);
}
