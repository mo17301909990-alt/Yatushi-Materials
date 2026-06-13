package com.it.yts_project.controller;

import com.it.yts_project.dto.*;
import com.it.yts_project.model.RgTaskDefinition;
import com.it.yts_project.service.ResourceGroupSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源组选择控制器
 */
@Slf4j
@RestController
@RequestMapping("/resource-group-select")
@RequiredArgsConstructor
public class ResourceGroupSelectController {
    
    private final ResourceGroupSelectService resourceGroupSelectService;
    
    /**
     * 获取所有任务定义
     */
    @GetMapping("/tasks")
    public ResponseEntity<Map<String, Object>> getTasks() {
        log.info("获取任务定义列表");
        List<RgTaskDefinition> tasks = resourceGroupSelectService.getAllTasks();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", tasks);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取任务需要的参数（判断是否需要填写尺寸、印张石数等）
     */
    @GetMapping("/task-params/{taskCode}")
    public ResponseEntity<Map<String, Object>> getTaskParams(@PathVariable String taskCode) {
        log.info("获取任务参数需求, 任务: {}", taskCode);
        
        Map<String, Boolean> params = resourceGroupSelectService.getTaskRequiredParams(taskCode);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", params);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 筛选资源组
     */
    @PostMapping("/select")
    public ResponseEntity<Map<String, Object>> selectResourceGroups(
            @RequestBody ResourceGroupSelectRequest request) {
        log.info("筛选资源组, 任务: {}, 参数: 石数={}, 厚度={}, 克重={}, 尺寸={}x{}", 
            request.getTaskCode(), 
            request.getSheetCount(),
            request.getThickness(),
            request.getGsm(),
            request.getWidth(),
            request.getHeight());
        
        ResourceGroupSelectResponse response = resourceGroupSelectService.selectResourceGroups(request);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取工作中心详情及该工作中心下的所有资源组
     */
    @GetMapping("/work-center/{workCenterCode}")
    public ResponseEntity<Map<String, Object>> getWorkCenterDetail(@PathVariable String workCenterCode) {
        log.info("获取工作中心详情, 工作中心: {}", workCenterCode);
        
        Map<String, Object> data = resourceGroupSelectService.getWorkCenterDetail(workCenterCode);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取公共参数的下拉选项（从规则中推断）
     */
    @GetMapping("/param-options")
    public ResponseEntity<Map<String, Object>> getParamOptions() {
        log.info("获取公共参数下拉选项");
        
        Map<String, java.util.List<String>> options = resourceGroupSelectService.getParamOptions();
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", options);
        return ResponseEntity.ok(result);
    }
}

