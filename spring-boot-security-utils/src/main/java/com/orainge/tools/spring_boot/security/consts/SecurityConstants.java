package com.orainge.tools.spring_boot.security.consts;

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
    public static final String ROLE_IGNORE_LOGIN_URL = "role_ignore_login_url";

    /**
     * 请求的 URL 没有在数据库中配置，或该用户未配置任何权限
     */
    public static final String ROLE_NO_CONFIGURE_URL = "role_no_configure_url";
}
