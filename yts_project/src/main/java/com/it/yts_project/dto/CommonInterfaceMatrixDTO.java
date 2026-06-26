package com.it.yts_project.dto;

import lombok.Data;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 常用界面燙印性 組合應用表 矩阵 DTO。
 * 用于导出/导入时在内存中表示完整的矩阵数据。
 */
@Data
public class CommonInterfaceMatrixDTO {

    /** 列头定义（有序） */
    private List<ColumnDef> patternAreaColumns;
    private List<ColumnDef> stampingTypeColumns;
    private List<ColumnDef> preProcessColumns;
    private List<ColumnDef> specialInterfaceColumns;

    /** 数据行（有序：按 paperType 分组，组内按 seriesName 排序） */
    private List<MatrixRow> rows;

    @Data
    public static class ColumnDef {
        private Integer id;
        private String name;
        /** 一级分组，如 常用界面、特殊界面 */
        private String group;
        /** 界面分类表头，如 前工序()印刷/過油面、過膠面、膠片、牛油紙 */
        private String subGroup;

        public ColumnDef() {}
        public ColumnDef(Integer id, String name, String group) {
            this.id = id;
            this.name = name;
            this.group = group;
        }
        public ColumnDef(Integer id, String name, String group, String subGroup) {
            this.id = id;
            this.name = name;
            this.group = group;
            this.subGroup = subGroup;
        }
    }

    @Data
    public static class MatrixRow {
        private String paperType;
        private String seriesName;

        /**
         * key = columnDef.id 的字符串表示（带前缀区分表来源），
         * value = V / X / NA / ▷ / 空
         * 前缀: PA_ = patternArea, ST_ = stampingType, PP_ = preProcess, SI_ = specialInterface
         */
        private Map<String, String> cells = new LinkedHashMap<>();
    }
}
