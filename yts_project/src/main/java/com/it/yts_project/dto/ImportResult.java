package com.it.yts_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

/**
 * 导入结果DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportResult<T> {
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 结果消息
     */
    private String message;
    
    /**
     * 成功导入的数据
     */
    private List<T> data;
    
    /**
     * 错误信息列表
     */
    private List<String> errorMessages;
    
    /**
     * 总记录数
     */
    private int totalCount;
    
    /**
     * 成功记录数
     */
    private int successCount;
    
    /**
     * 失败记录数
     */
    private int errorCount;
    
    /**
     * 新增记录数
     */
    private int createdCount;
    
    /**
     * 更新记录数
     */
    private int updatedCount;
    
    /**
     * 跳过记录数
     */
    private int skippedCount;
    
    /**
     * 创建成功结果
     */
    public static <T> ImportResult<T> success(List<T> data, String message) {
        return ImportResult.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .successCount(data != null ? data.size() : 0)
                .totalCount(data != null ? data.size() : 0)
                .build();
    }
    
    /**
     * 创建失败结果
     */
    public static <T> ImportResult<T> failure(String message) {
        return ImportResult.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
    
    /**
     * 创建部分成功结果
     */
    public static <T> ImportResult<T> partialSuccess(List<T> data, List<String> errorMessages, String message) {
        return ImportResult.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .errorMessages(errorMessages)
                .successCount(data != null ? data.size() : 0)
                .errorCount(errorMessages != null ? errorMessages.size() : 0)
                .totalCount((data != null ? data.size() : 0) + (errorMessages != null ? errorMessages.size() : 0))
                .build();
    }
}
