package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.interfaces.handler.LoginSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
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
@ConditionalOnMissingBean(LoginSuccessHandler.class)
public class LoginSuccessHandlerImpl implements LoginSuccessHandler {
    @Override
    public void onSuccess(String token, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    }
}
