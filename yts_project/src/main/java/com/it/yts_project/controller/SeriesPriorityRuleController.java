package com.it.yts_project.controller;

import com.it.yts_project.dto.SeriesPriorityRuleDTO;
import com.it.yts_project.dto.SeriesPriorityRuleItemDTO;
import com.it.yts_project.service.SeriesPriorityRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系列优先级规则 - 后台可灵活配置任意规则与系列名，任意适用界面可绑定任意规则
 */
@RestController
@RequestMapping(value = { "/api/series-priority-rules", "/series-priority-rules" })
public class SeriesPriorityRuleController {

    private static final Logger log = LoggerFactory.getLogger(SeriesPriorityRuleController.class);

    @Autowired
    private SeriesPriorityRuleService seriesPriorityRuleService;

    /** 列表所有规则 */
    @GetMapping
    public ResponseEntity<?> list() {
        try {
            List<SeriesPriorityRuleDTO> rules = seriesPriorityRuleService.listRules();
            return ResponseEntity.ok(rules);
        } catch (Exception e) {
            log.error("获取系列优先级规则列表失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取规则列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /** 获取规则详情（含档位列表，用于编辑） */
    @GetMapping("/{id}")
    public ResponseEntity<?> getWithItems(@PathVariable Integer id) {
        try {
            SeriesPriorityRuleDTO dto = seriesPriorityRuleService.getRuleWithItems(id);
            return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("获取系列优先级规则详情失败, id: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取规则详情失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /** 新建规则，body: { "ruleName": "规则名称", "sortOrder": 0, "isActive": true }，默认三档可后续在「配置档位」中修改 */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SeriesPriorityRuleDTO body) {
        try {
            SeriesPriorityRuleDTO created = seriesPriorityRuleService.createRule(
                body.getRuleName(), body.getSortOrder(), body.getIsActive());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("创建系列优先级规则失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建规则失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /** 更新规则名称/排序/状态，body: { "ruleName", "sortOrder", "isActive" } */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody SeriesPriorityRuleDTO body) {
        try {
            seriesPriorityRuleService.updateRule(id, body.getRuleName(), body.getSortOrder(), body.getIsActive());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("更新系列优先级规则失败, id: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新规则失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /** 删除规则（会解除所有引用该规则的适用界面绑定） */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            seriesPriorityRuleService.deleteRule(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("删除系列优先级规则失败, id: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除规则失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /** 保存规则档位（全量覆盖）：body 为档位列表，系列名可任意填写，必须且仅有一档 isPriceFallback: true */
    @PutMapping("/{id}/items")
    public ResponseEntity<?> saveItems(@PathVariable Integer id, @RequestBody List<SeriesPriorityRuleItemDTO> items) {
        try {
            seriesPriorityRuleService.saveRuleItems(id, items);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warn("保存规则档位失败, id: {}, 原因: {}", id, e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("保存规则档位失败, id: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "保存档位失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
