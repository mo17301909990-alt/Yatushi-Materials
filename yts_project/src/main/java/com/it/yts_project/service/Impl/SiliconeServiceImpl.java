package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeProductDTO;
import com.it.yts_project.mapper.SiliconeMapper;
import com.it.yts_project.service.SiliconeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 硅胶组合匹配 Service 实现
 */
@Service
@Transactional
public class SiliconeServiceImpl implements SiliconeService {

    private static final Set<String> VALID_TYPES = Set.of(
            "white_uv", "glow_ink", "glittering_star", "screen_uv",
            "led_uv_spray", "water_base_transparent", "coarse_uv",
            "orange_peel_uv", "sandblast_uv", "wrinkle_uv",
            "watercolor_ink", "mica_pearl"
    );

    /** 硅胶类型中文标签映射 */
    private static final Map<String, String> TYPE_LABELS = new LinkedHashMap<>();

    static {
        TYPE_LABELS.put("glow_ink", "发光油墨");
        TYPE_LABELS.put("white_uv", "白UV");
        TYPE_LABELS.put("screen_uv", "丝印UV");
        TYPE_LABELS.put("coarse_uv", "粗纹UV");
        TYPE_LABELS.put("orange_peel_uv", "橘皮UV");
        TYPE_LABELS.put("sandblast_uv", "喷砂UV");
        TYPE_LABELS.put("wrinkle_uv", "皱纹UV");
        TYPE_LABELS.put("watercolor_ink", "水彩油墨");
        TYPE_LABELS.put("mica_pearl", "云母珍珠");
        TYPE_LABELS.put("glittering_star", "闪烁星");
        TYPE_LABELS.put("led_uv_spray", "LED UV喷");
        TYPE_LABELS.put("water_base_transparent", "水性透明");
    }

    @Autowired
    private SiliconeMapper siliconeMapper;

    @Override
    public PagedResult<SiliconeProductDTO> match(List<String> types, String keyword, List<String> steps, int page, int size) {
        // 类型校验
        List<String> validatedTypes = validateAndFilterTypes(types);
        if (validatedTypes.isEmpty()) {
            return new PagedResult<>(Collections.emptyList(), 0L, size, page);
        }

        boolean hasSteps = steps != null && !steps.isEmpty();

        if (hasSteps) {
            return matchMultiStep(validatedTypes, keyword, steps, page, size);
        } else {
            return matchSingleStep(validatedTypes, keyword, null, page, size);
        }
    }

    /**
     * 单步骤 / 无步骤匹配
     */
    private PagedResult<SiliconeProductDTO> matchSingleStep(List<String> types, String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<Map<String, Object>> rows = siliconeMapper.searchProductsWithStep(types, keyword, stepName, offset, size);
        Long total = siliconeMapper.countProductsWithStep(types, keyword, stepName);

        List<SiliconeProductDTO> dtos = rows.stream()
                .map(this::rowToDTO)
                .collect(Collectors.toList());

        return new PagedResult<>(dtos, total, size, page);
    }

