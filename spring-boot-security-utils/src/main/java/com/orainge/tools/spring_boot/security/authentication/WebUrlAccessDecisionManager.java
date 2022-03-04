package com.orainge.tools.spring_boot.security.authentication;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.consts.SecurityConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;

/**
 * URL 访问权限判断<br>
 * 将 UrlFilterInvocationSecurityMetadataSource 所获得角色权限与当前登录用户的角色做对比<br>
 * 如果包含其中一个角色即可正常访问
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(WebUrlAccessDecisionManager.class)
public class WebUrlAccessDecisionManager implements AccessDecisionManager {
    @Resource
    private CustomSecurityConfig config;

    private static String noLoginTips;
    private static String noPermissionTips;
    private static String noConfigurePermission;

    @PostConstruct
    public void init() {
        noLoginTips = config.getTips().getNoLogin();
        noPermissionTips = config.getTips().getNoPermission();
        noConfigurePermission = config.getTips().getNoConfigurePermission();
    }

    /**
     * @param authentication 当前登录用户的角色信息
     * @param object         请求url信息
     * @param collection     `WebUrlFilterInvocationSecurityMetadataSource`中的getAttributes方法传来的，表示当前请求需要的角色（可能有多个）
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> collection) throws AccessDeniedException, AuthenticationException {
        // 遍历角色
//        if (authentication instanceof AnonymousAuthenticationToken) {
//            // 如果是匿名访问，则返回 "未登录"
//            throw new BadCredentialsException(noLoginTips);
//        } else {
//            // 如果已登录，表示该
//            throw new AccessDeniedException(noPermissionTips);
//        }
//
        // 遍历请求当前 URL 所需的角色
        for (ConfigAttribute ca : collection) {
            // 获取请求当前 URL 所需的权限 ID
            String needRole = ca.getAttribute();

            // 如果角色表示为 "忽略登录"
            if (SecurityConstants.ROLE_IGNORE_LOGIN_URL.equals(needRole)) {
                // 通过验证
                return;
            }

            // 如果角色表示为 "URL 未配置权限"，表示当前请求的 URL 不在系统可访问的 URL 中
            if (SecurityConstants.ROLE_NO_CONFIGURE_URL.equals(needRole)) {
                throw new AccessDeniedException(noConfigurePermission);
            }

            // 遍历该用户拥有的角色
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                // 只要该用户拥有一个请求当前 URL 所需的角色，则允许访问
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }

        // 该用户没有请求当前 URL 所需的角色
        throw new AccessDeniedException(noPermissionTips);
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
