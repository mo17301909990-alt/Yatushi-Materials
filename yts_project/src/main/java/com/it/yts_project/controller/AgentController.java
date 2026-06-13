package com.it.yts_project.controller;

import com.it.yts_project.agent.GoldFoilAgent;
import com.it.yts_project.dto.*;
import com.it.yts_project.service.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = { "/api/agent", "/agent" })
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class AgentController {

    private static final Logger log = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private AgentService agentService;

    @Autowired
    private GoldFoilAgent goldFoilAgent;

    /** 获取工艺 Agent 完整配置 */
    @GetMapping("/config")
    public ResponseEntity<AgentConfigDTO> getConfig() {
        return ResponseEntity.ok(agentService.getAgentConfig());
    }

    /** 获取所有材料分类（含标准书列表） */
    @GetMapping("/categories")
    public ResponseEntity<List<MaterialCategoryDTO>> getCategories() {
        return ResponseEntity.ok(agentService.getCategories());
    }

    /** 获取指定分类的标准书列表 */
    @GetMapping("/category/{categoryId}/standards")
    public ResponseEntity<List<MaterialStandardDTO>> getStandardsByCategory(@PathVariable String categoryId) {
        List<MaterialStandardDTO> standards = agentService.getStandardsByCategory(categoryId);
        return ResponseEntity.ok(standards);
    }

    /** 刷新标准书缓存（从文件系统重新扫描） */
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh() {
        agentService.refreshStandards();
        return ResponseEntity.ok("ok");
    }

    /**
     * Agent 查询对话入口（仅读操作）。
     * <p>接收用户自然语言查询，经过 detect→route→execute→build 流程返回结构化回复。</p>
     */
    @PostMapping("/query")
    public ResponseEntity<AgentResponse> query(@RequestBody AgentQueryRequest request) {
        if (request == null || request.getQuery() == null || request.getQuery().isBlank()) {
            return ResponseEntity.badRequest().body(
                new AgentResponse("help", null, null,
                    List.of("搜索产品", "检查兼容性", "查看帮助"), "请输入查询内容。")
            );
        }
        try {
            AgentResponse response = goldFoilAgent.agentRun(request.getQuery().trim());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Agent 查询失败", e);
            return ResponseEntity.status(500).body(
                new AgentResponse("error", null, null,
                    List.of("重新尝试", "查看帮助"), "查询处理失败: " + e.getMessage())
            );
        }
    }
}
