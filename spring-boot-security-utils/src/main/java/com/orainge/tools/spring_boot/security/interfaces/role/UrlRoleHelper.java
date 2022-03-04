package com.orainge.tools.spring_boot.security.interfaces.role;

import com.orainge.tools.spring_boot.security.vo.Role;

import java.util.List;

/**
 * URL 角色 Helper
 *
 * @author orainge
 * @since 2022/3/2
 */
public interface UrlRoleHelper {
    /**
     * 获取访问该 URL 所需的角色
     *
     * @param url 要访问的 URL
     * @return 访问该 URL 所需的角色列表
     */
    List<? extends Role> getNeedRoleList(String url);
}
