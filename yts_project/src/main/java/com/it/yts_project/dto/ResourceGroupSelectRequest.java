package com.it.yts_project.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 资源组筛选请求DTO
 */
@Data
public class ResourceGroupSelectRequest {
    /**
     * 任务编码：PM/TK/SS/GL/BP
     */
    private String taskCode;
    
    /**
     * 印张石数
     */
    private Integer sheetCount;
    
    /**
     * 厚度(mm)
     */
    private BigDecimal thickness;
    
    /**
     * 克重(g)
     */
    private Integer gsm;
    
    /**
     * 宽度(mm)
     */
    private Integer width;
    
    /**
     * 高度(mm)
     */
    private Integer height;
    
    /**
     * 事业部
     */
    private String department;
    
    /**
     * 产品类型
     */
    private String productType;
    
    /**
     * 适用界面（光面/哑面/特种）
     */
    private String suitableSurface;
    
    /**
     * 选纸张数(令)
     */
    private Integer selectedSheetCount;
    
    /**
     * 纹路方向（长纹/短纹）
     */
    private String grainDirection;
    
    /**
     * 当前评估单的任务列表（按顺序存任务编码）
     */
    private List<String> workflowTaskCodes;
}

