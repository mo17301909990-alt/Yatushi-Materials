package com.it.yts_project.mapper;

import com.it.yts_project.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    
    // 查询所有角色
    List<Role> findAllRoles();
    
    // 根据ID查询角色
    Role findRoleById(@Param("id") Integer id);
    
    // 根据角色键查询角色
    Role findRoleByKey(@Param("roleKey") String roleKey);
    
    // 插入角色
    int insertRole(Role role);
    
    // 更新角色
    int updateRole(Role role);
    
    // 删除角色
    int deleteRole(@Param("id") Integer id);
    
    // 根据用户ID查询角色列表
    List<Role> findRolesByUserId(@Param("userId") Integer userId);
}
