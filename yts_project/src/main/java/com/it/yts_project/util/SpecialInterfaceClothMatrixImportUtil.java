package com.it.yts_project.util;

import com.it.yts_project.dto.SpecialInterfaceClothMatrixDTO.MatrixRow;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 「布面紙+燙金」－組合應用表 矩阵导入解析。
 * 表头：材質、物料型號、[燙金紙系列…]；数据行：材质、物料型号、各系列单元格 V/X/▷。
 * 解析结果包含「系列名 → 文件中分组(烫金纸性能类型)」，供落库时校验并与 products 表一致。
 */
public final class SpecialInterfaceClothMatrixImportUtil {

    /** 解析结果：数据行 + 系列名→文件中表头分组(烫金纸性能类型)，用于导入校验与 paperType 落库 */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class ParseResult {
        private final List<MatrixRow> rows;
        /** 系列名 → 文件中该列所在分组名（如 普通耐磨、普通金纸），空表示未识别 */
        private final Map<String, String> seriesNameToFoilTypeFromFile;
    }

    private SpecialInterfaceClothMatrixImportUtil() {}

    public static ParseResult parseExcel(InputStream is, List<String> knownSeriesColumns, Set<String> validGroupNames) throws Exception {
        try (Workbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) throw new IllegalArgumentException("Excel file has no sheets");
            int totalRows = sheet.getLastRowNum() + 1;
            if (totalRows < 2) throw new IllegalArgumentException("Excel file has too few rows");

            int headerRowIdx = findHeaderRow(sheet, totalRows);
            Row headerRow = sheet.getRow(headerRowIdx);
            int materialCol = findColumnByName(headerRow, sheet, headerRowIdx, "材質", "材质");
            int modelCol = findColumnByName(headerRow, sheet, headerRowIdx, "物料型號", "物料型号");
            if (materialCol < 0) materialCol = 0;
            if (modelCol < 0) modelCol = 1;

            // 从表头实际文本构建「列索引 -> 系列名」映射；
            // 不再依赖已知列表顺序，这样导出后的 Excel/Word 中手动新增的系列列（如 TB813D）也能被识别。
            int firstSeriesCol = Math.max(materialCol, modelCol) + 1;
            Map<Integer, String> colToSeriesName = new LinkedHashMap<>();
            if (headerRow != null) {
                int lastCol = headerRow.getLastCellNum();
                for (int c = firstSeriesCol; c < lastCol; c++) {
                    String h = getCellStringValue(sheet, headerRowIdx, c).trim();
                    if (h.isEmpty()) continue;
                    // 跳过明显不是系列名的表头（安全兜底，目前导出里不会出现）
                    if ("圖標說明".equals(h) || "图标说明".equals(h)) continue;
                    colToSeriesName.put(c, h);
                }
            }
            if (colToSeriesName.isEmpty()) {
                throw new IllegalArgumentException("Cannot locate series columns in Excel header");
            }

            // 导出 Excel 中上一行为分组名（普通耐磨、普通金纸等），用于 paperType
            Map<String, String> seriesNameToFoilTypeFromFile = new LinkedHashMap<>();
            int groupRowIdx = headerRowIdx > 0 ? headerRowIdx - 1 : -1;
            if (groupRowIdx >= 0) {
                Row groupRow = sheet.getRow(groupRowIdx);
                for (Map.Entry<Integer, String> e : colToSeriesName.entrySet()) {
                    String g = groupRow != null ? getCellStringValue(sheet, groupRowIdx, e.getKey()).trim() : "";
                    g = (g != null ? g.trim() : "");
                    if (!g.isEmpty()) seriesNameToFoilTypeFromFile.put(e.getValue(), g);
                }
            }

            List<MatrixRow> rows = new ArrayList<>();
            for (int r = headerRowIdx + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                String material = getCellStringValue(sheet, r, materialCol).trim();
                String model = getCellStringValue(sheet, r, modelCol).trim();
                if (material.isEmpty() && model.isEmpty()) continue;

                MatrixRow mRow = new MatrixRow();
                mRow.setMaterialCategory(material);
                mRow.setMaterialModel(model);
                for (Map.Entry<Integer, String> e : colToSeriesName.entrySet()) {
                    int colIdx = e.getKey();
                    String seriesName = e.getValue();
                    String value = getCellStringValue(sheet, r, colIdx).trim();
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) {
                        mRow.getCells().put(seriesName, value);
                    }
                }
                rows.add(mRow);
            }
            return new ParseResult(rows, seriesNameToFoilTypeFromFile);
        }
    }

    public static ParseResult parseWord(InputStream is, List<String> knownSeriesColumns, Set<String> validGroupNames) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(is)) {
            XWPFTable table = findFirstTableWithHeader(doc, "材質", "物料型號", "材质", "物料型号");
            if (table == null) throw new IllegalArgumentException("Cannot find table with 材質/物料型號 in Word");

            List<XWPFTableRow> tableRows = table.getRows();
            if (tableRows.size() < 2) throw new IllegalArgumentException("Word table has too few rows");

            int headerRowIdx = 0;
            for (int r = 0; r < Math.min(5, tableRows.size()); r++) {
                String firstCell = getWordCellText(tableRows.get(r), 0);
                if (firstCell.contains("材質") || firstCell.contains("材质") || firstCell.contains("物料")) {
                    headerRowIdx = r;
                    break;
                }
            }

            XWPFTableRow headerRow = tableRows.get(headerRowIdx);
            int materialCol = findWordColumnByText(headerRow, "材質", "材质");
            int modelCol = findWordColumnByText(headerRow, "物料型號", "物料型号");
            if (materialCol < 0) materialCol = 0;
            if (modelCol < 0) modelCol = 1;

            int firstSeriesCol = Math.max(materialCol, modelCol) + 1;
            Map<Integer, String> colToSeriesName = new LinkedHashMap<>();
            Map<String, String> seriesNameToFoilTypeFromFile = new LinkedHashMap<>();
            // 导出的 .docx 表头为两行：当前行是「材質、物料型號、分组名(普通耐磨/普通金纸…)」，
            // 下一行才是「系列名(GN、TB813D、GN2…)」。优先用下一行作为系列名列，否则 TB813D 等会被读成分组名。
            int seriesNameRowIdx = headerRowIdx + 1;
            XWPFTableRow seriesNameRow = (seriesNameRowIdx < tableRows.size()) ? tableRows.get(seriesNameRowIdx) : null;
            XWPFTableRow rowForSeries = seriesNameRow;
            if (rowForSeries == null) rowForSeries = headerRow;
            List<XWPFTableCell> cellsForSeries = rowForSeries.getTableCells();
            for (int c = firstSeriesCol; c < cellsForSeries.size(); c++) {
                String h = getWordCellText(rowForSeries, c).trim();
                if (h.isEmpty()) continue;
                // 若取到的是分组名（与常见分组一致），且下一行存在，说明应使用下一行的系列名，本列可能被合并跳过
                if (rowForSeries == headerRow && seriesNameRow != null && isGroupHeaderName(h, validGroupNames)) {
                    continue;
                }
                colToSeriesName.put(c, h);
                // 从分组行（headerRow）按逻辑列取该列的分组名，作为该系列的烫金纸性能类型
                if (seriesNameRow != null && headerRow != null) {
                    String group = getWordCellTextAtLogicalColumn(headerRow, c);
                    group = (group != null ? group.trim() : "");
                    if (!group.isEmpty()) seriesNameToFoilTypeFromFile.put(h, group);
                }
            }
            if (colToSeriesName.isEmpty()) {
                throw new IllegalArgumentException("Cannot locate series columns in Word table header");
            }

            List<MatrixRow> rows = new ArrayList<>();
            int firstDataRowIdx = seriesNameRow != null ? seriesNameRowIdx + 1 : headerRowIdx + 1;
            for (int r = firstDataRowIdx; r < tableRows.size(); r++) {
                XWPFTableRow row = tableRows.get(r);
                List<XWPFTableCell> cells = row.getTableCells();
                String material = (materialCol < cells.size()) ? getWordCellText(row, materialCol).trim() : "";
                String model = (modelCol < cells.size()) ? getWordCellText(row, modelCol).trim() : "";
                if (material.isEmpty() && model.isEmpty()) continue;

                MatrixRow mRow = new MatrixRow();
                mRow.setMaterialCategory(material);
                mRow.setMaterialModel(model);
                for (Map.Entry<Integer, String> e : colToSeriesName.entrySet()) {
                    int colIdx = e.getKey();
                    String seriesName = e.getValue();
                    String value = (colIdx < cells.size()) ? cells.get(colIdx).getText().trim() : "";
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) {
                        mRow.getCells().put(seriesName, value);
                    }
                }
                rows.add(mRow);
            }
            return new ParseResult(rows, seriesNameToFoilTypeFromFile);
        }
    }

    public static ParseResult parseCsv(InputStream is, List<String> knownSeriesColumns) throws Exception {
        List<MatrixRow> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            int lineNum = 0;
            int materialCol = 0, modelCol = 1;
            Map<Integer, String> colToSeriesName = null;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                List<String> cells = parseCsvLine(line);
                if (cells.isEmpty()) continue;

                if (lineNum == 1 && (cells.get(0).contains("材") || cells.get(0).contains("物料"))) {
                    // 表头行：识别材质 / 物料列以及系列列名
                    for (int i = 0; i < cells.size(); i++) {
                        String h = cells.get(i).trim();
                        if (h.contains("材質") || h.contains("材质")) materialCol = i;
                        else if (h.contains("物料型號") || h.contains("物料型号")) modelCol = i;
                    }
                    int firstSeriesCol = Math.max(materialCol, modelCol) + 1;
                    colToSeriesName = new LinkedHashMap<>();
                    for (int i = firstSeriesCol; i < cells.size(); i++) {
                        String h = cells.get(i).trim();
                        if (!h.isEmpty()) {
                            colToSeriesName.put(i, h);
                        }
                    }
                    continue;
                }

                if (colToSeriesName == null) {
                    // 若首行不是表头，则无法可靠识别列结构
                    continue;
                }

                String material = cells.size() > materialCol ? cells.get(materialCol).trim() : "";
                String model = cells.size() > modelCol ? cells.get(modelCol).trim() : "";
                if (material.isEmpty() && model.isEmpty()) continue;

                MatrixRow mRow = new MatrixRow();
                mRow.setMaterialCategory(material);
                mRow.setMaterialModel(model);
                for (Map.Entry<Integer, String> e : colToSeriesName.entrySet()) {
                    int colIdx = e.getKey();
                    String seriesName = e.getValue();
                    String value = colIdx < cells.size() ? cells.get(colIdx).trim() : "";
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) {
                        mRow.getCells().put(seriesName, value);
                    }
                }
                rows.add(mRow);
            }
        }
        return new ParseResult(rows, Collections.emptyMap());
    }

    private static String getWordCellTextAtLogicalColumn(XWPFTableRow row, int logicalColIdx) {
        if (row == null || logicalColIdx < 0) return "";
        List<XWPFTableCell> cells = row.getTableCells();
        int logical = 0;
        for (XWPFTableCell cell : cells) {
            int span = getWordCellGridSpan(cell);
            if (logicalColIdx >= logical && logicalColIdx < logical + span) {
                String t = cell.getText();
                return t != null ? t.trim() : "";
            }
            logical += span;
        }
        return "";
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

    private static int findHeaderRow(Sheet sheet, int totalRows) {
        for (int r = 0; r < Math.min(10, totalRows); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            String first = getCellStringValue(sheet, r, 0).trim();
            String second = getCellStringValue(sheet, r, 1).trim();
            if (first.contains("材質") || first.contains("材质") || second.contains("物料型號") || second.contains("物料型号"))
                return r;
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

    /** 判断是否为表头分组名（与 products 表中的烫金纸性能类型完全一致），用于跳过用分组行当系列名 */
    private static boolean isGroupHeaderName(String h, Set<String> validGroupNames) {
        if (h == null || validGroupNames == null) return false;
        return validGroupNames.contains(h.trim());
    }

    private static String getWordCellText(XWPFTableRow row, int col) {
        List<XWPFTableCell> cells = row.getTableCells();
        return col < cells.size() ? cells.get(col).getText() : "";
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
        if (value.equals("▽") || value.equals("▷")) return "▷";
        if (value.equals("NA")) return "NA";
        return value;
    }
}
