package com.it.yts_project.controller;

import com.it.yts_project.model.ReferenceCoatingSurface;
import com.it.yts_project.service.ReferenceCoatingSurfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 後加工塗層面字典表 Controller
 * 提供涂层面字典的CRUD管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/reference/coating-surface")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class ReferenceCoatingSurfaceController {

    @Autowired
    private ReferenceCoatingSurfaceService referenceCoatingSurfaceService;

    /**
     * 查询所有
     */
    @GetMapping
    public ResponseEntity<List<ReferenceCoatingSurface>> findAll() {
        log.debug("查询所有涂层面字典");
        return ResponseEntity.ok(referenceCoatingSurfaceService.findAll());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferenceCoatingSurface> findById(@PathVariable Integer id) {
        log.debug("根据ID查询涂层面字典: {}", id);
        ReferenceCoatingSurface item = referenceCoatingSurfaceService.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    /**
     * 根据分类查询
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ReferenceCoatingSurface>> findByCategory(@PathVariable String category) {
        log.debug("根据分类查询涂层面字典: {}", category);
        return ResponseEntity.ok(referenceCoatingSurfaceService.findByCategory(category));
    }

    /**
     * 关键字搜索
     */
    @GetMapping("/search")
    public ResponseEntity<List<ReferenceCoatingSurface>> search(@RequestParam String keyword) {
        log.debug("关键字搜索涂层面字典: {}", keyword);
        return ResponseEntity.ok(referenceCoatingSurfaceService.search(keyword));
    }

    /**
     * 创建
     */
    @PostMapping
    public ResponseEntity<ReferenceCoatingSurface> create(@RequestBody ReferenceCoatingSurface coatingSurface) {
        log.debug("创建涂层面字典: {}", coatingSurface);
        return ResponseEntity.ok(referenceCoatingSurfaceService.create(coatingSurface));
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferenceCoatingSurface> update(@PathVariable Integer id, @RequestBody ReferenceCoatingSurface coatingSurface) {
        log.debug("更新涂层面字典: id={}, coatingSurface={}", id, coatingSurface);
        coatingSurface.setId(id);
        return ResponseEntity.ok(referenceCoatingSurfaceService.update(coatingSurface));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("删除涂层面字典: {}", id);
        if (referenceCoatingSurfaceService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
