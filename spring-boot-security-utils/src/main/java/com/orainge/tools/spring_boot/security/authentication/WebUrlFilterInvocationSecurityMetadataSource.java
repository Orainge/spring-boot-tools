package com.orainge.tools.spring_boot.security.authentication;

import com.orainge.tools.spring_boot.filter.MultiReadHttpServletRequest;
import com.orainge.tools.spring_boot.security.consts.SecurityConstants;
import com.orainge.tools.spring_boot.security.utils.url.IgnoreUrlUtils;
import com.orainge.tools.spring_boot.security.vo.Role;
import com.orainge.tools.spring_boot.security.utils.role.RoleUtils;
import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import com.orainge.tools.spring_boot.security.vo.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 获取访问 URL 所需要的角色信息<br>
 * 经过 WebAuthentication Filter 后，到达该 Filter
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(WebUrlFilterInvocationSecurityMetadataSource.class)
public class WebUrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Resource
    private RoleUtils roleUtils;

    @Resource
    private IgnoreUrlUtils ignoreUrlUtils;

    /***
     * 返回该 URL 所需要的用户权限信息
     *
     * @param object: 储存请求 URL 信息
     * @return null：标识不需要任何权限都可以访问
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;

        // 获取 Filter 链中的自定义 Request
        SecurityContextHolderAwareRequestWrapper wrapper = (SecurityContextHolderAwareRequestWrapper) filterInvocation.getRequest();

        // 获取当前请求 URL
        String requestUrl = wrapper.getRequestURI().replace(wrapper.getContextPath(), "");

        // 检查当前访问的 URL 是否忽略鉴权
        boolean ignoreUrlTag = false;

        Object request = wrapper.getRequest();
        if (request instanceof MultiReadHttpServletRequest) {
            // 从参数中获取判断结果
            Boolean tag = (Boolean) ((MultiReadHttpServletRequest) request).getExtraParams("IGNORE_URL");
            if (tag != null) {
                // 设置检查结果
                ignoreUrlTag = tag;
            }
        } else {
            // 调用工具类判断
            if (ignoreUrlUtils.checkIfIgnoreLogin(requestUrl)) {
                // 当前访问的 URL 忽略鉴权
                ignoreUrlTag = true;
            }
        }

        // 如果当前访问的 URL 是否忽略鉴权，则返回 null
        if (ignoreUrlTag) {
            return SecurityConstants.ROLE_IGNORE_LOGIN_LIST;
        }

        // 检查是否为忽略角色的 URL
        // 调用工具类判断
        if (ignoreUrlUtils.checkIfIgnoreRole(requestUrl)) {
            // 当前访问的 URL 忽略鉴权
            return SecurityConstants.ROLE_IGNORE_ROLE_LIST;
        }

        // 检查当前用户是否为忽略角色判断的用户
        User<?> user = ((SecurityUser<?>) ((Authentication) wrapper.getUserPrincipal()).getPrincipal()).getUser();
        if (user != null) {
            Boolean checkResult = checkIfIgnoreRole(user, requestUrl);
            if (checkResult == null) {
                // URL 未配置
                return SecurityConstants.ROLE_NO_CONFIGURE_LIST;
            } else if (checkResult) {
                // 当前用户忽略角色判断
                return SecurityConstants.ROLE_IGNORE_ROLE_LIST;
            }
        }

        // 获取角色列表
        List<? extends Role> needRoleList = roleUtils.getNeedRoleList(requestUrl);
        if (needRoleList.isEmpty()) {
            // 如果没有相应的角色列表，说明没有为该 URL 没有配置权限
            return SecurityConstants.ROLE_NO_CONFIGURE_LIST;
        } else {
            // 如果有相应的角色列表，则保存该 URL 对应角色权限信息
            return SecurityConfig.createList(
                    needRoleList.stream()
                            .map(Role::getId)
                            .toArray(String[]::new)
            );
        }
    }

    /**
     * 检查当前用户是否为忽略角色的用户
     *
     * @param user       用户凭据
     * @param requestUrl 当前请求的 URL
     * @return null: URL 非法 (不存在该 URL) true: 是; false: 否
     */
    public Boolean checkIfIgnoreRole(User<?> user, String requestUrl) {
        return false;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
