package com.it.yts_project.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 燙金+絲印LED UV灑閃粉 配对烫金纸型号矩阵 导入导出服务接口。
 */
public interface LeduvglitterMatrixService {

    /**
     * 导出矩阵为 Excel（CSV）
     */
    byte[] exportToExcel() throws Exception;

    /**
     * 导出矩阵为 Word（.docx）
     */
    byte[] exportToWord() throws Exception;

    /**
     * 导入矩阵文件（支持 .xlsx / .csv / .docx）
     *
     * @param file              上传的文件
     * @param conflictStrategy  "overwrite" 或 "skip"
     * @return 导入结果（totalRows, created, updated, skipped, errors, errorMessages）
     */
    Map<String, Object> importMatrix(MultipartFile file, String conflictStrategy) throws Exception;
}
