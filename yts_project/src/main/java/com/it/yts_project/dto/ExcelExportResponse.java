package com.it.yts_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Excel导出响应实体类
 * 用于统一Excel导出接口的响应格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelExportResponse {
    
    /**
     * 导出是否成功
     */
    private Boolean success;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    
    /**
     * 导出记录数
     */
    private Integer recordCount;
    
    /**
     * 导出时间
     */
    private LocalDateTime exportTime;
    
    /**
     * 错误信息（当success为false时）
     */
    private String errorMessage;
    
    /**
     * 创建成功响应
     */
    public static ExcelExportResponse success(String fileName, Long fileSize, Integer recordCount) {
        return ExcelExportResponse.builder()
                .success(true)
                .message("导出成功")
                .fileName(fileName)
                .fileSize(fileSize)
                .recordCount(recordCount)
                .exportTime(LocalDateTime.now())
                .build();
    }
    
    /**
     * 创建失败响应
     */
    public static ExcelExportResponse failure(String errorMessage) {
        return ExcelExportResponse.builder()
                .success(false)
                .message("导出失败")
                .errorMessage(errorMessage)
                .exportTime(LocalDateTime.now())
                .build();
    }
}
