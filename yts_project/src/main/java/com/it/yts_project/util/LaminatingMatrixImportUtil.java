package com.it.yts_project.util;

import com.it.yts_project.dto.LaminatingMatrixDTO.FoilColumnDef;

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

/**
 * 燙金後過膠－應用限制 矩陣導入解析。
 * 表頭：適用界面、過膠種類、燙後過膠後加工、[燙金紙列…]；數據行：對應儲存格 √/× 或 V/X。
 */
public final class LaminatingMatrixImportUtil {

    private LaminatingMatrixImportUtil() {}

    @lombok.Data
    public static class ParsedRow {
        private String productType;
        private String laminationMaterialName;
        private String postProcessingStepName;
        private Map<String, String> cells = new LinkedHashMap<>();
    }

    /** 解析結果：rows 必有；columnDefs 僅在從 Word/Excel 表頭構建時非空，供 applyImport 使用 */
    @lombok.Data
    public static class ParseResult {
        private List<ParsedRow> rows;
        private List<FoilColumnDef> columnDefs;
    }

    public static List<ParsedRow> parseExcel(InputStream is, List<FoilColumnDef> columnDefs) throws Exception {
        try (Workbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) throw new IllegalArgumentException("Excel file has no sheets");
            int totalRows = sheet.getLastRowNum() + 1;
            if (totalRows < 2) throw new IllegalArgumentException("Excel file has too few rows");

            int headerRowIdx = findHeaderRow(sheet, totalRows);
            Row headerRow = sheet.getRow(headerRowIdx);
            int colPrefix = findColumnByName(headerRow, sheet, headerRowIdx, "過膠種類(前綴)");
            int colSuffix = findColumnByName(headerRow, sheet, headerRowIdx, "過膠種類(後綴)");
            int col0 = findColumnByName(headerRow, sheet, headerRowIdx, "適用界面", "產品類型");
            int col1 = findColumnByName(headerRow, sheet, headerRowIdx, "過膠種類", "過膠材料");
            int colStep = findColumnByName(headerRow, sheet, headerRowIdx, "燙後過膠後加工", "過膠後工藝");
            boolean hasPrefixSuffix = colPrefix >= 0 && colSuffix >= 0;
            int dataStart = hasPrefixSuffix ? 3 : 3;
            if (colStep < 0) colStep = hasPrefixSuffix ? 2 : 2;
            if (col0 < 0) col0 = 0;
            if (col1 < 0) col1 = 1;

            List<String> colKeys = buildColumnKeysFromDefs(columnDefs);
            if (colKeys.isEmpty()) {
                for (int c = dataStart; c <= headerRow.getLastCellNum(); c++) {
                    String h = getCellStringValue(sheet, headerRowIdx, c).trim();
                    if (!h.isEmpty()) colKeys.add(h);
                }
            }

            List<ParsedRow> rows = new ArrayList<>();
            for (int r = headerRowIdx + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                String mat;
                if (hasPrefixSuffix) {
                    String px = getCellStringValue(sheet, r, colPrefix).trim();
                    String sx = getCellStringValue(sheet, r, colSuffix).trim();
                    mat = (px.isEmpty() && sx.isEmpty()) ? "" : (px + "-" + sx);
                } else {
                    mat = getCellStringValue(sheet, r, col1).trim();
                }
                String step = getCellStringValue(sheet, r, colStep).trim();
                if (mat.isEmpty()) continue;

                ParsedRow pr = new ParsedRow();
                if (!hasPrefixSuffix) pr.setProductType(getCellStringValue(sheet, r, col0).trim());
                pr.setLaminationMaterialName(mat);
                pr.setPostProcessingStepName(step);
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

    public static ParseResult parseWord(InputStream is, List<FoilColumnDef> columnDefs) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(is)) {
            XWPFTable table = findFirstTableWithHeader(doc, "過膠種類", "過膠", "產品類型", "燙金紙選用");
            if (table == null) throw new IllegalArgumentException("Cannot find table with 過膠種類/燙金紙選用 in Word");

            List<XWPFTableRow> tableRows = table.getRows();
            if (tableRows.size() < 2) throw new IllegalArgumentException("Word table has too few rows");

            int headerRowIdx = -1;
            int colPrefix = -1, colSuffix = -1, colStep = -1;
            for (int r = 0; r < Math.min(10, tableRows.size()); r++) {
                XWPFTableRow row = tableRows.get(r);
                colPrefix = findWordColumnByText(row, "過膠種類(前綴)");
                colSuffix = findWordColumnByText(row, "過膠種類(後綴)");
                colStep = findWordColumnByText(row, "燙後過膠後加工", "與燙金位置重疊");
                if (colPrefix >= 0 && colSuffix >= 0 && colStep >= 0) {
                    headerRowIdx = r;
                    break;
                }
            }
            if (headerRowIdx < 0) {
                for (int r = 0; r < Math.min(10, tableRows.size()); r++) {
                    String c0 = getWordCellText(tableRows.get(r), 0).trim();
                    String c1 = getWordCellText(tableRows.get(r), 1).trim();
                    if (c0.contains("過膠種類(前綴)") || c1.contains("過膠種類(前綴)")) {
                        headerRowIdx = r;
                        colPrefix = 0;
                        colSuffix = 1;
                        colStep = 2;
                        break;
                    }
                }
            }
            if (headerRowIdx < 0) headerRowIdx = 0;
            if (colPrefix < 0) colPrefix = 0;
            if (colSuffix < 0) colSuffix = 1;
            if (colStep < 0) colStep = 2;

            XWPFTableRow headerRow = tableRows.get(headerRowIdx);
            boolean hasPrefixSuffix = (colPrefix >= 0 && colSuffix >= 0);
            int col0 = findWordColumnByText(headerRow, "適用界面", "產品類型");
            int col1 = findWordColumnByText(headerRow, "過膠種類", "過膠材料");
            if (col0 < 0) col0 = 0;
            if (col1 < 0) col1 = 1;

            final int dataColStart = 3;
            List<FoilColumnDef> builtColumnDefs = null;
            List<String> colKeys;

            // 始終優先從 Word 表頭（燙金紙類型行 + 具體型號行）解析列定義，使導入結果與文件結構一致（如圖一導入後仍為圖一）
            // 與導出對齊：Row 2=過膠種類…，Row 3=燙金紙類型，Row 4=具體型號；類型行=header+1，型號行=header+2
            if (headerRowIdx + 2 < tableRows.size()) {
                int typeRowIdx = headerRowIdx + 1;
                int modelRowIdx = headerRowIdx + 2;
                XWPFTableRow typeRow = tableRows.get(typeRowIdx);
                XWPFTableRow modelRow = tableRows.get(modelRowIdx);
                int modelCells = modelRow.getTableCells().size();
                int numFoilCols = Math.max(0, modelCells - dataColStart);
                if (numFoilCols > 0) {
                    // 類型行可能有橫向合併（gridSpan），需按合併寬度展開，否則會錯配（如 L817 被標成鐳射燙金紙）
                    List<String> types = new ArrayList<>();
                    String lastType = "";
                    List<XWPFTableCell> typeCells = typeRow.getTableCells();
                    int logicalCol = 0;
                    for (int i = dataColStart; i < typeCells.size() && logicalCol < numFoilCols; i++) {
                        XWPFTableCell cell = typeCells.get(i);
                        String t = (cell.getText() != null ? cell.getText().trim() : "");
                        if (!t.isEmpty()) lastType = t;
                        int span = getWordCellGridSpan(cell);
                        for (int j = 0; j < span && logicalCol < numFoilCols; j++) {
                            types.add(lastType);
                            logicalCol++;
                        }
                    }
                    while (types.size() < numFoilCols) types.add(lastType);
                    List<FoilColumnDef> defs = new ArrayList<>();
                    for (int i = 0; i < numFoilCols; i++) {
                        String modelLabel = getWordCellText(modelRow, dataColStart + i).trim();
                        boolean jiehuo = modelLabel.contains("(街貨)");
                        String series = modelLabel.replace("(街貨)", "").trim();
                        FoilColumnDef col = new FoilColumnDef(series, null, jiehuo);
                        if (!types.get(i).isEmpty()) col.setFoilTypeName(types.get(i));
                        defs.add(col);
                    }
                    builtColumnDefs = defs;
                    colKeys = buildColumnKeysFromDefs(builtColumnDefs);
                } else {
                    colKeys = buildColumnKeysFromDefs(columnDefs);
                }
            } else {
                colKeys = buildColumnKeysFromDefs(columnDefs);
            }
            if (colKeys.isEmpty() && builtColumnDefs == null) {
                int typeRowIdx = (headerRowIdx + 2 < tableRows.size()) ? headerRowIdx + 2 : -1;
                if (typeRowIdx >= 0) {
                    XWPFTableRow typeRow = tableRows.get(typeRowIdx);
                    for (int c = dataColStart; c < typeRow.getTableCells().size(); c++) {
                        String h = getWordCellText(typeRow, c).trim();
                        if (!h.isEmpty()) colKeys.add(h);
                    }
                }
                if (colKeys.isEmpty()) {
                    for (int c = dataColStart; c < headerRow.getTableCells().size(); c++) {
                        String h = getWordCellText(headerRow, c).trim();
                        if (!h.isEmpty()) colKeys.add(h);
                    }
                }
            }

            int dataStartRowIdx = hasPrefixSuffix ? headerRowIdx + 3 : headerRowIdx + 1;
            int numFoilCols = colKeys.size();
            String lastPrefix = "", lastSuffix = "";
            List<ParsedRow> rows = new ArrayList<>();
            for (int r = dataStartRowIdx; r < tableRows.size(); r++) {
                XWPFTableRow row = tableRows.get(r);
                List<XWPFTableCell> cells = row.getTableCells();
                String pt = (col0 < cells.size()) ? getWordCellText(row, col0).trim() : "";
                String mat;
                int stepColIdx;
                int dataStart;
                if (hasPrefixSuffix && numFoilCols > 0 && cells.size() >= dataColStart + numFoilCols) {
                    String px = getWordCellText(row, colPrefix).trim();
                    String sx = getWordCellText(row, colSuffix).trim();
                    String effectivePrefix = px.isEmpty() ? lastPrefix : px;
                    String effectiveSuffix = sx.isEmpty() ? lastSuffix : sx;
                    if (!effectivePrefix.isEmpty() || !effectiveSuffix.isEmpty()) {
                        lastPrefix = effectivePrefix;
                        lastSuffix = effectiveSuffix;
                    }
                    mat = (lastPrefix.isEmpty() && lastSuffix.isEmpty()) ? "" : (lastPrefix + "-" + lastSuffix);
                    stepColIdx = colStep;
                    dataStart = dataColStart;
                } else if (hasPrefixSuffix && numFoilCols > 0 && cells.size() >= 1 + numFoilCols) {
                    mat = (lastPrefix.isEmpty() && lastSuffix.isEmpty()) ? "" : (lastPrefix + "-" + lastSuffix);
                    stepColIdx = 0;
                    dataStart = 1;
                } else {
                    mat = (col1 < cells.size()) ? getWordCellText(row, col1).trim() : "";
                    stepColIdx = colStep;
                    dataStart = dataColStart;
                }
                String step = getWordCellText(row, stepColIdx).trim();
                if (hasPrefixSuffix ? mat.isEmpty() : (pt.isEmpty() && mat.isEmpty())) continue;

                ParsedRow pr = new ParsedRow();
                if (!hasPrefixSuffix) pr.setProductType(pt);
                pr.setLaminationMaterialName(mat);
                pr.setPostProcessingStepName(step);
                for (int i = 0; i < colKeys.size(); i++) {
                    int colIdx = dataStart + i;
                    String value = colIdx < cells.size() ? getWordCellText(row, colIdx).trim() : "";
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) pr.getCells().put(colKeys.get(i), value);
                }
                rows.add(pr);
            }
            ParseResult result = new ParseResult();
            result.setRows(rows);
            result.setColumnDefs(builtColumnDefs);
            return result;
        }
    }

    public static List<ParsedRow> parseCsv(InputStream is, List<FoilColumnDef> columnDefs) throws Exception {
        List<ParsedRow> rows = new ArrayList<>();
        List<String> colKeys = buildColumnKeysFromDefs(columnDefs);
        int[] csvDataStart = { 3 };
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean headerDone = false;
            while ((line = reader.readLine()) != null) {
                List<String> cells = parseCsvLine(line);
                if (cells.isEmpty()) continue;
                String first = cells.get(0).trim();
                if (!headerDone && (first.contains("適用界面") || first.contains("過膠種類") || first.contains("圖例") || first.contains("燙金後過膠"))) {
                    if (first.contains("適用界面") || first.contains("過膠種類") && cells.size() > 3) {
                        colKeys.clear();
                        csvDataStart[0] = 3;
                        for (int i = csvDataStart[0]; i < cells.size(); i++) {
                            String h = cells.get(i).trim();
                            if (!h.isEmpty()) colKeys.add(h);
                        }
                        headerDone = true;
                    }
                    continue;
                }
                if (!headerDone) continue;
                int dataStart = csvDataStart[0];
                String mat;
                String step;
                String px = cells.size() > 0 ? cells.get(0).trim() : "";
                String sx = cells.size() > 1 ? cells.get(1).trim() : "";
                mat = (px.isEmpty() && sx.isEmpty()) ? "" : (px + "-" + sx);
                step = cells.size() > 2 ? cells.get(2).trim() : "";
                if (mat.isEmpty()) continue;

                ParsedRow pr = new ParsedRow();
                pr.setLaminationMaterialName(mat);
                pr.setPostProcessingStepName(step);
                for (int i = 0; i < colKeys.size(); i++) {
                    int colIdx = dataStart + i;
                    String value = colIdx < cells.size() ? cells.get(colIdx).trim() : "";
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) pr.getCells().put(colKeys.get(i), value);
                }
                rows.add(pr);
            }
        }
        return rows;
    }

    private static List<String> buildColumnKeysFromDefs(List<FoilColumnDef> columnDefs) {
        if (columnDefs == null || columnDefs.isEmpty()) return new ArrayList<>();
        List<String> keys = new ArrayList<>();
        for (FoilColumnDef col : columnDefs) keys.add(col.cellKey());
        return keys;
    }

    /** 找數據表頭行：優先識別「過膠種類(前綴)」所在行，避免把標題行「燙金後過膠-應用限制」當成表頭 */
    private static int findHeaderRow(Sheet sheet, int totalRows) {
        int fallback = 0;
        for (int r = 0; r < Math.min(10, totalRows); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            String first = getCellStringValue(sheet, r, 0).trim();
            String second = getCellStringValue(sheet, r, 1).trim();
            if (first.contains("過膠種類(前綴)") || first.contains("過膠種類(後綴)")
                    || second.contains("過膠種類(前綴)") || second.contains("過膠種類(後綴)")) {
                return r;
            }
            if (fallback == 0 && (first.contains("適用界面") || first.contains("過膠種類") || first.contains("產品類型"))) {
                fallback = r;
            }
        }
        return fallback;
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
                java.math.BigInteger val = cell.getCTTc().getTcPr().getGridSpan().getVal();
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
