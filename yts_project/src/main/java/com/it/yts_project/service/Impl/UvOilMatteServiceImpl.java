package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.StepCategoryGroup;
import com.it.yts_project.dto.UvOilMatteProductDTO;
import com.it.yts_project.mapper.UvOilMatteMapper;
import com.it.yts_project.model.UvOilMatteProduct;
import com.it.yts_project.model.UvOilMatteCompatibility;
import com.it.yts_project.service.UvOilMatteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * UV油_哑光UV油 Service实现类
 */
@Service
@Transactional
public class UvOilMatteServiceImpl implements UvOilMatteService {

    @Autowired
    private UvOilMatteMapper uvOilMatteMapper;

    // ========== 产品管理 ==========

    @Override
    public List<UvOilMatteProduct> getAllProducts() {
        return uvOilMatteMapper.findAllProducts();
    }

    @Override
    public UvOilMatteProduct getProductById(Integer id) {
        return uvOilMatteMapper.findProductById(id);
    }

    @Override
    public List<UvOilMatteProduct> searchProducts(String keyword) {
        return uvOilMatteMapper.searchProducts(keyword);
    }

    @Override
    public UvOilMatteProduct saveProduct(UvOilMatteProduct product) {
        if (product.getId() == null) {
            uvOilMatteMapper.insertProduct(product);
        } else {
            uvOilMatteMapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        uvOilMatteMapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<UvOilMatteCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return uvOilMatteMapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public UvOilMatteCompatibility getCompatibilityById(Integer id) {
        return uvOilMatteMapper.findCompatibilityById(id);
    }

    @Override
    public UvOilMatteCompatibility saveCompatibility(UvOilMatteCompatibility compatibility) {
        // 验证产品是否存在
        if (compatibility.getProductId() == null) {
            throw new IllegalArgumentException("产品ID不能为空");
        }

        UvOilMatteProduct product = uvOilMatteMapper.findProductById(compatibility.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
        }

        if (compatibility.getId() == null) {
            // 新增：检查唯一键
            UvOilMatteCompatibility existing = uvOilMatteMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            }
            uvOilMatteMapper.insertCompatibility(compatibility);
        } else {
            // 更新：检查唯一键(排除自身)
            UvOilMatteCompatibility existing = uvOilMatteMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null && !existing.getId().equals(compatibility.getId())) {
                throw new IllegalArgumentException("该产品与后加工工序的兼容性配置已存在");
            }
            uvOilMatteMapper.updateCompatibility(compatibility);
        }

        // 回填产品名称
        compatibility.setProductName(product.getMaterialName());
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) {
        uvOilMatteMapper.deleteCompatibilityById(id);
    }

    @Override
    public void batchSaveCompatibilities(List<UvOilMatteCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }

        for (UvOilMatteCompatibility compatibility : compatibilities) {
            // 验证产品ID
            if (compatibility.getProductId() == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            UvOilMatteProduct product = uvOilMatteMapper.findProductById(compatibility.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("产品ID " + compatibility.getProductId() + " 不存在");
            }

            // 检查是否已存在
            UvOilMatteCompatibility existing = uvOilMatteMapper.findCompatibilityByUniqueKey(
                compatibility.getProductId(), compatibility.getPostProcessingStep());
            if (existing != null) {
                // 已存在则更新
                compatibility.setId(existing.getId());
                uvOilMatteMapper.updateCompatibility(compatibility);
            } else {
                // 不存在则插入
                uvOilMatteMapper.insertCompatibility(compatibility);
            }
        }
    }

    // ========== 匹配查询 ==========

    @Override
    public PagedResult<UvOilMatteProductDTO> searchProducts(String keyword, String stepName, int page, int size) {
        int offset = (page - 1) * size;
        List<UvOilMatteProductDTO> items = uvOilMatteMapper.searchProductsWithStep(keyword, stepName, offset, size);
        Long total = uvOilMatteMapper.countProductsWithStep(keyword, stepName);
        return new PagedResult<>(items, total, size, page);
    }

