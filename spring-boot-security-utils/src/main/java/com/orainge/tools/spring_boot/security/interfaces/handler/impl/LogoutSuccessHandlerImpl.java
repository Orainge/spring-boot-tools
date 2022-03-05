package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.LogoutSuccessHandler;
import com.orainge.tools.spring_boot.utils.http.HttpResponseUtils;
import com.orainge.tools.spring_boot.bean.http.ApiResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录成功处理器
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(LogoutSuccessHandler.class)
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Resource
    private CustomSecurityConfig config;

    private static String logoutSuccessTips;

    @PostConstruct
    private void init() {
        logoutSuccessTips = config.getTips().getLogoutSuccess();
    }

    /**
     * 退出登录成功处理方法
     *
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param authentication 授权信息
     */
    public void onSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 返回退出登录成功信息
        HttpResponseUtils.writeBody(response, ApiResult.success().setMessage(logoutSuccessTips));
    }
}
