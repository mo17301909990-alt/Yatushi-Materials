package com.it.yts_project.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 燙金+絲印LED UV灑閃粉 配对烫金纸型号矩阵 DTO。
 * 行 = 加工重叠组合（固定「烫金+丝印LED UV洒闪粉」）+ 烫金界面（有粉纸面/无粉纸面等）；
 * 列 = 按 ①普通烫金纸、②耐磨烫金纸、③镭射烫金纸 分组的烫金纸系列；单元格 = V/X。
 */
@Data
public class LeduvglitterMatrixDTO {

    /** 列定义：按 paperType 分组，每组内为系列名，顺序与表格列一致。cellKey = paperType + "|" + seriesName */
    private List<SeriesColumnDef> columnDefs;

    /** 数据行：每行对应一个烫金界面（布面纸类型） */
    private List<MatrixRow> rows;

    /** 固定加工组合名称 */
    public static final String PROCESSING_COMBO = "燙金 + 絲印LED UV灑閃粉";

    @Data
    public static class SeriesColumnDef {
        /** 燙金紙性能類型：普通烫金纸 / 耐磨烫金纸 / 镭射烫金纸 */
        private String paperType;
        /** 烫金纸系列名称（如 SY6/SY6+系列、G1系列） */
        private String seriesName;

        /** 用于 cells Map 的 key */
        public String cellKey() {
            return (paperType != null ? paperType : "") + "|" + (seriesName != null ? seriesName : "");
        }

        public SeriesColumnDef() {}
        public SeriesColumnDef(String paperType, String seriesName) {
            this.paperType = paperType;
            this.seriesName = seriesName;
        }
    }

    @Data
    public static class MatrixRow {
        /** 烫金界面（布面纸类型名称，如 有粉纸面、无粉纸面） */
        private String hotStampingInterface;
        /** 布面纸类型ID（用于导入回写） */
        private Integer clothPaperTypeId;
        /**
         * key = SeriesColumnDef.cellKey()，value = V / X / 空
         */
        private Map<String, String> cells = new LinkedHashMap<>();
    }
}
