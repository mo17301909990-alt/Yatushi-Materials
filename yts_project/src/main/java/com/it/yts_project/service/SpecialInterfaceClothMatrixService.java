package com.it.yts_project.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 「布面紙+燙金」－組合應用表 矩阵导入导出服务接口。
 * 对应《特殊界面燙金：布面紙燙印性－指引書》中的表格。
 */
public interface SpecialInterfaceClothMatrixService {

    /**
     * 导出矩阵为 Excel（CSV，UTF-8 BOM）
     */
    byte[] exportToExcel() throws Exception;

    /**
     * 导出矩阵为 Word（.docx OOXML，与燙金+印刷UV 一致）
     */
    byte[] exportToWord() throws Exception;

    /**
     * 导入矩阵文件（支持 .xlsx / .docx / .csv）
     * @param file 上传的文件
     * @param conflictStrategy "overwrite" 或 "skip"
     * @return 导入结果统计（created, updated, skipped, errors, errorMessages 等）
     */
    Map<String, Object> importMatrix(MultipartFile file, String conflictStrategy) throws Exception;
}
