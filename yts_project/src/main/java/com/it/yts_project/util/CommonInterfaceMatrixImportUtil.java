package com.it.yts_project.util;

import com.it.yts_project.dto.CommonInterfaceMatrixDTO.ColumnDef;
import com.it.yts_project.dto.CommonInterfaceMatrixDTO.MatrixRow;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

/**
 * 常用界面燙印性 組合應用表 — Excel / Word 矩阵导入工具。
 *
 * 解析逻辑：
 * 1. 跳过标题行和图例行（通过关键字识别）
 * 2. 识别分组表头行（包含 "燙金圖案"、"燙金類型"、"常用界面"、"特殊界面" 等关键字）
 * 3. 识别子表头行（各列具体名称）
 * 4. 通过子表头名称与系统中的 ColumnDef 列表进行模糊匹配，建立列索引 → cellKey 映射
 * 5. 解析数据行，提取 paperType、seriesName 和各单元格的 V/X 值
 */
public final class CommonInterfaceMatrixImportUtil {

    private CommonInterfaceMatrixImportUtil() {}

    /** Word/Excel 解析结果：数据行 + 新增前工序列列 key 集合（PP_NEW_*）+ 工艺类型对应的前工序类型（用于创建选项时填 description）。 */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class ParseResult {
        private final List<MatrixRow> rows;
        private final Set<String> ppNewColumnKeys;
        /** 工艺类型名称 -> 前工序类型（如 "测试1" -> "测试"），从分组行 "(前工序)测试" 提取，可为 null 或空。 */
        private final Map<String, String> ppNewColumnPreProcessType;
    }

    // ==================== Excel 导入 ====================

    public static ParseResult parseExcel(
            InputStream is,
            List<ColumnDef> paColumns,
            List<ColumnDef> stColumns,
            List<ColumnDef> ppColumns,
            List<ColumnDef> siColumns) throws Exception {

        try (Workbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("Excel file has no sheets");
            }

            int totalRows = sheet.getLastRowNum() + 1;
            if (totalRows < 3) {
                throw new IllegalArgumentException("Excel file has too few rows to contain a valid matrix");
            }

            // 查找表头行：扫描前10行，找到包含 "系列" 的子表头行
            int subHeaderRowIdx = -1;
            int groupHeaderRowIdx = -1;
            for (int r = 0; r < Math.min(10, totalRows); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                for (int c = 0; c <= Math.min(5, row.getLastCellNum()); c++) {
                    String val = getCellStringValue(sheet, r, c);
                    if (val.contains("燙金圖案") || val.contains("燙金類型") || val.contains("常用界面")) {
                        groupHeaderRowIdx = r;
                    }
                    if ("系列".equals(val.trim())) {
                        subHeaderRowIdx = r;
                        break;
                    }
                }
                if (subHeaderRowIdx >= 0) break;
            }

            if (subHeaderRowIdx < 0) {
                if (groupHeaderRowIdx >= 0) {
                    subHeaderRowIdx = groupHeaderRowIdx + 1;
                } else {
                    throw new IllegalArgumentException("Cannot locate header row in Excel. Expected a row containing '系列'. If this file is Word, use .docx extension.");
                }
            }

            // 构建列映射：若有「列名行」（含系列的下一行再下一行，與 Word 第 4 行對應），用其讀取每列具體名稱，避免讀到分組名「適用界面」等
            Row subHeaderRow = sheet.getRow(subHeaderRowIdx);
            int columnNameRowIdx = subHeaderRowIdx;
            if (totalRows > subHeaderRowIdx + 2) {
                Row candidate = sheet.getRow(subHeaderRowIdx + 2);
                if (candidate != null && candidate.getLastCellNum() >= 2) {
                    columnNameRowIdx = subHeaderRowIdx + 2;
                }
            }
            Row columnNameRow = sheet.getRow(columnNameRowIdx);
            Map<Integer, String> colToCellKey = buildColumnMapping(
                    columnNameRow != null ? columnNameRow : subHeaderRow,
                    sheet, columnNameRow != null ? columnNameRowIdx : subHeaderRowIdx,
                    paColumns, stColumns, ppColumns, siColumns);
            // Excel：若某些表頭未匹配到已知列，且位於第一個 SI_ 列之前，視為新增前工序列（PP_NEW_），而不是 SI_NEW_
            adjustNewPreProcessColumns(colToCellKey);

            int seriesColIdx = findColumnByName(subHeaderRow, sheet, subHeaderRowIdx, "系列");
            int typeColIdx = findColumnByName(subHeaderRow, sheet, subHeaderRowIdx, "類型");

            if (seriesColIdx < 0) {
                seriesColIdx = 1;
            }
            if (typeColIdx < 0) {
                typeColIdx = 0;
            }

            // 解析数据行
            List<MatrixRow> rows = new ArrayList<>();
            String lastPaperType = "";

            for (int r = subHeaderRowIdx + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                String seriesName = getCellStringValue(sheet, r, seriesColIdx).trim();
                if (seriesName.isEmpty()) continue;

                String paperType = getCellStringValue(sheet, r, typeColIdx).trim();
                if (paperType.isEmpty()) {
                    paperType = lastPaperType;
                } else {
                    lastPaperType = paperType;
                }

                MatrixRow mRow = new MatrixRow();
                mRow.setPaperType(paperType);
                mRow.setSeriesName(seriesName);

                for (Map.Entry<Integer, String> entry : colToCellKey.entrySet()) {
                    String value = getCellStringValue(sheet, r, entry.getKey()).trim().toUpperCase();
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) {
                        mRow.getCells().put(entry.getValue(), value);
                    }
                }

                rows.add(mRow);
            }

