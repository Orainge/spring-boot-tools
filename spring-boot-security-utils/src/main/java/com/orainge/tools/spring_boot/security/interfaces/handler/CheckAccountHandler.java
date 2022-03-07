package com.orainge.tools.spring_boot.security.interfaces.handler;

import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 检查用户登录情况处理器
 *
 * @author orainge
 * @since 2022/3/2
 */
public interface CheckAccountHandler {
    /**
     * 检查账户是否可用<br>
     *
     * @param username       前端提交的用户名
     * @param password       前端提交的密码
     * @param securityUser   本次请求的用户信息
     * @param authentication 凭据信息
     * @throws AuthenticationException 验证异常
     */
    void checkAccount(String username, String password, SecurityUser<?> securityUser, Authentication authentication) throws AuthenticationException;
}
