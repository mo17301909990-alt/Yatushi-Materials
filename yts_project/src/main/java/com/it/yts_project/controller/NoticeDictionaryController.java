package com.it.yts_project.controller;

import com.it.yts_project.model.NoticeDictionary;
import com.it.yts_project.service.NoticeDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 注意事项字典表 Controller
 * 提供注意事项的CRUD管理接口
 */
@Slf4j
@RestController
@RequestMapping("/notice-dictionary")
public class NoticeDictionaryController {

    @Autowired
    private NoticeDictionaryService noticeDictionaryService;

    /**
     * 查询所有注意事项
     */
    @GetMapping
    public ResponseEntity<List<NoticeDictionary>> findAll() {
        log.debug("查询所有注意事项");
        return ResponseEntity.ok(noticeDictionaryService.findAll());
    }

    /**
     * 查询所有启用的注意事项
     */
    @GetMapping("/active")
    public ResponseEntity<List<NoticeDictionary>> findAllActive() {
        log.debug("查询所有启用的注意事项");
        return ResponseEntity.ok(noticeDictionaryService.findAllActive());
    }

    /**
     * 根据ID查询注意事项
     */
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDictionary> findById(@PathVariable Integer id) {
        log.debug("根据ID查询注意事项: {}", id);
        NoticeDictionary notice = noticeDictionaryService.findById(id);
        if (notice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notice);
    }

    /**
     * 根据编码查询注意事项
     */
    @GetMapping("/code/{noticeCode}")
    public ResponseEntity<NoticeDictionary> findByCode(@PathVariable String noticeCode) {
        log.debug("根据编码查询注意事项: {}", noticeCode);
        NoticeDictionary notice = noticeDictionaryService.findByCode(noticeCode);
        if (notice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notice);
    }

    /**
     * 根据分类查询注意事项
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<NoticeDictionary>> findByCategory(@PathVariable String category) {
        log.debug("根据分类查询注意事项: {}", category);
        return ResponseEntity.ok(noticeDictionaryService.findByCategory(category));
    }

    /**
     * 根据ID列表查询注意事项
     */
    @PostMapping("/by-ids")
    public ResponseEntity<List<NoticeDictionary>> findByIds(@RequestBody List<Integer> ids) {
        log.debug("根据ID列表查询注意事项: {}", ids);
        return ResponseEntity.ok(noticeDictionaryService.findByIds(ids));
    }

    /**
     * 创建注意事项
     */
    @PostMapping
    public ResponseEntity<NoticeDictionary> create(@RequestBody NoticeDictionary notice) {
        log.debug("创建注意事项: {}", notice);
        return ResponseEntity.ok(noticeDictionaryService.create(notice));
    }

    /**
     * 更新注意事项
     */
    @PutMapping("/{id}")
    public ResponseEntity<NoticeDictionary> update(@PathVariable Integer id, @RequestBody NoticeDictionary notice) {
        log.debug("更新注意事项: id={}, notice={}", id, notice);
        notice.setId(id);
        return ResponseEntity.ok(noticeDictionaryService.update(notice));
    }

    /**
     * 删除注意事项
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.debug("删除注意事项: {}", id);
        if (noticeDictionaryService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
