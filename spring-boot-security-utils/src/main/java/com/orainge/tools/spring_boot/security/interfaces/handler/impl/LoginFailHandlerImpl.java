package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.LoginFailHandler;
import com.orainge.tools.spring_boot.security.interfaces.handler.LoginSuccessHandler;
import com.orainge.tools.spring_boot.utils.ResponseUtils;
import com.orainge.tools.spring_boot.vo.ApiResult;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
@ConditionalOnMissingBean(LoginFailHandler.class)
public class LoginFailHandlerImpl implements LoginFailHandler {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private CustomSecurityConfig config;

    private CustomSecurityConfig.Tips tips;

    @PostConstruct
    public void init() {
        tips = config.getTips();
    }

    @Override
    public void onFail(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        ApiResult<?> result;
        // 判断异常
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            // 密码错误或用户不存在，提示信息在 Exception 里
            result = ApiResult.unauthorized().setMessage(e.getMessage());
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

        // 写入响应
        ResponseUtils.writeBody(response, result);
    }
}
