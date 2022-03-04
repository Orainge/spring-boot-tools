package com.orainge.tools.spring_boot.security.utils.token.handler.impl;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.consts.SecurityConstants;
import com.orainge.tools.spring_boot.security.utils.token.handler.TokenCreateHandler;
import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

/**
 * JWT token 生成器
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(TokenCreateHandler.class)
public class JwtTokenCreateHandler implements TokenCreateHandler {
    @Resource
    private CustomSecurityConfig config;

    private static int expireTime;

    private static String jwtSalt;

    private static final String JWT_ROLE_NAME = "jwt_login_role";

    @PostConstruct
    public void init() {
        expireTime = config.getToken().getExpireTime();
        jwtSalt = config.getJwt().getSalt();
    }

    @Override
    public String createToken(SecurityUser<?> userInfo) {
        // 更新登录令牌
        // 当前用户所拥有角色代码
        String roleCodes = userInfo.getRoleIds();

        // 签发时间
        Date nowDate = new Date();

        // token 过期时间 (默认 30 分钟)
        Date expireDate = new Date(System.currentTimeMillis() + (long) expireTime * 1000);

        // 生成jwt访问令牌
        return Jwts.builder()
                // 用户角色
                .claim(JWT_ROLE_NAME, roleCodes)
                // 主题 (存用户 ID)
                .setSubject(userInfo.getUser().getId())
                // 签发时间
                .setIssuedAt(nowDate)
                // 过期时间
                .setExpiration(expireDate)
                // 设置加密算法和密钥
                .signWith(SignatureAlgorithm.HS512, jwtSalt)
                .compact();
    }
}
