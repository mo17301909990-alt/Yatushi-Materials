package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.RoleMapper;
import com.it.yts_project.mapper.UserRoleMapper;
import com.it.yts_project.model.Role;
import com.it.yts_project.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> findAllRoles() {
        return roleMapper.findAllRoles();
    }

    @Override
    public Role findRoleById(Integer id) {
        return roleMapper.findRoleById(id);
    }

    @Override
    public Role findRoleByKey(String roleKey) {
        return roleMapper.findRoleByKey(roleKey);
    }

    @Override
    @Transactional
    public Role createRole(Role role) {
        role.setCreatedTime(new Date());
        role.setUpdatedTime(new Date());
        roleMapper.insertRole(role);
        return role;
    }

    @Override
    @Transactional
    public Role updateRole(Role role) {
        role.setUpdatedTime(new Date());
        roleMapper.updateRole(role);
        return role;
    }

    @Override
    @Transactional
    public boolean deleteRole(Integer id) {
        // 删除角色前，先删除用户角色关联
        userRoleMapper.deleteUserRolesByUserId(id);
        return roleMapper.deleteRole(id) > 0;
    }

    @Override
    public List<Role> findRolesByUserId(Integer userId) {
        return roleMapper.findRolesByUserId(userId);
    }

    @Override
    @Transactional
    public boolean assignRolesToUser(Integer userId, List<Integer> roleIds) {
        // 先删除用户现有角色
        userRoleMapper.deleteUserRolesByUserId(userId);
        // 批量插入新角色
        return userRoleMapper.batchInsertUserRoles(userId, roleIds) > 0;
    }

    @Override
    @Transactional
    public boolean removeUserRoles(Integer userId, List<Integer> roleIds) {
        for (Integer roleId : roleIds) {
            userRoleMapper.deleteUserRole(userId, roleId);
        }
        return true;
    }
}
