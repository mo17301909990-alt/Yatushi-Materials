package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 前工序步骤实体类
 * 对应数据库表：pre_processing_steps
 */
@Data
public class PreProcessingSteps {
    
    /**
     * 主键ID
     * 注意：导入时如果提供了此字段，系统会尝试更新已存在的记录；如果不提供，系统会根据唯一性字段判断是更新还是创建
     * 导出时会出现在最后一列，用户可以通过修改ID来更新已存在的记录
     */
    @ExcelProperty(value = "ID", index = 8)
    private Integer id;
    
    /**
     * 步骤名称，已废弃，请使用stepId字段
     */
    @ExcelIgnore
    private String stepName;
    
    /**
     * 前工序名称（从pre_processing_steps_options表的pre_processing_steps_name字段获取，用于导入导出）
     */
    @ExcelProperty(value = "工艺类型", index = 0)
    private String preProcessingStepsName;
    
    /**
     * 系列名称（对应products表的name字段）
     */
    @ExcelProperty(value = "产品系列", index = 1)
    private String seriesName;
    
    /**
     * 产品ID（对应products表的id字段）
     * 注意：此字段不用于导入导出，不会出现在Excel模板中
     * 导入时通过productModelNumber、seriesName和paperType自动转换为productId
     */
    @ExcelIgnore
    private Integer productId;
    
    /**
     * 产品型号（从products表关联查询，不存储在pre_processing_steps表中）
     * 导入时使用此字段，系统会自动转换为productId
     */
    @ExcelProperty(value = "产品型号", index = 2)
    private String productModelNumber;
    
    /**
     * 纸张类型（对应products表的hot_stamping_paper_type字段）
     */
    @ExcelProperty(value = "烫金纸性能类型", index = 3)
    private String paperType;
    
    /**
     * 步骤顺序
     */
    @ExcelProperty(value = "步骤顺序", index = 4)
    private Integer stepOrder;
    
    /**
     * 是否激活
     */
    @ExcelProperty(value = "是否激活", index = 5)
    private Boolean isActive;
    
    /**
     * 状态
     */
    @ExcelProperty(value = "状态", index = 6)
    private String status;
    
    /**
     * 描述
     */
    @ExcelProperty(value = "描述", index = 7)
    private String description;
    
    /**
     * 步骤ID（对应pre_processing_steps_options表的id）
     * 注意：此字段不用于导入导出，不会出现在Excel模板中
     * 导入时通过preProcessingStepsName（前工序名称）自动查找对应的stepId
     */
    @ExcelIgnore
    private Integer stepId;
    
    /**
     * 布面纸ID
     * 注意：此字段不用于导入导出，不会出现在Excel模板中
     */
    @ExcelIgnore
    private Integer clothPaperId;
    
    /**
     * 创建时间
     */
    @ExcelIgnore
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @ExcelIgnore
    private LocalDateTime updatedAt;
}
