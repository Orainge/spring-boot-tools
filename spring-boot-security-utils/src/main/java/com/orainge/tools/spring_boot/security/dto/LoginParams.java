package com.orainge.tools.spring_boot.security.dto;

import lombok.Data;

/**
 * 登录请求参数
 *
 * @author orainge
 * @since 2022/3/2
 */
@Data
public class LoginParams {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
