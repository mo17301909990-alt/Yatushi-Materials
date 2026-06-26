package com.it.yts_project.util;

import com.it.yts_project.dto.CommonInterfaceMatrixDTO;
import com.it.yts_project.dto.CommonInterfaceMatrixDTO.ColumnDef;
import com.it.yts_project.dto.CommonInterfaceMatrixDTO.MatrixRow;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 常用界面燙印性 組合應用表 — 矩阵导出工具。
 * Excel 导出为 CSV（用 Excel 可直接打开，避免 POI NoSuchFieldError: Factory）；
 * Word 导出为 HTML（.doc）或真正 .docx（OOXML，与燙金+印刷UV 一致）。
 * 表格结构：标题、图例、表头(類型|系列|燙金圖案…|燙金類型…|常用界面…|特殊界面…)、数据行。
 */
public final class CommonInterfaceMatrixExportUtil {

    private static final String TITLE = "五、燙金紙－常用界面燙印性 組合應用表";
    private static final String LEGEND = "圖標說明: \"✓\": 可以加工; \"✗\": 不可以加工; \"▷\": 需要打底處理";
    private static final byte[] UTF8_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    /** 表头浅绿底（与 HTML 版一致） */
    private static final String HEADER_FILL = "C6EFCE";
    /** V 单元格浅黄底 */
    private static final String CELL_OK_FILL = "FFFACD";
    /** A3 橫向：420mm × 297mm（twips） */
    private static final long A3_LANDSCAPE_W = 23811L;
    private static final long A3_LANDSCAPE_H = 16838L;
    private static final long MARGIN_TWIPS = 567L;
    private static final String W_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";
    /** 列寬權重（表格 100% 時按比例分配）：類型/系列權重加大，避免被壓成細條 */
    private static final long WIDTH_TYPE = 4000L;
    private static final long WIDTH_SERIES = 2200L;
    private static final long WIDTH_PA_ST = 500L;
    /** 前工序/適用界面列寬，足夠顯示工藝類型、界面類型名稱（至少 2～4 個漢字） */
    private static final long WIDTH_COMPAT = 900L;

    private CommonInterfaceMatrixExportUtil() {}

    // ==================== Excel 导出（CSV，避免 POI XSSF NoSuchFieldError） ====================

