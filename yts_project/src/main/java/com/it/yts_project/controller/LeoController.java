package com.it.yts_project.controller;

import com.it.yts_project.dto.LeoMatchParams;
import com.it.yts_project.dto.LeoProductVO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.service.LeoMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * LEO纸品 组合匹配系统 Controller
 * 统一查询 leo_book_board / leo_flat / leo_non_flat 三张表
 */
@RestController
@RequestMapping("/api/leo")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class LeoController {

    @Autowired
    private LeoMatchService leoMatchService;

    /**
     * 组合匹配查询
     * POST /api/leo/match
     * 参数: {"types":["book_board","flat","non_flat"], "steps":["丝印","烫金"], "keyword":"", "page":1, "size":15}
     */
    @PostMapping("/match")
    public ResponseEntity<PagedResult<LeoProductVO>> match(@RequestBody LeoMatchParams params) {
        if (params.getPage() == null || params.getPage() < 1) {
            params.setPage(1);
        }
        if (params.getSize() == null || params.getSize() < 1) {
            params.setSize(15);
        }
        PagedResult<LeoProductVO> result = leoMatchService.match(params);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有后加工工序（按大类分组）
     */
    @GetMapping("/steps")
    public ResponseEntity<List<Map<String, Object>>> getSteps() {
        List<Map<String, Object>> steps = leoMatchService.getSteps();
        return ResponseEntity.ok(steps);
    }
}
