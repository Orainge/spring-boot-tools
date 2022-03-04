package com.orainge.tools.spring_boot.security.filter;

import com.orainge.tools.spring_boot.filter.MultiReadHttpServletRequest;
import com.orainge.tools.spring_boot.filter.MultiReadHttpServletResponse;
import com.orainge.tools.spring_boot.security.authentication.WebAuthenticationEntryPoint;
import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.utils.token.TokenUtils;
import com.orainge.tools.spring_boot.security.utils.url.IgnoreUrlUtils;
import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import com.orainge.tools.spring_boot.security.authentication.WebUserDetailsServiceImpl;
import com.orainge.tools.spring_boot.security.vo.User;
import com.orainge.tools.spring_boot.utils.ResponseUtils;
import com.orainge.tools.spring_boot.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter: 访问鉴权，认证 token<br>
 * 除了登录和退出登录接口，请求到达后，第一个经过该 Filter
 *
 * @author orainge
 * @since 2022/3/2
 */
@Slf4j
@Component
@ConditionalOnMissingBean(WebAuthenticationFilter.class)
public class WebAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private CustomSecurityConfig config;

    @Resource
    private TokenUtils tokenUtils;

    @Resource
    private IgnoreUrlUtils ignoreUrlUtils;

    @Resource
    private WebAuthenticationEntryPoint webAuthenticationEntryPoint;

    @Resource
    private WebUserDetailsServiceImpl webUserDetailsService;

    private static String loginUrl;

    private static String tokenHeaderName;
    private static String tokenExpireTips;
    private static String noLoginTips;
    private static String systemExceptionTips;

    @PostConstruct
    public void init() {
        loginUrl = config.getUrl().getLogin().getUrl();

        // 获取提示语
        tokenHeaderName = config.getToken().getHeaderName();
        tokenExpireTips = config.getTips().getTokenExpire();
        noLoginTips = config.getTips().getNoLogin();
        systemExceptionTips = config.getTips().getSystemException();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(request);
        MultiReadHttpServletResponse wrappedResponse = new MultiReadHttpServletResponse(response);

        // 此处可根据 Content-type 来判断是否进行权限控制/注入 Principal 供 Controller 层使用
//        String contentType = request.getContentType();
//        if ((contentType == null && request.getContentLength() > 0) || // Content-Type 为空且 Body 不为空
//                (contentType != null && !contentType.contains(loginContentType)) // Content-Type 不为空且 Content-Type 不为 JSON
//        ) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        // 获取请求的 URl
        String requestUrl = UrlUtils.buildRequestUrl(request);

        // 检查请求 URL 是否为忽略的 URL
        if (ignoreUrlUtils.checkIfIgnore(request)) {
            // 如果是忽略访问的URL
            // 标识该 URL 是忽略访问的
            wrappedRequest.addExtraParams("IGNORE_URL", true);

            // 直接进行下面的 filter 操作
            filterChain.doFilter(wrappedRequest, wrappedResponse);
            return;
        }

        try {
            // 前后端分离情况下，前端登录后将 token 储存在 header 中，每次访问接口时通过 token 去拿用户权限
            String token = request.getHeader(tokenHeaderName);

            // 没有 token，且不是登录接口，返回未登录
            if (StringUtils.isBlank(token) && !requestUrl.equals(loginUrl)) {
                throw new BadCredentialsException(noLoginTips);
            }

            // 检查 token 是否正确
            User<?> checkResultUser = tokenUtils.checkToken(token);

            // token 不正确，返回 token 已过期
            if (checkResultUser == null) {
                throw new BadCredentialsException(tokenExpireTips);
            }

            // 获取用户 ID
            String userId = checkResultUser.getId();

            // 通过用户 ID 获取用户信息
            SecurityUser<?> securityUser = webUserDetailsService.getUserById(userId);
            if (securityUser == null || securityUser.getUser() == null) {
                throw new BadCredentialsException(tokenExpireTips);
            }

            // 设置请求的 token
            securityUser.setToken(token);

            // 创建凭据
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());

            // 全局注入角色权限信息和登录用户基本信息
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 验证通过，继续执行下面的 filter
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            webAuthenticationEntryPoint.commence(wrappedRequest, response, e);
        } catch (Exception e) {
            e.printStackTrace();
            // 其它异常
            SecurityContextHolder.clearContext();
            ResponseUtils.writeBody(response, ApiResult.error().setMessage(systemExceptionTips));
        }
    }
}
