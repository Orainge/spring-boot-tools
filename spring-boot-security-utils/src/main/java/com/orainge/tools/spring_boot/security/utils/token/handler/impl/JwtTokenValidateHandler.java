package com.orainge.tools.spring_boot.security.utils.token.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.utils.token.handler.TokenValidateHandler;
import com.orainge.tools.spring_boot.security.vo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * JWT Token 验证器
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(TokenValidateHandler.class)
public class JwtTokenValidateHandler implements TokenValidateHandler {
    @Resource
    private CustomSecurityConfig config;

    private static String jwtSalt;

    @PostConstruct
    public void init() {
        jwtSalt = config.getJwt().getSalt();
    }

    @Override
    public User<?> checkToken(String token) {
        try {
            // 获取jwt中的信息
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSalt)
                    .parseClaimsJws(token.replace("Bearer", ""))
                    .getBody();

            // 获取当前登录用户 ID
            String userId = claims.getSubject();

            // 返回用户信息
            return new User<>().setId(userId);
        } catch (Exception e) {
            // jwt 令牌过期 / 异常
            return null;
        }
    }
}
