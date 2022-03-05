package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.NoLoginHandler;
import com.orainge.tools.spring_boot.utils.http.HttpResponseUtils;
import com.orainge.tools.spring_boot.vo.http.ApiResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
    @Resource
    private CustomSecurityConfig customSecurityConfig;

    private static String noLoginTips;

    @PostConstruct
    public void init() {
        noLoginTips = customSecurityConfig.getTips().getNoLogin();
    }

    public void onNoLogin(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        HttpResponseUtils.writeBody(response, ApiResult.unauthorized().setMessage(e == null ? noLoginTips : e.getMessage()));
    }
}
