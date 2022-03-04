package com.orainge.tools.spring_boot.security.interfaces.handler;

import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import org.springframework.security.core.Authentication;

/**
 * 检查用户登录情况处理器
 *
 * @author orainge
 * @since 2022/3/2
 */
public interface CheckUserHandler {
    /**
     * 检查用户密码是否正确
     *
     * @param username       前端提交的用户名
     * @param password       前端提交的密码
     * @param securityUser   本次请求的用户信息
     * @param authentication 凭据信息
     * @return null: 验证正确; 非 null: 错误原因
     */
    String checkIfCorrect(String username, String password, SecurityUser<?> securityUser, Authentication authentication);
}
