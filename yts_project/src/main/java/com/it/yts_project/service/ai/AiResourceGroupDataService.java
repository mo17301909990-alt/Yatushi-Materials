package com.it.yts_project.service.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AiResourceGroupDataService {

    private static final Logger log = LoggerFactory.getLogger(AiResourceGroupDataService.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AiResourceGroupDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 生成资源组数据上下文文本，供 AI 注入。
     * 包含工序大类、资源组、选择规则、设备资源。
     */
    public String buildResourceGroupContext() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("【资源组数据 — 设备产能与选择规则】\n");
            sb.append("以下数据来自生产系统的 resource_group 模块，包含各工序可用的资源组、设备规格、产能参数、选择规则。\n\n");

            // 1. 工序大类
            List<Map<String, Object>> cats = jdbcTemplate.queryForList(
                    "SELECT code, name FROM staging_process_categories ORDER BY code"
            );
            sb.append("工序大类共 ").append(cats.size()).append(" 种：\n");
            for (Map<String, Object> c : cats) {
                sb.append("- ").append(c.get("code")).append(": ").append(c.get("name")).append("\n");
            }
            sb.append("\n");

            // 2. 资源组（按工序分类）
            List<Map<String, Object>> rgs = jdbcTemplate.queryForList(
                    "SELECT code, name, process_category_code, difficulty, notes " +
                    "FROM production_resource_groups ORDER BY process_category_code, code"
            );
            sb.append("资源组共 ").append(rgs.size()).append(" 个：\n");
            String lastCat = "";
            for (Map<String, Object> rg : rgs) {
                String cat = (String) rg.get("process_category_code");
                if (cat != null && !cat.equals(lastCat)) {
                    sb.append("\n【").append(cat).append("】\n");
                    lastCat = cat;
                }
                sb.append("- ").append(rg.get("code")).append(" ").append(rg.get("name"));
                Object diff = rg.get("difficulty");
                if (diff != null && !((String) diff).isBlank()) {
                    sb.append(" (").append(diff).append(")");
                }
                Object notes = rg.get("notes");
                if (notes != null && !((String) notes).isBlank()) {
                    String n = ((String) notes).replace('\n', ' ').replace('\r', ' ');
                    if (n.length() > 120) n = n.substring(0, 120) + "...";
                    sb.append(" — ").append(n);
                }
                sb.append("\n");
            }
            sb.append("\n");

            // 3. 规则
            List<Map<String, Object>> rules = jdbcTemplate.queryForList(
                    "SELECT r.resource_group_code, rg.name AS rg_name, r.rule_content, r.difficulty, r.source_file " +
                    "FROM production_rules r " +
                    "LEFT JOIN production_resource_groups rg ON r.resource_group_code = rg.code " +
                    "ORDER BY r.resource_group_code"
            );
            sb.append("选择规则共 ").append(rules.size()).append(" 条：\n");
            for (Map<String, Object> rule : rules) {
                sb.append("- ").append(rule.get("resource_group_code"))
                  .append(" ").append(rule.get("rg_name")).append("\n");
                String content = (String) rule.get("rule_content");
                if (content != null) {
                    // 截取前 3 行
                    String[] lines = content.split("\\n");
                    for (int i = 0; i < Math.min(lines.length, 4); i++) {
                        sb.append("  ").append(lines[i]).append("\n");
                    }
                    if (lines.length > 4) sb.append("  ...（共").append(lines.length).append("行）\n");
                }
                sb.append("\n");
            }

            // 4. 产能数据（精简：每 RG 的难度级和产能）
            List<Map<String, Object>> caps = jdbcTemplate.queryForList(
                    "SELECT resource_group_code, difficulty_level, capacity_per_hour, capacity_per_shift " +
                    "FROM production_capacity ORDER BY resource_group_code, difficulty_level"
            );
            sb.append("\n产能数据共 ").append(caps.size()).append(" 条（按 易/中/难/很难 分级）：\n");
            String lastRg = "";
            for (Map<String, Object> cap : caps) {
                String rgc = (String) cap.get("resource_group_code");
                if (!rgc.equals(lastRg)) {
                    sb.append(rgc).append(" ");
                    lastRg = rgc;
                } else {
                    sb.append("  ");
                }
                sb.append(cap.get("difficulty_level"))
                  .append(": 时=").append(cap.get("capacity_per_hour"))
                  .append(" 班=").append(cap.get("capacity_per_shift")).append("\n");
            }

            return sb.toString();
        } catch (Exception e) {
            log.warn("构建资源组上下文失败: {}", e.getMessage());
            return "(读取资源组数据失败: " + e.getMessage() + ")\n";
        }
    }
}
