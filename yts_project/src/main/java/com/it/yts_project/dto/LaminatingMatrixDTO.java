package com.it.yts_project.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 烫金后过胶－应用限制 矩阵 DTO。
 * 行 = 适用界面 + 过胶种类 + 烫后过胶后加工；列 = 烫金纸选用（系列+型号+街货）；单元格 = V/X。
 */
@Data
public class LaminatingMatrixDTO {

    /** 列定义：每列 = 烫金纸（系列+型号+是否街货），顺序与表格列一致 */
    private List<FoilColumnDef> columnDefs;

    /** 数据行：每行对应一组 (适用界面, 过胶种类, 烫后过胶后加工) */
    private List<MatrixRow> rows;

    @Data
    public static class FoilColumnDef {
        /** 烫金纸系列（对应 products.name） */
        private String foilSeries;
        /** 型号（对应 products.model_number） */
        private String modelNumber;
        private Boolean isJiehuo;
        /** 烫金纸类型（表头第二行，来自过胶兼容性列表的產品類型） */
        private String foilTypeName;
        /** 价格（用于列排序：从左到右从小到大，来自 pricing 表） */
        private BigDecimal price;

        public FoilColumnDef() {}
        public FoilColumnDef(String foilSeries, String modelNumber, Boolean isJiehuo) {
            this.foilSeries = foilSeries;
            this.modelNumber = modelNumber;
            this.isJiehuo = isJiehuo != null && isJiehuo;
        }

        /** 用于 cells Map 的 key，含產品類型以便同一型號在不同燙金紙性能類型下各占一列；格式 產品類型|系列|型號|J/N */
        public String cellKey() {
            String j = (isJiehuo != null && isJiehuo) ? "J" : "N";
            String type = (foilTypeName != null ? foilTypeName.trim() : "");
            return type + "|" + (foilSeries != null ? foilSeries : "") + "|" + (modelNumber != null ? modelNumber : "") + "|" + j;
        }

        /** 烫金纸类型（表头第二行）：来自 products.hot_stamping_paper_type，非系列名 */
        public String getTypeGroupName() {
            return foilTypeName != null ? foilTypeName.trim() : "";
        }
    }

    @Data
    public static class MatrixRow {
        /** 适用界面（产品类型） */
        private String productType;
        /** 过胶材料ID */
        private Integer laminationMaterialId;
        /** 过胶材料名称（原始） */
        private String laminationMaterialName;
        /** 过胶种类-前缀（「-」前的部分，如 普通预涂菲林） */
        private String laminationMaterialPrefix;
        /** 过胶种类-后缀（「-」后的部分，如 光/啞膠，无编号） */
        private String laminationMaterialSuffix;
        /** 过胶后工序ID（可为 null 表示无） */
        private Integer postProcessingStepId;
        /** 过胶后工序名称 */
        private String postProcessingStepName;
        /**
         * key = FoilColumnDef.cellKey()，value = V / X / 空
         */
        private Map<String, String> cells = new LinkedHashMap<>();
    }
}
