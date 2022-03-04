package com.orainge.tools.spring_boot.security.authentication;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义认证管理器
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(WebAuthenticationManager.class)
public class WebAuthenticationManager implements AuthenticationManager {
    @Resource
    private WebAuthenticationProvider webAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authObj = webAuthenticationProvider.authenticate(authentication);
        if (authObj == null) {
            throw new ProviderNotFoundException("未配置认证管理器，请联系开发人员处理！");
        } else {
            return authObj;
        }
    }
}