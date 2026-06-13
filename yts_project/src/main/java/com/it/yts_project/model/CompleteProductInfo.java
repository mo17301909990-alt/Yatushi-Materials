package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.it.yts_project.util.converter.CustomDoubleConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 完整产品信息模型 - 包含products、leo_gp_numbers、specifications表的数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompleteProductInfo {
    
    /**
     * 产品ID
     */
    @ExcelProperty("產品ID")
    private Integer id;
    
    /**
     * 燙金紙系列
     */
    @ExcelProperty("燙金紙系列")
    private String name;
    
    /**
     * 型号 (对应products表的model_number字段)
     */
    @ExcelProperty("型號")
    private String modelNumber;
    
    /**
     * 物料编号 (对应products表的material_number字段)
     */
    @ExcelProperty("物料編號")
    private String materialNumber;
    
    /**
     * 烫金纸张类型 (对应products表的hot_stamping_paper_type字段)
     */
    @ExcelProperty("燙金紙性能類型(請注意核對)")
    private String hotStampingPaperType;
    
    /**
     * 牌子 (对应products表的paizi字段)
     */
    @ExcelProperty("牌子")
    private String paizi;
    
    /**
     * 封度 (对应products表的fengdu字段)
     */
    @ExcelProperty("封度")
    private String fengdu;
    
    /**
     * 单位 (对应products表的danwei字段)
     */
    @ExcelProperty("計量單位")
    private String danwei;
    
    /**
     * 是否常用 (对应products表的isChangyong字段)
     */
    @ExcelProperty("常用與否")
    private String isChangyong;
    
    /**
     * 是否街货 (对应products表的isJiehuo字段)
     */
    @ExcelProperty("是否街貨")
    private Boolean isJiehuo;
    
    /**
     * 概述 (对应products表的gaishu字段)
     */
    @ExcelProperty("概述")
    private String gaishu;
    
    /**
     * 物料状态标记：可用、废弃 (对应products表的status字段)
     */
    @ExcelProperty("物料狀態")
    private String status;
    
    /**
     * 产品描述 (对应products表的descirption字段)
     */
    @ExcelProperty("說明")
    private String description;
    
    /**
     * Leo Touch编号 (对应leo_gp_numbers的company_number字段)
     */
    @ExcelProperty("Leo Touch編號")
    private String companyNumber;
    
    /**
     * SPNO (对应leo_gp_numbers的gp_no字段)
     */
    @ExcelProperty("SPNO")
    private String gpNo;
    
    /**
     * 颜色 (对应specifications表的color字段)
     */
    @ExcelProperty("顏色")
    private String color;
    
    /**
     * 颜色编码 (对应specifications表的color_num字段)
     */
    @ExcelProperty("顏色編碼")
    private String colorNum;
    
    /**
     * 尺寸 (对应specifications表的size字段)
     */
    @ExcelProperty("尺寸")
    private String size;
    
    /**
     * 金纸松紧度 (对应specifications表的tightness字段)
     */
    @ExcelProperty("金紙鬆緊度")
    private String tightness;
    
    /**
     * 金纸性能 (对应specifications表的performance字段)
     */
    @ExcelProperty("金紙性能")
    private String performance;
    
    /**
     * 温度范围 (对应specifications表的temperature_range字段)
     */
    @ExcelProperty("溫度範圍")
    private String temperatureRange;
    
    /**
     * 价格信息 (对应pricing表的price字段)
     * 使用自定义转换器处理空值和非数字内容
     */
    @ExcelProperty(value = "價格", converter = CustomDoubleConverter.class)
    private Double price;
    
    /**
     * 项目ID (用于关联)
     */
    @ExcelIgnore
    private Integer projectId;
}
