package com.orainge.tools.spring_boot.security.utils.token.handler;


/**
 * Token 过期 Handler 接口
 *
 * @author orainge
 * @since 2022/3/3
 */
public interface TokenExpireHandler {
    /**
     * 使 token 失效
     *
     * @param token 要失效的 token
     * @return true: 操作成功; false: 操作失败
     */
    boolean expireToken(String token);
}
