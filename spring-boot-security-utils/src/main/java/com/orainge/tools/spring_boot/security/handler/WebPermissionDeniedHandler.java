package com.orainge.tools.spring_boot.security.handler;

import com.orainge.tools.spring_boot.bean.http.ApiResult;
import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.NoPermissionHandler;
import com.orainge.tools.spring_boot.utils.http.HttpResponseUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Resource
    private CustomSecurityConfig customSecurityConfig;

    private static String noPermissionTips;

    @PostConstruct
    public void init() {
        noPermissionTips = customSecurityConfig.getTips().getNoPermission();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        // 执行 Handler
        noPermissionHandler.onNoPermission(request, response, e);

        // 返回错误提示
        HttpResponseUtils.writeBody(response, ApiResult.forbidden().setMessage(noPermissionTips));
    }
}