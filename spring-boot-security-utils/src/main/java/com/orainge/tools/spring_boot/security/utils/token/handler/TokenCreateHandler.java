package com.orainge.tools.spring_boot.security.utils.token.handler;

import com.orainge.tools.spring_boot.security.vo.SecurityUser;

/**
 * Token 生成 Handler 接口
 *
 * @author orainge
 * @since 2022/3/3
 */
public interface TokenCreateHandler {
    /**
     * 创建 token
     *
     * @param userInfo 登录的用户信息
     * @return 创建的 token
     */
    String createToken(SecurityUser<?> userInfo);
}