    @Override
    public PagedResult<UvOilMatteProductDTO> searchProductsMultiStep(String keyword, List<String> steps, int page, int size) {
        if (CollectionUtils.isEmpty(steps)) {
            return searchProducts(keyword, null, page, size);
        }
        if (steps.size() == 1) {
            // 单步骤走原有逻辑，但填充 compatibilityStatusMap 以便前端统一使用
            PagedResult<UvOilMatteProductDTO> result = searchProducts(keyword, steps.get(0), page, size);
            String singleStep = steps.get(0);
            for (UvOilMatteProductDTO dto : result.getItems()) {
                Map<String, String> map = new LinkedHashMap<>();
                map.put(singleStep, dto.getCompatibilityStatus());
                dto.setCompatibilityStatusMap(map);
            }
            return result;
        }

        int offset = (page - 1) * size;
        int stepCount = steps.size();

        // 1. 获取匹配所有步骤的产品 ID（分页）
        List<Integer> productIds = uvOilMatteMapper.searchMultiStepProductIds(keyword, steps, stepCount, offset, size);
        Long total = uvOilMatteMapper.countMultiStepProducts(keyword, steps, stepCount);

        if (CollectionUtils.isEmpty(productIds)) {
            return new PagedResult<>(Collections.emptyList(), total, size, page);
        }

        // 2. 查询完整产品信息
        List<UvOilMatteProduct> products = uvOilMatteMapper.findProductsByIds(productIds);
        List<UvOilMatteProductDTO> dtos = products.stream()
                .map(UvOilMatteProductDTO::fromProduct)
                .collect(Collectors.toList());

        // 3. 查询这些产品在选定步骤上的兼容性状态
        List<Map<String, Object>> statusRows = uvOilMatteMapper.findCompatibilityStatusForProducts(productIds, steps);

        // 4. 构建 productId -> (step -> status) 映射
        Map<Integer, Map<String, String>> statusMap = new LinkedHashMap<>();
        for (Map<String, Object> row : statusRows) {
            Integer pid = (Integer) row.get("product_id");
            String step = (String) row.get("post_processing_step");
            String status = (String) row.get("compatibility_status");
            statusMap.computeIfAbsent(pid, k -> new LinkedHashMap<>()).put(step, status);
        }

        // 5. 将状态映射注入 DTO
        for (UvOilMatteProductDTO dto : dtos) {
            Map<String, String> map = statusMap.getOrDefault(dto.getId(), Collections.emptyMap());
            dto.setCompatibilityStatusMap(map);
        }

        return new PagedResult<>(dtos, total, size, page);
    }

    @Override
    public List<StepCategoryGroup> getDistinctSteps() {
        List<String> rawSteps = uvOilMatteMapper.getDistinctPostProcessingSteps();
        return groupStepsByCategory(rawSteps);
    }

