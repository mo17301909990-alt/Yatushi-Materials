package com.it.yts_project.util;

import com.it.yts_project.dto.LaminatingMatrixDTO;
import com.it.yts_project.dto.LaminatingMatrixDTO.FoilColumnDef;
import com.it.yts_project.dto.LaminatingMatrixDTO.MatrixRow;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 燙金後過膠－應用限制 矩陣導出工具。
 * Excel 導出為 CSV；Word 導出為 HTML (.doc) 或真正 .docx（OOXML）。
 */
public final class LaminatingMatrixExportUtil {

    private static final String TITLE = "燙金後過膠-應用限制";
    private static final String LEGEND = "圖例：√ 可以加工；× 不可以加工";
    private static final byte[] UTF8_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    private static final String LIGHT_BLUE = "#ADD8E6";
    private static final String HEADER_FILL = "ADD8E6";
    /** A3 橫向：420mm × 297mm（twips） */
    private static final long A3_LANDSCAPE_W = 23811L;
    private static final long A3_LANDSCAPE_H = 16838L;
    private static final long MARGIN_TWIPS = 567L;

    private LaminatingMatrixExportUtil() {}

    public static byte[] exportToExcel(LaminatingMatrixDTO matrix) throws Exception {
        List<FoilColumnDef> cols = matrix.getColumnDefs();
        List<MatrixRow> rows = matrix.getRows();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(UTF8_BOM);

        StringBuilder sb = new StringBuilder();
        sb.append(escapeCsv(TITLE)).append("\n");
        sb.append(escapeCsv(LEGEND)).append("\n");

        // 表頭第1行：燙金紙選用
        List<String> headerRow1 = new ArrayList<>();
        headerRow1.add(escapeCsv(""));
        headerRow1.add(escapeCsv(""));
        headerRow1.add(escapeCsv(""));
        if (cols != null) {
            for (int i = 0; i < cols.size(); i++) {
                headerRow1.add(escapeCsv(i == 0 ? "燙金紙選用" : ""));
            }
        }
        sb.append(String.join(",", headerRow1)).append("\n");

        // 表頭第2行：燙金紙類型
        List<String> headerRow2 = new ArrayList<>();
        headerRow2.add(escapeCsv(""));
        headerRow2.add(escapeCsv(""));
        headerRow2.add(escapeCsv(""));
        if (cols != null) {
            for (FoilColumnDef col : cols) {
                headerRow2.add(escapeCsv(col.getTypeGroupName()));
            }
        }
        sb.append(String.join(",", headerRow2)).append("\n");

        // 表頭第3行：過膠種類(前綴)、過膠種類(後綴)、燙後過膠後加工、具體型號（顯示用，導入按列下標用 columnDefs.cellKey 匹配）
        List<String> headerRow3 = new ArrayList<>();
        headerRow3.add(escapeCsv("過膠種類(前綴)"));
        headerRow3.add(escapeCsv("過膠種類(後綴)"));
        headerRow3.add(escapeCsv("燙後過膠後加工(與燙金位置重疊)"));
        if (cols != null) {
            for (FoilColumnDef col : cols) {
                String label = nullToEmpty(col.getModelNumber());
                if (label.isEmpty()) label = nullToEmpty(col.getFoilSeries());
                if (Boolean.TRUE.equals(col.getIsJiehuo())) label += "(街貨)";
                headerRow3.add(escapeCsv(label));
            }
        }
        sb.append(String.join(",", headerRow3)).append("\n");

        if (rows != null) {
            for (MatrixRow row : rows) {
                List<String> rowCells = new ArrayList<>();
                rowCells.add(escapeCsv(nullToEmpty(row.getLaminationMaterialPrefix())));
                rowCells.add(escapeCsv(nullToEmpty(row.getLaminationMaterialSuffix())));
                rowCells.add(escapeCsv(nullToEmpty(row.getPostProcessingStepName())));
                if (cols != null) {
                    for (FoilColumnDef col : cols) {
                        String cell = row.getCells() != null ? row.getCells().get(col.cellKey()) : null;
                        String display = "V".equals(cell) ? "√" : ("X".equals(cell) ? "×" : "");
                        rowCells.add(escapeCsv(display));
                    }
                }
                sb.append(String.join(",", rowCells)).append("\n");
            }
        }

        out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        return out.toByteArray();
    }

