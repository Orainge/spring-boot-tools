package com.orainge.tools.spring_boot.security.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 角色类型
 *
 * @author orainge
 * @since 2022/3/2
 */
@Data
@Accessors(chain = true)
public class RoleType {
    /**
     * 角色类型 ID
     */
    protected String id;

    /**
     * 角色描述
     */
    protected String description;

    public RoleType(String id) {
        if (id == null || "".equals(id)) {
            throw new NullPointerException("参数 [角色 ID] 不能为空");
        }

        this.id = id;
    }

    public RoleType(String id, String description) {
        if (id == null || "".equals(id)) {
            throw new NullPointerException("参数 [角色 ID] 不能为空");
        } else if (description == null || "".equals(description)) {
            throw new NullPointerException("参数 [角色描述] 不能为空");
        }

        this.id = id;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleType roleType = (RoleType) o;
        return id.equals(roleType.id) && Objects.equals(description, roleType.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }
}