    /**
     * 多步骤 INTERSECT 匹配
     */
    private PagedResult<SiliconeProductDTO> matchMultiStep(List<String> types, String keyword, List<String> steps, int page, int size) {
        if (steps.size() == 1) {
            // 单步骤走简单路径
            PagedResult<SiliconeProductDTO> result = matchSingleStep(types, keyword, steps.get(0), page, size);
            String singleStep = steps.get(0);
            for (SiliconeProductDTO dto : result.getItems()) {
                Map<String, String> map = new LinkedHashMap<>();
                map.put(singleStep, dto.getCompatibilityStatus());
                dto.setCompatibilityStatusMap(map);
            }
            return result;
        }

        int offset = (page - 1) * size;
        int stepCount = steps.size();

        // 1. 获取匹配所有步骤的产品 (id, silicone_type)
        List<Map<String, Object>> productRefs = siliconeMapper.searchMultiStepProductIds(types, keyword, steps, stepCount, offset, size);
        Long total = siliconeMapper.countMultiStepProducts(types, keyword, steps, stepCount);

        if (CollectionUtils.isEmpty(productRefs)) {
            return new PagedResult<>(Collections.emptyList(), total, size, page);
        }

        // 2. 逐条查询完整产品信息（按类型分组查询避免跨表）
        List<SiliconeProductDTO> dtos = new ArrayList<>();
        Map<String, List<Integer>> typeToIds = new LinkedHashMap<>();

        for (Map<String, Object> ref : productRefs) {
            Integer id = (Integer) ref.get("id");
            String siliconeType = (String) ref.get("silicone_type");
            typeToIds.computeIfAbsent(siliconeType, k -> new ArrayList<>()).add(id);
        }

        // 3. 构建 (type_id -> silicone_type) 映射 & (type_id -> DTO) 映射
        Map<String, Map<Integer, SiliconeProductDTO>> typeProductMap = new LinkedHashMap<>();

        for (Map.Entry<String, List<Integer>> entry : typeToIds.entrySet()) {
            String siliconeType = entry.getKey();
            List<Integer> ids = entry.getValue();
            Map<Integer, SiliconeProductDTO> productMap = new LinkedHashMap<>();

            for (Integer id : ids) {
                Map<String, Object> productRow = siliconeMapper.findProductByIdAndType(id, siliconeType);
                if (productRow != null) {
                    SiliconeProductDTO dto = rowToDTO(productRow);
                    dto.setSiliconeType(siliconeType);
                    dto.setSiliconeTypeLabel(TYPE_LABELS.getOrDefault(siliconeType, siliconeType));
                    productMap.put(id, dto);
                }
            }
            typeProductMap.put(siliconeType, productMap);
        }

        // 4. 查询兼容性状态
        Map<Integer, SiliconeProductDTO> allDtos = new LinkedHashMap<>();
        for (Map.Entry<String, List<Integer>> entry : typeToIds.entrySet()) {
            String siliconeType = entry.getKey();
            List<Integer> ids = entry.getValue();

            List<Map<String, Object>> statusRows = siliconeMapper.findCompatibilityStatusForProducts(ids, steps, siliconeType);

            Map<Integer, Map<String, String>> statusMap = new LinkedHashMap<>();
            for (Map<String, Object> row : statusRows) {
                Integer pid = (Integer) row.get("product_id");
                String step = (String) row.get("post_processing_step");
                String status = (String) row.get("compatibility_status");
                statusMap.computeIfAbsent(pid, k -> new LinkedHashMap<>()).put(step, status);
            }

            Map<Integer, SiliconeProductDTO> productMap = typeProductMap.get(siliconeType);
            if (productMap != null) {
                for (Integer id : ids) {
                    SiliconeProductDTO dto = productMap.get(id);
                    if (dto != null) {
                        dto.setCompatibilityStatusMap(statusMap.getOrDefault(id, Collections.emptyMap()));
                        allDtos.put(id, dto);
                    }
                }
            }
        }

        // 5. 按原始顺序输出
        for (Map<String, Object> ref : productRefs) {
            Integer id = (Integer) ref.get("id");
            SiliconeProductDTO dto = allDtos.get(id);
            if (dto != null && !dtos.contains(dto)) {
                dtos.add(dto);
            }
        }

        return new PagedResult<>(dtos, total, size, page);
    }

