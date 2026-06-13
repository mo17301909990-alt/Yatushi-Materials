package com.it.yts_project.controller;

import com.it.yts_project.model.ReferenceProductFamily;
import com.it.yts_project.service.ReferenceProductFamilyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品家族字典表 Controller
 * 提供产品家族的CRUD管理接口
 */
@Slf4j
@RestController
@RequestMapping("/api/reference/product-family")
public class ReferenceProductFamilyController {

    @Autowired
    private ReferenceProductFamilyService referenceProductFamilyService;

    /**
     * 查询所有产品家族
     */
    @GetMapping
    public ResponseEntity<List<ReferenceProductFamily>> findAll() {
        log.debug("查询所有产品家族");
        return ResponseEntity.ok(referenceProductFamilyService.findAll());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferenceProductFamily> findById(@PathVariable Integer id) {
        log.debug("根据ID查询产品家族: {}", id);
        ReferenceProductFamily entity = referenceProductFamilyService.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    /**
     * 根据大类查询
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ReferenceProductFamily>> findByCategory(@PathVariable String category) {
        log.debug("根据大类查询产品家族: {}", category);
        return ResponseEntity.ok(referenceProductFamilyService.findByCategory(category));
    }

    /**
     * 查询所有不重复的大类列表
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> findDistinctCategories() {
        log.debug("查询所有大类列表");
        return ResponseEntity.ok(referenceProductFamilyService.findDistinctCategories());
    }

    /**
     * 查询所有不重复的子类列表
     */
    @GetMapping("/sub-categories")
    public ResponseEntity<List<String>> findDistinctSubCategories(
            @RequestParam(required = false) String category) {
        log.debug("查询子类列表, category={}", category);
        return ResponseEntity.ok(referenceProductFamilyService.findDistinctSubCategories(category));
    }

    /**
     * 创建
     */
    @PostMapping
    public ResponseEntity<ReferenceProductFamily> create(@RequestBody ReferenceProductFamily entity) {
        log.debug("创建产品家族: {}", entity);
        return ResponseEntity.ok(referenceProductFamilyService.create(entity));
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferenceProductFamily> update(
            @PathVariable Integer id,
            @RequestBody ReferenceProductFamily entity) {
        log.debug("更新产品家族: id={}, entity={}", id, entity);
        entity.setId(id);
        return ResponseEntity.ok(referenceProductFamilyService.update(entity));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("删除产品家族: {}", id);
        if (referenceProductFamilyService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
