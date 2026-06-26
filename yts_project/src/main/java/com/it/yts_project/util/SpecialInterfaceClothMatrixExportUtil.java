package com.it.yts_project.util;

import com.it.yts_project.dto.SpecialInterfaceClothMatrixDTO;
import com.it.yts_project.dto.SpecialInterfaceClothMatrixDTO.MatrixRow;
import com.it.yts_project.dto.SpecialInterfaceClothMatrixDTO.SeriesColumnDef;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 「布面紙+燙金」－組合應用表 矩阵导出工具。
 * 表头分组（普通金紙系列、耐磨金紙系列、鐳射金紙系列等）全部从 DTO 的 columnDefs.foilType 动态读取，不写死。
 */
public final class SpecialInterfaceClothMatrixExportUtil {

    private static final String TITLE = "「布面紙+燙金」－組合應用表";
    private static final String DESC = "目前布面紙上存在燙金困難、不穩定問題，經測試確認以下部分布料需絲印打底後才可燙金。為確保大貨正常生產，故制定以下「布面紙+燙金」資料供參考選用。";
    private static final String LEGEND = "圖標說明：√ 可以直接燙金；× 不可以燙金；▽ 需要作「絲印打底處理」再燙金";
    private static final byte[] UTF8_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    /** 表头及材質/物料型號列底色（与客户提供色板一致） */
    private static final String LIGHT_BLUE = "#ADD8E6";
    /** Docx 表头底色（POI 用 6 位十六进制，无 #） */
    private static final String HEADER_FILL = "ADD8E6";
    /** A4 210×297mm，twips */
    private static final long A4_W = 11906L;
    private static final long A4_H = 16838L;
    private static final long MARGIN_TOP_BOTTOM_TWIPS = 567L;
    private static final long MARGIN_LEFT_RIGHT_TWIPS = 425L;

    private SpecialInterfaceClothMatrixExportUtil() {}

    /* ======================== Excel (CSV) ======================== */

