package com.it.yts_project.controller;

import com.it.yts_project.service.CommonInterfaceMatrixService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 常用界面燙印性 組合應用表 矩阵导入导出控制器
 */
@RestController
@RequestMapping("/api/common-interface-matrix")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class CommonInterfaceMatrixController {

    private static final Logger log = LoggerFactory.getLogger(CommonInterfaceMatrixController.class);

    @Autowired
    private CommonInterfaceMatrixService matrixService;

    /**
     * 导出矩阵为 CSV（用 Excel 可直接打开，避免 POI 依赖冲突）
     */
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel() {
        try {
            byte[] data = matrixService.exportToExcel();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "常用界面燙印性_組合應用表_" + timestamp + ".csv";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
            headers.setContentDispositionFormData("attachment",
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            headers.setContentLength(data.length);

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 导出矩阵为 Word（.docx OOXML，与燙金+印刷UV 一致，A4/縱向/頁邊距寫入文檔）
     */
    @GetMapping("/export/word")
    public ResponseEntity<byte[]> exportWord() {
        try {
            byte[] data = matrixService.exportToWord();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "常用界面燙印性_組合應用表_" + timestamp + ".docx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            headers.setContentDispositionFormData("attachment",
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            headers.setContentLength(data.length);

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Common interface matrix export Word failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 导入矩阵文件（支持 .xlsx / .docx）
     * @param file 上传的文件
     * @param conflictStrategy 冲突策略: "overwrite" 或 "skip"（默认 skip）
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importMatrix(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "conflictStrategy", defaultValue = "skip") String conflictStrategy) {
        try {
            Map<String, Object> result = matrixService.importMatrix(file, conflictStrategy);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            log.warn("Common interface matrix import bad request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Common interface matrix import failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Import failed: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName())));
        }
    }
}
