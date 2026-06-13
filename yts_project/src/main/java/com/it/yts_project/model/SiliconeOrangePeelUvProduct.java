package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 硅胶桔纹UV(Orange Peel UV)物料产品模型
 * 基于：硅胶/硅胶桔纹UV(Orange Peel UV)标准书-20230718.xlsx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiliconeOrangePeelUvProduct {

    @ExcelProperty(value = "ID", index = 0)
    private Integer id;

    /** 物料编码（种类/材料/主類別/副類別/流水碼） */
    @ExcelProperty(value = "物料编码", index = 1)
    private String materialCode;

    /** 供應商物料型號/編號 */
    @ExcelProperty(value = "供应商物料型号/编号", index = 2)
    private String supplierCode;

    /** 採購申請編號（测试单号） */
    @ExcelProperty(value = "采购申请编号", index = 3)
    private String stockCode;

    /** 物料名稱 */
    @ExcelProperty(value = "物料名称", index = 4)
    private String materialName;

    /** 物料用途 */
    @ExcelProperty(value = "物料用途", index = 5)
    private String usageText;

    /** 材質 */
    @ExcelProperty(value = "材质", index = 6)
    private String materialType;

    /** 厚度 */
    @ExcelProperty(value = "厚度", index = 7)
    private String thickness;

    /** 尺寸 */
    @ExcelProperty(value = "尺寸", index = 8)
    private String sizeInfo;

    /** 顏色 */
    @ExcelProperty(value = "颜色", index = 9)
    private String color;

    /** 形狀 */
    @ExcelProperty(value = "形状", index = 10)
    private String shape;

    /** 測試員 */
    @ExcelProperty(value = "测试员", index = 11)
    private String responsiblePerson;

    /** 用紙尺寸-最小(mm) */
    @ExcelProperty(value = "用纸尺寸-最小", index = 12)
    private String minPaperSize;

    /** 用紙尺寸-最大(mm) */
    @ExcelProperty(value = "用纸尺寸-最大", index = 13)
    private String maxPaperSize;

    /** 點-最小(mm) */
    @ExcelProperty(value = "点-最小", index = 14)
    private String minDot;

    /** 點-最大(mm) */
    @ExcelProperty(value = "点-最大", index = 15)
    private String maxDot;

    /** 線-最小(mm) */
    @ExcelProperty(value = "线-最小", index = 16)
    private String minLine;

    /** 線-最大(mm) */
    @ExcelProperty(value = "线-最大", index = 17)
    private String maxLine;

    /** 間距-最小(mm) */
    @ExcelProperty(value = "间距-最小", index = 18)
    private String minSpacing;

    /** 間距-最大(mm) */
    @ExcelProperty(value = "间距-最大", index = 19)
    private String maxSpacing;

    /** 單個圖案加工面積-最小(mm) */
    @ExcelProperty(value = "单个图案加工面积-最小", index = 20)
    private String minPatternArea;

    /** 單個圖案加工面積-最大(mm) */
    @ExcelProperty(value = "单个图案加工面积-最大", index = 21)
    private String maxPatternArea;

    /** 結構應用(適用產品) */
    @ExcelProperty(value = "结构应用", index = 22)
    private String structureApplication;

    /** 單面(V/X) */
    @ExcelProperty(value = "单面", index = 23)
    private String singleSide;

    /** 雙面(V/X) */
    @ExcelProperty(value = "双面", index = 24)
    private String doubleSide;

    /** 封面(V/X) */
    @ExcelProperty(value = "封面", index = 25)
    private String coverPage;

    /** 書脊(V/X) */
    @ExcelProperty(value = "书脊", index = 26)
    private String bookSpine;

    /** 踩坑位(V/X) */
    @ExcelProperty(value = "踩坑位", index = 27)
    private String pitPosition;

    /** 內文(V/X) */
    @ExcelProperty(value = "内文", index = 28)
    private String innerPage;

    /** 1-紙張面-基材厚度 */
    @ExcelProperty(value = "纸张面-基材厚度", index = 29)
    private String interfacePaperBaseThickness;

    /** 1-紙張面-適用 */
    @ExcelProperty(value = "纸张面-适用", index = 30)
    private String interfacePaperSuitable;

    /** 1-紙張面-不適用 */
    @ExcelProperty(value = "纸张面-不适用", index = 31)
    private String interfacePaperUnsuitable;

    /** 2-印刷油墨面-適用 */
    @ExcelProperty(value = "印刷油墨面-适用", index = 32)
    private String interfaceInkSuitable;

    /** 2-印刷油墨面-不適用 */
    @ExcelProperty(value = "印刷油墨面-不适用", index = 33)
    private String interfaceInkUnsuitable;

    /** 3-後加工涂層面-適用 */
    @ExcelProperty(value = "后加工涂层-适用", index = 34)
    private String interfaceCoatingSuitable;

    /** 3-後加工涂層面-不適用 */
    @ExcelProperty(value = "后加工涂层-不适用", index = 35)
    private String interfaceCoatingUnsuitable;

    /** 設計限制补充说明 */
    @ExcelProperty(value = "设计信息", index = 36)
    private String designInfo;

    /** 適用界面补充说明 */
    @ExcelProperty(value = "适用界面", index = 37)
    private String applicableInterface;

    /** 注意事項、限制的備注與說明 */
    @ExcelProperty(value = "注意事项", index = 38)
    private String notes;

    /** 是否激活 */
    @ExcelProperty(value = "是否激活", index = 39)
    private String isActiveText;

    private Boolean isActive;

    @ExcelProperty(value = "创建时间", index = 40)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @ExcelProperty(value = "更新时间", index = 41)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public String getIsActiveText() {
        return isActive != null && isActive ? "是" : "否";
    }
}
