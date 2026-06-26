package com.it.yts_project.util;

import com.it.yts_project.dto.LeduvglitterMatrixDTO.SeriesColumnDef;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.math.BigInteger;

/**
 * 燙金+絲印LED UV灑閃粉 配对烫金纸型号矩阵导入解析。
 * 表头：序、加工重叠组合、烫金界面、[系列列…]；数据行：√/× 或 V/X。
 */
public final class LeduvglitterMatrixImportUtil {

    private LeduvglitterMatrixImportUtil() {}

    @lombok.Data
    public static class ParsedRow {
        private String hotStampingInterface;
        private Map<String, String> cells = new LinkedHashMap<>();
    }

    public static List<ParsedRow> parseExcel(InputStream is, List<SeriesColumnDef> columnDefs) throws Exception {
        try (Workbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) throw new IllegalArgumentException("Excel file has no sheets");
            int totalRows = sheet.getLastRowNum() + 1;
            if (totalRows < 2) throw new IllegalArgumentException("Excel file has too few rows");

            int headerRowIdx = findHeaderRow(sheet, totalRows);
            Row headerRow = sheet.getRow(headerRowIdx);
            int colInterface = findColumnByName(headerRow, sheet, headerRowIdx, "燙金界面", "烫金界面");
            if (colInterface < 0) colInterface = 2;
            int dataStart = 3;

            List<String> colKeys = buildColumnKeysFromDefs(columnDefs);
            if (colKeys.isEmpty() && headerRow != null) {
                for (int c = dataStart; c <= headerRow.getLastCellNum(); c++) {
                    String h = getCellStringValue(sheet, headerRowIdx, c).trim();
                    if (!h.isEmpty()) colKeys.add(h);
                }
            }

            List<ParsedRow> rows = new ArrayList<>();
            for (int r = headerRowIdx + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                String iface = getCellStringValue(sheet, r, colInterface).trim();
                if (iface.isEmpty()) continue;

                ParsedRow pr = new ParsedRow();
                pr.setHotStampingInterface(iface);
                for (int i = 0; i < colKeys.size(); i++) {
                    int colIdx = dataStart + i;
                    String value = getCellStringValue(sheet, r, colIdx).trim();
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) pr.getCells().put(colKeys.get(i), value);
                }
                rows.add(pr);
            }
            return rows;
        }
    }

    public static List<ParsedRow> parseWord(InputStream is, List<SeriesColumnDef> columnDefs) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(is)) {
            XWPFTable table = findFirstTableWithHeader(doc, "燙金界面", "烫金界面", "加工重疊組合", "配對燙金紙");
            if (table == null) throw new IllegalArgumentException("Cannot find table with 燙金界面/加工重叠组合 in Word");

            List<XWPFTableRow> tableRows = table.getRows();
            if (tableRows.size() < 2) throw new IllegalArgumentException("Word table has too few rows");

            int headerRowIdx = 0;
            for (int r = 0; r < Math.min(6, tableRows.size()); r++) {
                String first = getWordCellText(tableRows.get(r), 0) + getWordCellText(tableRows.get(r), 2);
                if (first.contains("燙金界面") || first.contains("烫金界面") || first.contains("加工重疊")) {
                    headerRowIdx = r;
                    break;
                }
            }
            // 表头可能是第4行（序、加工重叠组合、烫金界面、系列…）
            for (int r = headerRowIdx; r < Math.min(headerRowIdx + 3, tableRows.size()); r++) {
                if (findWordColumnByText(tableRows.get(r), "燙金界面", "烫金界面") >= 0) {
                    headerRowIdx = r;
                    break;
                }
            }

            XWPFTableRow headerRow = tableRows.get(headerRowIdx);
            int colInterface = findWordColumnByText(headerRow, "燙金界面", "烫金界面");
            if (colInterface < 0) colInterface = 2;
            int dataStart = 3;

            // 根據 Word 表頭構建列鍵（paperType|seriesName）：
            // 結構：headerRowIdx = 序/加工重疊組合/燙金界面/配對燙金紙型號...
            //       headerRowIdx + 1 = 性能類型行（普通金紙/普通耐磨/高耐磨…，可能有橫向合併 gridSpan）
            //       headerRowIdx + 2 = 系列名行（A23 / G1 / G6 / TB813 / ...）
            // 始終以文檔中「性能類型行」為準，按 gridSpan 建立邏輯列 → paperType 映射，避免錯配為高耐磨等。
            List<String> colKeys = new ArrayList<>();
            if (headerRowIdx + 2 < tableRows.size()) {
                XWPFTableRow paperTypeRow = tableRows.get(headerRowIdx + 1);
                XWPFTableRow seriesRow = tableRows.get(headerRowIdx + 2);
                List<XWPFTableCell> seriesCells = seriesRow.getTableCells();
                int cells = seriesCells.size();
                int numSeriesCols = cells - dataStart;
                if (numSeriesCols > 0) {
                    List<String> paperTypeByLogicalCol = new ArrayList<>(numSeriesCols);
                    for (int i = 0; i < numSeriesCols; i++) paperTypeByLogicalCol.add("");
                    List<XWPFTableCell> typeCells = paperTypeRow.getTableCells();
                    int logicalCol = 0;
                    for (int i = dataStart; i < typeCells.size() && logicalCol < numSeriesCols; i++) {
                        XWPFTableCell cell = typeCells.get(i);
                        String paperType = cell.getText();
                        if (paperType != null) paperType = paperType.trim();
                        else paperType = "";
                        int span = getWordCellGridSpan(cell);
                        for (int j = 0; j < span && logicalCol + j < numSeriesCols; j++)
                            paperTypeByLogicalCol.set(logicalCol + j, paperType);
                        logicalCol += span;
                    }
                    for (int c = dataStart; c < cells; c++) {
                        String seriesName = getWordCellText(seriesRow, c).trim();
                        if (seriesName.isEmpty()) continue;
                        int colLogicalIndex = c - dataStart;
                        String paperType = colLogicalIndex < paperTypeByLogicalCol.size() ? paperTypeByLogicalCol.get(colLogicalIndex) : "";
                        if (paperType == null) paperType = "";
                        colKeys.add(paperType + "|" + seriesName);
                    }
                }
            }
            if (colKeys.isEmpty()) {
                colKeys = buildColumnKeysFromDefs(columnDefs);
            }

            List<ParsedRow> rows = new ArrayList<>();
            for (int r = headerRowIdx + 3; r < tableRows.size(); r++) {
                XWPFTableRow row = tableRows.get(r);
                List<XWPFTableCell> cells = row.getTableCells();
                String iface = (colInterface < cells.size()) ? getWordCellText(row, colInterface).trim() : "";
                if (iface.isEmpty()) continue;

                ParsedRow pr = new ParsedRow();
                pr.setHotStampingInterface(iface);
                for (int i = 0; i < colKeys.size(); i++) {
                    int colIdx = dataStart + i;
                    String value = (colIdx < cells.size()) ? cells.get(colIdx).getText().trim() : "";
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) pr.getCells().put(colKeys.get(i), value);
                }
                rows.add(pr);
            }
            return rows;
        }
    }

    public static List<ParsedRow> parseCsv(InputStream is, List<SeriesColumnDef> columnDefs) throws Exception {
        List<ParsedRow> rows = new ArrayList<>();
        List<String> colKeys = buildColumnKeysFromDefs(columnDefs);
        int[] dataStart = { 3 };
        int[] colInterface = { 2 };
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            int lineNum = 0;
            boolean headerDone = false;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                List<String> cells = parseCsvLine(line);
                if (cells.isEmpty()) continue;
                String first = cells.size() > 0 ? cells.get(0).trim() : "";
                if (!headerDone && (first.contains("序") || cells.stream().anyMatch(c -> c.contains("燙金界面") || c.contains("烫金界面")))) {
                    for (int c = 0; c < cells.size(); c++) {
                        if (cells.get(c).contains("燙金界面") || cells.get(c).contains("烫金界面")) {
                            colInterface[0] = c;
                            break;
                        }
                    }
                    dataStart[0] = 3;
                    if (cells.size() > 3) {
                        colKeys.clear();
                        for (int i = dataStart[0]; i < cells.size(); i++) {
                            String h = cells.get(i).trim();
                            if (!h.isEmpty()) colKeys.add(h);
                        }
                    }
                    headerDone = true;
                    continue;
                }
                if (!headerDone) continue;
                String iface = cells.size() > colInterface[0] ? cells.get(colInterface[0]).trim() : "";
                if (iface.isEmpty()) continue;

                ParsedRow pr = new ParsedRow();
                pr.setHotStampingInterface(iface);
                for (int i = 0; i < colKeys.size(); i++) {
                    int colIdx = dataStart[0] + i;
                    String value = colIdx < cells.size() ? cells.get(colIdx).trim() : "";
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) pr.getCells().put(colKeys.get(i), value);
                }
                rows.add(pr);
            }
        }
        return rows;
    }

    private static List<String> buildColumnKeysFromDefs(List<SeriesColumnDef> columnDefs) {
        if (columnDefs == null || columnDefs.isEmpty()) return new ArrayList<>();
        List<String> keys = new ArrayList<>();
        for (SeriesColumnDef col : columnDefs) keys.add(col.cellKey());
        return keys;
    }

    private static int findHeaderRow(Sheet sheet, int totalRows) {
        for (int r = 0; r < Math.min(10, totalRows); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            for (int c = 0; c <= Math.min(5, row.getLastCellNum()); c++) {
                String val = getCellStringValue(sheet, r, c).trim();
                if (val.contains("燙金界面") || val.contains("烫金界面") || val.contains("加工重疊")) return r;
            }
        }
        return 0;
    }

    private static int findColumnByName(Row row, Sheet sheet, int rowIdx, String... names) {
        if (row == null) return -1;
        for (int c = 0; c <= row.getLastCellNum(); c++) {
            String val = getCellStringValue(sheet, rowIdx, c).trim();
            for (String n : names) if (n.equals(val) || val.contains(n)) return c;
        }
        return -1;
    }

    private static String getCellStringValue(Sheet sheet, int rowIdx, int colIdx) {
        Row row = sheet.getRow(rowIdx);
        if (row == null) return "";
        Cell cell = row.getCell(colIdx);
        if (cell == null) return "";
        return cell.toString().trim();
    }

    private static XWPFTable findFirstTableWithHeader(XWPFDocument doc, String... keywords) {
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    String text = cell.getText();
                    for (String k : keywords) if (text != null && text.contains(k)) return table;
                }
            }
        }
        return null;
    }

    private static int findWordColumnByText(XWPFTableRow row, String... names) {
        List<XWPFTableCell> cells = row.getTableCells();
        for (int c = 0; c < cells.size(); c++) {
            String val = cells.get(c).getText().trim();
            for (String n : names) if (n.equals(val) || val.contains(n)) return c;
        }
        return -1;
    }

    private static String getWordCellText(XWPFTableRow row, int col) {
        List<XWPFTableCell> cells = row.getTableCells();
        return col < cells.size() ? cells.get(col).getText() : "";
    }

    private static int getWordCellGridSpan(XWPFTableCell cell) {
        if (cell == null) return 1;
        try {
            if (cell.getCTTc() != null && cell.getCTTc().getTcPr() != null && cell.getCTTc().getTcPr().getGridSpan() != null) {
                BigInteger val = cell.getCTTc().getTcPr().getGridSpan().getVal();
                return val != null ? val.intValue() : 1;
            }
        } catch (Exception ignored) { /* NOP */ }
        return 1;
    }

    private static List<String> parseCsvLine(String line) {
        List<String> out = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                inQuotes = !inQuotes;
            } else if ((ch == ',' && !inQuotes) || ch == '\n' || ch == '\r') {
                out.add(cur.toString().trim());
                cur = new StringBuilder();
                if (ch != ',') break;
            } else {
                cur.append(ch);
            }
        }
        out.add(cur.toString().trim());
        return out;
    }

    private static String normalizeStatus(String value) {
        if (value == null) return "";
        value = value.trim().toUpperCase();
        if (value.equals("√") || value.equals("V")) return "V";
        if (value.equals("×") || value.equals("X")) return "X";
        return value;
    }
}
