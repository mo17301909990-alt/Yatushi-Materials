package com.it.yts_project.controller;

import com.it.yts_project.model.UnifiedCompatibility;
import com.it.yts_project.service.CompatibilityQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compatibility")
public class CompatibilityController {

    @Autowired
    private CompatibilityQueryService compatibilityQueryService;

    @GetMapping("/unified-search")
    public List<UnifiedCompatibility> unifiedSearch(@RequestParam String keywords) {
        return compatibilityQueryService.searchUnified(keywords);
    }
}
