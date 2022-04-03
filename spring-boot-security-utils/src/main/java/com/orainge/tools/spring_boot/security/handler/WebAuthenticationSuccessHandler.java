package com.orainge.tools.spring_boot.security.handler;

import com.orainge.tools.spring_boot.bean.http.ApiResult;
import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import com.orainge.tools.spring_boot.security.interfaces.handler.LoginSuccessHandler;
import com.orainge.tools.spring_boot.utils.http.HttpResponseUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler: 登录成功
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(WebAuthenticationSuccessHandler.class)
public class WebAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private CustomSecurityConfig config;

    private static String tokenHeaderName;
    private static String loginSuccessTips;

    @PostConstruct
    public void init() {
        tokenHeaderName = config.getToken().getHeaderName();
        loginSuccessTips = config.getTips().getLoginSuccess();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 当前登录的用户信息
        SecurityUser<?> securityUser = ((SecurityUser<?>) authentication.getPrincipal());

        // 获取 token
        String token = securityUser.getToken();

        // token 写入 header
        response.setHeader(tokenHeaderName, token);

        // 执行 Handler
        loginSuccessHandler.onSuccess(token, request, response, authentication);

        // 返回登录成功信息
        HttpResponseUtils.writeBody(response, new ApiResult<>().setSuccess()
                .setMessage(loginSuccessTips)
                .setData(getSuccessData(securityUser, token)));
    }

    /**
     * 获取登录成功的数据<br>
     * 默认返回 token，如有需要请重载此方法
     *
     * @param token 登录成功的密钥
     */
    public Object getSuccessData(SecurityUser<?> securityUser, String token) {
        return token;
    }
}