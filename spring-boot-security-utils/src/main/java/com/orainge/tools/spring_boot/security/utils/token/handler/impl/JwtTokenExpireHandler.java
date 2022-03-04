package com.orainge.tools.spring_boot.security.utils.token.handler.impl;

import com.orainge.tools.spring_boot.security.utils.token.handler.TokenExpireHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * Token 验证 Handler 接口
 *
 * @author orainge
 * @since 2022/3/3
 */
@Component
@ConditionalOnMissingBean(TokenExpireHandler.class)
public class JwtTokenExpireHandler implements TokenExpireHandler {
    /**
     * 使 token 失效
     *
     * @param token 要失效的 token
     * @return true: 操作成功; false: 操作失败
     */
    public boolean expireToken(String token) {
        // JWT TOKEN 无法失效，默认返回成功
        return true;
    }
}
