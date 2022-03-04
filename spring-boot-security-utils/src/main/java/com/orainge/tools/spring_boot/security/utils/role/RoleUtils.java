package com.orainge.tools.spring_boot.security.utils.role;

import com.orainge.tools.spring_boot.security.vo.Role;
import com.orainge.tools.spring_boot.security.interfaces.role.UrlRoleHelper;
import com.orainge.tools.spring_boot.security.interfaces.role.UserRoleHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户角色工具类
 *
 * @author orainge
 * @since 2022/3/2
 */
@Component
@ConditionalOnMissingBean(RoleUtils.class)
public class RoleUtils {
    @Resource
    private UserRoleHelper userRoleHelper;

    @Resource
    private UrlRoleHelper urlRoleHelper;

    /**
     * 根据用户 ID 获取该用户所属的角色
     *
     * @param userId 用户 ID
     * @return 该用户所属的角色列表
     */
    public List<? extends Role> getHasRoleList(String userId) {
        return userRoleHelper.getHasRoleList(userId);
    }

    /**
     * 根据 URL 获取访问该 URL 需要的角色
     *
     * @param url 需要访问的 URL 地址
     * @return 访问该 URL 需要的角色列表
     */
    public List<? extends Role> getNeedRoleList(String url) {
        return urlRoleHelper.getNeedRoleList(url);
    }
}
