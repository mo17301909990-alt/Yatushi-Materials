package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.LeduvglitterMatrixDTO;
import com.it.yts_project.dto.LeduvglitterMatrixDTO.MatrixRow;
import com.it.yts_project.dto.LeduvglitterMatrixDTO.SeriesColumnDef;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.model.PostProcessingLeduvglitter;
import com.it.yts_project.service.LeduvglitterMatrixService;
import com.it.yts_project.service.PostProcessingLeduvglitterService;
import com.it.yts_project.util.LeduvglitterMatrixExportUtil;
import com.it.yts_project.util.LeduvglitterMatrixImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 燙金+絲印LED UV灑閃粉 配对烫金纸型号矩阵服务实现。
 * 行 = 烫金界面（布面纸类型）；列 = paperType + 系列名；单元格 = V/X。
 */
@Service
public class LeduvglitterMatrixServiceImpl implements LeduvglitterMatrixService {

    @Autowired
    private PostProcessingLeduvglitterService leduvglitterService;

    @Autowired(required = false)
    private ProductsMapper productsMapper;

    /** 烫金纸性能类型导出排序：普通金紙 → 普通耐磨 → 高耐磨 → 镭射；同类型内按系列名排序 */
    private static final List<String> PAPER_TYPE_ORDER = Arrays.asList(
        "普通金紙", "普通烫金纸", "普通燙金纸",
        "普通耐磨", "耐磨烫金纸", "耐磨燙金纸",
        "高耐磨",
        "镭射烫金纸", "鐳射燙金纸", "镭射"
    );

    @Override
    public byte[] exportToExcel() throws Exception {
        LeduvglitterMatrixDTO matrix = buildMatrix();
        return LeduvglitterMatrixExportUtil.exportToExcel(matrix);
    }

    @Override
    public byte[] exportToWord() throws Exception {
        LeduvglitterMatrixDTO matrix = buildMatrix();
        return LeduvglitterMatrixExportUtil.exportToDocx(matrix);
    }

    @Override
    public Map<String, Object> importMatrix(MultipartFile file, String conflictStrategy) throws Exception {
        LeduvglitterMatrixDTO matrix = buildMatrix();
        String name = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        List<LeduvglitterMatrixImportUtil.ParsedRow> parsed;
        if (name.endsWith(".docx") || name.endsWith(".doc")) {
            parsed = LeduvglitterMatrixImportUtil.parseWord(file.getInputStream(), matrix.getColumnDefs());
        } else if (name.endsWith(".csv")) {
            parsed = LeduvglitterMatrixImportUtil.parseCsv(file.getInputStream(), matrix.getColumnDefs());
        } else {
            parsed = LeduvglitterMatrixImportUtil.parseExcel(file.getInputStream(), matrix.getColumnDefs());
        }
        return applyImport(parsed, conflictStrategy);
    }

