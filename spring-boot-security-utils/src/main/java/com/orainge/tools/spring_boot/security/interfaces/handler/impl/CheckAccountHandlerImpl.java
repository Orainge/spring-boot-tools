package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.CheckAccountHandler;
import com.orainge.tools.spring_boot.security.utils.password.PasswordUtils;
import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 检查账户情况处理器默认实现
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(CheckAccountHandler.class)
public class CheckAccountHandlerImpl implements CheckAccountHandler {
    @Resource
    protected CustomSecurityConfig config;

    @Resource
    protected PasswordUtils passwordUtils;

    /**
     * 检查账户是否可用<br>
     * 可以继承此类或实现接口来实现自定义的验证过程
     *
     * @param username       前端提交的用户名
     * @param password       前端提交的密码
     * @param securityUser   从数据库中获取用户信息
     * @param authentication 凭据信息
     * @throws AuthenticationException 验证异常
     */
    public void checkAccount(String username, String password, SecurityUser<?> securityUser, Authentication authentication) throws AuthenticationException {
        // 验证密码
        boolean accountValid = passwordUtils.isValidPassword(password, securityUser.getPassword(), securityUser);

        // 密码不正确
        if (!accountValid) {
            throw new BadCredentialsException(config.getTips().getIncorrectPassword());
        }
    }
}
