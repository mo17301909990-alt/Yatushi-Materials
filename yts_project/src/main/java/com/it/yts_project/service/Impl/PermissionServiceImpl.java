package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.PermissionMapper;
import com.it.yts_project.mapper.RolePermissionMapper;
import com.it.yts_project.model.Permission;
import com.it.yts_project.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Permission> findAllPermissions() {
        return permissionMapper.findAllPermissions();
    }

    @Override
    public Permission findPermissionById(Integer id) {
        return permissionMapper.findPermissionById(id);
    }

    @Override
    public Permission findPermissionByKey(String permissionKey) {
        return permissionMapper.findPermissionByKey(permissionKey);
    }

    @Override
    @Transactional
    public Permission createPermission(Permission permission) {
        permission.setCreatedTime(new Date());
        permission.setUpdatedTime(new Date());
        permissionMapper.insertPermission(permission);
        return permission;
    }

    @Override
    @Transactional
    public Permission updatePermission(Permission permission) {
        permission.setUpdatedTime(new Date());
        permissionMapper.updatePermission(permission);
        return permission;
    }

    @Override
    @Transactional
    public boolean deletePermission(Integer id) {
        // 删除权限前，先删除角色权限关联
        rolePermissionMapper.deleteRolePermissionsByRoleId(id);
        return permissionMapper.deletePermission(id) > 0;
    }

    @Override
    public List<Permission> findPermissionsByRoleId(Integer roleId) {
        return permissionMapper.findPermissionsByRoleId(roleId);
    }

    @Override
    public List<Permission> findPermissionsByUserId(Integer userId) {
        return permissionMapper.findPermissionsByUserId(userId);
    }

    @Override
    public List<Permission> findMenuPermissions() {
        return permissionMapper.findMenuPermissions();
    }

    @Override
    @Transactional
    public boolean assignPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        // 先删除角色现有权限
        rolePermissionMapper.deleteRolePermissionsByRoleId(roleId);
        // 批量插入新权限
        return rolePermissionMapper.batchInsertRolePermissions(roleId, permissionIds) > 0;
    }

    @Override
    @Transactional
    public boolean removeRolePermissions(Integer roleId, List<Integer> permissionIds) {
        for (Integer permissionId : permissionIds) {
            rolePermissionMapper.deleteRolePermission(roleId, permissionId);
        }
        return true;
    }
}
