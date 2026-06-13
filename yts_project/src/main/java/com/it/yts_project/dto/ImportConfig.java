package com.it.yts_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 导入配置DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportConfig {
    
    /**
     * 冲突处理策略
     */
    public enum ConflictStrategy {
        /**
         * 跳过冲突数据（不导入）
         */
        SKIP,
        
        /**
         * 覆盖现有数据
         */
        OVERWRITE,
        
        /**
         * 创建新记录（忽略冲突）
         */
        CREATE_NEW,
        
        /**
         * 提示用户选择
         */
        PROMPT_USER
    }
    
    /**
     * 冲突处理策略
     */
    private ConflictStrategy conflictStrategy;
    
    /**
     * 是否验证数据完整性
     */
    private boolean validateData;
    
    /**
     * 是否在导入前备份现有数据
     */
    private boolean backupBeforeImport;
    
    /**
     * 是否发送导入结果通知
     */
    private boolean sendNotification;
    
    /**
     * 最大导入记录数
     */
    private int maxImportCount;
    
    /**
     * 默认配置：跳过冲突（安全模式）
     */
    public static ImportConfig defaultConfig() {
        return ImportConfig.builder()
                .conflictStrategy(ConflictStrategy.SKIP)
                .validateData(true)
                .backupBeforeImport(false)
                .sendNotification(true)
                .maxImportCount(1000)
                .build();
    }
    
    /**
     * 覆盖模式配置
     */
    public static ImportConfig overwriteConfig() {
        return ImportConfig.builder()
                .conflictStrategy(ConflictStrategy.OVERWRITE)
                .validateData(true)
                .backupBeforeImport(true)
                .sendNotification(true)
                .maxImportCount(1000)
                .build();
    }
    
    /**
     * 严格模式配置
     */
    public static ImportConfig strictConfig() {
        return ImportConfig.builder()
                .conflictStrategy(ConflictStrategy.SKIP)
                .validateData(true)
                .backupBeforeImport(false)
                .sendNotification(true)
                .maxImportCount(500)
                .build();
    }
}
