package com.it.yts_project.service;

import com.it.yts_project.model.Permission;

import java.util.List;

public interface PermissionService {
    
    // 查询所有权限
    List<Permission> findAllPermissions();
    
    // 根据ID查询权限
    Permission findPermissionById(Integer id);
    
    // 根据权限键查询权限
    Permission findPermissionByKey(String permissionKey);
    
    // 创建权限
    Permission createPermission(Permission permission);
    
    // 更新权限
    Permission updatePermission(Permission permission);
    
    // 删除权限
    boolean deletePermission(Integer id);
    
    // 根据角色ID查询权限列表
    List<Permission> findPermissionsByRoleId(Integer roleId);
    
    // 根据用户ID查询权限列表
    List<Permission> findPermissionsByUserId(Integer userId);
    
    // 查询菜单权限（用于构建菜单树）
    List<Permission> findMenuPermissions();
    
    // 为角色分配权限
    boolean assignPermissionsToRole(Integer roleId, List<Integer> permissionIds);
    
    // 移除角色权限
    boolean removeRolePermissions(Integer roleId, List<Integer> permissionIds);
}
