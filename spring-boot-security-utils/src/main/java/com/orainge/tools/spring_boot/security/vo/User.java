package com.orainge.tools.spring_boot.security.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户信息
 *
 * @author orainge
 * @since 2022/3/2
 */
@Data
@Accessors(chain = true)
public class User<T> {
    /**
     * 用户 ID
     */
    protected String id;

    /**
     * 用户名
     */
    protected String username;

    /**
     * 密码
     */
    protected String password;

    /**
     * 密码盐
     */
    protected String salt;

    /**
     * 其它信息
     */
    private T otherInfo;

    public User() {

    }

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
