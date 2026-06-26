package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.LeoMatchParams;
import com.it.yts_project.dto.LeoProductVO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.mapper.LeoBookBoardMapper;
import com.it.yts_project.mapper.LeoFlatMapper;
import com.it.yts_project.mapper.LeoMapper;
import com.it.yts_project.mapper.LeoNonFlatMapper;
import com.it.yts_project.model.LeoBookBoardCompatibility;
import com.it.yts_project.model.LeoBookBoardProduct;
import com.it.yts_project.model.LeoFlatProduct;
import com.it.yts_project.model.LeoNonFlatProduct;
import com.it.yts_project.service.LeoMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * LEO纸品 组合匹配查询 Service 实现
 */
@Service
@Transactional
public class LeoMatchServiceImpl implements LeoMatchService {

    @Autowired
    private LeoMapper leoMapper;

    @Autowired
    private LeoBookBoardMapper leoBookBoardMapper;

    @Autowired
    private LeoFlatMapper leoFlatMapper;

    @Autowired
    private LeoNonFlatMapper leoNonFlatMapper;

    @Override
    public PagedResult<LeoProductVO> match(LeoMatchParams params) {
        // 默认选所有类型
        List<String> types = params.getTypes();
        if (CollectionUtils.isEmpty(types)) {
            types = Arrays.asList("book_board", "flat", "non_flat");
        }

        List<String> steps = params.getSteps();
        int page = params.getPage() != null && params.getPage() > 0 ? params.getPage() : 1;
        int size = params.getSize() != null && params.getSize() > 0 ? params.getSize() : 15;
        int offset = (page - 1) * size;

        // 多步骤时计算步骤数
        Integer stepCount = (steps != null && !steps.isEmpty()) ? steps.size() : 0;

        // 1. 查询总数
        Long total = leoMapper.countLeoProducts(types, steps, stepCount, params.getKeyword());

        if (total == null || total == 0) {
            return new PagedResult<>(Collections.emptyList(), 0L, size, page);
        }

        // 2. 查询分页的 (id, source_type) 对
        List<Map<String, Object>> idPairs = leoMapper.searchLeoProductIds(
                types, steps, stepCount, params.getKeyword(), offset, size);

        if (CollectionUtils.isEmpty(idPairs)) {
            return new PagedResult<>(Collections.emptyList(), total, size, page);
        }

        // 3. 按类型分组，批量查询完整产品信息
        List<Integer> bookBoardIds = new ArrayList<>();
        List<Integer> flatIds = new ArrayList<>();
        List<Integer> nonFlatIds = new ArrayList<>();
        Map<String, List<Integer>> typeToProductIds = new LinkedHashMap<>();

        for (Map<String, Object> pair : idPairs) {
            Integer id = (Integer) pair.get("id");
            String sourceType = (String) pair.get("source_type");
            typeToProductIds.computeIfAbsent(sourceType, k -> new ArrayList<>()).add(id);
            switch (sourceType) {
                case "book_board":
                    bookBoardIds.add(id);
                    break;
                case "flat":
                    flatIds.add(id);
                    break;
                case "non_flat":
                    nonFlatIds.add(id);
                    break;
            }
        }

        // 4. 按类型查询并构建 VO
        // 使用 LinkedHashMap 保持原有顺序
        Map<String, LeoProductVO> productMap = new LinkedHashMap<>();

        for (String sourceType : new String[]{"book_board", "flat", "non_flat"}) {
            List<Integer> ids = typeToProductIds.get(sourceType);
            if (CollectionUtils.isEmpty(ids)) continue;

            switch (sourceType) {
                case "book_board":
                    for (Integer id : ids) {
                        LeoBookBoardProduct p = leoBookBoardMapper.findProductById(id);
                        if (p != null) {
                            LeoProductVO vo = LeoProductVO.fromBookBoardProduct(p);
                            vo.setSourceType("book_board");
                            productMap.put("book_board_" + id, vo);
                        }
                    }
                    break;
                case "flat":
                    for (Integer id : ids) {
                        LeoFlatProduct p = leoFlatMapper.findProductById(id);
                        if (p != null) {
                            LeoProductVO vo = LeoProductVO.fromFlatProduct(p);
                            vo.setSourceType("flat");
                            productMap.put("flat_" + id, vo);
                        }
                    }
                    break;
                case "non_flat":
                    for (Integer id : ids) {
                        LeoNonFlatProduct p = leoNonFlatMapper.findProductById(id);
                        if (p != null) {
                            LeoProductVO vo = LeoProductVO.fromNonFlatProduct(p);
                            vo.setSourceType("non_flat");
                            productMap.put("non_flat_" + id, vo);
                        }
                    }
                    break;
            }
        }

        // 5. 按原始查询顺序排列结果
        List<LeoProductVO> orderedItems = new ArrayList<>();
        for (Map<String, Object> pair : idPairs) {
            Integer id = (Integer) pair.get("id");
            String sourceType = (String) pair.get("source_type");
            LeoProductVO vo = productMap.get(sourceType + "_" + id);
            if (vo != null) {
                orderedItems.add(vo);
            }
        }

        // 6. 查询兼容性状态（如果有步骤选择）
        if (!CollectionUtils.isEmpty(steps)) {
            Map<String, String> compatStatusMap = queryCompatibilityStatus(orderedItems, steps);
            for (LeoProductVO vo : orderedItems) {
                String key = vo.getSourceType() + "_" + vo.getId();
                String status = compatStatusMap.get(key);
                if (status != null) {
                    Map<String, String> statusMap = new LinkedHashMap<>();
                    // For single step, put the status directly
                    for (String step : steps) {
                        statusMap.put(step, compatStatusMap.get(key + "_" + step));
                    }
                    vo.setCompatibilityStatusMap(statusMap);
                }
            }
        }

        return new PagedResult<>(orderedItems, total, size, page);
    }

