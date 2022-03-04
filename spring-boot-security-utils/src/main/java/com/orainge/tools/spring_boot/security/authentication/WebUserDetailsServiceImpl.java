package com.orainge.tools.spring_boot.security.authentication;

import com.orainge.tools.spring_boot.security.config.CustomSecurityConfig;
import com.orainge.tools.spring_boot.security.vo.SecurityUser;
import com.orainge.tools.spring_boot.security.vo.User;
import com.orainge.tools.spring_boot.security.interfaces.user.UserInfoHandler;
import com.orainge.tools.spring_boot.security.utils.role.RoleUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 认证用户详情服务
 *
 * @author orainge
 * @since 2022/3/2
 */
@Service("userDetailsService")
@ConditionalOnMissingBean(WebUserDetailsServiceImpl.class)
public class WebUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private CustomSecurityConfig config;

    @Resource
    private UserInfoHandler userInfoHandler;

    @Resource
    private RoleUtils roleUtils;

    /***
     * 根据账号获取用户信息
     *
     * @param username 用户名
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中取出用户信息
        User<?> user = userInfoHandler.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(config.getTips().getNoUser());
        } else {
            // 返回UserDetails实现类
            return new SecurityUser<>(user, roleUtils.getHasRoleList(user.getId()));
        }
    }

    /***
     * 根据用户 Id 获取用户信息
     *
     * @param userId 用户 ID
     */
    public SecurityUser<?> getUserById(String userId) {
        if (userId == null) {
            return null;
        }

        User<?> user = userInfoHandler.getUserById(userId);

        if (user == null) {
            return null;
        } else {
            // 返回UserDetails实现类
            return new SecurityUser<>(user, roleUtils.getHasRoleList(user.getId()));
        }
    }
}