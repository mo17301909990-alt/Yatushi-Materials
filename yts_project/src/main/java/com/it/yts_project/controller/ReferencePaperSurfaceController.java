package com.it.yts_project.controller;

import com.it.yts_project.model.ReferencePaperSurface;
import com.it.yts_project.service.ReferencePaperSurfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 纸張面字典表 Controller
 * 提供纸张面字典的CRUD管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/reference/paper-surface")
public class ReferencePaperSurfaceController {

    @Autowired
    private ReferencePaperSurfaceService referencePaperSurfaceService;

    /**
     * 查询所有
     */
    @GetMapping
    public ResponseEntity<List<ReferencePaperSurface>> findAll() {
        log.debug("查询所有纸张面字典");
        return ResponseEntity.ok(referencePaperSurfaceService.findAll());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferencePaperSurface> findById(@PathVariable Integer id) {
        log.debug("根据ID查询纸张面字典: {}", id);
        ReferencePaperSurface item = referencePaperSurfaceService.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    /**
     * 根据分类查询
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ReferencePaperSurface>> findByCategory(@PathVariable String category) {
        log.debug("根据分类查询纸张面字典: {}", category);
        return ResponseEntity.ok(referencePaperSurfaceService.findByCategory(category));
    }

    /**
     * 创建
     */
    @PostMapping
    public ResponseEntity<ReferencePaperSurface> create(@RequestBody ReferencePaperSurface paperSurface) {
        log.debug("创建纸张面字典: {}", paperSurface);
        return ResponseEntity.ok(referencePaperSurfaceService.create(paperSurface));
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferencePaperSurface> update(@PathVariable Integer id, @RequestBody ReferencePaperSurface paperSurface) {
        log.debug("更新纸张面字典: id={}, paperSurface={}", id, paperSurface);
        paperSurface.setId(id);
        return ResponseEntity.ok(referencePaperSurfaceService.update(paperSurface));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("删除纸张面字典: {}", id);
        if (referencePaperSurfaceService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
