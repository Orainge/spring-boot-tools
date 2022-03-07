package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.interfaces.handler.LogoutSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

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
    /**
     * 退出登录成功处理方法
     *
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param authentication 授权信息
     */
    public void onSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    }
}
