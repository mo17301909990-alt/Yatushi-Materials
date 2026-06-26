package com.it.yts_project.util;

import com.it.yts_project.dto.LeduvglitterMatrixDTO;
import com.it.yts_project.dto.LeduvglitterMatrixDTO.MatrixRow;
import com.it.yts_project.dto.LeduvglitterMatrixDTO.SeriesColumnDef;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
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
 * 燙金+絲印LED UV灑閃粉 配对烫金纸型号矩阵导出工具。
 * Excel 导出为 CSV；Word 导出为 .docx。
 */
public final class LeduvglitterMatrixExportUtil {

    /** 表頭第1行：段落標題 */
    private static final String TITLE = "四、“燙金 + 絲印LED UV灑閃粉” (僅適用於「賀咭/紙袋產品」)";
    /** 表頭第2行：參考資料說明 */
    private static final String SUBTITLE = "參考資料《#N-SS-005 絲印灑閃粉工藝-指引書》。";
    private static final String LEGEND = "圖例：√ 適用；× 不適用";
    private static final byte[] UTF8_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    private static final String HEADER_FILL = "ADD8E6";
    private static final long A3_LANDSCAPE_W = 23811L;
    private static final long A3_LANDSCAPE_H = 16838L;
    private static final long MARGIN_TWIPS = 567L;
    private static final String W_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";

    private LeduvglitterMatrixExportUtil() {}