    private LeduvglitterMatrixDTO buildMatrix() {
        List<PostProcessingLeduvglitter> all = leduvglitterService.getAllLeduvglitterConfigs();
        if (all == null) all = new ArrayList<>();

        // 行维度：按 (clothPaperTypeId, clothPaperTypeName) 去重
        LinkedHashMap<String, MatrixRow> rowMap = new LinkedHashMap<>();
        for (PostProcessingLeduvglitter c : all) {
            Integer typeId = c.getClothPaperTypeId();
            String typeName = c.getClothPaperTypeName() != null ? c.getClothPaperTypeName().trim() : "";
            String rowKey = (typeId != null ? typeId : "") + "\t" + typeName;
            rowMap.putIfAbsent(rowKey, new MatrixRow());
            MatrixRow row = rowMap.get(rowKey);
            row.setHotStampingInterface(typeName);
            row.setClothPaperTypeId(typeId);
        }

        // 列维度：按 (paperType, productName) 去重，并按 paperType 分组排序
        LinkedHashSet<String> colKeys = new LinkedHashSet<>();
        List<SeriesColumnDef> columnDefs = new ArrayList<>();
        for (PostProcessingLeduvglitter c : all) {
            String paperType = c.getPaperType() != null ? c.getPaperType().trim() : "";
            String series = c.getProductName() != null ? c.getProductName().trim() : "";
            SeriesColumnDef col = new SeriesColumnDef(paperType, series);
            String key = col.cellKey();
            if (colKeys.add(key) && !series.isEmpty()) {
                columnDefs.add(col);
            }
        }
        sortSeriesColumns(columnDefs);

        // 单元格：从 all 填充 row.cells（取每条记录的 productName+paperType+clothPaperTypeId 唯一确定一格）
        for (PostProcessingLeduvglitter c : all) {
            Integer typeId = c.getClothPaperTypeId();
            String typeName = c.getClothPaperTypeName() != null ? c.getClothPaperTypeName().trim() : "";
            String rowKey = (typeId != null ? typeId : "") + "\t" + typeName;
            MatrixRow row = rowMap.get(rowKey);
            if (row == null) continue;
            String paperType = c.getPaperType() != null ? c.getPaperType().trim() : "";
            String series = c.getProductName() != null ? c.getProductName().trim() : "";
            String colKey = (paperType + "|" + series);
            String status = normalizeStatus(c.getCompatibilityStatus());
            if (!status.isEmpty()) row.getCells().put(colKey, status);
        }

        List<MatrixRow> rows = new ArrayList<>(rowMap.values());
        rows.sort(Comparator
            .comparing(MatrixRow::getHotStampingInterface, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(MatrixRow::getClothPaperTypeId, Comparator.nullsFirst(Comparator.naturalOrder())));

        LeduvglitterMatrixDTO dto = new LeduvglitterMatrixDTO();
        dto.setColumnDefs(columnDefs);
        dto.setRows(rows);
        return dto;
    }

    private static String normalizeStatus(String s) {
        if (s == null || s.trim().isEmpty()) return "";
        s = s.trim().toUpperCase();
        if ("V".equals(s) || "√".equals(s)) return "V";
        if ("X".equals(s) || "×".equals(s)) return "X";
        return s;
    }

    private static void sortSeriesColumns(List<SeriesColumnDef> columnDefs) {
        columnDefs.sort((a, b) -> {
            int orderA = PAPER_TYPE_ORDER.indexOf(a.getPaperType());
            int orderB = PAPER_TYPE_ORDER.indexOf(b.getPaperType());
            if (orderA < 0) orderA = 999;
            if (orderB < 0) orderB = 999;
            int c = Integer.compare(orderA, orderB);
            if (c != 0) return c;
            return Comparator.nullsFirst(String::compareTo).compare(a.getSeriesName(), b.getSeriesName());
        });
    }

    private Map<String, Object> applyImport(List<LeduvglitterMatrixImportUtil.ParsedRow> parsed, String conflictStrategy) {
        Map<String, Object> result = new HashMap<>();
        result.put("totalRows", parsed != null ? parsed.size() : 0);
        int created = 0, updated = 0, skipped = 0, errors = 0;
        List<String> errorMessages = new ArrayList<>();

        if (parsed == null || parsed.isEmpty()) {
            result.put("created", 0);
            result.put("updated", 0);
            result.put("skipped", 0);
            result.put("errors", 0);
            result.put("errorMessages", errorMessages);
            return result;
        }

        List<com.it.yts_project.dto.ClothPaperTypeDTO> allTypes = getClothPaperTypesForImport();
        Map<String, Integer> interfaceNameToId = new HashMap<>();
        for (com.it.yts_project.dto.ClothPaperTypeDTO t : allTypes) {
            String name = (t.getCategory() != null ? t.getCategory() : "") + "." + (t.getTypeName() != null ? t.getTypeName() : "");
            if (!name.equals(".")) interfaceNameToId.put(name.trim(), t.getId());
            if (t.getTypeName() != null && !t.getTypeName().isEmpty())
                interfaceNameToId.put(t.getTypeName().trim(), t.getId());
        }

        boolean overwrite = "overwrite".equalsIgnoreCase(conflictStrategy);
        for (int i = 0; i < parsed.size(); i++) {
            LeduvglitterMatrixImportUtil.ParsedRow pr = parsed.get(i);
            String interfaceName = pr.getHotStampingInterface();
            if (interfaceName == null || interfaceName.trim().isEmpty()) {
                errorMessages.add("第" + (i + 2) + "行: 燙金界面為空");
                errors++;
                continue;
            }
            Integer clothPaperTypeId = interfaceNameToId.get(interfaceName.trim());
            if (clothPaperTypeId == null) {
                errorMessages.add("第" + (i + 2) + "行: 燙金界面「" + interfaceName + "」不存在");
                errors++;
                continue;
            }
            for (Map.Entry<String, String> e : pr.getCells().entrySet()) {
                String colKey = e.getKey();
                String status = e.getValue();
                if (status == null || status.isEmpty()) continue;
                if (!"V".equals(status) && !"X".equals(status)) continue;
                String[] parts = colKey.split("\\|", -1);
                if (parts.length < 2) continue;
                String paperType = parts[0];
                String seriesName = parts[1];
                if (seriesName.isEmpty()) continue;

                // 校驗 products 表：該系列是否支持當前燙金紙性能類型
                if (productsMapper != null && paperType != null && !paperType.isEmpty()) {
                    boolean supports = productsMapper.checkSeriesSupportsPaperType(seriesName, paperType);
                    if (!supports) {
                        errors++;
                        errorMessages.add("燙金界面「" + interfaceName + "」下，燙金紙系列「" + seriesName
                                + "」不支持燙金紙性能類型「" + paperType + "」（products 表中不存在對應組合）");
                        continue;
                    }
                }

                PostProcessingLeduvglitter toSave = new PostProcessingLeduvglitter();
                toSave.setProductName(seriesName);
                toSave.setProductModelNumber(null);
                toSave.setClothPaperTypeId(clothPaperTypeId);
                toSave.setPaperType(paperType.isEmpty() ? null : paperType);
                toSave.setCompatibilityStatus(status);
                try {
                    PostProcessingLeduvglitter existing = leduvglitterService.findByUniqueKey(
                        seriesName, null, clothPaperTypeId, paperType.isEmpty() ? null : paperType);
                    if (existing != null) {
                        if (overwrite) {
                            toSave.setId(existing.getId());
                            leduvglitterService.updateLeduvglitterConfig(toSave);
                            updated++;
                        } else {
                            skipped++;
                        }
                    } else {
                        leduvglitterService.createLeduvglitterConfig(toSave);
                        created++;
                    }
                } catch (Exception ex) {
                    errors++;
                    errorMessages.add("保存失败: " + seriesName + "/" + interfaceName + " - " + ex.getMessage());
                }
            }
        }

        result.put("created", created);
        result.put("updated", updated);
        result.put("skipped", skipped);
        result.put("errors", errors);
        result.put("errorMessages", errorMessages);
        return result;
    }

    @Autowired(required = false)
    private com.it.yts_project.service.ClothPaperTypeService clothPaperTypeService;

    private List<com.it.yts_project.dto.ClothPaperTypeDTO> getClothPaperTypesForImport() {
        if (clothPaperTypeService == null) return Collections.emptyList();
        try {
            return clothPaperTypeService.getAllClothPaperTypes();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
