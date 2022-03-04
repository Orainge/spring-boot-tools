package com.orainge.tools.spring_boot.security.interfaces.role;

import com.orainge.tools.spring_boot.security.vo.Role;

import java.util.List;

/**
 * 用户角色 Helper
 *
 * @author orainge
 * @since 2022/3/2
 */
public interface UserRoleHelper {
    /**
     * 获取该用户拥有的角色列表
     *
     * @param userId 用户 ID
     * @return 角色列表
     */
    List<? extends Role> getHasRoleList(String userId);
}
