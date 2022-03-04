package com.orainge.tools.spring_boot.security.utils.password.impl;

import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import com.orainge.tools.spring_boot.security.utils.password.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

/**
 * 密码工具简单实现
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@Slf4j
@ConditionalOnMissingBean(PasswordUtils.class)
public class SimplePasswordUtilsImpl implements PasswordUtils {
    public static final int HASH_ITERATIONS = 1;

    /**
     * 校验密码是否一致
     *
     * @param commitPassword   前端提交的密码
     * @param dataBasePassword 数据库中的密码
     * @param securityUser     用户参数
     * @return ture: 密码正确; false: 密码不正确
     */
    public boolean isValidPassword(String commitPassword, String dataBasePassword, SecurityUser<?> securityUser) {
        return dataBasePassword.equalsIgnoreCase(encodePassword(commitPassword, securityUser.getUser().getSalt()));
    }

    /**
     * 对密码进行编码
     *
     * @param password 未加密密码
     * @param salt     盐值
     * @return 加密后的密码
     */
    public String encodePassword(String password, Object salt) {
        String encodedPassword;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            if (salt != null) {
                digest.reset();
                digest.update(((String) salt).getBytes());
            }
            byte[] hashed = digest.digest(password.getBytes());
            int iterations = HASH_ITERATIONS - 1;
            for (int i = 0; i < iterations; ++i) {
                digest.reset();
                hashed = digest.digest(hashed);
            }
            encodedPassword = new String(Hex.encode(hashed));
        } catch (Exception e) {
            log.error("验证密码异常:", e);
            return null;
        }
        return encodedPassword;
    }
}