    public static byte[] exportToExcel(LeduvglitterMatrixDTO matrix) throws Exception {
        List<SeriesColumnDef> cols = matrix.getColumnDefs();
        List<MatrixRow> rows = matrix.getRows();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(UTF8_BOM);

        StringBuilder sb = new StringBuilder();
        sb.append(escapeCsv(TITLE)).append("\n");
        sb.append(escapeCsv(SUBTITLE)).append("\n");
        sb.append(escapeCsv(LEGEND)).append("\n");

        // 表头第1行：序、加工重叠组合、烫金界面、配对烫金纸型号(按类型分组列)
        List<String> headerRow1 = new ArrayList<>();
        headerRow1.add(escapeCsv("序"));
        headerRow1.add(escapeCsv("加工重疊組合"));
        headerRow1.add(escapeCsv("燙金界面"));
        if (cols != null) {
            for (int j = 0; j < cols.size(); j++) {
                headerRow1.add(escapeCsv(j == 0 ? "配對燙金紙型號" : ""));
            }
        }
        sb.append(String.join(",", headerRow1)).append("\n");

        // 表头第2行：空、空、空、烫金纸性能类型（相同类型只填第一格，其余空，实现“合并”效果）
        List<String> headerRow2 = new ArrayList<>();
        headerRow2.add(escapeCsv(""));
        headerRow2.add(escapeCsv(""));
        headerRow2.add(escapeCsv(""));
        if (cols != null) {
            String prevPaperType = null;
            for (SeriesColumnDef col : cols) {
                String pt = col.getPaperType() != null ? col.getPaperType() : "";
                String cell = (prevPaperType == null || !pt.equals(prevPaperType)) ? pt : "";
                headerRow2.add(escapeCsv(cell));
                prevPaperType = pt;
            }
        }
        sb.append(String.join(",", headerRow2)).append("\n");

        // 表头第3行：序、加工重叠组合、烫金界面、系列名...
        List<String> headerRow3 = new ArrayList<>();
        headerRow3.add(escapeCsv("序"));
        headerRow3.add(escapeCsv("加工重疊組合"));
        headerRow3.add(escapeCsv("燙金界面"));
        if (cols != null) {
            for (SeriesColumnDef col : cols) {
                headerRow3.add(escapeCsv(col.getSeriesName() != null ? col.getSeriesName() : ""));
            }
        }
        sb.append(String.join(",", headerRow3)).append("\n");

        if (rows != null) {
            int seq = 1;
            for (MatrixRow row : rows) {
                List<String> rowCells = new ArrayList<>();
                rowCells.add(escapeCsv(String.valueOf(seq++)));
                rowCells.add(escapeCsv(LeduvglitterMatrixDTO.PROCESSING_COMBO));
                rowCells.add(escapeCsv(nullToEmpty(row.getHotStampingInterface())));
                if (cols != null) {
                    for (SeriesColumnDef col : cols) {
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

    public static byte[] exportToDocx(LeduvglitterMatrixDTO matrix) throws Exception {
        List<SeriesColumnDef> cols = matrix.getColumnDefs();
        List<MatrixRow> rows = matrix.getRows();
        int colCount = cols != null ? cols.size() : 0;
        int totalCols = 3 + colCount;
        int dataRows = rows != null ? rows.size() : 0;
        int totalRows = 5 + dataRows; // 标题、副标题、图例、表头2行、数据

        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph titleP = doc.createParagraph();
            titleP.createRun().setText(TITLE);
            titleP.createRun().setBold(true);
            doc.createParagraph().createRun().setText(SUBTITLE);
            doc.createParagraph().createRun().setText(LEGEND);

            XWPFTable table = doc.createTable(totalRows, totalCols);
            table.setWidth("100%");

            // Row 0: 标题（左對齊，整行淺藍底）
            XWPFTableRow titleRow = table.getRow(0);
            setCellText(titleRow.getCell(0), TITLE, 1);
            mergeCellsHorizontally(table, 0, 0, totalCols - 1);
            setCellShading(titleRow.getCell(0), HEADER_FILL);
            setCellParagraphLeft(titleRow.getCell(0));

            // Row 1: 參考資料說明（表頭第2行，左對齊，整行淺藍底）
            XWPFTableRow subtitleRow = table.getRow(1);
            setCellText(subtitleRow.getCell(0), SUBTITLE, 1);
            mergeCellsHorizontally(table, 1, 0, totalCols - 1);
            setCellShading(subtitleRow.getCell(0), HEADER_FILL);
            setCellParagraphLeft(subtitleRow.getCell(0));

            // Row 2: 序、加工重叠组合、烫金界面、配对烫金纸型号(colspan)
            setCellText(table.getRow(2).getCell(0), "序", 1);
            setCellText(table.getRow(2).getCell(1), "加工重疊組合", 1);
            setCellText(table.getRow(2).getCell(2), "燙金界面", 1);
            mergeCellsVertically(table, 0, 2, 4);
            mergeCellsVertically(table, 1, 2, 4);
            mergeCellsVertically(table, 2, 2, 4);
            if (colCount > 0) {
                setCellText(table.getRow(2).getCell(3), "配對燙金紙型號", 1);
                mergeCellsHorizontally(table, 2, 3, totalCols - 1);
            }
            for (int c = 0; c < totalCols; c++) {
                setCellShading(table.getRow(2).getCell(c), HEADER_FILL);
                setCellParagraphCenter(table.getRow(2).getCell(c));
            }

            // Row 3: 左3列 vMerge continue，右侧 paperType 分组合并
            setVMergeContinue(table.getRow(3).getCell(0));
            setVMergeContinue(table.getRow(3).getCell(1));
            setVMergeContinue(table.getRow(3).getCell(2));
            if (cols != null && !cols.isEmpty()) {
                String prevType = null;
                int groupStart = 0;
                for (int i = 0; i <= cols.size(); i++) {
                    String type = i < cols.size() ? cols.get(i).getPaperType() : null;
                    if (i > 0 && (i >= cols.size() || !(type != null && type.equals(prevType)))) {
                        int firstCol = 3 + groupStart;
                        int lastCol = 3 + (i - 1);
                        setCellText(table.getRow(3).getCell(firstCol), prevType != null ? prevType : "", 1);
                        if (firstCol < lastCol) mergeCellsHorizontally(table, 3, firstCol, lastCol);
                        groupStart = i;
                    }
                    prevType = type;
                }
            }
            for (int c = 3; c < totalCols; c++) {
                setCellShading(table.getRow(3).getCell(c), HEADER_FILL);
                setCellParagraphCenter(table.getRow(3).getCell(c));
            }

            // Row 4: 序、加工重叠组合、烫金界面、系列名...
            setVMergeContinue(table.getRow(4).getCell(0));
            setVMergeContinue(table.getRow(4).getCell(1));
            setVMergeContinue(table.getRow(4).getCell(2));
            if (cols != null) {
                for (int i = 0; i < cols.size(); i++) {
                    setCellText(table.getRow(4).getCell(3 + i), nullToEmpty(cols.get(i).getSeriesName()), 1);
                    setCellShading(table.getRow(4).getCell(3 + i), HEADER_FILL);
                    setCellParagraphCenter(table.getRow(4).getCell(3 + i));
                }
            }

            // 数据行（加工重疊組合相同則縱向合併：僅首行填寫，最後統一 mergeCellsVertically）
            for (int i = 0; i < dataRows; i++) {
                MatrixRow row = rows.get(i);
                XWPFTableRow tableRow = table.getRow(5 + i);
                setCellText(tableRow.getCell(0), String.valueOf(i + 1), 1);
                if (i == 0) {
                    setCellText(tableRow.getCell(1), LeduvglitterMatrixDTO.PROCESSING_COMBO, 1);
                }
                setCellText(tableRow.getCell(2), nullToEmpty(row.getHotStampingInterface()), 1);
                if (cols != null) {
                    for (int j = 0; j < cols.size(); j++) {
                        String cell = row.getCells() != null ? row.getCells().get(cols.get(j).cellKey()) : null;
                        String display = "V".equals(cell) ? "√" : ("X".equals(cell) ? "×" : "");
                        setCellText(tableRow.getCell(3 + j), display, 1);
                        setCellParagraphCenter(tableRow.getCell(3 + j));
                    }
                }
            }
            if (dataRows > 1) {
                mergeCellsVertically(table, 1, 5, 5 + dataRows - 1);
            }
            // 加工重疊組合列：上下左右居中顯示
            if (dataRows > 0) {
                XWPFTableCell comboCell = table.getRow(5).getCell(1);
                setCellParagraphCenter(comboCell);
                setCellVerticalCenter(comboCell);
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                doc.write(out);
                byte[] docxBytes = out.toByteArray();
                return patchDocumentXmlA3Landscape(docxBytes);
            }
        }
    }

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

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