    @Override
    public List<Map<String, Object>> getSteps(List<String> types) {
        List<String> validatedTypes = validateAndFilterTypes(types);
        if (validatedTypes.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> dbPairs = siliconeMapper.getDistinctStepsWithCategory(validatedTypes);
        // 按 category 分组
        Map<String, List<String>> grouped = new LinkedHashMap<>();
        for (Map<String, Object> pair : dbPairs) {
            String category = (String) pair.get("category");
            String step = (String) pair.get("step");
            if (category != null && step != null) {
                grouped.computeIfAbsent(category, k -> new ArrayList<>()).add(step);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : grouped.entrySet()) {
            Map<String, Object> group = new LinkedHashMap<>();
            group.put("category", entry.getKey());
            group.put("steps", entry.getValue());
            result.add(group);
        }
        return result;
    }

    @Override
    public SiliconeProductDTO getProductDetail(Integer id, String type) {
        String validatedType = validateSingleType(type);
        if (validatedType == null) {
            return null;
        }
        Map<String, Object> productRow = siliconeMapper.findProductByIdAndType(id, validatedType);
        if (productRow == null) {
            return null;
        }
        SiliconeProductDTO dto = rowToDTO(productRow);
        dto.setSiliconeType(validatedType);
        dto.setSiliconeTypeLabel(TYPE_LABELS.getOrDefault(validatedType, validatedType));

        List<Map<String, Object>> compatibilities = siliconeMapper.findCompatibilitiesByProductIdAndType(id, validatedType);
        dto.setCompatibilities(compatibilities);
        return dto;
    }

    // ========== 工具方法 ==========

    /**
     * 将数据库行 Map 转换为 SiliconeProductDTO
     */
    @SuppressWarnings("unchecked")
    private SiliconeProductDTO rowToDTO(Map<String, Object> row) {
        SiliconeProductDTO dto = new SiliconeProductDTO();
        dto.setId(toInt(row.get("id")));
        dto.setMaterialCode(str(row.get("material_code")));
        dto.setSupplierCode(str(row.get("supplier_code")));
        dto.setStockCode(str(row.get("stock_code")));
        dto.setMaterialName(str(row.get("material_name")));
        dto.setUsage(str(row.get("usage")));
        dto.setCategory(str(row.get("category")));
        dto.setColor(str(row.get("color")));
        dto.setThickness(str(row.get("thickness")));
        dto.setShape(str(row.get("shape")));
        dto.setTester(str(row.get("tester")));
        dto.setResponsiblePerson(str(row.get("responsible_person")));
        dto.setMinSheetSize(str(row.get("min_sheet_size")));
        dto.setMaxSheetSize(str(row.get("max_sheet_size")));
        dto.setMinSpacing(str(row.get("min_spacing")));
        dto.setMaxSpacing(str(row.get("max_spacing")));
        dto.setDesignInfo(str(row.get("design_info")));
        dto.setApplicableInterface(str(row.get("applicable_interface")));
        dto.setNotes(str(row.get("notes")));
        dto.setIsActive(bool(row.get("is_active")));
        dto.setCreatedAt(toLocalDateTime(row.get("created_at")));
        dto.setUpdatedAt(toLocalDateTime(row.get("updated_at")));
        dto.setSiliconeType(str(row.get("silicone_type")));
        dto.setSiliconeTypeLabel(TYPE_LABELS.getOrDefault(str(row.get("silicone_type")), str(row.get("silicone_type"))));
        dto.setCompatibilityStatus(str(row.get("compatibility_status")));
        return dto;
    }

    /**
     * 校验并过滤类型列表，只保留合法类型
     */
    private List<String> validateAndFilterTypes(List<String> types) {
        if (types == null || types.isEmpty()) {
            return new ArrayList<>(VALID_TYPES); // 默认查全部
        }
        return types.stream()
                .filter(VALID_TYPES::contains)
                .collect(Collectors.toList());
    }

    /**
     * 校验单个类型，返回合法类型名
     */
    private String validateSingleType(String type) {
        if (type == null || !VALID_TYPES.contains(type)) {
            return null;
        }
        return type;
    }

    // ========== 类型转换工具 ==========

    private String str(Object v) {
        return v != null ? v.toString() : null;
    }

    private Integer toInt(Object v) {
        if (v instanceof Number) return ((Number) v).intValue();
        return null;
    }

    private Boolean bool(Object v) {
        if (v instanceof Boolean) return (Boolean) v;
        if (v instanceof Number) return ((Number) v).intValue() == 1;
        return null;
    }

    private LocalDateTime toLocalDateTime(Object v) {
        if (v instanceof LocalDateTime) return (LocalDateTime) v;
        if (v instanceof java.sql.Timestamp) return ((java.sql.Timestamp) v).toLocalDateTime();
        return null;
    }
}
