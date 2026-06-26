package com.it.yts_project.controller;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeProductDTO;
import com.it.yts_project.dto.SiliconeQueryParams;
import com.it.yts_project.service.SiliconeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 硅胶组合匹配 Controller
 * 合并 12 张 silicone_*_product 表的统一匹配查询入口
 */
@RestController
@RequestMapping({"/api/silicone"})
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://120.26.101.0",
        "http://120.26.101.0:80",
        "http://120.26.101.0:8080",
        "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeController {

    @Autowired
    private SiliconeService siliconeService;

    /**
     * 组合匹配查询：多类型 UNION ALL + 多步骤 INTERSECT + 分页
     */
    @PostMapping("/match")
    public ResponseEntity<PagedResult<SiliconeProductDTO>> match(@RequestBody SiliconeQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) {
            params.setPage(1);
        }
        if (params.getSize() == null || params.getSize() < 1) {
            params.setSize(15);
        }
        PagedResult<SiliconeProductDTO> result = siliconeService.match(
                params.getTypes(), params.getKeyword(), params.getSteps(),
                params.getPage(), params.getSize());
        return ResponseEntity.ok(result);
    }

    /**
     * 获取后加工工序步骤（跨所有硅胶类型）
     */
    @GetMapping("/steps")
    public ResponseEntity<List<Map<String, Object>>> getSteps(
            @RequestParam(value = "types", required = false) List<String> types) {
        List<Map<String, Object>> steps = siliconeService.getSteps(types);
        return ResponseEntity.ok(steps);
    }

    /**
     * 获取产品详情（含兼容性列表）
     */
    @GetMapping("/products/{type}/{id}/detail")
    public ResponseEntity<SiliconeProductDTO> getProductDetail(
            @PathVariable String type,
            @PathVariable Integer id) {
        SiliconeProductDTO dto = siliconeService.getProductDetail(id, type);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
}
