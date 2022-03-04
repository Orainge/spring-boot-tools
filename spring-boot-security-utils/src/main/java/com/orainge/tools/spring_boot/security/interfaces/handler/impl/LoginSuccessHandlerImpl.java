package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.LoginSuccessHandler;
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
 * 登录成功处理器默认实现
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(LoginSuccessHandler.class)
public class LoginSuccessHandlerImpl implements LoginSuccessHandler {
    @Resource
    private CustomSecurityConfig config;

    private static String loginSuccessTips;

    @PostConstruct
    private void init() {
        loginSuccessTips = config.getTips().getLoginSuccess();
    }

    @Override
    public void onSuccess(String token, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 返回登录成功信息
        ResponseUtils.writeBody(response, ApiResult.success().setMessage(loginSuccessTips).setData(token));
    }
}
