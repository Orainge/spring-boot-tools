package com.orainge.tools.spring_boot.security.authentication;

import com.orainge.tools.spring_boot.filter.MultiReadHttpServletRequest;
import com.orainge.tools.spring_boot.filter.MultiReadHttpServletResponse;
import com.orainge.tools.spring_boot.security.interfaces.handler.NoLoginHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证权限入口<br>
 * 在未登录的情况下访问所有接口都会拦截到此（除了放行忽略接口）
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(WebAuthenticationEntryPoint.class)
public class WebAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Resource
    private NoLoginHandler noLoginHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        noLoginHandler.onNoLogin(new MultiReadHttpServletRequest(request), new MultiReadHttpServletResponse(response), e);
    }
}