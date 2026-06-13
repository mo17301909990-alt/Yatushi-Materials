package com.it.yts_project.mapper;

import com.it.yts_project.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {
    
    // 查询所有权限
    List<Permission> findAllPermissions();
    
    // 根据ID查询权限
    Permission findPermissionById(@Param("id") Integer id);
    
    // 根据权限键查询权限
    Permission findPermissionByKey(@Param("permissionKey") String permissionKey);
    
    // 插入权限
    int insertPermission(Permission permission);
    
    // 更新权限
    int updatePermission(Permission permission);
    
    // 删除权限
    int deletePermission(@Param("id") Integer id);
    
    // 根据角色ID查询权限列表
    List<Permission> findPermissionsByRoleId(@Param("roleId") Integer roleId);
    
    // 根据用户ID查询权限列表
    List<Permission> findPermissionsByUserId(@Param("userId") Integer userId);
    
    // 查询菜单权限（用于构建菜单树）
    List<Permission> findMenuPermissions();
}
