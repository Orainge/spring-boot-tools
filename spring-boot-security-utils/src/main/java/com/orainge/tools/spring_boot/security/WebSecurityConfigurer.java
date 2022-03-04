package com.orainge.tools.spring_boot.security;

import com.orainge.tools.spring_boot.security.authentication.WebAuthenticationEntryPoint;
import com.orainge.tools.spring_boot.security.authentication.WebUrlAccessDecisionManager;
import com.orainge.tools.spring_boot.security.authentication.WebUrlFilterInvocationSecurityMetadataSource;
import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.filter.WebAuthenticationFilter;
import com.orainge.tools.spring_boot.security.filter.WebAuthenticationProcessingFilter;
import com.orainge.tools.spring_boot.security.handler.WebLogoutHandler;
import com.orainge.tools.spring_boot.security.handler.WebLogoutSuccessHandler;
import com.orainge.tools.spring_boot.security.handler.WebPermissionDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * Spring Security 配置
 *
 * @author orainge
 * @since 2022/3/2
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnMissingBean(WebSecurityConfigurer.class)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Resource
    private CustomSecurityConfig securityConfig;

    /**
     * Filter: 访问鉴权，认证token、签名
     */
    @Resource
    private WebAuthenticationFilter webAuthenticationFilter;

    /**
     * 认证权限入口<br>
     * 在未登录的情况下访问所有接口都会拦截到此（除了放行忽略接口）
     */
    @Resource
    private WebAuthenticationEntryPoint adminAuthenticationEntryPoint;

    /**
     * Filter: 用户密码校验过滤器
     */
    @Resource
    private WebAuthenticationProcessingFilter webAuthenticationProcessingFilter;

    /**
     * Handler: 退出登录
     */
    @Resource
    private WebLogoutHandler webLogoutHandler;

    /**
     * Handler: 退出登录操作成功
     */
    @Resource
    private WebLogoutSuccessHandler webLogoutSuccessHandler;

    // 上面是登录认证相关，下面为 URL 权限相关

    /**
     * 获取访问 URL 所需要的角色信息
     */
    @Resource
    private WebUrlFilterInvocationSecurityMetadataSource webUrlFilterInvocationSecurityMetadataSource;

    /**
     * URL 访问权限判断<br>
     * 将 UrlFilterInvocationSecurityMetadataSource 所获得角色权限与当前登录用户的角色做对比<br>
     * 如果包含其中一个角色即可正常访问
     */
    @Resource
    private WebUrlAccessDecisionManager urlAccessDecisionManager;

    /**
     * Handler: 已登录但无权限处理
     */
    @Resource
    private WebPermissionDeniedHandler urlAccessDeniedHandler;

    /**
     * 登录处理
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用CSRF 开启跨域
        http.csrf().disable().cors();

        // 未登录认证异常
        http.exceptionHandling().authenticationEntryPoint(adminAuthenticationEntryPoint);

        // 登录过后访问无权限的接口时自定义403响应内容
        http.exceptionHandling().accessDeniedHandler(urlAccessDeniedHandler);

        // 开始登录配置
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();

        // url权限认证处理
        registry.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setSecurityMetadataSource(webUrlFilterInvocationSecurityMetadataSource);
                o.setAccessDecisionManager(urlAccessDecisionManager);
                return o;
            }
        });

        // 添加不需要登录就能访问的 URL
        List<String> permitAllUrlList = securityConfig.getUrl().getPermitAll();
        if (!permitAllUrlList.isEmpty()) {
            permitAllUrlList.forEach(url -> registry.antMatchers(url).permitAll());
            log.info("[Spring Security 配置] 已添加 permitAll URL: {}", permitAllUrlList);
        }

        // 允许匿名的url - 可理解为放行接口 - 多个接口使用,分割
        registry.antMatchers(securityConfig.getUrl().getLogin().getUrl()).permitAll();

        // OPTIONS(选项)：查找适用于一个特定网址资源的通讯选择。 在不需执行具体的涉及数据传输的动作情况下， 允许客户端来确定与资源相关的选项以及 / 或者要求， 或是一个服务器的性能
        registry.antMatchers(HttpMethod.OPTIONS, "/**").denyAll();

        // 退出登录配置
        registry.and().logout()
                .logoutUrl(securityConfig.getUrl().getLogout().getUrl())
                .addLogoutHandler(webLogoutHandler)
                .logoutSuccessHandler(webLogoutSuccessHandler);

        // 防止iframe 造成跨域
        registry.and().headers().frameOptions().disable();

        // 其余所有请求都需要认证
        registry.anyRequest().authenticated();

        // 不创建会话 - 即通过前端传 token 到后台过滤器中验证是否存在访问权限
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 自定义过滤器: 登录时认证用户名、密码
        http.addFilterAt(webAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(webAuthenticationFilter, BasicAuthenticationFilter.class);
    }

    /**
     * 配置忽略的 URL<br>
     * web.ignoring(): 会直接过滤该 URL, 不会经过 Spring Security 过滤器链<br>
     * http.permitAll(): 不会绕开 Spring Security 验证，相当于是允许该路径通过
     */
    @Override
    public void configure(WebSecurity web) {
        try {
            List<String> ignoreUrlList = securityConfig.getUrl().getIgnore();
            if (!ignoreUrlList.isEmpty()) {
                WebSecurity.IgnoredRequestConfigurer webIgnoring = web.ignoring();
                ignoreUrlList.forEach(webIgnoring::antMatchers);
                log.info("[Spring Security 配置] 已添加 ignore URL: {}", ignoreUrlList);
            }
        } catch (Exception ignore) {
        }
    }
}