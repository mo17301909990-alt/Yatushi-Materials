package com.it.yts_project.mapper;

import com.it.yts_project.model.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    
    // 插入用户角色关联
    int insertUserRole(UserRole userRole);
    
    // 删除用户角色关联
    int deleteUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
    
    // 删除用户的所有角色
    int deleteUserRolesByUserId(@Param("userId") Integer userId);
    
    // 根据用户ID查询角色ID列表
    List<Integer> findRoleIdsByUserId(@Param("userId") Integer userId);
    
    // 批量插入用户角色关联
    int batchInsertUserRoles(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds);
}
