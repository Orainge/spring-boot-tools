package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.interfaces.handler.NoLoginHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 未登录处理器默认实现
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(NoLoginHandler.class)
public class NoLoginHandlerImpl implements NoLoginHandler {
    public void onNoLogin(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
    }
}
