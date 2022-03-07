package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.interfaces.handler.NoPermissionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 没有访问权限处理器默认实现
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(NoPermissionHandler.class)
public class NoPermissionHandlerImpl implements NoPermissionHandler {
    public void onNoPermission(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
    }
}
