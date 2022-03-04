package com.orainge.tools.spring_boot.security.utils.password;

import com.orainge.tools.spring_boot.security.vo.SecurityUser;

/**
 * 密码工具接口
 *
 * @author orainge
 * @since 2022/3/2
 */
public interface PasswordUtils {
    /**
     * 校验密码是否一致
     *
     * @param commitPassword   前端提交的密码
     * @param dataBasePassword 数据库中的密码
     * @param securityUser     用户参数
     * @return ture: 密码正确; false: 密码不正确
     */
    boolean isValidPassword(String commitPassword, String dataBasePassword, SecurityUser<?> securityUser);

    /**
     * 对密码进行编码
     *
     * @param password 未加密密码
     * @param params   其它加密参数
     * @return 加密后的密码
     */
    String encodePassword(String password, Object params);
}