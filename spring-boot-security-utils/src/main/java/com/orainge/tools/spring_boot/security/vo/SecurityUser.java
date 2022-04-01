package com.orainge.tools.spring_boot.security.vo;

import com.orainge.tools.spring_boot.security.utils.role.RoleUtils;
import com.orainge.tools.spring_boot.utils.BeanUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 安全认证用户详情信息
 *
 * @author orainge
 * @since 2022/3/2
 */
@Getter
@Slf4j
public class SecurityUser<T> implements UserDetails {
    /**
     * 当前登录的用户信息
     */
    protected transient User<T> user;

    /**
     * 当前登录用户的 token
     */
    protected transient String token;

    /**
     * 当前用户拥有的角色列表
     */
    protected transient List<? extends Role> roleList;

    public SecurityUser() {
    }

    public SecurityUser(User<T> user, List<? extends Role> roleList) {
        if (user != null) {
            this.user = user;
        }

        if (roleList == null) {
            this.roleList = new LinkedList<>();
        } else {
            this.roleList = roleList;
        }
    }

    /**
     * 获取 RoleIds
     */
    public String getRoleIds(){
        StringJoiner roleCodes = new StringJoiner(",", "[", "]");
        this.roleList.forEach(item -> roleCodes.add(item.getId()));
        return roleCodes.toString();
    }

    /**
     * 获取该用户拥有哪些角色
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 获取角色工具类
        RoleUtils roleUtils = BeanUtils.getBeanByClass(RoleUtils.class);

        // 获取角色列表
        List<? extends Role> roleList = roleUtils.getHasRoleList(user.getId());

        // 返回角色 ID 列表
        return roleList.stream()
                .map(item -> new SimpleGrantedAuthority(item.getId()))
                .collect(Collectors.toList());
    }

    public SecurityUser<T> setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}