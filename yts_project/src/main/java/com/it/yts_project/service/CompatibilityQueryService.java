package com.it.yts_project.service;

import com.it.yts_project.dto.CompatibilityProductDTO;
import com.it.yts_project.dto.CompatibilityQueryResult;
import com.it.yts_project.mapper.CodeMappingMapper;
import com.it.yts_project.model.UnifiedCompatibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 兼容性查询服务 — Phase 1 关键词兼容性 MVP。
 *
 * <p>跨 15 组 P0 物料模块 + 1 组 lamination 特殊表做关键词模糊搜索，
 * 返回匹配物料及其兼容性数据。同时提供 production 层已验证次数的汇总统计。</p>
 *
 * <p>直接使用 {@link JdbcTemplate} 执行 SQL，避免为一次性查询创建大量 Mapper XML。</p>
 */
@Service
public class CompatibilityQueryService {

    private static final Logger log = LoggerFactory.getLogger(CompatibilityQueryService.class);

    private final JdbcTemplate jdbcTemplate;
    private final CodeMappingMapper codeMappingMapper;

    @Autowired
    public CompatibilityQueryService(JdbcTemplate jdbcTemplate, CodeMappingMapper codeMappingMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.codeMappingMapper = codeMappingMapper;
    }

    // ======================== 模块表定义 ========================

    /**
     * 单模块表元信息
     *
     * @param productTable 物料产品表名
     * @param compatTable  兼容性表名
     * @param displayName  中文模块名（展示用）
     */
    private record ModuleTable(String productTable, String compatTable, String displayName) {

        /** 返回产品表 SELECT 字段，处理不同表的列名差异 */
        String selectProductColumns() {
            if ("lamination_material_products".equals(productTable)) {
                // lamination 特殊表：usage 列名为 usage_text，无 notes 列
                return "id, material_code, material_name, category, color, usage_text AS usage, NULL AS notes";
            }
            return "id, material_code, material_name, category, color, usage, notes";
        }
    }

    /**
     * 15 组标准 P0 模块 + 1 组 lamination 特殊模块。
     * 共 16 组 product/compat 表对。
     */
    private static final List<ModuleTable> MODULES = List.of(
            // ---- 硅胶系列 ----
            new ModuleTable("silicone_coarse_uv_product",              "silicone_coarse_uv_compatibility",              "硅胶粗UV"),
            new ModuleTable("silicone_glittering_star_product",        "silicone_glittering_star_compatibility",        "硅胶闪烁星"),
            new ModuleTable("silicone_glow_ink_product",               "silicone_glow_ink_compatibility",               "硅胶发光油墨"),
            new ModuleTable("silicone_led_uv_spray_product",           "silicone_led_uv_spray_compatibility",           "硅胶LED UV喷"),
            new ModuleTable("silicone_mica_pearl_product",             "silicone_mica_pearl_compatibility",             "硅胶云母珠光"),
            new ModuleTable("silicone_orange_peel_uv_product",         "silicone_orange_peel_uv_compatibility",         "硅胶橘皮UV"),
            new ModuleTable("silicone_sandblast_uv_product",           "silicone_sandblast_uv_compatibility",           "硅胶喷砂UV"),
            new ModuleTable("silicone_screen_uv_product",              "silicone_screen_uv_compatibility",              "硅胶网印UV"),
            new ModuleTable("silicone_water_base_transparent_product", "silicone_water_base_transparent_compatibility", "硅胶水基透明"),
            new ModuleTable("silicone_watercolor_ink_product",         "silicone_watercolor_ink_compatibility",         "硅胶水彩油墨"),
            new ModuleTable("silicone_white_uv_product",               "silicone_white_uv_compatibility",               "硅胶白UV"),
            new ModuleTable("silicone_wrinkle_uv_product",             "silicone_wrinkle_uv_compatibility",             "硅胶皱纹UV"),
            // ---- UV 油 / 水性油系列 ----
            new ModuleTable("uv_oil_matte_product",                    "uv_oil_matte_compatibility",                    "UV油哑光"),
            new ModuleTable("water_varnish_matte_product",             "water_varnish_matte_compatibility",             "水性哑光油"),
            new ModuleTable("yaguang_uv_oil_product",                  "yaguang_uv_oil_compatibility",                  "压光UV油"),
            // ---- 裱纸（特殊） ----
            new ModuleTable("lamination_material_products",            "lamination_material_compatibility",             "裱纸物料")
    );

