package com.orainge.tools.spring_boot.security.handler;

import com.orainge.tools.spring_boot.filter.MultiReadHttpServletRequest;
import com.orainge.tools.spring_boot.filter.MultiReadHttpServletResponse;
import com.orainge.tools.spring_boot.security.interfaces.handler.NoPermissionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler: 已登录但无权限处理
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(WebPermissionDeniedHandler.class)
public class WebPermissionDeniedHandler implements AccessDeniedHandler {
    @Resource
    private NoPermissionHandler noPermissionHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        noPermissionHandler.onNoPermission(new MultiReadHttpServletRequest(request), new MultiReadHttpServletResponse(response), e);
    }
}