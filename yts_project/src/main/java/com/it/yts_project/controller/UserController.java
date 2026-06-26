package com.it.yts_project.controller;

import com.it.yts_project.dto.BatchSavePreferencesRequest;
import com.it.yts_project.dto.UserDTO;
import com.it.yts_project.dto.UserMatchPreferenceDTO;
import com.it.yts_project.service.UserMatchPreferenceService;
import com.it.yts_project.service.UserService;
import com.it.yts_project.service.RoleService;
import com.it.yts_project.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/users", "/users"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMatchPreferenceService userMatchPreferenceService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            UserDTO registeredUser = userService.registerUser(userDTO);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            UserDTO user = userService.loginUser(userDTO.getUsername(), userDTO.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ==================== 匹配偏好相关接口 ====================

    /**
     * 获取用户的匹配偏好（按字段分组）
     * @param userId 用户ID
     * @return 按字段分组的匹配偏好
     */
    @GetMapping("/{userId}/match-preferences")
    public ResponseEntity<?> getUserMatchPreferences(@PathVariable Integer userId) {
        try {
            Map<String, List<String>> preferences = userMatchPreferenceService.getUserPreferencesGrouped(userId);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取匹配偏好失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 批量保存用户匹配偏好
     * @param userId 用户ID
     * @param request 批量保存请求
     * @return 保存结果
     */
    @PostMapping("/{userId}/match-preferences")
    public ResponseEntity<?> batchSaveMatchPreferences(@PathVariable Integer userId,
                                                      @RequestBody BatchSavePreferencesRequest request) {
        try {
            // 确保请求中的用户ID与路径参数一致
            request.setUserId(userId);

            int savedCount = userMatchPreferenceService.batchSavePreferences(request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "匹配偏好保存成功");
            response.put("savedCount", savedCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "保存匹配偏好失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除单个匹配偏好
     * @param id 匹配偏好ID
     * @return 删除结果
     */
    @DeleteMapping("/match-preferences/{id}")
    public ResponseEntity<?> deleteMatchPreference(@PathVariable Integer id) {
        try {
            int deletedCount = userMatchPreferenceService.deletePreference(id);

            Map<String, Object> response = new HashMap<>();
            if (deletedCount > 0) {
                response.put("message", "匹配偏好删除成功");
            } else {
                response.put("message", "未找到要删除的匹配偏好");
            }
            response.put("deletedCount", deletedCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "删除匹配偏好失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ==================== 用户管理相关接口 ====================

    /**
     * 获取所有用户列表
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<com.it.yts_project.model.User> users = userService.getAllUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "获取用户列表成功");
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取用户列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            com.it.yts_project.model.User user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "用户不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取用户信息失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody com.it.yts_project.model.User user) {
        try {
            user.setId(id);
            com.it.yts_project.model.User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "更新用户信息失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            boolean success = userService.deleteUser(id);
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("message", "删除用户成功");
            } else {
                response.put("message", "删除用户失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "删除用户失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取用户角色列表
     */
    @GetMapping("/{id}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable Integer id) {
        try {
            List<Role> roles = roleService.findRolesByUserId(id);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取用户角色失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取管理员用户列表（用于发送反馈消息）
     */
    @GetMapping("/admins")
    public ResponseEntity<?> getAdminUsers() {
        try {
            List<com.it.yts_project.model.User> adminUsers = userService.getAdminUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "获取管理员列表成功");
            response.put("data", adminUsers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "获取管理员列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}