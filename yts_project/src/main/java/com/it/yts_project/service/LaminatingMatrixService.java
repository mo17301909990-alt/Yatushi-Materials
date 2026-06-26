package com.it.yts_project.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 烫金后过胶－应用限制 矩阵导入导出服务接口。
 * 对应《燙後加工："燙金 + 過膠"－組合應用指引書》。
 */
public interface LaminatingMatrixService {

    /**
     * 导出矩阵为 Excel（CSV）
     */
    byte[] exportToExcel() throws Exception;

    /**
     * 导出矩阵为 Word（.docx OOXML，与「燙金+印刷UV」矩阵导出方式一致）
     */
    byte[] exportToWord() throws Exception;

    /**
     * 导入矩阵文件（支持 .xlsx / .csv / .docx）
     *
     * @param file             上传的文件
     * @param conflictStrategy "overwrite" 或 "skip"
     * @return 导入结果统计（totalRows, created, updated, skipped, errors, errorMessages）
     */
    Map<String, Object> importMatrix(MultipartFile file, String conflictStrategy) throws Exception;
}
