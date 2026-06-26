package com.it.yts_project.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 常用界面燙印性 組合應用表 矩阵导入导出服务接口
 */
public interface CommonInterfaceMatrixService {

    /**
     * 导出矩阵为 Excel (.xlsx)
     */
    byte[] exportToExcel() throws Exception;

    /**
     * 导出矩阵为 Word (.docx)
     */
    byte[] exportToWord() throws Exception;

    /**
     * 导入矩阵文件（支持 .xlsx / .docx）
     * @param file 上传的文件
     * @param conflictStrategy "overwrite" 或 "skip"
     * @return 导入结果统计
     */
    Map<String, Object> importMatrix(MultipartFile file, String conflictStrategy) throws Exception;
}
