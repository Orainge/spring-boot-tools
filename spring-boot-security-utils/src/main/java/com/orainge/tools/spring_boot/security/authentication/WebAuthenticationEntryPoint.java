package com.orainge.tools.spring_boot.security.authentication;

import com.orainge.tools.spring_boot.bean.http.ApiResult;
import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.NoLoginHandler;
import com.orainge.tools.spring_boot.utils.http.HttpResponseUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Resource
    private CustomSecurityConfig customSecurityConfig;

    private static String noLoginTips;

    @PostConstruct
    public void init() {
        noLoginTips = customSecurityConfig.getTips().getNoLogin();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        // 执行 Handler
        noLoginHandler.onNoLogin(request, response, e);

        // 返回未登录的信息
        HttpResponseUtils.writeBody(response, ApiResult.unauthorized().setMessage(e == null ? noLoginTips : e.getMessage()));
    }
}