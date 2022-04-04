package com.orainge.tools.spring_boot.security.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

/**
 * 登录框架设置
 *
 * @author orainge
 * @since 2022/3/2
 */
@Configuration
@ConfigurationProperties(prefix = "security")
@Data
public class CustomSecurityConfig {
    /**
     * JWT 配置
     */
    private JWTConfig jwt;
    /**
     * token 配置
     */
    private Token token;

    /**
     * URL 配置
     */
    private URL url;

    /**
     * 提示语配置
     */
    private Tips tips;

    @PostConstruct
    public void init() {
        // 忽略错误页面
        this.url.permitAll.add("/error");
    }

    @Data
    public static class JWTConfig {
        /**
         * JWT 密码盐
         */
        private String salt = "20HwNQinK8q9";
    }

    @Data
    public static class Token {
        /**
         * token 在请求头里的名称
         */
        private String headerName = "X-TOKEN";

        /**
         * token 有效期 (单位: 秒)<br>
         * 默认 1800 秒 (30分钟)
         */
        private Integer expireTime = 1800;
    }

    @Data
    public static class URL {
        /**
         * 登录接口配置
         */
        private URLInfo login = new URLInfo("/login", "POST", "application/json");

        /**
         * 退出登录接口配置
         */
        private URLInfo logout = new URLInfo("/logout", "POST", "application/json");

        /**
         * (不推荐) 不需要登录就可以访问的 URL
         * 在此配置的路径不经过 Spring Security 过滤器链
         */
        private List<String> ignore = new LinkedList<>();

        /**
         * (推荐) 不需要登录就可以访问的 URL
         * 在此配置的路径经过 Spring Security 过滤器链，但 Spring Security 会放行
         */
        private List<String> permitAll = new LinkedList<>();

        /**
         * 需要登录，但是忽略角色判断的 URL
         */
        private List<String> ignoreRole = new LinkedList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class URLInfo {
        /**
         * 请求 URL
         */
        private String url;

        /**
         * 请求方式
         */
        private String method;

        /**
         * 请求 Content-Type
         */
        private String contentType;
    }

    @Data
    public static class Tips {
        /**
         * 账号配置异常
         */
        private String accountConfigureException = "账号配置异常，请联系管理员处理";

        /**
         * 获取 Token 失败
         */
        private String getTokenException = "获取 Token 失败，请稍候再试";

        /**
         * 用户不存在
         */
        private String noUser = "用户不存在";

        /**
         * 登录成功
         */
        private String loginSuccess = "登录成功";

        /**
         * 登录请求参数不正确
         */
        private String loginParamsError = "登录请求参数不正确";

        /**
         * 登录异常
         */
        private String loginException = "登录失败，请稍后再试";

        /**
         * 退出登录成功
         */
        private String logoutSuccess = "退出登录成功";

        /**
         * 密码不正确
         */
        private String incorrectPassword = "密码不正确";

        /**
         * 无权限访问
         */
        private String noPermission = "无权限访问";

        /**
         * 无任何已配置权限
         */
        private String noConfigurePermission = "无任何已配置权限，请联系管理员进行分配！";

        /**
         * 用户未登录
         */
        private String noLogin = "用户未登录";

        /**
         * 账户已锁定
         */
        private String accountLocked = "账户已锁定";

        /**
         * 账户已过期
         */
        private String accountExpired = "账户已过期";

        /**
         * 账户被禁用
         */
        private String accountDisabled = "账户被禁用";

        /**
         * 证书已过期
         */
        private String credentialsExpired = "证书已过期";

        /**
         * token 已过期
         */
        private String tokenExpire = "token 已过期";

        /**
         * 系统异常
         */
        private String systemException = "系统忙，请稍候再试";
    }
}
