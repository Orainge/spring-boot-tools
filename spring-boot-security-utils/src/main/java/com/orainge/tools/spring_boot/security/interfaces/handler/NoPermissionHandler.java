package com.orainge.tools.spring_boot.security.interfaces.handler;

import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 没有访问权限处理器
 *
 * @author orainge
 * @since 2022/3/2
 */
public interface NoPermissionHandler {
    /**
     * 未登录处理方法
     *
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @param exception 异常信息
     */
    void onNoPermission(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception);
}