    // ======================== Public API ========================

    /**
     * 搜索关键词匹配的物料及其兼容性。
     *
     * <p>遍历所有 P0 模块表，对 {@code material_name} 和 {@code material_code}
     * 做 ILIKE 模糊搜索。每个模块最多返回 5 条匹配物料，每条物料附带其兼容性列表。</p>
     *
     * @param keywords 搜索关键词（空格不裁剪，整体模糊匹配）
     * @return {@link CompatibilityQueryResult}，无匹配时 products 为空列表
     */
    public CompatibilityQueryResult searchCompatibility(String keywords) {
        if (keywords == null || keywords.isBlank()) {
            return CompatibilityQueryResult.builder()
                    .products(List.of())
                    .productionStats(null)
                    .build();
        }

        String likePattern = "%" + keywords.trim() + "%";
        List<CompatibilityProductDTO> allProducts = new ArrayList<>();

        for (ModuleTable module : MODULES) {
            try {
                searchModule(module, likePattern, allProducts);
            } catch (Exception e) {
                log.warn("搜索模块 [{}] 异常: {}", module.displayName, e.getMessage());
            }
        }

        return CompatibilityQueryResult.builder()
                .products(allProducts)
                .productionStats(null)
                .build();
    }

    /**
     * 从 production 数据层捞取物料名称模糊匹配的已验证次数统计。
     *
     * <p>查询 {@code material_process_compatibility} 和 {@code hot_stamping_compatibility}
     * 两张生产表，按 V（兼容）/ X（不兼容）分别汇总。</p>
     *
     * @param materialName 物料名称
     * @return {@link CompatibilityQueryResult.ProductionStats}，无匹配时返回 {@code null}
     */
    public CompatibilityQueryResult.ProductionStats getProductionStats(String materialName) {
        if (materialName == null || materialName.isBlank()) {
            return null;
        }

        // Step 1: 通过 code_mapping 表精确查找 P0 material_name 对应的映射
        List<Map<String, Object>> mappingRows = jdbcTemplate.queryForList(
                "SELECT target_type, target_id, target_code FROM code_mapping WHERE p0_material_name = ?",
                materialName.trim()
        );

        if (mappingRows.isEmpty()) {
            return null;
        }

        Map<String, Object> mapping = mappingRows.get(0);
        String targetType = (String) mapping.get("target_type");

        int verified = 0;
        int notCompatible = 0;

        try {
            if ("material_catalog".equals(targetType)) {
                // 用 target_id 去查 material_process_compatibility
                Number targetId = (Number) mapping.get("target_id");
                if (targetId == null) return null;

                List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                        """
                        SELECT COUNT(*) FILTER (WHERE compatibility_status = 'V') AS v_count,
                               COUNT(*) FILTER (WHERE compatibility_status = 'X') AS x_count
                        FROM material_process_compatibility
                        WHERE material_id = ?
                        """,
                        targetId.intValue()
                );
                if (!rows.isEmpty()) {
                    Map<String, Object> row = rows.get(0);
                    verified = ((Number) row.getOrDefault("v_count", 0)).intValue();
                    notCompatible = ((Number) row.getOrDefault("x_count", 0)).intValue();
                }

            } else if ("paper_performance".equals(targetType)) {
                // 用 target_code 去查 hot_stamping_compatibility
                String targetCode = (String) mapping.get("target_code");
                if (targetCode == null || targetCode.isBlank()) return null;

                List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                        """
                        SELECT COUNT(*) FILTER (WHERE compatibility = 'V') AS v_count,
                               COUNT(*) FILTER (WHERE compatibility = 'X') AS x_count
                        FROM hot_stamping_compatibility
                        WHERE paper_performance = ?
                        """,
                        targetCode
                );
                if (!rows.isEmpty()) {
                    Map<String, Object> row = rows.get(0);
                    verified = ((Number) row.getOrDefault("v_count", 0)).intValue();
                    notCompatible = ((Number) row.getOrDefault("x_count", 0)).intValue();
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            log.warn("获取生产统计失败（materialName={}）: {}", materialName, e.getMessage());
            return null;
        }

        if (verified == 0 && notCompatible == 0) {
            return null;
        }

        var stats = new CompatibilityQueryResult.ProductionStats();
        stats.setMaterialName(materialName.trim());
        stats.setVerifiedCount(verified);
        stats.setNotCompatibleCount(notCompatible);
        stats.setTotalCount(verified + notCompatible);
        return stats;
    }

