package com.orainge.tools.spring_boot.security.filter;

import com.orainge.tools.spring_boot.filter.MultiReadHttpServletRequest;
import com.orainge.tools.spring_boot.security.authentication.WebAuthenticationManager;
import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.dto.LoginParams;
import com.orainge.tools.spring_boot.security.handler.WebLoginFailureHandler;
import com.orainge.tools.spring_boot.security.handler.WebAuthenticationSuccessHandler;
import com.orainge.tools.spring_boot.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter: 用户密码校验过滤器
 *
 * @author orainge
 * @since 2022/3/2
 */
@Slf4j
@Component
@ConditionalOnMissingBean(WebAuthenticationProcessingFilter.class)
public class WebAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    @Resource
    private CustomSecurityConfig config;

    @Resource
    private WebAuthenticationManager webAuthenticationManager;

    @Resource
    private WebAuthenticationSuccessHandler webAuthenticationSuccessHandler;

    @Resource
    private WebLoginFailureHandler webAuthenticationFailureHandler;

    private static String loginContentType;
    private static String loginParamsError;

    @PostConstruct
    public void init() {
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(
                config.getUrl().getLogin().getUrl(),
                config.getUrl().getLogin().getMethod())
        );

        this.setAuthenticationManager(webAuthenticationManager);
        this.setAuthenticationSuccessHandler(webAuthenticationSuccessHandler);
        this.setAuthenticationFailureHandler(webAuthenticationFailureHandler);

        loginContentType = config.getUrl().getLogin().getContentType();
        loginParamsError = config.getTips().getLoginParamsError();
    }

    public WebAuthenticationProcessingFilter() {
        // 默认登录 URL，在配置文件中配置会在方法 init() 中注入
        super("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType() == null || !request.getContentType().contains(loginContentType)) {
            throw new AuthenticationServiceException("不支持该请求头 [" + request.getContentType() + "]");
        }

        UsernamePasswordAuthenticationToken authRequest;
        try {
            MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(request);

            // 将前端传递的数据转换成 LoginParams
            String paramsJson = wrappedRequest.getJsonBody();
            LoginParams loginParams = getLoginParams(paramsJson);

            if (loginParams == null) {
                throw new AuthenticationServiceException(loginParamsError);
            }

            authRequest = new UsernamePasswordAuthenticationToken(loginParams.getUsername(), loginParams.getPassword(), null);
            authRequest.setDetails(authenticationDetailsSource.buildDetails(wrappedRequest));
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取请求参数 Bean<br>
     * 可以通过重载此方法返回自定义格式参数
     *
     * @param paramsJson 请求参数 json
     */
    protected LoginParams getLoginParams(String paramsJson) {
        return JsonUtils.parseObject(paramsJson, LoginParams.class);
    }
}