    @Override
    public List<Map<String, Object>> getSteps() {
        // 获取所有步骤的原始数据（含分类）
        List<Map<String, Object>> dbPairs = leoMapper.getDistinctStepsWithCategory();

        // 按分类分组
        Map<String, List<String>> grouped = new LinkedHashMap<>();
        for (Map<String, Object> pair : dbPairs) {
            String category = (String) pair.get("category");
            String step = (String) pair.get("step");
            if (category != null && step != null) {
                grouped.computeIfAbsent(category, k -> new ArrayList<>()).add(step);
            }
        }

        // 格式化输出
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
     * 查询指定产品在指定步骤上的兼容性状态
     */
    private Map<String, String> queryCompatibilityStatus(List<LeoProductVO> items, List<String> steps) {
        Map<String, String> result = new LinkedHashMap<>();

        // 按类型分组提取ID
        List<Integer> bookBoardIds = new ArrayList<>();
        List<Integer> flatIds = new ArrayList<>();
        List<Integer> nonFlatIds = new ArrayList<>();

        for (LeoProductVO vo : items) {
            switch (vo.getSourceType()) {
                case "book_board":
                    bookBoardIds.add(vo.getId());
                    break;
                case "flat":
                    flatIds.add(vo.getId());
                    break;
                case "non_flat":
                    nonFlatIds.add(vo.getId());
                    break;
            }
        }

        // 按类型分别查询
        if (!bookBoardIds.isEmpty()) {
            List<Map<String, Object>> rows = leoMapper.findBookBoardCompatibilityStatus(bookBoardIds, steps);
            fillStatusMap(result, rows, "book_board");
        }
        if (!flatIds.isEmpty()) {
            List<Map<String, Object>> rows = leoMapper.findFlatCompatibilityStatus(flatIds, steps);
            fillStatusMap(result, rows, "flat");
        }
        if (!nonFlatIds.isEmpty()) {
            List<Map<String, Object>> rows = leoMapper.findNonFlatCompatibilityStatus(nonFlatIds, steps);
            fillStatusMap(result, rows, "non_flat");
        }

        return result;
    }

    private void fillStatusMap(Map<String, String> map, List<Map<String, Object>> rows, String sourceType) {
        for (Map<String, Object> row : rows) {
            Integer productId = (Integer) row.get("product_id");
            String step = (String) row.get("post_processing_step");
            String status = (String) row.get("compatibility_status");
            String key = sourceType + "_" + productId + "_" + step;
            map.put(key, status);
        }
    }
}