            Set<String> ppNewColumnKeys = new HashSet<>();
            for (String v : colToCellKey.values()) {
                if (v != null && v.startsWith("PP_NEW_")) ppNewColumnKeys.add(v);
            }
            return new ParseResult(rows, ppNewColumnKeys, null);
        }
    }

    // ==================== Word 导入 ====================

    public static ParseResult parseWord(
            InputStream is,
            List<ColumnDef> paColumns,
            List<ColumnDef> stColumns,
            List<ColumnDef> ppColumns,
            List<ColumnDef> siColumns) throws Exception {

        try (XWPFDocument doc = new XWPFDocument(is)) {
            XWPFTable targetTable = findTargetTable(doc);
            if (targetTable == null) {
                throw new IllegalArgumentException("Cannot find the target table in Word document");
            }

            List<XWPFTableRow> tableRows = targetTable.getRows();
            if (tableRows.size() < 3) {
                throw new IllegalArgumentException("Word table has too few rows");
            }

            int paCount = paColumns != null ? paColumns.size() : 0;
            int stCount = stColumns != null ? stColumns.size() : 0;
            int ppCount = ppColumns != null ? ppColumns.size() : 0;
            int siCount = siColumns != null ? siColumns.size() : 0;

            // 查找表头行
            int subHeaderRowIdx = -1;
            int groupHeaderRowIdx = -1;
            for (int r = 0; r < Math.min(10, tableRows.size()); r++) {
                XWPFTableRow row = tableRows.get(r);
                for (int c = 0; c < Math.min(6, row.getTableCells().size()); c++) {
                    String val = getWordCellText(row, c);
                    if (val.contains("燙金圖案") || val.contains("燙金類型") || val.contains("常用界面")) {
                        groupHeaderRowIdx = r;
                    }
                    if ("系列".equals(val.trim())) {
                        subHeaderRowIdx = r;
                        break;
                    }
                }
                if (subHeaderRowIdx >= 0) break;
            }

            if (subHeaderRowIdx < 0) {
                subHeaderRowIdx = groupHeaderRowIdx >= 0 ? groupHeaderRowIdx + 1 : 1;
            }
            // 构建列映射（并保留分組行供後續提取「前工序類型」，用於工藝類型所屬前工序類型）
            XWPFTableRow subHeaderRow = tableRows.get(subHeaderRowIdx);
            XWPFTableRow categoryRowForPreProcessType = null;
            int compatStartColForPreProcessType = 2 + paCount + stCount;
            Map<Integer, String> colToCellKey;
            if (isNewDocxExport(targetTable, paCount, stCount, ppCount, siCount)) {
                // 新版 .docx：優先用「列名行」（subHeader 下一行再下一行）逐列匹配表頭，未匹配用 PP_NEW_/SI_NEW_，確保用戶手動改名/新增列能被識別。
                // PA/ST/PP/SI 的列順序與 ColumnDef 一致，對於多行表頭（如烫金類型+位置類型）的 ST 區，可按固定順序補全映射。
                int nameRowIdx = subHeaderRowIdx + 2;
                int categoryRowIdx = subHeaderRowIdx + 1;
                XWPFTableRow nameRow = (tableRows.size() > nameRowIdx) ? tableRows.get(nameRowIdx) : null;
                XWPFTableRow categoryRow = (tableRows.size() > categoryRowIdx) ? tableRows.get(categoryRowIdx) : null;
                if (nameRow != null) {
                    colToCellKey = buildWordColumnMapping(nameRow, paColumns, stColumns, ppColumns, siColumns);
                    // ST 區：根據表頭「烫金分類」所在單元格的邏輯列範圍重建映射，避免把烫金圖案列誤當烫金分類。
                    // 1) 能與現有 ColumnDef 對上的 → ST_列ID；2) 對不上 → ST_NEW_烫金類型__POS__位置類型
                    XWPFTableRow topRow = tableRows.get(0);
                    List<XWPFTableCell> topCells = topRow.getTableCells();
                    int stLogicalStart = -1, stLogicalEnd = -1;
                    int logicalAccum = 0;
                    for (int c = 0; c < topCells.size(); c++) {
                        String t = getWordCellText(topRow, c);
                        int span = getWordCellGridSpan(topCells.get(c));
                        if (t != null && (t.contains("烫金分类") || t.contains("燙金分類"))) {
                            stLogicalStart = logicalAccum;
                            stLogicalEnd = logicalAccum + span - 1;
                            break;
                        }
                        logicalAccum += span;
                    }
                    if (stLogicalStart >= 0 && stLogicalEnd >= stLogicalStart) {
                        for (int logicalCol = stLogicalStart; logicalCol <= stLogicalEnd; logicalCol++) {
                            String stamping = getWordCellTextAtLogicalColumn(tableRows.get(1), logicalCol);
                            String position = getWordCellTextAtLogicalColumn(tableRows.get(2), logicalCol);
                            stamping = stamping != null ? stamping.trim() : "";
                            position = position != null ? position.trim() : "";
                            if (stamping.isEmpty()) continue;
                            String stampingNorm = normalizeText(stamping);
                            String posNorm = normalizeText(position.isEmpty() ? "默认" : position);
                            Integer matchedId = null;
                            if (stColumns != null) {
                                for (ColumnDef cd : stColumns) {
                                    String cdStamp = cd.getSubGroup() != null ? cd.getSubGroup().trim() : "";
                                    if (cdStamp.isEmpty() && cd.getName() != null) cdStamp = cd.getName().trim();
                                    String cdPos = cd.getName() != null ? cd.getName().trim() : "";
                                    String cdStampNorm = normalizeText(cdStamp);
                                    String cdPosNorm = normalizeText(cdPos.isEmpty() ? "默认" : cdPos);
                                    if (stampingNorm.equals(cdStampNorm) && posNorm.equals(cdPosNorm)) {
                                        matchedId = cd.getId();
                                        break;
                                    }
                                }
                            }
                            if (matchedId != null) {
                                colToCellKey.put(logicalCol, "ST_" + matchedId);
                            } else {
                                String posForKey = position != null ? position : "";
                                colToCellKey.put(logicalCol, "ST_NEW_" + stamping + "__POS__" + posForKey);
                            }
                        }
                    }
                    int compatStartCol = 2 + paCount + stCount;
                    if (categoryRow != null) {
                        int siStartCol = compatStartCol + ppCount;
                        // 給 SI_NEW_ 補充分類（__CAT__），分類源自「適用界面」下面那一行
                        for (Map.Entry<Integer, String> e : new LinkedHashMap<>(colToCellKey).entrySet()) {
                            String k = e.getValue();
                            if (k != null && k.startsWith("SI_NEW_") && !k.contains("__CAT__")) {
                                String cat = getCategoryAtColumn(categoryRow, e.getKey(), siStartCol);
                                if (cat != null && !cat.trim().isEmpty()) {
                                    colToCellKey.put(e.getKey(), k + "__CAT__" + cat.trim());
                                }
                            }
                        }
                        // 若某列所在分組行（categoryRow）包含「前工序」，則無論列名是否帶「前工序」前綴，一律視為前工序列 → PP_NEW_
                        adjustNewPreProcessColumnsByGroup(colToCellKey, categoryRow, compatStartCol);
                        categoryRowForPreProcessType = categoryRow;
                        compatStartColForPreProcessType = compatStartCol;
                    }
                    // 位置在第一個 SI_ 之前的 SI_NEW_ 視為 PP_NEW_（適用界面(前工序) 區中的新增前工序列，如「測試1」「測試2」）
                    adjustNewPreProcessColumns(colToCellKey);
                } else {
                    // 理論上新版 .docx 一定有列名行，這裡只作保險兜底
                    colToCellKey = buildWordColumnMappingForNewExport(paColumns, stColumns, ppColumns, siColumns);
                }
            } else {
                // 兼容舊版 / 手工調整過表頭的 Word：用「列名行」建映射（有第 4 行用第 4 行，否則用含「系列」那行），避免讀到分組名「適用界面」「適用界面(前工序)」當作類型名
                int columnNameRowIdx = (tableRows.size() > 3) ? 3 : subHeaderRowIdx;
                XWPFTableRow columnNameRow = tableRows.get(columnNameRowIdx);
                colToCellKey = buildWordColumnMapping(
                        columnNameRow, paColumns, stColumns, ppColumns, siColumns);
                // 有 4 行以上時，從「適用界面」下面那一行為 SI_NEW_ 列補充分類（測試1 等）
                if (tableRows.size() > 3 && columnNameRowIdx >= 1) {
                    int categoryRowIdxForOld = columnNameRowIdx - 1;
                    for (int r = 0; r < tableRows.size() - 1; r++) {
                        if (rowContainsWord(tableRows.get(r), "适用界面") || rowContainsWord(tableRows.get(r), "適用界面")) {
                            categoryRowIdxForOld = r + 1;
                            break;
                        }
                    }
                    XWPFTableRow categoryRowForOld = tableRows.get(categoryRowIdxForOld);
                    int compatStartCol = 2 + (paColumns != null ? paColumns.size() : 0) + (stColumns != null ? stColumns.size() : 0);
                    for (Map.Entry<Integer, String> e : new LinkedHashMap<>(colToCellKey).entrySet()) {
                        String k = e.getValue();
                        if (k != null && k.startsWith("SI_NEW_") && !k.contains("__CAT__")) {
                            String cat = getCategoryAtColumn(categoryRowForOld, e.getKey(), compatStartCol);
                            if (cat != null && !cat.trim().isEmpty()) {
                                colToCellKey.put(e.getKey(), k + "__CAT__" + cat.trim());
                            }
                        }
                    }
                    // 舊版 Word：若某列所在分組行包含「前工序」，則視為前工序列（PP_NEW_）
                    adjustNewPreProcessColumnsByGroup(colToCellKey, categoryRowForOld, compatStartCol);
                    categoryRowForPreProcessType = categoryRowForOld;
                    compatStartColForPreProcessType = compatStartCol;
                }
                // Word 舊版：若某些未匹配表頭被暫時標為 SI_NEW_，但列位置在已知 SI_ 列之前，視為新增前工序列（PP_NEW_）
                adjustNewPreProcessColumns(colToCellKey);
            }

            int seriesColIdx = findWordColumnByName(subHeaderRow, "系列");
            int typeColIdx = findWordColumnByName(subHeaderRow, "類型");
            if (seriesColIdx < 0) seriesColIdx = 1;
            if (typeColIdx < 0) typeColIdx = 0;

            // 解析数据行
            List<MatrixRow> rows = new ArrayList<>();
            String lastPaperType = "";

            for (int r = subHeaderRowIdx + 1; r < tableRows.size(); r++) {
                XWPFTableRow row = tableRows.get(r);
                List<XWPFTableCell> cells = row.getTableCells();

                String seriesName = (seriesColIdx < cells.size()) ? cells.get(seriesColIdx).getText().trim() : "";
                if (seriesName.isEmpty()) continue;

                String paperType = (typeColIdx < cells.size()) ? cells.get(typeColIdx).getText().trim() : "";
                if (paperType.isEmpty()) {
                    paperType = lastPaperType;
                } else {
                    lastPaperType = paperType;
                }

                MatrixRow mRow = new MatrixRow();
                mRow.setPaperType(paperType);
                mRow.setSeriesName(seriesName);

                for (Map.Entry<Integer, String> entry : colToCellKey.entrySet()) {
                    int colIdx = entry.getKey();
                    String value = (colIdx < cells.size()) ? cells.get(colIdx).getText().trim().toUpperCase() : "";
                    value = normalizeStatus(value);
                    if (!value.isEmpty()) {
                        mRow.getCells().put(entry.getValue(), value);
                    }
                }

                rows.add(mRow);
            }

            Set<String> ppNewColumnKeys = new HashSet<>();
            for (String v : colToCellKey.values()) {
                if (v != null && v.startsWith("PP_NEW_")) ppNewColumnKeys.add(v);
            }
            Map<String, String> ppNewColumnPreProcessType = buildPpNewColumnPreProcessType(colToCellKey, categoryRowForPreProcessType, compatStartColForPreProcessType);
            return new ParseResult(rows, ppNewColumnKeys, ppNewColumnPreProcessType);
        }
    }

    /**
     * 從分組行為每個 PP_NEW_ 列取得對應的「前工序類型」（如 "(前工序)测试" → "测试"），
     * 使創建前工序選項時可寫入 description，區分「前工序類型」與「工藝類型」。
     */
    private static Map<String, String> buildPpNewColumnPreProcessType(Map<Integer, String> colToCellKey,
                                                                     XWPFTableRow categoryRow, int compatStartCol) {
        Map<String, String> out = new HashMap<>();
        if (categoryRow == null || colToCellKey == null) return out;
        for (Map.Entry<Integer, String> e : colToCellKey.entrySet()) {
            String v = e.getValue();
            if (v == null || !v.startsWith("PP_NEW_")) continue;
            String stepName = v.substring(7).trim();
            int idx = stepName.indexOf("__CAT__");
            if (idx >= 0) stepName = stepName.substring(0, idx);
            if (stepName.isEmpty()) continue;
            String group = getCategoryAtColumn(categoryRow, e.getKey(), compatStartCol);
            String preProcessType = extractPreProcessTypeFromGroup(group);
            if (preProcessType != null && !preProcessType.isEmpty()) out.put(stepName, preProcessType);
        }
        return out;
    }

    /** 從分組文字提取前工序類型，如 "(前工序)测试"、"前工序测试" → "测试"。 */
    private static String extractPreProcessTypeFromGroup(String group) {
        if (group == null || (group = group.trim()).isEmpty()) return null;
        if (group.startsWith("(前工序)")) return group.substring("(前工序)".length()).trim();
        if (group.startsWith("（前工序）")) return group.substring("（前工序）".length()).trim();
        if (group.contains("(前工序)")) {
            int i = group.indexOf("(前工序)");
            return group.substring(i + "(前工序)".length()).trim();
        }
        if (group.contains("（前工序）")) {
            int i = group.indexOf("（前工序）");
            return group.substring(i + "（前工序）".length()).trim();
        }
        // 無括號時：前工序测试、前工序過膠面 等 → 取「前工序」後面的部分（3 個字元）
        if (group.startsWith("前工序")) return group.substring(3).trim();
        return null;
    }

    // ==================== 列映射构建 ====================

    private static Map<Integer, String> buildColumnMapping(
            Row subHeaderRow, Sheet sheet, int subHeaderRowIdx,
            List<ColumnDef> paColumns, List<ColumnDef> stColumns,
            List<ColumnDef> ppColumns, List<ColumnDef> siColumns) {

        Map<Integer, String> mapping = new LinkedHashMap<>();
        if (subHeaderRow == null) return mapping;

        int lastCol = subHeaderRow.getLastCellNum();
        for (int c = 0; c < lastCol; c++) {
            String headerName = getCellStringValue(sheet, subHeaderRowIdx, c).trim();
            if (headerName.isEmpty() || "類型".equals(headerName) || "系列".equals(headerName)
                    || headerName.contains("規格") || headerName.contains("價格")
                    || headerName.contains("應用產品") || headerName.contains("溫度")) {
                continue;
            }

            String cellKey = matchColumnDef(headerName, paColumns, stColumns, ppColumns, siColumns);
            if (cellKey != null) {
                mapping.put(c, cellKey);
            } else if (!headerName.isEmpty()) {
                // 未匹配：表頭以「前工序:」或「(前工序)」開頭視為新增前工序列，否則視為新增適用界面
                mapping.put(c, mapUnmatchedHeaderToCellKey(headerName));
            }
        }
        return mapping;
    }

    private static Map<Integer, String> buildWordColumnMapping(
            XWPFTableRow subHeaderRow,
            List<ColumnDef> paColumns, List<ColumnDef> stColumns,
            List<ColumnDef> ppColumns, List<ColumnDef> siColumns) {

        Map<Integer, String> mapping = new LinkedHashMap<>();
        List<XWPFTableCell> cells = subHeaderRow.getTableCells();

        for (int c = 0; c < cells.size(); c++) {
            String headerName = cells.get(c).getText().trim();
            if (headerName.isEmpty() || "類型".equals(headerName) || "系列".equals(headerName)
                    || headerName.contains("規格") || headerName.contains("價格")
                    || headerName.contains("應用產品") || headerName.contains("溫度")) {
                continue;
            }

            String cellKey = matchColumnDef(headerName, paColumns, stColumns, ppColumns, siColumns);
            if (cellKey != null) {
                mapping.put(c, cellKey);
            } else if (!headerName.isEmpty()) {
                mapping.put(c, mapUnmatchedHeaderToCellKey(headerName));
            }
        }
        return mapping;
    }

    /** 未匹配表頭：前工序:xxx 或 (前工序)xxx → PP_NEW_xxx，否則 → SI_NEW_xxx */
    private static String mapUnmatchedHeaderToCellKey(String headerName) {
        String t = headerName.trim();
        if (t.startsWith("前工序") && (t.length() == 3 || t.charAt(3) == ':' || t.charAt(3) == '：')) {
            String name = t.substring(3).replaceFirst("^[：:]\\s*", "").trim();
            if (!name.isEmpty()) return "PP_NEW_" + name;
        }
        if (t.startsWith("(前工序)") || t.startsWith("（前工序）")) {
            String name = t.replaceFirst("^[\\(（]前工序[\\)）]\\s*", "").trim();
            if (!name.isEmpty()) return "PP_NEW_" + name;
        }
        return "SI_NEW_" + t;
    }

    /**
     * 新版系統導出的 .docx：列順序與 ColumnDef 一致，直接按順序從第 3 列開始映射：
     * 2..PA、ST、PP、SI 依次排列。
     */
    private static Map<Integer, String> buildWordColumnMappingForNewExport(
            List<ColumnDef> paColumns, List<ColumnDef> stColumns,
            List<ColumnDef> ppColumns, List<ColumnDef> siColumns) {

        Map<Integer, String> mapping = new LinkedHashMap<>();
        int colIdx = 2; // 0=類型, 1=系列

        if (paColumns != null) {
            for (ColumnDef col : paColumns) {
                mapping.put(colIdx++, "PA_" + col.getId());
            }
        }
        if (stColumns != null) {
            for (ColumnDef col : stColumns) {
                mapping.put(colIdx++, "ST_" + col.getId());
            }
        }
        if (ppColumns != null) {
            for (ColumnDef col : ppColumns) {
                mapping.put(colIdx++, "PP_" + col.getId());
            }
        }
        if (siColumns != null) {
            for (ColumnDef col : siColumns) {
                mapping.put(colIdx++, "SI_" + col.getId());
            }
        }
        return mapping;
    }

    /**
     * 将表头名称与系统中的 ColumnDef 进行匹配，返回 cellKey（如 "PA_1", "ST_3"）。
     * 匹配策略：精确匹配 > 包含匹配 > 去空格/标点后匹配
     */
    private static String matchColumnDef(String headerName,
                                          List<ColumnDef> paColumns, List<ColumnDef> stColumns,
                                          List<ColumnDef> ppColumns, List<ColumnDef> siColumns) {
        // 精确匹配
        for (ColumnDef col : paColumns) {
            if (col.getName().equals(headerName)) return "PA_" + col.getId();
        }
        for (ColumnDef col : stColumns) {
            if (col.getName().equals(headerName)) return "ST_" + col.getId();
        }
        for (ColumnDef col : ppColumns) {
            if (col.getName().equals(headerName)) return "PP_" + col.getId();
        }
        for (ColumnDef col : siColumns) {
            if (col.getName().equals(headerName)) return "SI_" + col.getId();
        }

        // 包含匹配（表头包含选项名 或 选项名包含表头）
        String normalized = normalizeText(headerName);
        for (ColumnDef col : paColumns) {
            if (fuzzyMatch(normalized, normalizeText(col.getName()))) return "PA_" + col.getId();
        }
        for (ColumnDef col : stColumns) {
            if (fuzzyMatch(normalized, normalizeText(col.getName()))) return "ST_" + col.getId();
        }
        for (ColumnDef col : ppColumns) {
            if (fuzzyMatch(normalized, normalizeText(col.getName()))) return "PP_" + col.getId();
        }
        for (ColumnDef col : siColumns) {
            if (fuzzyMatch(normalized, normalizeText(col.getName()))) return "SI_" + col.getId();
        }

        return null;
    }

    private static boolean fuzzyMatch(String a, String b) {
        if (a.isEmpty() || b.isEmpty()) return false;
        return a.contains(b) || b.contains(a);
    }

    private static String normalizeText(String text) {
        if (text == null) return "";
        return text.replaceAll("[\\s\\-_/\\\\()（）]", "").trim();
    }

    // ==================== 辅助方法 ====================

    private static int findColumnByName(Row row, Sheet sheet, int rowIdx, String name) {
        if (row == null) return -1;
        for (int c = 0; c < row.getLastCellNum(); c++) {
            String val = getCellStringValue(sheet, rowIdx, c).trim();
            if (name.equals(val)) return c;
        }
        return -1;
    }

    private static int findWordColumnByName(XWPFTableRow row, String name) {
        List<XWPFTableCell> cells = row.getTableCells();
        for (int c = 0; c < cells.size(); c++) {
            if (name.equals(cells.get(c).getText().trim())) return c;
        }
        return -1;
    }

    /**
     * 获取 Excel 单元格字符串值，处理合并单元格的情况。
     */
    private static String getCellStringValue(Sheet sheet, int rowIdx, int colIdx) {
        // 先检查是否在合并区域内
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            if (range.isInRange(rowIdx, colIdx)) {
                Row firstRow = sheet.getRow(range.getFirstRow());
                if (firstRow != null) {
                    Cell firstCell = firstRow.getCell(range.getFirstColumn());
                    if (firstCell != null) {
                        return cellToString(firstCell);
                    }
                }
                return "";
            }
        }

        Row row = sheet.getRow(rowIdx);
        if (row == null) return "";
        Cell cell = row.getCell(colIdx);
        if (cell == null) return "";
        return cellToString(cell);
    }

    private static String cellToString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                double d = cell.getNumericCellValue();
                if (d == Math.floor(d) && !Double.isInfinite(d)) {
                    yield String.valueOf((long) d);
                }
                yield String.valueOf(d);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield cell.getStringCellValue();
                } catch (Exception e) {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            default -> "";
        };
    }

    private static String getWordCellText(XWPFTableRow row, int colIdx) {
        if (row == null) return "";
        List<XWPFTableCell> cells = row.getTableCells();
        if (colIdx < 0 || colIdx >= cells.size()) return "";
        String t = cells.get(colIdx).getText();
        return (t != null ? t.trim() : "");
    }

    /** 取 Word 行中某逻辑列所在单元格的文本（考虑 gridSpan 合并列）. */
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

    /**
     * 根據物理列索引計算邏輯列索引（按 gridSpan 累加），
     * 便於在已知物理列位置時，使用 getWordCellTextAtLogicalColumn 在其他行上取對應文字。
     */
    private static int getLogicalColumnIndex(XWPFTableRow row, int physicalColIdx) {
        if (row == null || physicalColIdx < 0) return physicalColIdx;
        List<XWPFTableCell> cells = row.getTableCells();
        if (physicalColIdx >= cells.size()) return physicalColIdx;
        int logical = 0;
        for (int i = 0; i < physicalColIdx; i++) {
            logical += getWordCellGridSpan(cells.get(i));
        }
        return logical;
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

    private static boolean rowContainsWord(XWPFTableRow row, String word) {
        if (row == null || word == null || word.isEmpty()) return false;
        for (XWPFTableCell cell : row.getTableCells()) {
            String t = cell.getText();
            if (t != null && t.contains(word)) return true;
        }
        return false;
    }

    /**
     * 取分類行某逻辑列的有效值：按 gridSpan 計算邏輯列，若該格為空則向左取最近非空值。
     * colIdx 為列名行的列索引（與邏輯列對齊），可正確對應合併後的分組行。
     */
    private static String getCategoryAtColumn(XWPFTableRow categoryRow, int colIdx, int startCol) {
        if (categoryRow == null) return null;
        for (int c = colIdx; c >= startCol && c >= 0; c--) {
            String v = getWordCellTextAtLogicalColumn(categoryRow, c);
            if (v != null && !v.isEmpty()) return v;
        }
        return null;
    }

    /**
     * 若某些未匹配表頭被標記為 SI_NEW_，但其列索引位於第一個已知 SI_ 列之前，
     * 則視為「適用界面(前工序)」區下新增的前工序列，改為 PP_NEW_ 前綴，避免被誤當成新增適用界面列。
     */
    private static void adjustNewPreProcessColumns(Map<Integer, String> mapping) {
        if (mapping == null || mapping.isEmpty()) return;
        int firstSiCol = Integer.MAX_VALUE;
        for (Map.Entry<Integer, String> e : mapping.entrySet()) {
            String v = e.getValue();
            if (v != null && v.startsWith("SI_") && !v.startsWith("SI_NEW_")) {
                firstSiCol = Math.min(firstSiCol, e.getKey());
            }
        }
        if (firstSiCol == Integer.MAX_VALUE) return;
        for (Map.Entry<Integer, String> e : mapping.entrySet()) {
            int col = e.getKey();
            String v = e.getValue();
            if (v != null && v.startsWith("SI_NEW_") && col < firstSiCol) {
                String nameWithCat = v.substring("SI_NEW_".length());
                int idx = nameWithCat.indexOf("__CAT__");
                String name = idx >= 0 ? nameWithCat.substring(0, idx) : nameWithCat;
                mapping.put(col, "PP_NEW_" + name);
            }
        }
    }

    /**
     * 基於分組行判斷前工序列：
     * - 「适用界面(前工序)」下的都是前工序：含 (前工序)过胶面、(前工序)印刷/过油面、(前工序)测试 等工序類型及其下的工藝類型列（如 过胶面普通…、测试1、测试2）→ PP_NEW_
     * - 僅「适用界面」下（無前工序）的才是適用界面（SI），保持 SI_NEW_
     * __CAT__ 僅用於 SI 分類，轉為 PP 時丟棄。
     */
    private static void adjustNewPreProcessColumnsByGroup(Map<Integer, String> mapping,
                                                         XWPFTableRow categoryRow,
                                                         int compatStartCol) {
        if (mapping == null || mapping.isEmpty() || categoryRow == null) return;
        for (Map.Entry<Integer, String> e : new LinkedHashMap<>(mapping).entrySet()) {
            int col = e.getKey();
            if (col < compatStartCol) continue;
            String v = e.getValue();
            if (v == null || !v.startsWith("SI_NEW_")) continue;
            String group = getCategoryAtColumn(categoryRow, col, compatStartCol);
            if (group != null && (group.contains("前工序") || group.contains("(前工序)") || group.contains("（前工序）") || group.contains("適用界面(前工序)") || group.contains("适用界面(前工序)"))) {
                String nameWithCat = v.substring("SI_NEW_".length());
                String name = nameWithCat;
                int idx = nameWithCat.indexOf("__CAT__");
                if (idx >= 0) name = nameWithCat.substring(0, idx);
                mapping.put(col, "PP_NEW_" + name);
            }
        }
    }

    /**
     * 在 Word 文档中查找目标表格（包含"常用界面"或"燙印性"关键字的表格）
     */
    private static XWPFTable findTargetTable(XWPFDocument doc) {
        List<XWPFTable> tables = doc.getTables();
        if (tables.isEmpty()) return null;

        for (XWPFTable table : tables) {
            if (table.getRows().size() < 3) continue;
            for (int r = 0; r < Math.min(5, table.getRows().size()); r++) {
                XWPFTableRow row = table.getRow(r);
                for (XWPFTableCell cell : row.getTableCells()) {
                    String text = cell.getText();
                    if (text.contains("常用界面") || text.contains("燙印性") || text.contains("燙金圖案")) {
                        return table;
                    }
                }
            }
        }

        // 如果没找到关键字，返回最大的表格
        return tables.stream()
                .max(Comparator.comparingInt(t -> t.getRows().size()))
                .orElse(null);
    }

    /**
     * 判斷是否為當前系統導出的新 .docx 版式：
     * Row0: 含「金紙/金纸」「燙印性」,
     * Row1: 第一、二列為「類型/类型」「系列」,
     * 列數與 ColumnDef 總數基本一致。
     */
    private static boolean isNewDocxExport(XWPFTable table,
                                           int paCount, int stCount, int ppCount, int siCount) {
        if (table == null) return false;
        List<XWPFTableRow> rows = table.getRows();
        if (rows.size() < 2) return false;

        XWPFTableRow row0 = rows.get(0);
        List<XWPFTableCell> row0Cells = row0.getTableCells();
        if (row0Cells.size() < 2) return false;

        String first = Optional.ofNullable(row0Cells.get(0).getText()).orElse("");
        boolean hasJinZhi = first.contains("金纸") || first.contains("金紙");
        boolean hasTanYin = row0Cells.stream()
                .map(c -> Optional.ofNullable(c.getText()).orElse(""))
                .anyMatch(t -> t.contains("燙印性") || t.contains("烫印性"));
        if (!hasJinZhi || !hasTanYin) return false;

        if (rows.size() < 2) return false;
        XWPFTableRow row1 = rows.get(1);
        List<XWPFTableCell> row1Cells = row1.getTableCells();
        if (row1Cells.size() < 2) return false;

        String typeHeader = Optional.ofNullable(row1Cells.get(0).getText()).orElse("").trim();
        String seriesHeader = Optional.ofNullable(row1Cells.get(1).getText()).orElse("").trim();
        if (!"類型".equals(typeHeader) && !"类型".equals(typeHeader)) return false;
        if (!"系列".equals(seriesHeader)) return false;

        // 表頭首行常為合併格（金纸/烫金图案/烫金分类/烫印性），列數可能少於 expectedCols，不依賴 row0 列數判斷
        return true;
    }

    /**
     * 标准化兼容性状态值
     */
    private static String normalizeStatus(String value) {
        if (value == null || value.isEmpty()) return "";
        value = value.trim();

        if ("V".equalsIgnoreCase(value) || "✓".equals(value) || "√".equals(value) || "〇".equals(value)) {
            return "V";
        }
        if ("X".equalsIgnoreCase(value) || "✗".equals(value) || "×".equals(value) || "✕".equals(value)) {
            return "X";
        }
        if ("NA".equalsIgnoreCase(value) || "N/A".equalsIgnoreCase(value)) {
            return "NA";
        }
        if ("▷".equals(value) || "►".equals(value) || ">".equals(value)) {
            return "▷";
        }

        // 其余符号（如「－」「—」等）一律视为「无有效状态」，返回空串，后续导入逻辑会跳过该单元格，避免违反 DB check constraint
        return "";
    }
}