    // ======================== 统一兼容性查询 ========================

    /**
     * 统一兼容性查询 — 收敛到 material_process_compatibility 主表。
     * 此表已有 10,071 行、95 种工序、8 个工序大类，覆盖大部分场景。
     * 对于特殊表，后续通过 code_mapping 桥接。
     *
     * @param keywords 搜索关键词（物料名称或编码）
     * @return 统一兼容性记录列表
     */
    public List<UnifiedCompatibility> searchUnified(String keywords) {
        if (keywords == null || keywords.isBlank()) {
            return List.of();
        }

        String likePattern = "%" + keywords.trim() + "%";
        String sql = """
            SELECT mc.material_code, mc.material_name,
                   mpc.operation_name AS process_operation,
                   mpc.compatibility_status, mpc.condition_desc
            FROM material_process_compatibility mpc
            JOIN material_catalog mc ON mpc.material_id = mc.id
            WHERE mc.material_name ILIKE ? OR mc.material_code ILIKE ?
            LIMIT 20
            """;
        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(UnifiedCompatibility.class),
                likePattern, likePattern);
    }

    // ======================== Internal ========================

    /**
     * 在单个模块表中搜索关键词，匹配结果追加到 {@code sink}。
     * 每模块上限 5 条。
     */
    private void searchModule(ModuleTable module, String likePattern,
                              List<CompatibilityProductDTO> sink) {
        // Step 1: 搜索产品
        String productSql = "SELECT " + module.selectProductColumns()
                + " FROM " + module.productTable
                + " WHERE (material_name ILIKE ? OR material_code ILIKE ?)"
                + "   AND is_active = true"
                + " LIMIT 5";

        List<CompatibilityProductDTO> products = jdbcTemplate.query(
                productSql,
                ps -> {
                    ps.setString(1, likePattern);
                    ps.setString(2, likePattern);
                },
                (rs, rowNum) -> {
                    var dto = new CompatibilityProductDTO();
                    dto.setProductId(rs.getInt("id"));
                    dto.setMaterialCode(rs.getString("material_code"));
                    dto.setMaterialName(rs.getString("material_name"));
                    dto.setModuleName(module.displayName);
                    dto.setCategory(rs.getString("category"));
                    dto.setColor(rs.getString("color"));
                    dto.setUsage(rs.getString("usage"));
                    dto.setNotes(rs.getString("notes"));
                    return dto;
                }
        );

        if (products.isEmpty()) {
            return;
        }

        // Step 2: 为每条物料拉取其兼容性数据
        String compatSql = "SELECT post_processing_step, compatibility_status, remark"
                + " FROM " + module.compatTable
                + " WHERE product_id = ?"
                + " ORDER BY display_order NULLS LAST";

        for (CompatibilityProductDTO product : products) {
            List<CompatibilityProductDTO.CompatibilityItem> compats = jdbcTemplate.query(
                    compatSql,
                    ps -> ps.setInt(1, product.getProductId()),
                    (rs, rowNum) -> {
                        var item = new CompatibilityProductDTO.CompatibilityItem();
                        item.setPostProcessingStep(rs.getString("post_processing_step"));
                        item.setCompatibilityStatus(rs.getString("compatibility_status"));
                        item.setRemark(rs.getString("remark"));
                        return item;
                    }
            );
            product.setCompatibilities(compats);
        }

        sink.addAll(products);
    }
}
