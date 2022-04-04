package com.orainge.tools.spring_boot.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 账号配置错误异常
 *
 * @author orainge
 * @since 2022/04/04
 */
public class AccountConfigureException extends AuthenticationException {
    public AccountConfigureException(String msg) {
        super(msg);
    }

    public AccountConfigureException(String msg, Throwable t) {
        super(msg, t);
    }
}