    public static byte[] exportToExcel(SpecialInterfaceClothMatrixDTO matrix) throws Exception {
        List<SeriesColumnDef> cols = matrix.getColumnDefs();
        List<MatrixRow> rows = matrix.getRows();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(UTF8_BOM);

        StringBuilder sb = new StringBuilder();
        sb.append(escapeCsv(TITLE)).append("\n");
        sb.append(escapeCsv(DESC)).append("\n");
        sb.append(escapeCsv(LEGEND)).append("\n");

        // 表头行1：烫金纸类型分组
        List<String> groupHeader = new ArrayList<>();
        groupHeader.add(""); // 材質
        groupHeader.add(""); // 物料型號
        if (cols != null) {
            for (SeriesColumnDef col : cols) {
                groupHeader.add(escapeCsv(nullToEmpty(col.getFoilType())));
            }
        }
        sb.append(String.join(",", groupHeader)).append("\n");

        // 表头行2：材質、物料型號、各系列名
        List<String> headerCells = new ArrayList<>();
        headerCells.add(escapeCsv("材質"));
        headerCells.add(escapeCsv("物料型號"));
        if (cols != null) {
            for (SeriesColumnDef col : cols) {
                headerCells.add(escapeCsv(nullToEmpty(col.getSeriesName())));
            }
        }
        sb.append(String.join(",", headerCells)).append("\n");

        if (rows != null && !rows.isEmpty()) {
            List<MatrixRow> sortedRows = new ArrayList<>(rows);
            sortedRows.sort(Comparator
                .comparing(MatrixRow::getMaterialCategory, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(MatrixRow::getMaterialModel, Comparator.nullsFirst(Comparator.naturalOrder())));
            for (MatrixRow row : sortedRows) {
                List<String> rowCells = new ArrayList<>();
                rowCells.add(escapeCsv(nullToEmpty(row.getMaterialCategory())));
                rowCells.add(escapeCsv(nullToEmpty(row.getMaterialModel())));
                if (cols != null) {
                    for (SeriesColumnDef col : cols) {
                        String cell = row.getCells() != null ? row.getCells().get(col.cellKey()) : null;
                        rowCells.add(escapeCsv(nullToEmpty(cell)));
                    }
                }
                sb.append(String.join(",", rowCells)).append("\n");
            }
        }

        out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        return out.toByteArray();
    }

    /* ======================== Word (HTML) ======================== */

    public static byte[] exportToWord(SpecialInterfaceClothMatrixDTO matrix) throws Exception {
        List<SeriesColumnDef> cols = matrix.getColumnDefs();
        List<MatrixRow> rows = matrix.getRows();
        int colCount = cols != null ? cols.size() : 0;

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/><title>").append(escapeHtml(TITLE)).append("</title>");
        html.append("<style>");
        html.append("table{border-collapse:collapse;width:100%;border:1px solid #000}");
        html.append(" th,td{border:1px solid #000;padding:4px 8px}");
        html.append(" thead th{font-weight:bold;text-align:center;background:").append(LIGHT_BLUE).append("}");
        html.append(" .th-group{text-align:center}");
        html.append(" .th-cloth{text-align:center}");
        html.append(" .th-foil-type{text-align:center}");
        html.append(" .td-material{text-align:center;vertical-align:middle}");
        html.append(" .td-model{text-align:left}");
        html.append(" .td-symbol{text-align:center}");
        html.append(" tbody td{background:#fff}");
        html.append(" .th-title{text-align:center;font-size:14pt;font-weight:bold;background:").append(LIGHT_BLUE).append("}");
        html.append(" .th-desc{text-align:left;font-size:9pt;font-weight:normal;background:").append(LIGHT_BLUE).append("}");
        html.append(" .th-legend{text-align:left;font-size:9pt;font-weight:normal;background:").append(LIGHT_BLUE).append("}");
        html.append("</style></head><body>");

        int totalCols = 2 + colCount;
        html.append("<table><thead>");
        // 第1行：标题（全列合并）
        html.append("<tr><th class=\"th-title\" colspan=\"").append(totalCols).append("\">")
            .append(escapeHtml(TITLE)).append("</th></tr>");
        // 第2行：说明（全列合并）
        html.append("<tr><th class=\"th-desc\" colspan=\"").append(totalCols).append("\">")
            .append(escapeHtml(DESC)).append("</th></tr>");
        // 第3行：图标说明（全列合并）
        html.append("<tr><th class=\"th-legend\" colspan=\"").append(totalCols).append("\">")
            .append(escapeHtml(LEGEND)).append("</th></tr>");
        // 第4行：布面纸(colspan=2) + 烫金纸(colspan=N)，布面纸区域用浅蓝底色
        html.append("<tr>");
        html.append("<th class=\"th-cloth\" colspan=\"2\">布面纸</th>");
        if (colCount > 0) {
            html.append("<th class=\"th-group\" colspan=\"").append(colCount).append("\">烫金纸</th>");
        }
        html.append("</tr>");

        // 第2行：材質(rowspan=2) + 物料型號(rowspan=2) + 烫金纸类型分组（从 columnDefs 的 foilType 动态生成 colspan）
        if (colCount > 0 && cols != null) {
            html.append("<tr>");
            html.append("<th class=\"th-cloth\" rowspan=\"2\">材質</th><th class=\"th-cloth\" rowspan=\"2\">物料型號</th>");
            List<GroupRun> groupRuns = buildFoilTypeGroupRuns(cols);
            for (GroupRun gr : groupRuns) {
                html.append("<th class=\"th-foil-type\" colspan=\"").append(gr.count).append("\">")
                    .append(escapeHtml(gr.label)).append("</th>");
            }
            html.append("</tr>");
        }

        // 第3行：各系列名（材質/物料型號已由上行 rowspan=2 覆盖）
        html.append("<tr>");
        if (cols != null) {
            for (SeriesColumnDef col : cols) {
                html.append("<th>").append(escapeHtml(nullToEmpty(col.getSeriesName()))).append("</th>");
            }
        }
        html.append("</tr></thead><tbody>");

        if (rows != null && !rows.isEmpty()) {
            List<MatrixRow> sortedRows = new ArrayList<>(rows);
            sortedRows.sort(Comparator
                .comparing(MatrixRow::getMaterialCategory, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(MatrixRow::getMaterialModel, Comparator.nullsFirst(Comparator.naturalOrder())));
            List<int[]> materialRuns = buildMaterialRuns(sortedRows);
            int runIdx = 0;
            int rowInRun = 0;
            int runSpan = materialRuns.isEmpty() ? 0 : materialRuns.get(0)[1];
            for (int i = 0; i < sortedRows.size(); i++) {
                MatrixRow row = sortedRows.get(i);
                html.append("<tr>");
                if (runSpan > 0 && rowInRun == 0) {
                    html.append("<td class=\"td-material\" rowspan=\"").append(runSpan).append("\">")
                        .append(escapeHtml(nullToEmpty(row.getMaterialCategory()))).append("</td>");
                }
                html.append("<td class=\"td-model\">").append(escapeHtml(nullToEmpty(row.getMaterialModel()))).append("</td>");
                if (cols != null) {
                    for (SeriesColumnDef col : cols) {
                        String cell = row.getCells() != null ? row.getCells().get(col.cellKey()) : null;
                        appendCompatCell(html, cell);
                    }
                }
                html.append("</tr>");
                rowInRun++;
                if (runSpan > 0 && rowInRun >= runSpan) {
                    runIdx++;
                    rowInRun = 0;
                    runSpan = runIdx < materialRuns.size() ? materialRuns.get(runIdx)[1] : 0;
                }
            }
        }
        html.append("</tbody></table></body></html>");
        return html.toString().getBytes(StandardCharsets.UTF_8);
    }

    /* ======================== Word (DOCX OOXML) ======================== */

    /**
     * 導出為真正的 .docx（OOXML），與「燙金+印刷UV」一致，A4、縱向、頁邊距 1cm/0.75cm。
     */
    public static byte[] exportToDocx(SpecialInterfaceClothMatrixDTO matrix) throws Exception {
        List<SeriesColumnDef> cols = matrix.getColumnDefs();
        List<MatrixRow> rows = matrix.getRows();
        int colCount = cols != null ? cols.size() : 0;
        int totalCols = Math.max(2, 2 + colCount);

        List<MatrixRow> sortedRows = new ArrayList<>();
        if (rows != null && !rows.isEmpty()) {
            sortedRows.addAll(rows);
            sortedRows.sort(Comparator
                .comparing(MatrixRow::getMaterialCategory, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(MatrixRow::getMaterialModel, Comparator.nullsFirst(Comparator.naturalOrder())));
        }
        int dataRows = sortedRows.size();
        int totalRows = 6 + dataRows;

        try (XWPFDocument doc = new XWPFDocument()) {
            applySectionProperties(doc);
            XWPFTable table = doc.createTable(totalRows, totalCols);
            table.setWidth("100%");

            // Row 0: 标题
            setCellText(table.getRow(0).getCell(0), TITLE, 1);
            mergeCellsHorizontally(table, 0, 0, totalCols - 1);
            setCellShading(table.getRow(0).getCell(0), HEADER_FILL);
            setCellParagraphCenter(table.getRow(0).getCell(0));

            // Row 1: 说明
            setCellText(table.getRow(1).getCell(0), DESC, 1);
            mergeCellsHorizontally(table, 1, 0, totalCols - 1);
            setCellShading(table.getRow(1).getCell(0), HEADER_FILL);

            // Row 2: 图标说明
            setCellText(table.getRow(2).getCell(0), LEGEND, 1);
            mergeCellsHorizontally(table, 2, 0, totalCols - 1);
            setCellShading(table.getRow(2).getCell(0), HEADER_FILL);

            // Row 3: 布面纸(colspan=2) + 烫金纸(colspan=colCount)
            setCellText(table.getRow(3).getCell(0), "布面纸", 1);
            mergeCellsHorizontally(table, 3, 0, 1);
            setCellShading(table.getRow(3).getCell(0), HEADER_FILL);
            setCellShading(table.getRow(3).getCell(1), HEADER_FILL);
            setCellParagraphCenter(table.getRow(3).getCell(0));
            if (colCount > 0) {
                setCellText(table.getRow(3).getCell(2), "烫金纸", 1);
                mergeCellsHorizontally(table, 3, 2, totalCols - 1);
                setCellParagraphCenter(table.getRow(3).getCell(2));
                for (int c = 2; c < totalCols; c++) {
                    setCellShading(table.getRow(3).getCell(c), HEADER_FILL);
                }
            }

            // Row 4: 材質、物料型號、烫金纸类型分组
            setCellText(table.getRow(4).getCell(0), "材質", 1);
            setCellText(table.getRow(4).getCell(1), "物料型號", 1);
            setCellShading(table.getRow(4).getCell(0), HEADER_FILL);
            setCellShading(table.getRow(4).getCell(1), HEADER_FILL);
            setCellParagraphCenter(table.getRow(4).getCell(0));
            setCellParagraphCenter(table.getRow(4).getCell(1));
            if (colCount > 0 && cols != null) {
                List<GroupRun> groupRuns = buildFoilTypeGroupRuns(cols);
                int startCol = 2;
                for (GroupRun gr : groupRuns) {
                    setCellText(table.getRow(4).getCell(startCol), gr.label, 1);
                    mergeCellsHorizontally(table, 4, startCol, startCol + gr.count - 1);
                    for (int c = startCol; c < startCol + gr.count; c++) {
                        setCellShading(table.getRow(4).getCell(c), HEADER_FILL);
                        setCellParagraphCenter(table.getRow(4).getCell(c));
                    }
                    startCol += gr.count;
                }
            }
            mergeCellsVertically(table, 0, 4, 5);
            mergeCellsVertically(table, 1, 4, 5);

            // Row 5: 系列名（材質/物料型號 列 vMerge 延續）
            setVMergeContinue(table.getRow(5).getCell(0));
            setVMergeContinue(table.getRow(5).getCell(1));
            if (colCount > 0 && cols != null) {
                for (int c = 0; c < colCount; c++) {
                    XWPFTableCell cell = table.getRow(5).getCell(2 + c);
                    setCellText(cell, nullToEmpty(cols.get(c).getSeriesName()), 1);
                    setCellShading(cell, HEADER_FILL);
                    setCellParagraphCenter(cell);
                }
            }

            // Data rows
            List<int[]> materialRuns = buildMaterialRuns(sortedRows);
            int runIdx = 0;
            int rowInRun = 0;
            int runSpan = materialRuns.isEmpty() ? 0 : materialRuns.get(0)[1];
            for (int i = 0; i < dataRows; i++) {
                MatrixRow row = sortedRows.get(i);
                XWPFTableRow tableRow = table.getRow(6 + i);
                if (runSpan > 0 && rowInRun == 0) {
                    setCellText(tableRow.getCell(0), nullToEmpty(row.getMaterialCategory()), 1);
                    mergeCellsVertically(table, 0, 6 + i, 6 + i + runSpan - 1);
                    setCellParagraphCenter(tableRow.getCell(0));
                    setCellVerticalCenter(tableRow.getCell(0));
                } else if (runSpan > 0) {
                    setVMergeContinue(tableRow.getCell(0));
                }
                setCellText(tableRow.getCell(1), nullToEmpty(row.getMaterialModel()), 1);
                if (cols != null) {
                    for (int c = 0; c < colCount; c++) {
                        String cellVal = row.getCells() != null ? row.getCells().get(cols.get(c).cellKey()) : null;
                        setCellText(tableRow.getCell(2 + c), compatDisplay(cellVal), 1);
                        setCellParagraphCenter(tableRow.getCell(2 + c));
                    }
                }
                rowInRun++;
                if (runSpan > 0 && rowInRun >= runSpan) {
                    runIdx++;
                    rowInRun = 0;
                    runSpan = runIdx < materialRuns.size() ? materialRuns.get(runIdx)[1] : 0;
                }
            }

            applyTableCenterAndTightPadding(table);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                doc.write(out);
                return out.toByteArray();
            }
        }
    }

    @SuppressWarnings("java:S3011")
    private static void applySectionProperties(XWPFDocument doc) {
        org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody body = doc.getDocument().getBody();
        CTSectPr sectPr = body.getSectPr();
        if (sectPr == null) sectPr = body.addNewSectPr();
        Object pgSz = sectPr.isSetPgSz() ? invoke(sectPr, "getPgSz") : invoke(sectPr, "addNewPgSz");
        invokeSet(pgSz, "setW", BigInteger.valueOf(A4_W));
        invokeSet(pgSz, "setH", BigInteger.valueOf(A4_H));
        Object pgMar = sectPr.isSetPgMar() ? invoke(sectPr, "getPgMar") : invoke(sectPr, "addNewPgMar");
        invokeSet(pgMar, "setLeft", BigInteger.valueOf(MARGIN_LEFT_RIGHT_TWIPS));
        invokeSet(pgMar, "setRight", BigInteger.valueOf(MARGIN_LEFT_RIGHT_TWIPS));
        invokeSet(pgMar, "setTop", BigInteger.valueOf(MARGIN_TOP_BOTTOM_TWIPS));
        invokeSet(pgMar, "setBottom", BigInteger.valueOf(MARGIN_TOP_BOTTOM_TWIPS));
    }

    private static Object invoke(Object target, String methodName) {
        try {
            Method m = target.getClass().getMethod(methodName);
            return m.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException("Reflection " + methodName, e);
        }
    }

    private static void invokeSet(Object target, String methodName, BigInteger value) {
        Class<?> clazz = target.getClass();
        long val = value.longValue();
        Object[] candidates = {value, Long.valueOf(val), val};
        for (Method m : clazz.getMethods()) {
            if (!m.getName().equals(methodName) || m.getParameterCount() != 1) continue;
            Class<?> p = m.getParameterTypes()[0];
            for (Object arg : candidates) {
                if (arg instanceof Long && (p == long.class || p == Long.class)) {
                    try {
                        m.invoke(target, p == long.class ? ((Long) arg).longValue() : arg);
                        return;
                    } catch (Exception ignored) { }
                    break;
                }
                if (arg instanceof BigInteger && p == BigInteger.class) {
                    try {
                        m.invoke(target, arg);
                        return;
                    } catch (Exception ignored) { }
                    break;
                }
                if (p.isAssignableFrom(arg.getClass())) {
                    try {
                        m.invoke(target, arg);
                        return;
                    } catch (Exception ignored) { }
                }
            }
            try {
                m.invoke(target, value);
                return;
            } catch (Exception e1) {
                try {
                    m.invoke(target, Long.valueOf(val));
                    return;
                } catch (Exception e2) {
                    // continue
                }
            }
        }
        throw new RuntimeException("No compatible setter: " + methodName + " on " + clazz.getName());
    }

    private static void setCellText(XWPFTableCell cell, String text, int colspan) {
        cell.setText(text != null ? text : "");
        if (colspan > 1 && cell.getCTTc().getTcPr() == null) cell.getCTTc().addNewTcPr();
        if (colspan > 1) {
            if (cell.getCTTc().getTcPr().getGridSpan() == null) cell.getCTTc().getTcPr().addNewGridSpan();
            cell.getCTTc().getTcPr().getGridSpan().setVal(BigInteger.valueOf(colspan));
        }
    }

    private static void setCellShading(XWPFTableCell cell, String fillHex) {
        CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
        CTShd shd = tcPr.isSetShd() ? tcPr.getShd() : tcPr.addNewShd();
        shd.setFill(fillHex);
        shd.setVal(STShd.CLEAR);
    }

    private static void setCellParagraphCenter(XWPFTableCell cell) {
        if (cell == null) return;
        for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : cell.getParagraphs()) {
            p.setAlignment(ParagraphAlignment.CENTER);
        }
    }

    private static void setCellVerticalCenter(XWPFTableCell cell) {
        if (cell != null) {
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        }
    }

    private static void setVMergeContinue(XWPFTableCell cell) {
        if (cell.getCTTc().getTcPr() == null) cell.getCTTc().addNewTcPr();
        if (cell.getCTTc().getTcPr().getVMerge() == null) cell.getCTTc().getTcPr().addNewVMerge();
        cell.getCTTc().getTcPr().getVMerge().setVal(STMerge.CONTINUE);
    }

    private static void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int r = fromRow; r <= toRow; r++) {
            XWPFTableCell cell = table.getRow(r).getCell(col);
            if (cell.getCTTc().getTcPr() == null) cell.getCTTc().addNewTcPr();
            if (cell.getCTTc().getTcPr().getVMerge() == null) cell.getCTTc().getTcPr().addNewVMerge();
            cell.getCTTc().getTcPr().getVMerge().setVal(r == fromRow ? STMerge.RESTART : STMerge.CONTINUE);
        }
    }

    private static void mergeCellsHorizontally(XWPFTable table, int rowIndex, int colStart, int colEnd) {
        XWPFTableRow row = table.getRow(rowIndex);
        for (int c = colStart; c <= colEnd; c++) {
            XWPFTableCell cell = row.getCell(c);
            if (cell.getCTTc().getTcPr() == null) cell.getCTTc().addNewTcPr();
            if (cell.getCTTc().getTcPr().getHMerge() == null) cell.getCTTc().getTcPr().addNewHMerge();
            cell.getCTTc().getTcPr().getHMerge().setVal(c == colStart ? STMerge.RESTART : STMerge.CONTINUE);
        }
    }

    private static void applyTableCenterAndTightPadding(XWPFTable table) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                setCellParagraphCenter(cell);
                setCellVerticalCenter(cell);
            }
        }
    }

    private static String compatDisplay(String value) {
        String v = value != null ? value.trim() : "";
        if ("V".equals(v)) return "√";
        if ("X".equals(v)) return "×";
        if ("▷".equals(v)) return "▽";
        return v;
    }

    /* ======================== 内部工具方法 ======================== */

    private static final class GroupRun {
        final String label;
        final int count;
        GroupRun(String label, int count) { this.label = label; this.count = count; }
    }

    /** 按 columnDefs 的 foilType 连续分段，生成 (foilType, colspan) 列表 */
    private static List<GroupRun> buildFoilTypeGroupRuns(List<SeriesColumnDef> cols) {
        List<GroupRun> runs = new ArrayList<>();
        if (cols == null || cols.isEmpty()) return runs;
        String prevGroup = null;
        int count = 0;
        for (SeriesColumnDef col : cols) {
            String group = nullToEmpty(col.getFoilType());
            if (group.isEmpty()) group = "其他";
            if (prevGroup != null && !prevGroup.equals(group)) {
                runs.add(new GroupRun(prevGroup, count));
                count = 0;
            }
            prevGroup = group;
            count++;
        }
        if (prevGroup != null) runs.add(new GroupRun(prevGroup, count));
        return runs;
    }

    private static List<int[]> buildMaterialRuns(List<MatrixRow> rows) {
        List<int[]> runs = new ArrayList<>();
        if (rows == null || rows.isEmpty()) return runs;
        String prev = null;
        int start = 0;
        for (int i = 0; i <= rows.size(); i++) {
            String cur = i < rows.size() ? nullToEmpty(rows.get(i).getMaterialCategory()) : null;
            if (prev != null && !prev.equals(cur)) {
                runs.add(new int[]{start, i - start});
                start = i;
            }
            prev = i < rows.size() ? cur : null;
        }
        if (start < rows.size()) {
            runs.add(new int[]{start, rows.size() - start});
        }
        return runs;
    }

    private static void appendCompatCell(StringBuilder html, String value) {
        String v = value != null ? value.trim() : "";
        String display = "V".equals(v) ? "√" : ("X".equals(v) ? "×" : ("▷".equals(v) ? "▽" : v));
        html.append("<td class=\"td-symbol\">").append(escapeHtml(display)).append("</td>");
    }

    private static String nullToEmpty(String s) {
        return s != null ? s : "";
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