    public static byte[] exportToExcel(CommonInterfaceMatrixDTO matrix) throws Exception {
        List<ColumnDef> paColumns = matrix.getPatternAreaColumns();
        List<ColumnDef> stColumns = matrix.getStampingTypeColumns();
        List<ColumnDef> ppColumns = matrix.getPreProcessColumns();
        List<ColumnDef> siColumns = matrix.getSpecialInterfaceColumns();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(UTF8_BOM);

        StringBuilder line = new StringBuilder();
        line.append(escapeCsv(TITLE)).append("\n");
        line.append(escapeCsv(LEGEND)).append("\n");

        List<String> headerCells = new ArrayList<>();
        headerCells.add("類型");
        headerCells.add("系列");
        for (ColumnDef c : paColumns) headerCells.add(c.getName());
        // 烫金分類：Excel 表頭同時顯示烫金類型與位置類型（若有），例如「平面烫金-到邊位」
        for (ColumnDef c : stColumns) {
            String stamping = c.getSubGroup() != null && !c.getSubGroup().trim().isEmpty() ? c.getSubGroup().trim() : "";
            String pos = c.getName() != null ? c.getName().trim() : "";
            if (!stamping.isEmpty() && !pos.isEmpty() && !"默认".equals(pos)) {
                headerCells.add(stamping + "-" + pos);
            } else if (!stamping.isEmpty()) {
                headerCells.add(stamping);
            } else {
                headerCells.add(c.getName());
            }
        }
        for (ColumnDef c : ppColumns) headerCells.add(c.getName());
        for (ColumnDef c : siColumns) headerCells.add(c.getName());
        line.append(String.join(",", headerCells.stream().map(CommonInterfaceMatrixExportUtil::escapeCsv).toList())).append("\n");

        List<MatrixRow> rows = matrix.getRows();
        List<int[]> typeRuns = buildTypeRuns(rows);
        int runIdx = 0, rowInRun = 0, runSpan = typeRuns.isEmpty() ? 0 : typeRuns.get(0)[1];
        for (MatrixRow mRow : rows) {
            List<String> rowCells = new ArrayList<>();
            String typeCell = (runSpan > 0 && rowInRun == 0) ? (mRow.getPaperType() != null ? mRow.getPaperType() : "") : "";
            rowCells.add(typeCell);
            rowCells.add(mRow.getSeriesName() != null ? mRow.getSeriesName() : "");
            for (ColumnDef c : paColumns) rowCells.add(mRow.getCells().getOrDefault("PA_" + c.getId(), ""));
            for (ColumnDef c : stColumns) rowCells.add(mRow.getCells().getOrDefault("ST_" + c.getId(), ""));
            for (ColumnDef c : ppColumns) rowCells.add(mRow.getCells().getOrDefault("PP_" + c.getId(), ""));
            for (ColumnDef c : siColumns) rowCells.add(mRow.getCells().getOrDefault("SI_" + c.getId(), ""));
            line.append(String.join(",", rowCells.stream().map(CommonInterfaceMatrixExportUtil::escapeCsv).toList())).append("\n");
            rowInRun++;
            if (runSpan > 0 && rowInRun >= runSpan) {
                runIdx++;
                rowInRun = 0;
                runSpan = runIdx < typeRuns.size() ? typeRuns.get(runIdx)[1] : 0;
            }
        }

        out.write(line.toString().getBytes(StandardCharsets.UTF_8));
        return out.toByteArray();
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    // ==================== Word 导出（HTML，可被 Word 打开，避免 POI XWPF NoSuchFieldError） ====================

    public static byte[] exportToWord(CommonInterfaceMatrixDTO matrix) throws Exception {
        List<ColumnDef> paColumns = matrix.getPatternAreaColumns();
        List<ColumnDef> stColumns = matrix.getStampingTypeColumns();
        List<ColumnDef> ppColumns = matrix.getPreProcessColumns();
        List<ColumnDef> siColumns = matrix.getSpecialInterfaceColumns();

        int paCount = paColumns.size(), stCount = stColumns.size(), ppCount = ppColumns.size(), siCount = siColumns.size();
        int colCompat = ppCount + siCount;

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/><title>").append(escapeHtml(TITLE)).append("</title>");
        html.append("<style>table{border-collapse:collapse;width:100%} th,td{border:1px solid #000;padding:4px 8px;text-align:center}");
        html.append("th{background:#c6efce;font-weight:bold} .title{text-align:center;font-size:14pt;font-weight:bold}");
        html.append(" .legend{font-size:9pt} .cell-ok{background:#fffacd} .cell-no{background:#fff}</style></head><body>");
        html.append("<p class=\"title\">").append(escapeHtml(TITLE)).append("</p>");
        html.append("<p class=\"legend\">").append(escapeHtml(LEGEND)).append("</p>");
        html.append("<table><thead>");

        html.append("<tr>");
        html.append("<th colspan=\"2\">金纸</th>");
        if (paCount > 0) html.append("<th colspan=\"").append(paCount).append("\">烫金图案</th>");
        if (stCount > 0) html.append("<th colspan=\"").append(stCount).append("\">烫金分类</th>");
        if (colCompat > 0) html.append("<th colspan=\"").append(colCompat).append("\">烫印性</th>");
        html.append("</tr><tr>");
        html.append("<th rowspan=\"3\">類型</th><th rowspan=\"3\">系列</th>");
        for (ColumnDef c : paColumns) html.append("<th rowspan=\"3\">").append(escapeHtml(c.getName())).append("</th>");
        for (ColumnDef c : stColumns) html.append("<th rowspan=\"3\">").append(escapeHtml(c.getName())).append("</th>");
        if (ppCount > 0) html.append("<th colspan=\"").append(ppCount).append("\">適用界面(前工序)</th>");
        if (siCount > 0) html.append("<th colspan=\"").append(siCount).append("\">適用界面</th>");
        html.append("</tr><tr>");
        List<SubGroupRun> compatRuns = buildSubGroupRuns(ppColumns, siColumns);
        for (SubGroupRun run : compatRuns) {
            html.append("<th colspan=\"").append(run.count).append("\">").append(escapeHtml(run.label)).append("</th>");
        }
        html.append("</tr><tr>");
        for (ColumnDef c : ppColumns) html.append("<th>").append(escapeHtml(c.getName())).append("</th>");
        for (ColumnDef c : siColumns) html.append("<th>").append(escapeHtml(c.getName())).append("</th>");
        html.append("</tr></thead><tbody>");

        List<MatrixRow> rows = matrix.getRows();
        List<int[]> typeRuns = buildTypeRuns(rows);
        int runIdx = 0, rowInRun = 0, runSpan = typeRuns.isEmpty() ? 0 : typeRuns.get(0)[1];
        for (MatrixRow mRow : rows) {
            html.append("<tr>");
            if (runSpan > 0 && rowInRun == 0) {
                html.append("<td rowspan=\"").append(runSpan).append("\">")
                    .append(escapeHtml(mRow.getPaperType() != null ? mRow.getPaperType() : "")).append("</td>");
            } else if (runSpan == 0 && rowInRun == 0) {
                html.append("<td>").append(escapeHtml(mRow.getPaperType() != null ? mRow.getPaperType() : "")).append("</td>");
            }
            html.append("<td>").append(escapeHtml(mRow.getSeriesName() != null ? mRow.getSeriesName() : "")).append("</td>");
            for (ColumnDef c : paColumns) {
                appendCompatCell(html, mRow.getCells().getOrDefault("PA_" + c.getId(), ""));
            }
            for (ColumnDef c : stColumns) {
                appendCompatCell(html, mRow.getCells().getOrDefault("ST_" + c.getId(), ""));
            }
            for (ColumnDef c : ppColumns) {
                appendCompatCell(html, mRow.getCells().getOrDefault("PP_" + c.getId(), ""));
            }
            for (ColumnDef c : siColumns) {
                appendCompatCell(html, mRow.getCells().getOrDefault("SI_" + c.getId(), ""));
            }
            html.append("</tr>");
            rowInRun++;
            if (runSpan > 0 && rowInRun >= runSpan) {
                runIdx++;
                rowInRun = 0;
                runSpan = runIdx < typeRuns.size() ? typeRuns.get(runIdx)[1] : 0;
            }
        }
        html.append("</tbody></table></body></html>");
        return html.toString().getBytes(StandardCharsets.UTF_8);
    }

    /** 烫印性下的界面分类分段，表头文案全部来自列定义的 subGroup（或 group 兜底），不写死。 */
    private static List<SubGroupRun> buildSubGroupRuns(List<ColumnDef> ppColumns, List<ColumnDef> siColumns) {
        List<SubGroupRun> runs = new ArrayList<>();
        String prev = null;
        int count = 0;
        if (ppColumns != null) {
            for (ColumnDef c : ppColumns) {
                String sg = (c.getSubGroup() != null && !c.getSubGroup().isEmpty()) ? c.getSubGroup() : c.getGroup();
                if (sg == null) sg = "";
                if (prev != null && prev.equals(sg)) count++; else { if (prev != null) runs.add(new SubGroupRun(prev, count)); prev = sg; count = 1; }
            }
        }
        if (siColumns != null) {
            for (ColumnDef c : siColumns) {
                String sg = (c.getSubGroup() != null && !c.getSubGroup().isEmpty()) ? c.getSubGroup() : c.getGroup();
                if (sg == null) sg = "";
                if (prev != null && prev.equals(sg)) count++; else { if (prev != null) runs.add(new SubGroupRun(prev, count)); prev = sg; count = 1; }
            }
        }
        if (prev != null) runs.add(new SubGroupRun(prev, count));
        return runs;
    }

    private static final class SubGroupRun {
        final String label;
        final int count;
        SubGroupRun(String label, int count) { this.label = label; this.count = count; }
    }

    /** 按連續相同 paperType 分段，每段 [startIndex, count]。當前用 count 即可。返回每段的 [0]=無用, [1]=行數。 */
    private static List<int[]> buildTypeRuns(List<MatrixRow> rows) {
        List<int[]> runs = new ArrayList<>();
        if (rows == null || rows.isEmpty()) return runs;
        String prev = null;
        int count = 0;
        for (MatrixRow r : rows) {
            String pt = r.getPaperType() != null ? r.getPaperType() : "";
            if (prev != null && prev.equals(pt)) {
                count++;
            } else {
                if (prev != null) runs.add(new int[] { 0, count });
                prev = pt;
                count = 1;
            }
        }
        if (prev != null) runs.add(new int[] { 0, count });
        return runs;
    }

    /** 客户版样式：√ 浅黄底，X 白底；显示 V→√、X→X */
    private static void appendCompatCell(StringBuilder html, String value) {
        String v = value != null ? value.trim() : "";
        String display = "V".equals(v) ? "√" : ("X".equals(v) ? "X" : v);
        String cls = "V".equals(v) ? "cell-ok" : ("X".equals(v) ? "cell-no" : "");
        if (!cls.isEmpty()) html.append("<td class=\"").append(cls).append("\">");
        else html.append("<td>");
        html.append(escapeHtml(display)).append("</td>");
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    // ==================== Word 导出为真正 .docx（OOXML） ====================

    /**
     * 導出為真正的 .docx（OOXML），A4、縱向、頁邊距約 1cm，與「燙金+印刷UV」矩陣導出方式一致。
     */
    public static byte[] exportToDocx(CommonInterfaceMatrixDTO matrix) throws Exception {
        if (matrix == null) {
            throw new IllegalArgumentException("CommonInterface matrix DTO is null");
        }
        List<ColumnDef> paColumns = matrix.getPatternAreaColumns() != null ? matrix.getPatternAreaColumns() : Collections.emptyList();
        List<ColumnDef> stColumns = matrix.getStampingTypeColumns() != null ? matrix.getStampingTypeColumns() : Collections.emptyList();
        List<ColumnDef> ppColumns = matrix.getPreProcessColumns() != null ? matrix.getPreProcessColumns() : Collections.emptyList();
        List<ColumnDef> siColumns = matrix.getSpecialInterfaceColumns() != null ? matrix.getSpecialInterfaceColumns() : Collections.emptyList();
        int paCount = paColumns.size();
        int stCount = stColumns.size();
        int ppCount = ppColumns.size();
        int siCount = siColumns.size();
        int totalCols = Math.max(2, 2 + paCount + stCount + ppCount + siCount);
        List<MatrixRow> rows = matrix.getRows() != null ? matrix.getRows() : Collections.emptyList();
        int dataRows = rows.size();
        int totalRows = 4 + dataRows;

        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph titleP = doc.createParagraph();
            titleP.setAlignment(ParagraphAlignment.CENTER);
            titleP.createRun().setText(TITLE);
            if (!titleP.getRuns().isEmpty()) {
                titleP.getRuns().get(0).setBold(true);
            }
            doc.createParagraph().createRun().setText(LEGEND);

            XWPFTable table = doc.createTable(totalRows, totalCols);
            // 表格寬度 100% 鋪滿頁面，列寬用 tblGrid 做比例分配，避免總寬過大被整體壓縮成細條
            table.setWidth("100%");
            if (table.getCTTbl().getTblGrid() == null) {
                table.getCTTbl().addNewTblGrid();
            }
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(WIDTH_TYPE));
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(WIDTH_SERIES));
            for (int c = 0; c < paCount + stCount; c++) {
                table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(WIDTH_PA_ST));
            }
            for (int c = 0; c < ppCount + siCount; c++) {
                table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(WIDTH_COMPAT));
            }

            // Row 0: 金纸(colspan 2) | 烫金图案(colspan pa) | 烫金分类(colspan st) | 烫印性(colspan pp+si)
            setCellText(table.getRow(0).getCell(0), "金纸", 1);
            mergeCellsHorizontally(table, 0, 0, 1);
            if (paCount > 0) {
                setCellText(table.getRow(0).getCell(2), "烫金图案", 1);
                mergeCellsHorizontally(table, 0, 2, 2 + paCount - 1);
            }
            if (stCount > 0) {
                setCellText(table.getRow(0).getCell(2 + paCount), "烫金分类", 1);
                mergeCellsHorizontally(table, 0, 2 + paCount, 2 + paCount + stCount - 1);
            }
            if (ppCount + siCount > 0) {
                setCellText(table.getRow(0).getCell(2 + paCount + stCount), "烫印性", 1);
                mergeCellsHorizontally(table, 0, 2 + paCount + stCount, totalCols - 1);
            }
            for (int c = 0; c < totalCols; c++) {
                setCellShading(table.getRow(0).getCell(c), HEADER_FILL);
                setCellParagraphCenter(table.getRow(0).getCell(c));
                setCellVerticalCenter(table.getRow(0).getCell(c));
            }

            // 預計算：哪些烫金類型「完全沒有區分位置類型」（該類型下所有列的位置類型都為空/默認），
            // 這類型在表頭第二行不顯示位置名稱，直接與第一行的烫金類型縱向合併。
            boolean[] stNoExplicitPosition = new boolean[stCount];
            if (stColumns != null && !stColumns.isEmpty()) {
                int idx = 0;
                while (idx < stColumns.size()) {
                    ColumnDef first = stColumns.get(idx);
                    String stamping = first.getSubGroup() != null ? first.getSubGroup().trim() : "";
                    int j = idx;
                    boolean hasNonDefaultPos = false;
                    while (j < stColumns.size()) {
                        ColumnDef cj = stColumns.get(j);
                        String sj = cj.getSubGroup() != null ? cj.getSubGroup().trim() : "";
                        if (!Objects.equals(sj, stamping)) break;
                        String name = cj.getName() != null ? cj.getName().trim() : "";
                        if (!name.isEmpty() && !"默认".equals(name)) {
                            hasNonDefaultPos = true;
                        }
                        j++;
                    }
                    // 若該烫金類型下所有列的位置類型均為空/默認，則整個分組視為「無位置維度」，第二行不需要單獨顯示位置。
                    if (!hasNonDefaultPos) {
                        for (int k = idx; k < j; k++) {
                            stNoExplicitPosition[k] = true;
                        }
                    }
                    idx = j;
                }
            }

            // Row 1: 類型(rowspan 3) | 系列(rowspan 3) | PA cols(rowspan 3) | ST cols(按烫金類型分組橫向合併) | 適用界面(前工序)(colspan pp) | 適用界面(colspan si)
            setCellText(table.getRow(1).getCell(0), "類型", 1);
            setCellText(table.getRow(1).getCell(1), "系列", 1);
            mergeCellsVertically(table, 0, 1, 3);
            mergeCellsVertically(table, 1, 1, 3);
            int colIdx = 2;
            for (ColumnDef c : paColumns) { // PA 仍然整列縱向合併
                setCellText(table.getRow(1).getCell(colIdx), nullToEmpty(c.getName()), 1);
                mergeCellsVertically(table, colIdx, 1, 3);
                colIdx++;
            }
            // ST：第二行按烫金類型(subGroup) 分組橫向合併，第三行不再縱向合併
            int stStartCol = colIdx;
            if (!stColumns.isEmpty()) {
                String prevStamping = null;
                int runStartCol = stStartCol;
                int runCount = 0;
                for (int i = 0; i < stColumns.size(); i++) {
                    ColumnDef c = stColumns.get(i);
                    String stamping = c.getSubGroup() != null ? c.getSubGroup().trim() : "";
                    if (stamping.isEmpty()) {
                        stamping = nullToEmpty(c.getName());
                    }
                    if (prevStamping != null && prevStamping.equals(stamping)) {
                        runCount++;
                    } else {
                        if (prevStamping != null) {
                            setCellText(table.getRow(1).getCell(runStartCol), prevStamping, 1);
                            if (runCount > 1) {
                                mergeCellsHorizontally(table, 1, runStartCol, runStartCol + runCount - 1);
                            }
                            for (int k = 0; k < runCount; k++) {
                                setCellShading(table.getRow(1).getCell(runStartCol + k), HEADER_FILL);
                                setCellParagraphCenter(table.getRow(1).getCell(runStartCol + k));
                                setCellVerticalCenter(table.getRow(1).getCell(runStartCol + k));
                            }
                            runStartCol += runCount;
                        }
                        prevStamping = stamping;
                        runCount = 1;
                    }
                }
                if (prevStamping != null) {
                    setCellText(table.getRow(1).getCell(runStartCol), prevStamping, 1);
                    if (runCount > 1) {
                        mergeCellsHorizontally(table, 1, runStartCol, runStartCol + runCount - 1);
                    }
                    for (int k = 0; k < runCount; k++) {
                        setCellShading(table.getRow(1).getCell(runStartCol + k), HEADER_FILL);
                        setCellParagraphCenter(table.getRow(1).getCell(runStartCol + k));
                        setCellVerticalCenter(table.getRow(1).getCell(runStartCol + k));
                    }
                    colIdx = runStartCol + runCount;
                } else {
                    colIdx = stStartCol;
                }
            }
            if (ppCount > 0) {
                setCellText(table.getRow(1).getCell(colIdx), "適用界面(前工序)", 1);
                mergeCellsHorizontally(table, 1, colIdx, colIdx + ppCount - 1);
                colIdx += ppCount;
            }
            if (siCount > 0) {
                setCellText(table.getRow(1).getCell(colIdx), "適用界面", 1);
                mergeCellsHorizontally(table, 1, colIdx, totalCols - 1);
            }
            for (int c = 0; c < totalCols; c++) {
                setCellShading(table.getRow(1).getCell(c), HEADER_FILL);
                setCellParagraphCenter(table.getRow(1).getCell(c));
                setCellVerticalCenter(table.getRow(1).getCell(c));
            }

            // Row 2: vMerge continue for 類型,系列,PA; ST 顯示位置類型名稱(僅對有明確位置區分的列); 然後是適用界面分類分段
            for (int c = 0; c < 2 + paCount; c++) {
                setVMergeContinue(table.getRow(2).getCell(c));
            }
            // ST 區：逐列寫位置類型（ColumnDef.name，空則顯示「默认」）
            int stHeaderStartCol = 2 + paCount;
            colIdx = stHeaderStartCol;
            for (int i = 0; i < stColumns.size(); i++) {
                ColumnDef c = stColumns.get(i);
                XWPFTableCell cell = table.getRow(2).getCell(colIdx);
                if (!stNoExplicitPosition[i]) {
                    String pos = nullToEmpty(c.getName());
                    setCellText(cell, pos, 1);
                    setCellShading(cell, HEADER_FILL);
                    setCellParagraphCenter(cell);
                    setCellVerticalCenter(cell);
                } else {
                    // 該烫金類型完全沒有明確位置類型：不顯示第 2/3 行位置文字，將第 1~3 行整塊縱向合併
                    mergeCellsVertically(table, colIdx, 1, 3);
                }
                colIdx++;
            }
            List<SubGroupRun> compatRuns = buildSubGroupRuns(ppColumns, siColumns);
            colIdx = 2 + paCount + stCount;
            for (SubGroupRun run : compatRuns) {
                setCellText(table.getRow(2).getCell(colIdx), run.label, 1);
                if (run.count > 1) mergeCellsHorizontally(table, 2, colIdx, colIdx + run.count - 1);
                for (int k = 0; k < run.count; k++) {
                    setCellShading(table.getRow(2).getCell(colIdx + k), HEADER_FILL);
                    setCellParagraphCenter(table.getRow(2).getCell(colIdx + k));
                    setCellVerticalCenter(table.getRow(2).getCell(colIdx + k));
                }
                colIdx += run.count;
            }

            // Row 3: vMerge continue for 類型,系列,PA；ST 與 Row2 合併（有位置類型時合併 2~3 行，無位置類型時已在 1~3 行合併）；然後是 pp / si 列名
            for (int c = 0; c < 2 + paCount; c++) {
                setVMergeContinue(table.getRow(3).getCell(c));
            }
            // 對於有明確位置類型的烫金列：Row2/Row3 縱向合併，避免出現第三行空白格
            for (int i = 0; i < stColumns.size(); i++) {
                if (!stNoExplicitPosition[i]) {
                    int col = stHeaderStartCol + i;
                    mergeCellsVertically(table, col, 2, 3);
                }
            }
            colIdx = 2 + paCount + stCount;  // 必須重置，否則會寫到錯列/越界
            final int headerNameFontSize = 11;
            for (ColumnDef c : ppColumns) {
                XWPFTableCell cell = table.getRow(3).getCell(colIdx);
                setCellTextWithFontSize(cell, compatColumnHeaderName(c, "前工序"), headerNameFontSize);
                setCellShading(cell, HEADER_FILL);
                setCellParagraphCenter(cell);
                setCellVerticalCenter(cell);
                colIdx++;
            }
            for (ColumnDef c : siColumns) {
                XWPFTableCell cell = table.getRow(3).getCell(colIdx);
                setCellTextWithFontSize(cell, compatColumnHeaderName(c, "适用界面"), headerNameFontSize);
                setCellShading(cell, HEADER_FILL);
                setCellParagraphCenter(cell);
                setCellVerticalCenter(cell);
                colIdx++;
            }

            // 設置列寬，避免「類型」與「適用界面(前工序)」等表頭被壓縮不顯示
            XWPFTableRow widthRow = table.getRow(3);
            setCellWidthTwips(widthRow.getCell(0), WIDTH_TYPE);
            setCellWidthTwips(widthRow.getCell(1), WIDTH_SERIES);
            for (int c = 2; c < 2 + paCount + stCount; c++) {
                setCellWidthTwips(widthRow.getCell(c), WIDTH_PA_ST);
            }
            for (int c = 2 + paCount + stCount; c < totalCols; c++) {
                setCellWidthTwips(widthRow.getCell(c), WIDTH_COMPAT);
            }
            // 表頭第 2、3 行設最小行高，確保「前工序工藝類型」「適用界面類型名稱」可見
            setRowHeightAtLeast(table.getRow(2), 320);
            setRowHeightAtLeast(table.getRow(3), 400);

            // Data rows: 類型(rowspan by typeRuns), 系列, PA, ST, PP, SI cells
            List<int[]> typeRuns = buildTypeRuns(rows);
            int runIdx = 0, rowInRun = 0, runSpan = typeRuns.isEmpty() ? 0 : typeRuns.get(0)[1];
            for (int i = 0; i < dataRows; i++) {
                MatrixRow mRow = rows.get(i);
                XWPFTableRow tableRow = table.getRow(4 + i);
                String typeDisplay = dataRowTypeDisplay(mRow.getPaperType(), mRow.getSeriesName());
                String seriesDisplay = dataRowSeriesDisplay(mRow.getPaperType(), mRow.getSeriesName());
                if (runSpan > 0 && rowInRun == 0) {
                    setCellText(tableRow.getCell(0), typeDisplay, 1);
                    if (runSpan > 1) mergeCellsVertically(table, 0, 4 + i, 4 + i + runSpan - 1);
                } else if (runSpan == 0 && rowInRun == 0) {
                    setCellText(tableRow.getCell(0), typeDisplay, 1);
                } else {
                    setVMergeContinue(tableRow.getCell(0));
                }
                setCellText(tableRow.getCell(1), seriesDisplay, 1);
                colIdx = 2;
                Map<String, String> cells = mRow.getCells() != null ? mRow.getCells() : Collections.emptyMap();
                for (ColumnDef c : paColumns) {
                    appendCompatCellDocx(tableRow.getCell(colIdx), cells.getOrDefault("PA_" + c.getId(), ""));
                    colIdx++;
                }
                for (ColumnDef c : stColumns) {
                    appendCompatCellDocx(tableRow.getCell(colIdx), cells.getOrDefault("ST_" + c.getId(), ""));
                    colIdx++;
                }
                for (ColumnDef c : ppColumns) {
                    appendCompatCellDocx(tableRow.getCell(colIdx), cells.getOrDefault("PP_" + c.getId(), ""));
                    colIdx++;
                }
                for (ColumnDef c : siColumns) {
                    appendCompatCellDocx(tableRow.getCell(colIdx), cells.getOrDefault("SI_" + c.getId(), ""));
                    colIdx++;
                }
                setCellParagraphCenter(tableRow.getCell(1));
                setCellVerticalCenter(tableRow.getCell(0));
                setCellVerticalCenter(tableRow.getCell(1));
                for (int c = 2; c < totalCols; c++) {
                    setCellParagraphCenter(tableRow.getCell(c));
                    setCellVerticalCenter(tableRow.getCell(c));
                }
                rowInRun++;
                if (runSpan > 0 && rowInRun >= runSpan) {
                    runIdx++;
                    rowInRun = 0;
                    runSpan = runIdx < typeRuns.size() ? typeRuns.get(runIdx)[1] : 0;
                }
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                doc.write(out);
                byte[] docxBytes = out.toByteArray();
                return patchDocumentXmlA3Landscape(docxBytes);
            }
        }
    }

    /**
     * 在已寫入的 docx 壓縮包內直接修改 word/document.xml：替換 sectPr 為 A3 橫向、頁邊距。
     */
    private static byte[] patchDocumentXmlA3Landscape(byte[] docxBytes) throws IOException {
        String sectPr = "<w:sectPr xmlns:w=\"" + W_NS + "\">"
            + "<w:pgSz w:w=\"" + A3_LANDSCAPE_W + "\" w:h=\"" + A3_LANDSCAPE_H + "\" w:orient=\"landscape\"/>"
            + "<w:pgMar w:left=\"" + MARGIN_TWIPS + "\" w:right=\"" + MARGIN_TWIPS + "\" w:top=\"" + MARGIN_TWIPS + "\" w:bottom=\"" + MARGIN_TWIPS + "\"/>"
            + "</w:sectPr>";
        Pattern removeSectPr = Pattern.compile("<w:sectPr[^>]*>[\\s\\S]*?</w:sectPr>");
        ByteArrayOutputStream out = new ByteArrayOutputStream(docxBytes.length + 512);
        try (ZipInputStream zis = new ZipInputStream(new java.io.ByteArrayInputStream(docxBytes));
             ZipOutputStream zos = new ZipOutputStream(out)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                zos.putNextEntry(new ZipEntry(name));
                if ("word/document.xml".equals(name)) {
                    String xml = new String(zis.readAllBytes(), StandardCharsets.UTF_8);
                    xml = removeSectPr.matcher(xml).replaceAll("");
                    xml = xml.replace("</w:body>", sectPr + "</w:body>");
                    zos.write(xml.getBytes(StandardCharsets.UTF_8));
                } else {
                    zis.transferTo(zos);
                }
                zos.closeEntry();
            }
        }
        return out.toByteArray();
    }

    private static void appendCompatCellDocx(XWPFTableCell cell, String value) {
        String v = value != null ? value.trim() : "";
        String display = "V".equals(v) ? "√" : ("X".equals(v) ? "X" : (v.isEmpty() ? "－" : v));
        setCellText(cell, display, 1);
        if ("V".equals(v)) setCellShading(cell, CELL_OK_FILL);
    }

    private static void setCellText(XWPFTableCell cell, String text, int colspan) {
        if (cell == null) return;
        cell.setText(text != null ? text : "");
        if (colspan > 1) {
            CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
            if (tcPr.getGridSpan() == null) tcPr.addNewGridSpan();
            tcPr.getGridSpan().setVal(BigInteger.valueOf(colspan));
        }
    }

    /** 寫入表頭文字並設字體大小，確保「前工序/適用界面」類型名可見 */
    private static void setCellTextWithFontSize(XWPFTableCell cell, String text, int fontSizePt) {
        if (cell == null) return;
        String t = text != null ? text : "";
        cell.setText(t);
        if (fontSizePt > 0 && !cell.getParagraphs().isEmpty()) {
            for (org.apache.poi.xwpf.usermodel.XWPFParagraph p : cell.getParagraphs()) {
                if (!p.getRuns().isEmpty()) {
                    p.getRuns().get(0).setFontSize(fontSizePt);
                }
            }
        }
    }

    private static void setCellShading(XWPFTableCell cell, String fillHex) {
        if (cell == null) return;
        CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
        CTShd shd = tcPr.isSetShd() ? tcPr.getShd() : tcPr.addNewShd();
        shd.setFill(fillHex);
        shd.setVal(STShd.CLEAR);
    }

    /** 設置儲存格寬度（twips），使「類型」與前工序/適用界面表頭可讀；type=DXA 表示寬度單位為 twips */
    private static void setCellWidthTwips(XWPFTableCell cell, long widthTwips) {
        if (cell == null) return;
        CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
        if (tcPr.getTcW() == null) tcPr.addNewTcW();
        tcPr.getTcW().setW(BigInteger.valueOf(widthTwips));
        tcPr.getTcW().setType(STTblWidth.DXA);
    }

    /** 設置表頭行最小高度（twips），避免「前工序工藝類型」「適用界面類型名稱」那一行被壓扁不可見 */
    private static void setRowHeightAtLeast(XWPFTableRow row, int twips) {
        if (row == null || twips <= 0) return;
        var ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();
        CTHeight h = trPr.addNewTrHeight();
        h.setVal(BigInteger.valueOf(twips));
        h.setHRule(STHeightRule.AT_LEAST);
    }

    private static void setCellParagraphCenter(XWPFTableCell cell) {
        if (cell == null) return;
        for (XWPFParagraph p : cell.getParagraphs()) {
            p.setAlignment(ParagraphAlignment.CENTER);
        }
    }

    private static void setCellVerticalCenter(XWPFTableCell cell) {
        if (cell != null) {
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        }
    }

    private static void setVMergeContinue(XWPFTableCell cell) {
        if (cell == null) return;
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

    /** 數據行「類型」列顯示：有 paperType 用 paperType，否則用 seriesName，都空用 "－"。 */
    private static String dataRowTypeDisplay(String paperType, String seriesName) {
        if (paperType != null && !paperType.trim().isEmpty()) return paperType.trim();
        if (seriesName != null && !seriesName.trim().isEmpty()) return seriesName.trim();
        return "－";
    }

    /** 數據行「系列」列顯示：有 seriesName 用 seriesName，否則用 paperType，都空用 "－"。 */
    private static String dataRowSeriesDisplay(String paperType, String seriesName) {
        if (seriesName != null && !seriesName.trim().isEmpty()) return seriesName.trim();
        if (paperType != null && !paperType.trim().isEmpty()) return paperType.trim();
        return "－";
    }

    /** 前工序/适用界面列的表头：优先用 name（工艺类型名称），空则用 subGroup 或 group，最后用 fallback。 */
    private static String compatColumnHeaderName(ColumnDef c, String fallback) {
        if (c == null) return fallback;
        String n = c.getName();
        if (n != null && !n.trim().isEmpty()) return n.trim();
        n = c.getSubGroup();
        if (n != null && !n.trim().isEmpty()) return n.trim();
        n = c.getGroup();
        if (n != null && !n.trim().isEmpty()) return n.trim();
        return fallback;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
