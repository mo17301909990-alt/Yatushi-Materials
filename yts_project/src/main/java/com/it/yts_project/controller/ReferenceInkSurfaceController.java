package com.it.yts_project.controller;

import com.it.yts_project.model.ReferenceInkSurface;
import com.it.yts_project.service.ReferenceInkSurfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 印刷油墨面字典表 Controller
 * 提供印刷油墨面字典的CRUD管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/reference/ink-surface")
public class ReferenceInkSurfaceController {

    @Autowired
    private ReferenceInkSurfaceService referenceInkSurfaceService;

    /**
     * 查询所有
     */
    @GetMapping
    public ResponseEntity<List<ReferenceInkSurface>> findAll() {
        log.debug("查询所有印刷油墨面字典");
        return ResponseEntity.ok(referenceInkSurfaceService.findAll());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferenceInkSurface> findById(@PathVariable Integer id) {
        log.debug("根据ID查询印刷油墨面字典: {}", id);
        ReferenceInkSurface item = referenceInkSurfaceService.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    /**
     * 根据分类查询
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ReferenceInkSurface>> findByCategory(@PathVariable String category) {
        log.debug("根据分类查询印刷油墨面字典: {}", category);
        return ResponseEntity.ok(referenceInkSurfaceService.findByCategory(category));
    }

    /**
     * 创建
     */
    @PostMapping
    public ResponseEntity<ReferenceInkSurface> create(@RequestBody ReferenceInkSurface inkSurface) {
        log.debug("创建印刷油墨面字典: {}", inkSurface);
        return ResponseEntity.ok(referenceInkSurfaceService.create(inkSurface));
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferenceInkSurface> update(@PathVariable Integer id, @RequestBody ReferenceInkSurface inkSurface) {
        log.debug("更新印刷油墨面字典: id={}, inkSurface={}", id, inkSurface);
        inkSurface.setId(id);
        return ResponseEntity.ok(referenceInkSurfaceService.update(inkSurface));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("删除印刷油墨面字典: {}", id);
        if (referenceInkSurfaceService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
