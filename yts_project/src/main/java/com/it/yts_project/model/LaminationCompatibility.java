package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 过胶兼容性模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaminationCompatibility {
    
    /**
     * 兼容性ID
     */
    @ExcelIgnore
    private Integer id;
    
    /**
     * 箔纸系列（对应products表的name）
     */
    @ExcelProperty("產品系列")
    private String foilSeries;
    
    /**
     * 界面类型ID（废弃字段，固定为0）
     * 注意：此字段不用于导入导出，不会出现在Excel模板中
     */
    @ExcelIgnore
    private Integer interfaceTypeId;
    
    /**
     * 后处理工序ID（对应post_processing_options的id，可为null表示无后加工）
     * 注意：此字段不用于导入导出，不会出现在Excel模板中
     * 导入时通过postProcessingStepName（过胶后工藝名称）自动查找对应的postProcessingStepId
     */
    @ExcelIgnore
    private Integer postProcessingStepId;
    
    /**
     * 过胶材料ID（对应lamination_material_options的id）
     * 注意：此字段不用于导入导出，不会出现在Excel模板中
     * 导入时通过laminationMaterialName（过胶材料名称）自动查找对应的laminationMaterialId
     */
    @ExcelIgnore
    private Integer laminationMaterialId;
    
    /**
     * 兼容性（V=兼容，X=不兼容）
     */
    @ExcelProperty("兼容性狀態(V:兼容, X:不兼容)")
    private String compatibility;
    
    /**
     * 产品类型（对应products表的hot_stamping_paper_type字段）
     */
    @ExcelProperty("產品類型")
    private String productType;
    
    /**
     * 型号（对应products表的model_number）
     */
    @ExcelProperty("產品型號")
    private String modelNumber;
    
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

    /**
     * 关联的注意事项ID列表
     * 对应数据库字段: notice_ids (INTEGER[])
     */
    @ExcelIgnore
    private List<Integer> noticeIds;

    // 关联对象（用于显示和导出）
    /**
     * 过胶材料名称（用于导出，格式：materialName）
     * 注意：此字段不存储在数据库中，仅用于Excel导出显示
     * 导入时使用此字段来查找对应的laminationMaterialId
     */
    @ExcelProperty("過膠材料")
    private String laminationMaterialName;
    
    /**
     * 过胶材料描述（用于显示）
     * 注意：此字段不存储在数据库中，仅用于前端显示
     */
    @ExcelIgnore
    private String laminationMaterialDescription;
    
    /**
     * 后处理工序名称（用于导出）
     * 注意：此字段不存储在数据库中，仅用于Excel导出显示
     * 导入时使用此字段来查找对应的postProcessingStepId
     */
    @ExcelProperty("過膠後工藝")
    private String postProcessingStepName;

    /**
     * 街货标识（Boolean类型，用于数据库存储）
     */
    @ExcelIgnore
    private Boolean isJiehuo;
    
    /**
     * 街货标识（String类型，用于Excel导入导出）
     */
    @ExcelProperty("街貨(是/否)")
    private String isJiehuoText;
    
    /**
     * 获取街货标识文本
     */
    public String getIsJiehuoText() {
        if (isJiehuo == null) {
            return "否";
        }
        return isJiehuo ? "是" : "否";
    }
    
    /**
     * 设置街货标识文本（用于Excel导入）
     */
    public void setIsJiehuoText(String isJiehuoText) {
        this.isJiehuoText = isJiehuoText;
        if (isJiehuoText != null) {
            this.isJiehuo = "是".equals(isJiehuoText.trim()) || "true".equalsIgnoreCase(isJiehuoText.trim()) || "1".equals(isJiehuoText.trim());
        } else {
            this.isJiehuo = false;
        }
    }
}