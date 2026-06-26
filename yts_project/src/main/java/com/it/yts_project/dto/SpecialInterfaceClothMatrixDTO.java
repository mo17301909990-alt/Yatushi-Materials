package com.it.yts_project.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 「布面紙+燙金」－組合應用表 矩阵 DTO。
 * 行 = 特殊界面布面纸类型（材质 + 物料型号），列 = (烫金纸类型, 燙金紙系列) 组合，单元格 = V/X/▷。
 */
@Data
public class SpecialInterfaceClothMatrixDTO {

    /** 列定义：每列 = (烫金纸类型 + 燙金紙系列)，顺序与表格列一致 */
    private List<SeriesColumnDef> columnDefs;

    /** 数据行：每行对应一个特殊界面布面纸类型 */
    private List<MatrixRow> rows;

    @Data
    public static class SeriesColumnDef {
        /** 烫金纸类型（如 普通金紙、耐磨金紙、鐳射金紙），来自 cloth_paper_compatibility.paper_type */
        private String foilType;
        /** 燙金紙系列名称（如 L187系列、ST系列），来自 cloth_paper_compatibility.product_name */
        private String seriesName;

        public SeriesColumnDef() {}
        public SeriesColumnDef(String foilType, String seriesName) {
            this.foilType = foilType;
            this.seriesName = seriesName;
        }

        /** 用于 cells Map 的 key，区分同名系列在不同类型下的列 */
        public String cellKey() {
            return (foilType != null ? foilType : "") + "\t" + (seriesName != null ? seriesName : "");
        }
    }

    @Data
    public static class MatrixRow {
        private Integer clothPaperTypeId;
        /** 材质（对应 category 或 typeName） */
        private String materialCategory;
        /** 物料型号（对应 typeName） */
        private String materialModel;
        /**
         * key = SeriesColumnDef.cellKey()，value = V / X / ▷ / NA / 空
         */
        private Map<String, String> cells = new LinkedHashMap<>();
    }
}
