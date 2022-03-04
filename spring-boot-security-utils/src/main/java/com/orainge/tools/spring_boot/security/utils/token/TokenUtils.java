package com.orainge.tools.spring_boot.security.utils.token;

import com.orainge.tools.spring_boot.security.utils.token.handler.TokenCreateHandler;
import com.orainge.tools.spring_boot.security.utils.token.handler.TokenExpireHandler;
import com.orainge.tools.spring_boot.security.utils.token.handler.TokenValidateHandler;
import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import com.orainge.tools.spring_boot.security.vo.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Token 工具类
 *
 * @author orainge
 * @since 2022/3/3
 */
@Component
@ConditionalOnMissingBean(TokenUtils.class)
public class TokenUtils {
    @Resource
    private TokenCreateHandler tokenCreator;

    @Resource
    private TokenValidateHandler tokenValidator;

    @Resource
    private TokenExpireHandler tokenExpireHandler;

    /**
     * 创建 token
     *
     * @param userInfo 登录的用户信息
     * @return 创建的 token
     */
    public String createToken(SecurityUser<?> userInfo) {
        return tokenCreator.createToken(userInfo);
    }

    /**
     * 检查 token 并返回用户信息<br>
     * 返回的信息中必须包含 userId
     *
     * @param token 请求的 token
     * @return null: token 不正确; 非 null: 该 token 对应的用户信息
     */
    public User<?> checkToken(String token) {
        return tokenValidator.checkToken(token);
    }

    /**
     * 使 token 失效
     *
     * @param token 要失效的 token
     * @return true: 操作成功; false: 操作失败
     */
    public boolean expireToken(String token) {
        return tokenExpireHandler.expireToken(token);
    }
}