    public static byte[] exportToWord(LaminatingMatrixDTO matrix) throws Exception {
        List<FoilColumnDef> cols = matrix.getColumnDefs();
        List<MatrixRow> rows = matrix.getRows();
        int colCount = cols != null ? cols.size() : 0;
        int totalCols = 3 + colCount;

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/><title>").append(escapeHtml(TITLE)).append("</title>");
        html.append("<style>");
        html.append("table{border-collapse:collapse;width:100%;border:1px solid #000}");
        html.append(" th,td{border:1px solid #000;padding:4px 8px}");
        html.append(" thead th{font-weight:bold;text-align:center;background:").append(LIGHT_BLUE).append("}");
        html.append(" tbody td{background:#fff}");
        html.append(" .td-left{text-align:left}");
        html.append(" .td-center{text-align:center}");
        html.append("</style></head><body>");

        html.append("<table><thead>");
        html.append("<tr><th colspan=\"").append(totalCols).append("\">").append(escapeHtml(TITLE)).append("</th></tr>");
        html.append("<tr><th colspan=\"").append(totalCols).append("\">").append(escapeHtml(LEGEND)).append("</th></tr>");
        // 表頭第1行：燙金紙選用（合併燙金紙列）；左側三列 rowspan=3
        html.append("<tr>");
        html.append("<th rowspan=\"3\">過膠種類(前綴)</th><th rowspan=\"3\">過膠種類(後綴)</th><th rowspan=\"3\">燙後過膠後加工(與燙金位置重疊)</th>");
        html.append("<th colspan=\"").append(colCount).append("\">燙金紙選用</th>");
        html.append("</tr>");
        // 表頭第2行：燙金紙類型（產品類型），同類型已排在一起，合併為 colspan
        if (cols != null && !cols.isEmpty()) {
            html.append("<tr>");
            String prevType = null;
            int span = 0;
            for (int i = 0; i < cols.size(); i++) {
                String type = cols.get(i).getTypeGroupName();
                if (type.equals(prevType)) {
                    span++;
                } else {
                    if (prevType != null) {
                        html.append("<th colspan=\"").append(span).append("\">").append(escapeHtml(prevType)).append("</th>");
                    }
                    prevType = type;
                    span = 1;
                }
            }
            if (prevType != null) {
                html.append("<th colspan=\"").append(span).append("\">").append(escapeHtml(prevType)).append("</th>");
            }
            html.append("</tr>");
        }
        // 表頭第3行：具體型號（左側三列由 rowspan 佔位，僅輸出燙金紙型號列）
        html.append("<tr>");
        if (cols != null) {
            for (FoilColumnDef col : cols) {
                String label = nullToEmpty(col.getModelNumber());
                if (label.isEmpty()) label = nullToEmpty(col.getFoilSeries());
                if (Boolean.TRUE.equals(col.getIsJiehuo())) label += "(街貨)";
                html.append("<th>").append(escapeHtml(label)).append("</th>");
            }
        }
        html.append("</tr></thead><tbody>");

        if (rows != null) {
            // 预计算每行前缀/后缀的 rowspan：同(前缀,后缀)连续多行只输出一次并合并
            int n = rows.size();
            int[] prefixSuffixRowspan = new int[n];
            for (int i = 0; i < n; i++) {
                MatrixRow r = rows.get(i);
                String p = nullToEmpty(r.getLaminationMaterialPrefix());
                String s = nullToEmpty(r.getLaminationMaterialSuffix());
                if (i == 0 || !p.equals(nullToEmpty(rows.get(i - 1).getLaminationMaterialPrefix()))
                        || !s.equals(nullToEmpty(rows.get(i - 1).getLaminationMaterialSuffix()))) {
                    int span = 1;
                    for (int j = i + 1; j < n; j++) {
                        MatrixRow rj = rows.get(j);
                        if (p.equals(nullToEmpty(rj.getLaminationMaterialPrefix()))
                                && s.equals(nullToEmpty(rj.getLaminationMaterialSuffix()))) span++;
                        else break;
                    }
                    prefixSuffixRowspan[i] = span;
                } else {
                    prefixSuffixRowspan[i] = 0;
                }
            }
            for (int i = 0; i < n; i++) {
                MatrixRow row = rows.get(i);
                html.append("<tr>");
                if (prefixSuffixRowspan[i] > 0) {
                    html.append("<td class=\"td-left\" rowspan=\"").append(prefixSuffixRowspan[i]).append("\">")
                            .append(escapeHtml(nullToEmpty(row.getLaminationMaterialPrefix()))).append("</td>");
                    html.append("<td class=\"td-left\" rowspan=\"").append(prefixSuffixRowspan[i]).append("\">")
                            .append(escapeHtml(nullToEmpty(row.getLaminationMaterialSuffix()))).append("</td>");
                }
                html.append("<td class=\"td-left\">").append(escapeHtml(nullToEmpty(row.getPostProcessingStepName()))).append("</td>");
                if (cols != null) {
                    for (FoilColumnDef col : cols) {
                        String cell = row.getCells() != null ? row.getCells().get(col.cellKey()) : null;
                        String display = "V".equals(cell) ? "√" : ("X".equals(cell) ? "×" : "");
                        html.append("<td class=\"td-center\">").append(escapeHtml(display)).append("</td>");
                    }
                }
                html.append("</tr>");
            }
        }

        html.append("</tbody></table>");
        html.append("<p>* 燙後過膠起到保護燙金作用，基於成本考慮；耐磨燙金紙不適用於燙後過膠。</p>");
        html.append("<p>* 若有表內以外的物料，請交測試員做針對性測試。</p>");
        html.append("</body></html>");

        return html.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 導出為真正的 .docx（OOXML），與「燙金+印刷UV」矩陣導出方式一致。
     * A4、縱向、頁邊距上下左右約 1cm。
     */
    public static byte[] exportToDocx(LaminatingMatrixDTO matrix) throws Exception {
        List<FoilColumnDef> cols = matrix.getColumnDefs();
        List<MatrixRow> rows = matrix.getRows();
        int colCount = cols != null ? cols.size() : 0;
        int totalCols = 3 + colCount;
        int dataRows = rows != null ? rows.size() : 0;
        int totalRows = 5 + dataRows; // 標題、圖例、表頭3行、數據

        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph titleP = doc.createParagraph();
            org.apache.poi.xwpf.usermodel.XWPFRun titleRun = titleP.createRun();
            titleRun.setText(TITLE);
            titleRun.setBold(true);
            doc.createParagraph().createRun().setText(LEGEND);

            XWPFTable table = doc.createTable(totalRows, totalCols);
            table.setWidth("100%");

            // Row 0: 標題 colspan=totalCols
            setCellText(table.getRow(0).getCell(0), TITLE, 1);
            mergeCellsHorizontally(table, 0, 0, totalCols - 1);
            setCellShading(table.getRow(0).getCell(0), HEADER_FILL);
            setCellParagraphCenter(table.getRow(0).getCell(0));

            // Row 1: 圖例 colspan=totalCols
            setCellText(table.getRow(1).getCell(0), LEGEND, 1);
            mergeCellsHorizontally(table, 1, 0, totalCols - 1);
            setCellParagraphCenter(table.getRow(1).getCell(0));

            // Row 2: 左3列 rowspan=3，右側「燙金紙選用」colspan=colCount
            setCellText(table.getRow(2).getCell(0), "過膠種類(前綴)", 1);
            setCellText(table.getRow(2).getCell(1), "過膠種類(後綴)", 1);
            setCellText(table.getRow(2).getCell(2), "燙後過膠後加工(與燙金位置重疊)", 1);
            mergeCellsVertically(table, 0, 2, 4);
            mergeCellsVertically(table, 1, 2, 4);
            mergeCellsVertically(table, 2, 2, 4);
            if (colCount > 0) {
                setCellText(table.getRow(2).getCell(3), "燙金紙選用", 1);
                mergeCellsHorizontally(table, 2, 3, totalCols - 1);
            }
            for (int c = 0; c < totalCols; c++) {
                setCellShading(table.getRow(2).getCell(c), HEADER_FILL);
                setCellParagraphCenter(table.getRow(2).getCell(c));
                setCellVerticalCenter(table.getRow(2).getCell(c));
            }

            // Row 3: 燙金紙類型（左3列 vMerge continue），右側同類型合併
            setVMergeContinue(table.getRow(3).getCell(0));
            setVMergeContinue(table.getRow(3).getCell(1));
            setVMergeContinue(table.getRow(3).getCell(2));
            if (cols != null && !cols.isEmpty()) {
                String prevType = null;
                int groupStartFoilIdx = 0;
                for (int i = 0; i <= cols.size(); i++) {
                    String type = i < cols.size() ? cols.get(i).getTypeGroupName() : null;
                    if (i > 0 && (i >= cols.size() || !(type != null && type.equals(prevType)))) {
                        int firstCol = 3 + groupStartFoilIdx;
                        int lastCol = 3 + (i - 1);
                        setCellText(table.getRow(3).getCell(firstCol), prevType != null ? prevType : "", 1);
                        if (firstCol < lastCol) mergeCellsHorizontally(table, 3, firstCol, lastCol);
                        groupStartFoilIdx = i;
                    }
                    prevType = type;
                }
            }
            for (int c = 3; c < totalCols; c++) {
                setCellShading(table.getRow(3).getCell(c), HEADER_FILL);
                setCellParagraphCenter(table.getRow(3).getCell(c));
                setCellVerticalCenter(table.getRow(3).getCell(c));
            }

            // Row 4: 具體型號（左3列 vMerge continue）
            setVMergeContinue(table.getRow(4).getCell(0));
            setVMergeContinue(table.getRow(4).getCell(1));
            setVMergeContinue(table.getRow(4).getCell(2));
            if (cols != null) {
                for (int i = 0; i < cols.size(); i++) {
                    FoilColumnDef col = cols.get(i);
                    String label = nullToEmpty(col.getModelNumber());
                    if (label.isEmpty()) label = nullToEmpty(col.getFoilSeries());
                    if (Boolean.TRUE.equals(col.getIsJiehuo())) label += "(街貨)";
                    setCellText(table.getRow(4).getCell(3 + i), label, 1);
                    setCellShading(table.getRow(4).getCell(3 + i), HEADER_FILL);
                    setCellParagraphCenter(table.getRow(4).getCell(3 + i));
                    setCellVerticalCenter(table.getRow(4).getCell(3 + i));
                }
            }

            // 數據行：前綴/後綴 rowspan（無適用界面列）
            int[] prefixSuffixRowspan = computePrefixSuffixRowspan(rows);
            for (int i = 0; i < dataRows; i++) {
                MatrixRow row = rows.get(i);
                XWPFTableRow tableRow = table.getRow(5 + i);
                if (prefixSuffixRowspan[i] > 0) {
                    setCellText(tableRow.getCell(0), nullToEmpty(row.getLaminationMaterialPrefix()), 1);
                    setCellText(tableRow.getCell(1), nullToEmpty(row.getLaminationMaterialSuffix()), 1);
                    if (prefixSuffixRowspan[i] > 1) {
                        mergeCellsVertically(table, 0, 5 + i, 5 + i + prefixSuffixRowspan[i] - 1);
                        mergeCellsVertically(table, 1, 5 + i, 5 + i + prefixSuffixRowspan[i] - 1);
                    }
                } else {
                    setVMergeContinue(tableRow.getCell(0));
                    setVMergeContinue(tableRow.getCell(1));
                }
                setCellText(tableRow.getCell(2), nullToEmpty(row.getPostProcessingStepName()), 1);
                if (cols != null) {
                    for (int j = 0; j < cols.size(); j++) {
                        String cell = row.getCells() != null ? row.getCells().get(cols.get(j).cellKey()) : null;
                        String display = "V".equals(cell) ? "√" : ("X".equals(cell) ? "×" : "");
                        setCellText(tableRow.getCell(3 + j), display, 1);
                        setCellParagraphCenter(tableRow.getCell(3 + j));
                        setCellVerticalCenter(tableRow.getCell(3 + j));
                    }
                }
                setCellParagraphLeft(tableRow.getCell(0));
                setCellParagraphLeft(tableRow.getCell(1));
                setCellParagraphLeft(tableRow.getCell(2));
                setCellVerticalCenter(tableRow.getCell(0));
                setCellVerticalCenter(tableRow.getCell(1));
                setCellVerticalCenter(tableRow.getCell(2));
            }

            doc.createParagraph().createRun().setText("* 燙後過膠起到保護燙金作用，基於成本考慮；耐磨燙金紙不適用於燙後過膠。");
            doc.createParagraph().createRun().setText("* 若有表內以外的物料，請交測試員做針對性測試。");

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                doc.write(out);
                byte[] docxBytes = out.toByteArray();
                return patchDocumentXmlA3Landscape(docxBytes);
            }
        }
    }

    /**
     * 在已寫入的 docx 壓縮包內直接修改 word/document.xml：移除原有 sectPr，在 &lt;/w:body&gt; 前插入 A3 橫向的 sectPr。
     * 避免 POI 寫入時忽略或覆蓋版面設定。
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

    private static int[] computePrefixSuffixRowspan(List<MatrixRow> rows) {
        if (rows == null || rows.isEmpty()) return new int[0];
        int n = rows.size();
        int[] out = new int[n];
        for (int i = 0; i < n; i++) {
            String p = nullToEmpty(rows.get(i).getLaminationMaterialPrefix());
            String s = nullToEmpty(rows.get(i).getLaminationMaterialSuffix());
            if (i == 0 || !p.equals(nullToEmpty(rows.get(i - 1).getLaminationMaterialPrefix()))
                    || !s.equals(nullToEmpty(rows.get(i - 1).getLaminationMaterialSuffix()))) {
                int span = 1;
                for (int j = i + 1; j < n; j++) {
                    if (p.equals(nullToEmpty(rows.get(j).getLaminationMaterialPrefix()))
                            && s.equals(nullToEmpty(rows.get(j).getLaminationMaterialSuffix()))) span++;
                    else break;
                }
                out[i] = span;
            } else {
                out[i] = 0;
            }
        }
        return out;
    }

    private static final String W_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";


    private static void setCellText(XWPFTableCell cell, String text, int colspan) {
        if (cell == null) return;
        cell.setText(text != null ? text : "");
        if (colspan > 1) {
            CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
            if (tcPr.getGridSpan() == null) tcPr.addNewGridSpan();
            tcPr.getGridSpan().setVal(BigInteger.valueOf(colspan));
        }
    }

    private static void setCellShading(XWPFTableCell cell, String fillHex) {
        if (cell == null) return;
        CTTcPr tcPr = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
        CTShd shd = tcPr.isSetShd() ? tcPr.getShd() : tcPr.addNewShd();
        shd.setFill(fillHex);
        shd.setVal(STShd.CLEAR);
    }

    private static void setCellParagraphCenter(XWPFTableCell cell) {
        if (cell == null) return;
        for (XWPFParagraph p : cell.getParagraphs()) {
            p.setAlignment(ParagraphAlignment.CENTER);
        }
    }

    private static void setCellParagraphLeft(XWPFTableCell cell) {
        if (cell == null) return;
        for (XWPFParagraph p : cell.getParagraphs()) {
            p.setAlignment(ParagraphAlignment.LEFT);
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

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
