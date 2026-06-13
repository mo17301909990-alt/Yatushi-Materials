package com.it.yts_project.controller;

import com.it.yts_project.dto.GoldFoilProductDTO;
import com.it.yts_project.dto.GoldFoilQueryParams;
import com.it.yts_project.dto.MatchResultWithNotices;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.NoticeDictionary;
import com.it.yts_project.service.GoldFoilProductService;
import com.it.yts_project.service.MatchingNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/gold-foil")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class GoldFoilProductController {

    private static final Logger log = LoggerFactory.getLogger(GoldFoilProductController.class);

    @Autowired
    private GoldFoilProductService goldFoilProductService;
    
    @Autowired
    private MatchingNoticeService matchingNoticeService;

    /**
     * 返回右侧全部烫金纸供应型号列表
     * @return 烫金纸产品列表
     */
    @GetMapping("/products")
    public ResponseEntity<List<GoldFoilProductDTO>> getAllGoldFoilProducts() {
        try {
            return ResponseEntity.ok(goldFoilProductService.getAllGoldFoilProducts());
        } catch (Exception e) {
            log.error("getAllGoldFoilProducts 失败", e);
            String msg = e.getMessage();
            if (msg == null || msg.isBlank()) {
                msg = e.getClass().getSimpleName();
            }
            throw new RuntimeException("燙金紙供應型號列表加載失敗: " + msg, e);
        }
    }

    /**
     * 获取去重的公司编号和型号列表（用于输入下拉建议）
     */
    @GetMapping("/distinct-values")
    public ResponseEntity<Object> getDistinctValues() {
        try {
            List<String> companyNumbers = goldFoilProductService.getDistinctCompanyNumbers();
            List<String> gpNumbers = goldFoilProductService.getDistinctGpNumbers();
            return ResponseEntity.ok(java.util.Map.of(
                "companyNumbers", companyNumbers,
                "gpNumbers", gpNumbers
            ));
        } catch (Exception e) {
            log.error("获取去重值失败", e);
            return ResponseEntity.ok(java.util.Map.of(
                "companyNumbers", java.util.Collections.emptyList(),
                "gpNumbers", java.util.Collections.emptyList()
            ));
        }
    }

    /**
     * 匹配查询：使用 GoldFoilQueryParams 参数进行查询
     * 这个方法支持更灵活的查询，特别是对于产品类型和烫金图案类型的处理
     * 可用于第一次匹配、第二次匹配和未来的第三次匹配
     *
     * @param params 查询参数，包含公司编号、产品类型、烫金图案类型等
     * @return 匹配的烫金纸产品列表（分页结果）和相关注意事项
     */
    @PostMapping("/match")
    public ResponseEntity<MatchResultWithNotices<GoldFoilProductDTO>> getProducts(@RequestBody GoldFoilQueryParams params) {
        try {
            log.debug("接收到匹配请求，参数：{}", params);
            log.debug("分页参数 - pageSize: {}, offset: {}", params.getPageSize(), params.getOffset());
            log.debug("排序参数 - sortBy: {}, sortOrder: {}", params.getSortBy(), params.getSortOrder());
            log.debug("过胶相关参数 - postProcessingStepId: {}, laminationMaterialId: {}", 
                      params.getPostProcessingStepId(), params.getLaminationMaterialId());

            // 设置默认分页参数
            if (params.getPageSize() == null || params.getPageSize() <= 0) {
                params.setPageSize(15);
            }
            if (params.getOffset() == null || params.getOffset() < 0) {
                params.setOffset(0);
            }

            // 计算当前页码（从1开始）
            int currentPage = (params.getOffset() / params.getPageSize()) + 1;

            // 并行获取数据和总数
            List<GoldFoilProductDTO> products = goldFoilProductService.getProducts(params);
            Long total = goldFoilProductService.countProducts(params);

            // 创建分页结果
            PagedResult<GoldFoilProductDTO> pagedResult = new PagedResult<>(
                products,
                total,
                params.getPageSize(),
                currentPage
            );
            
            // 收集匹配规则相关的注意事项
            List<NoticeDictionary> notices = Collections.emptyList();
            try {
                notices = matchingNoticeService.collectMatchingNotices(params);
                log.debug("收集到注意事项数量：{}", notices.size());
            } catch (Exception e) {
                log.warn("收集注意事项失败，继续返回产品结果: {}", e.getMessage());
            }
            
            // 创建包含注意事项的返回结果
            MatchResultWithNotices<GoldFoilProductDTO> result = new MatchResultWithNotices<>(pagedResult, notices);

            log.debug("匹配查询结果数量：{}，总记录数：{}，注意事项数量：{}", products.size(), total, notices.size());
            
            if (products.size() == 0) {
                log.warn("查询返回0条记录，请检查查询条件和数据");
                if (params.getPostProcessingStepId() != null && params.getLaminationMaterialId() != null) {
                    log.debug("过胶查询参数: stepId={}, materialId={}", 
                             params.getPostProcessingStepId(), params.getLaminationMaterialId());
                    log.debug("建议检查:");
                    log.debug("1. lamination_compatibility表中是否存在对应的兼容记录");
                    log.debug("2. products表中是否存在对应的产品记录");
                    log.debug("3. JOIN条件是否正确匹配");
                }
            } else {
                log.debug("查询成功，返回产品：");
                for (int i = 0; i < Math.min(3, products.size()); i++) {
                    log.debug("  - {} ({})", products.get(i).getName(), products.get(i).getModelNumber());
                }
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("匹配查询失败", e);
            // 返回空结果，避免前端报错
            int currentPage = 1;
            if (params != null && params.getPageSize() != null && params.getPageSize() > 0) {
                int offset = params.getOffset() != null ? params.getOffset() : 0;
                currentPage = (offset / params.getPageSize()) + 1;
            }
            PagedResult<GoldFoilProductDTO> emptyResult = new PagedResult<>(List.of(), 0L, 
                params != null && params.getPageSize() != null ? params.getPageSize() : 15, 
                currentPage);
            return ResponseEntity.status(500)
                .body(new MatchResultWithNotices<>(emptyResult, Collections.emptyList()));
        }
    }
}
