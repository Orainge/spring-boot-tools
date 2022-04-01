package com.orainge.tools.spring_boot.security.consts;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * 认证常用变量
 *
 * @author orainge
 * @since 2022/3/2
 */
public class SecurityConstants {
    /**
     * 忽略登录的 URL
     */
    public static final String ROLE_IGNORE_LOGIN_URL = "R_ILU";

    /**
     * 需要登录，但是忽略角色判断的 URL
     */
    public static final String ROLE_IGNORE_ROLE_URL = "R_IRU";

    /**
     * 请求的 URL 没有在数据库中配置，或该用户未配置任何权限
     */
    public static final String ROLE_NO_CONFIGURE_URL = "R_NCU";

    public static final List<ConfigAttribute> ROLE_IGNORE_LOGIN_LIST = SecurityConfig.createList(ROLE_IGNORE_LOGIN_URL);
    public static final List<ConfigAttribute> ROLE_IGNORE_ROLE_LIST = SecurityConfig.createList(ROLE_IGNORE_ROLE_URL);
    public static final List<ConfigAttribute> ROLE_NO_CONFIGURE_LIST = SecurityConfig.createList(ROLE_NO_CONFIGURE_URL);
}
