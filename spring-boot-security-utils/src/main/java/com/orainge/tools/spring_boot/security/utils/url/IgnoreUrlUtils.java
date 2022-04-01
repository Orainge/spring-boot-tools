package com.orainge.tools.spring_boot.security.utils.url;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * 忽略 URL 工具类
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(IgnoreUrlUtils.class)
public class IgnoreUrlUtils {
    @Resource
    private AntPathMatcher antPathMatcher;

    @Resource
    private CustomSecurityConfig config;

    private static final List<String> IGNORE_LOGIN_URL_LIST = new LinkedList<>();

    private static final List<String> IGNORE_ROLE_URL_LIST = new LinkedList<>();

    @PostConstruct
    public void init() {
        IGNORE_LOGIN_URL_LIST.add(config.getUrl().getLogin().getUrl());
        IGNORE_LOGIN_URL_LIST.addAll(config.getUrl().getIgnore());
        IGNORE_LOGIN_URL_LIST.addAll(config.getUrl().getPermitAll());
        IGNORE_ROLE_URL_LIST.addAll(config.getUrl().getIgnoreRole());
    }

    /**
     * 检查是不是不需要登录就可以访问的 URL (包括登录 URL)
     *
     * @param request HttpServletRequest
     * @return true: 是; false: 不是
     */
    public boolean checkIfIgnoreLogin(HttpServletRequest request) {
        String requestUrl = UrlUtils.buildRequestUrl(request);
        for (String ignoreUrl : IGNORE_LOGIN_URL_LIST) {
            if (antPathMatcher.match(ignoreUrl, requestUrl)) {
                // 如果是忽略访问的URL
                return true;
            }
        }

        return false;
    }

    /**
     * 检查是不是不需要登录就可以访问的 URL (包括登录 URL)
     *
     * @param requestUrl 请求 URL (不包含项目二级路径)
     * @return true: 是; false: 不是
     */
    public boolean checkIfIgnoreLogin(String requestUrl) {
        for (String url : IGNORE_LOGIN_URL_LIST) {
            if (antPathMatcher.match(url, requestUrl)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查是不是忽略角色的 URL (包括登录 URL)
     *
     * @param requestUrl 请求 URL (不包含项目二级路径)
     * @return true: 是; false: 不是
     */
    public boolean checkIfIgnoreRole(String requestUrl) {
        for (String url : IGNORE_ROLE_URL_LIST) {
            if (antPathMatcher.match(url, requestUrl)) {
                return true;
            }
        }

        return false;
    }
}
