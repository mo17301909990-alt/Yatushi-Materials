package com.it.yts_project.controller;

import com.it.yts_project.model.ReferenceWritingFunction;
import com.it.yts_project.service.ReferenceWritingFunctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表面写字功能字典表 Controller
 * 提供写字功能字典的CRUD管理接口
 */
@Slf4j
@RestController
@RequestMapping("/reference/writing-function")
public class ReferenceWritingFunctionController {

    @Autowired
    private ReferenceWritingFunctionService referenceWritingFunctionService;

    /**
     * 查询所有
     */
    @GetMapping
    public ResponseEntity<List<ReferenceWritingFunction>> findAll() {
        log.debug("查询所有写字功能字典");
        return ResponseEntity.ok(referenceWritingFunctionService.findAll());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferenceWritingFunction> findById(@PathVariable Integer id) {
        log.debug("根据ID查询写字功能字典: {}", id);
        ReferenceWritingFunction item = referenceWritingFunctionService.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    /**
     * 根据分类查询
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ReferenceWritingFunction>> findByCategory(@PathVariable String category) {
        log.debug("根据分类查询写字功能字典: {}", category);
        return ResponseEntity.ok(referenceWritingFunctionService.findByCategory(category));
    }

    /**
     * 创建
     */
    @PostMapping
    public ResponseEntity<ReferenceWritingFunction> create(@RequestBody ReferenceWritingFunction writingFunction) {
        log.debug("创建写字功能字典: {}", writingFunction);
        return ResponseEntity.ok(referenceWritingFunctionService.create(writingFunction));
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferenceWritingFunction> update(@PathVariable Integer id, @RequestBody ReferenceWritingFunction writingFunction) {
        log.debug("更新写字功能字典: id={}, writingFunction={}", id, writingFunction);
        writingFunction.setId(id);
        return ResponseEntity.ok(referenceWritingFunctionService.update(writingFunction));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("删除写字功能字典: {}", id);
        if (referenceWritingFunctionService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
