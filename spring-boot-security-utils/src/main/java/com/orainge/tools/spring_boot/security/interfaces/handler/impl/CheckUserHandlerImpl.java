package com.orainge.tools.spring_boot.security.interfaces.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.interfaces.handler.CheckUserHandler;
import com.orainge.tools.spring_boot.security.utils.password.PasswordUtils;
import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 检查用户登录情况处理器默认实现
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(CheckUserHandler.class)
public class CheckUserHandlerImpl implements CheckUserHandler {
    @Resource
    private CustomSecurityConfig config;

    @Resource
    private PasswordUtils passwordUtils;

    /**
     * 检查用户密码是否正确
     *
     * @param username       前端提交的用户名
     * @param password       前端提交的密码
     * @param securityUser   从数据库中获取用户信息
     * @param authentication 凭据信息
     * @return null: 验证正确; 非 null: 错误原因
     */
    public String checkIfCorrect(String username, String password, SecurityUser<?> securityUser, Authentication authentication) {
        // 验证密码
        boolean isValid = passwordUtils.isValidPassword(password, securityUser.getPassword(), securityUser);

        if (!isValid) {
            return config.getTips().getIncorrectPassword();
        }

        // 验证通过
        return null;
    }
}
