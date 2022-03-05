package com.orainge.tools.spring_boot.security.handler;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.utils.token.TokenUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录操作 Handler
 *
 * @author orainge
 * @since 2022/3/3
 */
@Component
@ConditionalOnMissingBean(WebLogoutHandler.class)
public class WebLogoutHandler implements LogoutHandler {
    @Resource
    private CustomSecurityConfig config;

    @Resource
    private TokenUtils tokenUtils;

    private static String tokenHeaderName;

    @PostConstruct
    public void init() {
        tokenHeaderName = config.getToken().getHeaderName();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 从请求头中获取 token
        String token = request.getHeader(tokenHeaderName);

        // 判断 token 是否存在
        if (token == null || "".equals(token)) {
            // token 不存在，直接提示退出成功
            return;
        }

        // 使 token 过期
        tokenUtils.expireToken(token);
    }
}
