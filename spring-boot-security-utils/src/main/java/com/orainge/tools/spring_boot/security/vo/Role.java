package com.orainge.tools.spring_boot.security.vo;

import lombok.Getter;

import java.util.Objects;

/**
 * 用户角色实体类<br>
 * 角色是抽象的，角色的创建可以有多种来源
 *
 * @author orainge
 * @since 2022/3/2
 */
@Getter
public class Role {
    /**
     * 角色 ID
     */
    protected String id;

    /**
     * 关系表 ID (关系表里的 ID)
     */
    protected String relationId;

    /**
     * 角色授权类型
     */
    protected RoleType type;

    public Role(String relationId, RoleType type) {
        if (relationId == null || "".equals(relationId)) {
            throw new NullPointerException("参数 [关系表 ID] 不能为空");
        } else if (type == null) {
            throw new NullPointerException("参数 [角色类型] 不能为空");
        } else {
            String typeId = type.getId();
            if (typeId == null || "".equals(typeId)) {
                throw new NullPointerException("参数 [角色类型 ID] 不能为空");
            }
        }

        this.relationId = relationId;
        this.type = type;
        this.id = type.getId() + "_" + relationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id) && Objects.equals(relationId, role.relationId) && Objects.equals(type, role.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, relationId, type);
    }
}
