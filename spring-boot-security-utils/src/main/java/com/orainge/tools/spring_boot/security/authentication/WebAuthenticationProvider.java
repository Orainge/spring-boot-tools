package com.orainge.tools.spring_boot.security.authentication;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.CheckAccountHandler;
import com.orainge.tools.spring_boot.security.utils.token.TokenUtils;
import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 自定义认证处理
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(WebAuthenticationProvider.class)
public class WebAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private CheckAccountHandler checkUserHandler;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private TokenUtils tokenUtils;

    @Resource
    private CustomSecurityConfig config;

    private static String getTokenExceptionTips;

    @PostConstruct
    public void init() {
        getTokenExceptionTips = config.getTips().getGetTokenException();
    }

    @Override

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取前端提交的用户名、密码
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // 从数据库中获取用户信息
        SecurityUser<?> userInfo = (SecurityUser<?>) userDetailsService.loadUserByUsername(userName);

        // 检查密码是否正确
        // 这里要抛出异常
        // 可以抛出的异常见 WebLoginFailureHandler
        checkUserHandler.checkAccount(userName, password, userInfo, authentication);

        // 获取 token
        String token = tokenUtils.createToken(userInfo);

        // 如果生成 token 失败，则返回错误提示
        if (token == null) {
            throw new BadCredentialsException(getTokenExceptionTips);
        }

        // 更新 token
        userInfo.setToken(token);

        // 返回登录成功的凭据
        return new UsernamePasswordAuthenticationToken(userInfo, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}