    @Override
    public List<Map<String, Object>> getSteps() {
        List<Map<String, Object>> dbPairs = uvOilMatteMapper.getDistinctStepsWithCategory();
        // category -> [steps]
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

    /**
     * 将扁平的步骤列表按大类分组。
     * <p>
     * 优先从步骤名中的 "category/step" 前缀解析分类；
     * 如果步骤名无前缀，则使用预定义映射 {@link #STEP_TO_CATEGORY} 推断分类。
     * 无法推断分类的步骤归入"其他"类别。
     */
    private List<StepCategoryGroup> groupStepsByCategory(List<String> rawSteps) {
        // category -> steps
        Map<String, Set<String>> grouped = new LinkedHashMap<>();

        // 初始化有序类别
        String[] CATEGORY_ORDER = {"印刷", "燙金", "過膠", "絲印", "植毛", "啤", "手工", "其他"};
        for (String cat : CATEGORY_ORDER) {
            grouped.put(cat, new LinkedHashSet<>());
        }

        for (String step : rawSteps) {
            if (step == null || step.isBlank()) continue;

            String category;
            String stepName;

            // 检查是否已包含 "category/step" 前缀
            int slashIdx = step.indexOf('/');
            if (slashIdx > 0 && step.length() > slashIdx + 1) {
                category = step.substring(0, slashIdx).trim();
                stepName = step.substring(slashIdx + 1).trim();
            } else {
                stepName = step;
                category = STEP_TO_CATEGORY.getOrDefault(step, "其他");
            }

            // 确保分类存在
            grouped.putIfAbsent(category, new LinkedHashSet<>());
            grouped.get(category).add(stepName);
        }

        // 转为有序列表，移除空分类
        return grouped.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(e -> new StepCategoryGroup(e.getKey(), new ArrayList<>(e.getValue())))
                .collect(Collectors.toList());
    }

    /** 步骤名到分类的预定义映射（xlsx 8 大类约 77 步） */
    private static final Map<String, String> STEP_TO_CATEGORY = new HashMap<>();

    static {
        // ===== 印刷（13 步） =====
        STEP_TO_CATEGORY.put("單面印刷", "印刷");
        STEP_TO_CATEGORY.put("雙面印刷", "印刷");
        STEP_TO_CATEGORY.put("連線印刷", "印刷");
        STEP_TO_CATEGORY.put("離線印刷", "印刷");
        STEP_TO_CATEGORY.put("油性油", "印刷");
        STEP_TO_CATEGORY.put("水性油", "印刷");
        STEP_TO_CATEGORY.put("UV油", "印刷");
        STEP_TO_CATEGORY.put("掃金粉", "印刷");
        STEP_TO_CATEGORY.put("逆向油", "印刷");
        STEP_TO_CATEGORY.put("冷燙", "印刷");
        STEP_TO_CATEGORY.put("仿皮革", "印刷");
        STEP_TO_CATEGORY.put("凸字粉", "印刷");
        STEP_TO_CATEGORY.put("凸字粉溝閃粉", "印刷");

        // ===== 烫金（13 步） =====
        STEP_TO_CATEGORY.put("普通燙金", "燙金");
        STEP_TO_CATEGORY.put("鐳射燙金", "燙金");
        STEP_TO_CATEGORY.put("熒光燙金", "燙金");
        STEP_TO_CATEGORY.put("折光紋燙金", "燙金");
        STEP_TO_CATEGORY.put("立體燙金", "燙金");
        STEP_TO_CATEGORY.put("立體燙金擊凸", "燙金");
        STEP_TO_CATEGORY.put("擊凸", "燙金");
        STEP_TO_CATEGORY.put("擊凹", "燙金");
        STEP_TO_CATEGORY.put("立體擊凸", "燙金");
        STEP_TO_CATEGORY.put("深層擊凸", "燙金");
        STEP_TO_CATEGORY.put("超級深層擊凸", "燙金");
        STEP_TO_CATEGORY.put("擊紋", "燙金");
        STEP_TO_CATEGORY.put("熱辣", "燙金");

        // ===== 过胶（4 步） =====
        STEP_TO_CATEGORY.put("單面過膠", "過膠");
        STEP_TO_CATEGORY.put("雙面過膠", "過膠");
        STEP_TO_CATEGORY.put("即塗型過膠", "過膠");
        STEP_TO_CATEGORY.put("預塗型過膠", "過膠");

        // ===== 丝印（14 步） =====
        STEP_TO_CATEGORY.put("局部UV", "絲印");
        STEP_TO_CATEGORY.put("啞UV", "絲印");
        STEP_TO_CATEGORY.put("溝閃粉", "絲印");
        STEP_TO_CATEGORY.put("灑閃粉", "絲印");
        STEP_TO_CATEGORY.put("凸字油", "絲印");
        STEP_TO_CATEGORY.put("有色凸字油", "絲印");
        STEP_TO_CATEGORY.put("磨砂UV", "絲印");
        STEP_TO_CATEGORY.put("夜光油墨", "絲印");
        STEP_TO_CATEGORY.put("皺紋UV", "絲印");
        STEP_TO_CATEGORY.put("LED珍珠", "絲印");
        STEP_TO_CATEGORY.put("巖石UV", "絲印");
        STEP_TO_CATEGORY.put("橘皮UV", "絲印");
        STEP_TO_CATEGORY.put("遇水變透明", "絲印");
        STEP_TO_CATEGORY.put("色墨", "絲印");

        // ===== 植毛（6 步） =====
        STEP_TO_CATEGORY.put("普通植毛", "植毛");
        STEP_TO_CATEGORY.put("透明植毛", "植毛");
        STEP_TO_CATEGORY.put("閃粉植毛", "植毛");
        STEP_TO_CATEGORY.put("植毛壓紋", "植毛");
        STEP_TO_CATEGORY.put("熒光毛", "植毛");
        STEP_TO_CATEGORY.put("長毛", "植毛");

        // ===== 啤（6 步） =====
        STEP_TO_CATEGORY.put("機啤", "啤");
        STEP_TO_CATEGORY.put("手啤", "啤");
        STEP_TO_CATEGORY.put("正啤折", "啤");
        STEP_TO_CATEGORY.put("反啤折", "啤");
        STEP_TO_CATEGORY.put("啤切", "啤");
        STEP_TO_CATEGORY.put("啤半穿", "啤");

        // ===== 手工（9 步） =====
        STEP_TO_CATEGORY.put("壓紋", "手工");
        STEP_TO_CATEGORY.put("壓竹脊", "手工");
        STEP_TO_CATEGORY.put("燙鑽", "手工");
        STEP_TO_CATEGORY.put("粘配件", "手工");
        STEP_TO_CATEGORY.put("塗膠", "手工");
        STEP_TO_CATEGORY.put("閘圓角", "手工");
        STEP_TO_CATEGORY.put("人手掃色邊", "手工");
        STEP_TO_CATEGORY.put("機掃色邊", "手工");
        STEP_TO_CATEGORY.put("輥金邊", "手工");

        // ===== 其他（6 步） =====
        // 这些默认归入"其他"类，但我们也显式映射一次
        STEP_TO_CATEGORY.put("鐳射介紙", "其他");
        STEP_TO_CATEGORY.put("車線", "其他");
        STEP_TO_CATEGORY.put("粘接", "其他");
        STEP_TO_CATEGORY.put("粘透明/可移性貼紙", "其他");
        STEP_TO_CATEGORY.put("以產品原組合配件筆為準", "其他");
        STEP_TO_CATEGORY.put("以客戶指定配件筆為準", "其他");
    }

    @Override
    public UvOilMatteProductDTO getProductDetail(Integer id) {
        UvOilMatteProduct product = uvOilMatteMapper.findProductById(id);
        if (product == null) {
            return null;
        }
        List<UvOilMatteCompatibility> compatibilities = uvOilMatteMapper.findCompatibilitiesByProductId(id);
        UvOilMatteProductDTO dto = UvOilMatteProductDTO.fromProduct(product);
        dto.setCompatibilities(compatibilities);
        return dto;
    }
}
