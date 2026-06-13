package com.it.yts_project.mapper;

import com.it.yts_project.model.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RolePermissionMapper {
    
    // 插入角色权限关联
    int insertRolePermission(RolePermission rolePermission);
    
    // 删除角色权限关联
    int deleteRolePermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);
    
    // 删除角色的所有权限
    int deleteRolePermissionsByRoleId(@Param("roleId") Integer roleId);
    
    // 根据角色ID查询权限ID列表
    List<Integer> findPermissionIdsByRoleId(@Param("roleId") Integer roleId);
    
    // 批量插入角色权限关联
    int batchInsertRolePermissions(@Param("roleId") Integer roleId, @Param("permissionIds") List<Integer> permissionIds);
}
