package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.NoPermissionHandler;
import com.orainge.tools.spring_boot.utils.http.HttpResponseUtils;
import com.orainge.tools.spring_boot.vo.http.ApiResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
    @Resource
    private CustomSecurityConfig customSecurityConfig;

    private static String noPermissionTips;

    @PostConstruct
    public void init() {
        noPermissionTips = customSecurityConfig.getTips().getNoPermission();
    }

    public void onNoPermission(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        HttpResponseUtils.writeBody(response, ApiResult.forbidden().setMessage(noPermissionTips));
    }
}
