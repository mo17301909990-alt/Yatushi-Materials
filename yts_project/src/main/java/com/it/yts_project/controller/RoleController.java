package com.it.yts_project.controller;

import com.it.yts_project.model.Role;
import com.it.yts_project.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // 获取所有角色
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            List<Role> roles = roleService.findAllRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取角色列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 根据ID获取角色
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Integer id) {
        try {
            Role role = roleService.findRoleById(id);
            if (role != null) {
                return ResponseEntity.ok(role);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "角色不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取角色失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 创建角色
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            return ResponseEntity.ok(createdRole);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "创建角色失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 更新角色
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody Role role) {
        try {
            role.setId(id);
            Role updatedRole = roleService.updateRole(role);
            return ResponseEntity.ok(updatedRole);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "更新角色失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 删除角色
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Integer id) {
        try {
            boolean success = roleService.deleteRole(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("message", "角色删除成功");
            } else {
                response.put("message", "角色删除失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "删除角色失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 根据用户ID获取角色列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getRolesByUserId(@PathVariable Integer userId) {
        try {
            List<Role> roles = roleService.findRolesByUserId(userId);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取用户角色失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 为用户分配角色
    @PostMapping("/assign")
    public ResponseEntity<?> assignRolesToUser(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            @SuppressWarnings("unchecked")
            List<Integer> roleIds = (List<Integer>) request.get("roleIds");
            
            boolean success = roleService.assignRolesToUser(userId, roleIds);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("message", "角色分配成功");
            } else {
                response.put("message", "角色分配失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "分配角色失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
