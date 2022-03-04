package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.LogoutFailHandler;
import com.orainge.tools.spring_boot.utils.ResponseUtils;
import com.orainge.tools.spring_boot.vo.ApiResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录失败处理器
 *
 * @author orainge
 * @since 2022/3/3
 */
@Component
@ConditionalOnMissingBean(LogoutFailHandlerImpl.class)
public class LogoutFailHandlerImpl implements LogoutFailHandler {
    /**
     * 退出登录失败处理方法
     *
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @param exception 异常信息
     * @param message   错误信息
     */
    public void onFail(HttpServletRequest request, HttpServletResponse response, Exception exception, String message) {
        // 返回退出登录失败信息
        ResponseUtils.writeBody(response, ApiResult.forbidden().setMessage(message));
    }
}
