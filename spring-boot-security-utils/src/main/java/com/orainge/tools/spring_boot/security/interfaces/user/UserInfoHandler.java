package com.orainge.tools.spring_boot.security.interfaces.user;

import com.orainge.tools.spring_boot.security.vo.User;

/**
 * 用户信息处理器
 *
 * @author orainge
 * @since 2022/3/2
 */
public interface UserInfoHandler {
    /**
     * 通过用户名获取用户信息
     *
     * @param username 用户名
     */
    User<?> getUserByUsername(String username);

    /**
     * 通过用户 ID 获取用户信息
     *
     * @param userId 用户 ID
     */
    User<?> getUserById(String userId);
}
