package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.interfaces.handler.LoginFailHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录成功处理器默认实现
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(LoginFailHandler.class)
public class LoginFailHandlerImpl implements LoginFailHandler {
    @Override
    public void onFail(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
    }
}
