package com.it.yts_project.controller;

import com.it.yts_project.model.Permission;
import com.it.yts_project.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // 获取所有权限
    @GetMapping
    public ResponseEntity<?> getAllPermissions() {
        try {
            List<Permission> permissions = permissionService.findAllPermissions();
            return ResponseEntity.ok(permissions);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取权限列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 获取菜单权限
    @GetMapping("/menu")
    public ResponseEntity<?> getMenuPermissions() {
        try {
            List<Permission> permissions = permissionService.findMenuPermissions();
            return ResponseEntity.ok(permissions);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取菜单权限失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 根据ID获取权限
    @GetMapping("/{id}")
    public ResponseEntity<?> getPermissionById(@PathVariable Integer id) {
        try {
            Permission permission = permissionService.findPermissionById(id);
            if (permission != null) {
                return ResponseEntity.ok(permission);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "权限不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取权限失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 创建权限
    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody Permission permission) {
        try {
            Permission createdPermission = permissionService.createPermission(permission);
            return ResponseEntity.ok(createdPermission);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "创建权限失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 更新权限
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable Integer id, @RequestBody Permission permission) {
        try {
            permission.setId(id);
            Permission updatedPermission = permissionService.updatePermission(permission);
            return ResponseEntity.ok(updatedPermission);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "更新权限失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 删除权限
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable Integer id) {
        try {
            boolean success = permissionService.deletePermission(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("message", "权限删除成功");
            } else {
                response.put("message", "权限删除失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "删除权限失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 根据角色ID获取权限列表
    @GetMapping("/role/{roleId}")
    public ResponseEntity<?> getPermissionsByRoleId(@PathVariable Integer roleId) {
        try {
            List<Permission> permissions = permissionService.findPermissionsByRoleId(roleId);
            return ResponseEntity.ok(permissions);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取角色权限失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 根据用户ID获取权限列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPermissionsByUserId(@PathVariable Integer userId) {
        try {
            List<Permission> permissions = permissionService.findPermissionsByUserId(userId);
            return ResponseEntity.ok(permissions);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取用户权限失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 为角色分配权限
    @PostMapping("/assign")
    public ResponseEntity<?> assignPermissionsToRole(@RequestBody Map<String, Object> request) {
        try {
            Integer roleId = (Integer) request.get("roleId");
            @SuppressWarnings("unchecked")
            List<Integer> permissionIds = (List<Integer>) request.get("permissionIds");
            
            boolean success = permissionService.assignPermissionsToRole(roleId, permissionIds);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("message", "权限分配成功");
            } else {
                response.put("message", "权限分配失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "分配权限失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
