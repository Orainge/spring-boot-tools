package com.orainge.tools.spring_boot.security.handler;

import com.orainge.tools.spring_boot.bean.http.ApiResult;
import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.exception.AccountConfigureException;
import com.orainge.tools.spring_boot.security.interfaces.handler.LoginFailHandler;
import com.orainge.tools.spring_boot.utils.http.HttpResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private LoginFailHandler loginFailHandler;

    @Resource
    private CustomSecurityConfig config;

    private CustomSecurityConfig.Tips tips;

    @PostConstruct
    public void init() {
        tips = config.getTips();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        ApiResult<?> result;
        // 判断异常
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            // 密码错误或用户不存在，提示信息在 Exception 里
            result = ApiResult.unauthorized().setMessage(e.getMessage());
        } else if (e instanceof AccountConfigureException) {
            // 账号配置异常
            result = ApiResult.unauthorized().setMessage(tips.getAccountConfigureException());
        } else if (e instanceof LockedException) {
            result = ApiResult.unauthorized().setMessage(tips.getAccountLocked());
        } else if (e instanceof CredentialsExpiredException) {
            result = ApiResult.unauthorized().setMessage(tips.getCredentialsExpired());
        } else if (e instanceof AccountExpiredException) {
            result = ApiResult.unauthorized().setMessage(tips.getAccountExpired());
        } else if (e instanceof DisabledException) {
            result = ApiResult.unauthorized().setMessage(tips.getAccountDisabled());
        } else {
            log.error("[登录失败 Handler] 处理登录信息时出现异常", e);
            result = ApiResult.error().setMessage(tips.getLoginException());
        }

        // 执行 Handler
        loginFailHandler.onFail(request, response, e);

        // 写入响应
        HttpResponseUtils.writeBody(response, result);
    }
}
