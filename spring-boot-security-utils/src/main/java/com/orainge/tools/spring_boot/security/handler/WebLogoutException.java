package com.orainge.tools.spring_boot.security.handler;

public class WebLogoutException extends RuntimeException {
    public WebLogoutException() {
        super("退出登录异常");
    }
}
