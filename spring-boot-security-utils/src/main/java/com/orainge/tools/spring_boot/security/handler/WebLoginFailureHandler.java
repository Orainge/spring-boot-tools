package com.orainge.tools.spring_boot.security.handler;

import com.orainge.tools.spring_boot.filter.MultiReadHttpServletRequest;
import com.orainge.tools.spring_boot.filter.MultiReadHttpServletResponse;
import com.orainge.tools.spring_boot.security.interfaces.handler.LoginFailHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler: 登录失败
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(WebLoginFailureHandler.class)
public class WebLoginFailureHandler implements AuthenticationFailureHandler {
    @Resource
    private LoginFailHandler loginFailHandler;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        loginFailHandler.onFail(new MultiReadHttpServletRequest(request), new MultiReadHttpServletResponse(response), e);
    }
}
