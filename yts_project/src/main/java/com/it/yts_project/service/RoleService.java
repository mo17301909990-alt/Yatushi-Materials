package com.it.yts_project.service;

import com.it.yts_project.model.Role;

import java.util.List;

public interface RoleService {
    
    // 查询所有角色
    List<Role> findAllRoles();
    
    // 根据ID查询角色
    Role findRoleById(Integer id);
    
    // 根据角色键查询角色
    Role findRoleByKey(String roleKey);
    
    // 创建角色
    Role createRole(Role role);
    
    // 更新角色
    Role updateRole(Role role);
    
    // 删除角色
    boolean deleteRole(Integer id);
    
    // 根据用户ID查询角色列表
    List<Role> findRolesByUserId(Integer userId);
    
    // 为用户分配角色
    boolean assignRolesToUser(Integer userId, List<Integer> roleIds);
    
    // 移除用户角色
    boolean removeUserRoles(Integer userId, List<Integer> roleIds);
}
