package com.it.yts_project.controller;

import com.it.yts_project.model.SysAnnouncement;
import com.it.yts_project.service.SysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统消息控制器（JeecgBoot 标准）
 */
@RestController
@RequestMapping("/sys/announcement")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SysAnnouncementController {

    @Autowired
    private SysAnnouncementService announcementService;

    /**
     * 创建消息（草稿）
     */
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody SysAnnouncement announcement) {
        try {
            announcementService.save(announcement);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "添加成功");
            response.put("result", announcement);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "添加失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 更新消息（仅草稿）
     */
    @PutMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody SysAnnouncement announcement) {
        try {
            announcementService.update(announcement);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除消息（仅草稿或已撤回）
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String id) {
        try {
            announcementService.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据ID查询消息
     */
    @GetMapping("/queryById")
    public ResponseEntity<?> queryById(@RequestParam String id) {
        try {
            SysAnnouncement announcement = announcementService.getById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", announcement);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 查询消息列表（管理员）
     */
    @GetMapping("/list")
    public ResponseEntity<?> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String msgCategory,
            @RequestParam(required = false) Integer sendStatus,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            Map<String, Object> result = announcementService.list(title, msgCategory, sendStatus, pageNo, pageSize);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 发布消息
     */
    @PostMapping("/doRelease")
    public ResponseEntity<?> doRelease(@RequestBody Map<String, String> params) {
        try {
            String id = params.get("id");
            Map<String, Object> result = announcementService.doRelease(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "发布成功");
            response.put("result", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "发布失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 撤回消息
     */
    @PostMapping("/doRevoke")
    public ResponseEntity<?> doRevoke(@RequestBody Map<String, String> params) {
        try {
            String id = params.get("id");
            announcementService.doRevoke(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "撤回成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "撤回失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 查询用户消息列表
     */
    @GetMapping("/listByUser")
    public ResponseEntity<?> listByUser(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String msgCategory,
            @RequestParam(required = false) Integer readFlag,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam Integer userId) {
        try {
            Map<String, Object> result = announcementService.listByUser(userId, msgCategory, readFlag, startTime, endTime, pageNo, pageSize);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取用户未读消息数量
     */
    @GetMapping("/unread/count")
    public ResponseEntity<?> getUnreadCount(@RequestParam Integer userId) {
        try {
            Integer count = announcementService.getUnreadCount(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            Map<String, Integer> result = new HashMap<>();
            result.put("unreadCount", count);
            response.put("result", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 标记消息为已读
     */
    @PutMapping("/read/{id}")
    public ResponseEntity<?> read(@PathVariable String id, @RequestParam Integer userId) {
        try {
            announcementService.read(id, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "标记成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "标记失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 批量标记为已读
     */
    @PutMapping("/batchRead")
    public ResponseEntity<?> batchRead(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            Integer userId = (Integer) params.get("userId");
            announcementService.batchRead(ids, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "批量标记成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量标记失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 用户删除消息
     */
    @DeleteMapping("/deleteByUser")
    public ResponseEntity<?> deleteByUser(@RequestParam String id, @RequestParam Integer userId) {
        try {
            announcementService.deleteByUser(id, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

