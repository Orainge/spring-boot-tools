package com.orainge.tools.spring_boot.security.utils.token.handler;

import com.orainge.tools.spring_boot.security.vo.User;

/**
 * Token 验证 Handler 接口
 *
 * @author orainge
 * @since 2022/3/3
 */
public interface TokenValidateHandler {
    /**
     * 检查 token 并返回用户信息<br>
     * 返回的信息中必须包含 userId
     *
     * @param token 请求的 token
     * @return null: token 不正确; 非 null: 该 token 对应的用户信息
     */
    User<?> checkToken(String token);
}
