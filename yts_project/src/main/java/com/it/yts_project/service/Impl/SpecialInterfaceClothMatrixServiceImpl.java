package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.ClothPaperCompatibilityDTO;
import com.it.yts_project.dto.ClothPaperTypeDTO;
import com.it.yts_project.dto.SpecialInterfaceClothMatrixDTO;
import com.it.yts_project.dto.SpecialInterfaceClothMatrixDTO.MatrixRow;
import com.it.yts_project.dto.SpecialInterfaceClothMatrixDTO.SeriesColumnDef;
import com.it.yts_project.service.ClothPaperCompatibilityService;
import com.it.yts_project.service.ClothPaperTypeService;
import com.it.yts_project.service.SpecialInterfaceClothMatrixService;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.util.SpecialInterfaceClothMatrixExportUtil;
import com.it.yts_project.util.SpecialInterfaceClothMatrixImportUtil;
import com.it.yts_project.util.SpecialInterfaceClothMatrixImportUtil.ParseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 「布面紙+燙金」－組合應用表 矩阵服务实现。
 * 行 = 勾选「特殊界面布面纸」的布面纸类型（材质+物料型号），
 * 列 = (烫金纸类型 paperType + 燙金紙系列 productName) 组合，
 * 单元格 = V/X/▷。
 */
@Service
public class SpecialInterfaceClothMatrixServiceImpl implements SpecialInterfaceClothMatrixService {

    private static final List<String> STATUS_PRIORITY = Arrays.asList("V", "▷", "X", "NA");

    @Autowired
    private ClothPaperTypeService clothPaperTypeService;
    @Autowired
    private ClothPaperCompatibilityService clothPaperCompatibilityService;
    @Autowired
    private ProductsMapper productsMapper;

    @Override
    public byte[] exportToExcel() throws Exception {
        SpecialInterfaceClothMatrixDTO matrix = buildMatrix();
        return SpecialInterfaceClothMatrixExportUtil.exportToExcel(matrix);
    }

    @Override
    public byte[] exportToWord() throws Exception {
        SpecialInterfaceClothMatrixDTO matrix = buildMatrix();
        return SpecialInterfaceClothMatrixExportUtil.exportToDocx(matrix);
    }

    @Override
    public Map<String, Object> importMatrix(MultipartFile file, String conflictStrategy) throws Exception {
        SpecialInterfaceClothMatrixDTO matrix = buildMatrix();
        String name = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        List<String> seriesNames = matrix.getColumnDefs().stream()
                .map(SeriesColumnDef::getSeriesName).collect(Collectors.toList());
        Set<String> validGroupNames = new HashSet<>();
        List<String> distinctPaperTypes = productsMapper.getAllDistinctPaperTypes();
        if (distinctPaperTypes != null) {
            distinctPaperTypes.stream().filter(pt -> pt != null && !pt.trim().isEmpty())
                    .forEach(pt -> validGroupNames.add(pt.trim()));
        }
        ParseResult parseResult;
        if (name.endsWith(".docx") || name.endsWith(".doc")) {
            parseResult = SpecialInterfaceClothMatrixImportUtil.parseWord(file.getInputStream(), seriesNames, validGroupNames);
        } else if (name.endsWith(".csv")) {
            parseResult = SpecialInterfaceClothMatrixImportUtil.parseCsv(file.getInputStream(), seriesNames);
        } else {
            parseResult = SpecialInterfaceClothMatrixImportUtil.parseExcel(file.getInputStream(), seriesNames, validGroupNames);
        }
        return applyImport(parseResult, matrix, conflictStrategy);
    }

    private SpecialInterfaceClothMatrixDTO buildMatrix() {
        List<ClothPaperTypeDTO> types = clothPaperTypeService.getActiveSpecialInterfaceClothPaperTypes();
        if (types == null) types = new ArrayList<>();

        List<ClothPaperCompatibilityDTO> allCompat = clothPaperCompatibilityService.getAllCompatibility();
        if (allCompat == null) allCompat = new ArrayList<>();

        Set<Integer> typeIdSet = types.stream()
                .map(ClothPaperTypeDTO::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        List<ClothPaperCompatibilityDTO> compatForTypes = allCompat.stream()
                .filter(c -> c.getClothPaperTypeId() != null && typeIdSet.contains(c.getClothPaperTypeId()))
                .collect(Collectors.toList());

        // 收集所有 (paperType, productName) 组合作为列定义
        LinkedHashSet<String> seenColumnKeys = new LinkedHashSet<>();
        List<SeriesColumnDef> columnDefs = new ArrayList<>();
        for (ClothPaperCompatibilityDTO c : compatForTypes) {
            String foilType = c.getPaperType() != null ? c.getPaperType().trim() : "";
            String productName = c.getProductName() != null ? c.getProductName().trim() : "";
            if (productName.isEmpty()) continue;
            String key = foilType + "\t" + productName;
            if (seenColumnKeys.add(key)) {
                columnDefs.add(new SeriesColumnDef(foilType, productName));
            }
        }
        // 按 foilType 分组排序，同组内按 productName 排序
        columnDefs.sort(Comparator
                .comparing(SeriesColumnDef::getFoilType, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(SeriesColumnDef::getSeriesName, Comparator.nullsFirst(Comparator.naturalOrder())));

        // 构建 cell 数据：key = SeriesColumnDef.cellKey()
        Map<String, String> typeProductToStatus = new HashMap<>();
        for (ClothPaperCompatibilityDTO c : compatForTypes) {
            Integer typeId = c.getClothPaperTypeId();
            String foilType = c.getPaperType() != null ? c.getPaperType().trim() : "";
            String product = c.getProductName() != null ? c.getProductName().trim() : "";
            String status = c.getCompatibilityStatus() != null ? c.getCompatibilityStatus().trim() : "";
            if (typeId == null || product.isEmpty() || status.isEmpty()) continue;
            String cellKey = foilType + "\t" + product;
            String mapKey = typeId + "\t" + cellKey;
            String existing = typeProductToStatus.get(mapKey);
            if (existing == null || compareStatusPriority(status, existing) < 0)
                typeProductToStatus.put(mapKey, status);
        }

        List<MatrixRow> rows = new ArrayList<>();
        for (ClothPaperTypeDTO t : types) {
            Integer id = t.getId();
            if (id == null) continue;
            MatrixRow row = new MatrixRow();
            row.setClothPaperTypeId(id);
            String category = t.getCategory() != null ? t.getCategory().trim() : "";
            String typeName = t.getTypeName() != null ? t.getTypeName().trim() : "";
            row.setMaterialCategory(category.isEmpty() ? typeName : category);
            row.setMaterialModel(typeName);
            for (SeriesColumnDef col : columnDefs) {
                String cellKey = col.cellKey();
                String status = typeProductToStatus.get(id + "\t" + cellKey);
                if (status != null) row.getCells().put(cellKey, status);
            }
            rows.add(row);
        }

        SpecialInterfaceClothMatrixDTO dto = new SpecialInterfaceClothMatrixDTO();
        dto.setColumnDefs(columnDefs);
        dto.setRows(rows);
        return dto;
    }

    private static int compareStatusPriority(String status, String existing) {
        int a = STATUS_PRIORITY.indexOf(status);
        int b = STATUS_PRIORITY.indexOf(existing);
        if (a < 0) a = STATUS_PRIORITY.size();
        if (b < 0) b = STATUS_PRIORITY.size();
        return a - b;
    }

    private Map<String, Object> applyImport(ParseResult parseResult,
                                            SpecialInterfaceClothMatrixDTO matrix, String conflictStrategy) {
        Map<String, Object> result = new HashMap<>();
        List<MatrixRow> parsed = parseResult != null ? parseResult.getRows() : null;
        Map<String, String> seriesNameToFoilTypeFromFile = parseResult != null && parseResult.getSeriesNameToFoilTypeFromFile() != null
                ? parseResult.getSeriesNameToFoilTypeFromFile() : Collections.emptyMap();
        int totalRows = parsed != null ? parsed.size() : 0;
        result.put("totalRows", totalRows);
        if (parsed == null || parsed.isEmpty()) {
            result.put("created", 0);
            result.put("updated", 0);
            result.put("skipped", 0);
            result.put("errors", 0);
            result.put("message", "No data rows to apply.");
            return result;
        }

        boolean overwrite = "overwrite".equalsIgnoreCase(conflictStrategy);
        int created = 0, updated = 0, skipped = 0, errors = 0;
        List<String> errorMessages = new ArrayList<>();

        // 收集文件中出现的所有系列名
        Set<String> seriesInFile = new HashSet<>();
        for (MatrixRow row : parsed) {
            if (row.getCells() != null) {
                for (String seriesName : row.getCells().keySet()) {
                    if (seriesName != null && !seriesName.trim().isEmpty()) {
                        seriesInFile.add(seriesName.trim());
                    }
                }
            }
        }
        // 校验 products 表：系列必须存在；文件中烫金纸性能类型须与 products 完全一致（不做繁简/别名映射，不一致则提示并跳过该列）
        Set<String> invalidSeries = new HashSet<>();
        Set<String> seriesWithPaperTypeMismatch = new HashSet<>();
        Map<String, String> seriesNameToResolvedPaperType = new HashMap<>();
        for (String seriesName : seriesInFile) {
            List<com.it.yts_project.model.Products> productsList = productsMapper.getProductsBySeriesName(seriesName);
            if (productsList == null || productsList.isEmpty()) {
                invalidSeries.add(seriesName);
                if (errorMessages.size() < 200) {
                    errorMessages.add("系列「" + seriesName + "」在 products 表中不存在，已跳过该列。");
                }
                continue;
            }
            List<String> productPaperTypes = productsMapper.getPaperTypesBySeriesName(seriesName);
            if (productPaperTypes == null) productPaperTypes = new ArrayList<>();
            if (productPaperTypes.isEmpty()) {
                productPaperTypes = productsMapper.getPaperTypesBySeriesNameFallback(seriesName);
            }
            if (productPaperTypes == null) productPaperTypes = new ArrayList<>();
            List<String> trimmedProductPaperTypes = productPaperTypes.stream()
                    .filter(pt -> pt != null && !pt.trim().isEmpty())
                    .map(String::trim)
                    .collect(Collectors.toList());

            String fileFoilType = seriesNameToFoilTypeFromFile.get(seriesName);
            if (fileFoilType != null) fileFoilType = fileFoilType.trim();
            if (fileFoilType != null && !fileFoilType.isEmpty()) {
                if (trimmedProductPaperTypes.contains(fileFoilType)) {
                    seriesNameToResolvedPaperType.put(seriesName, fileFoilType);
                } else {
                    seriesWithPaperTypeMismatch.add(seriesName);
                    if (errorMessages.size() < 200) {
                        String sysTypes = trimmedProductPaperTypes.isEmpty() ? "（无）" : String.join("、", trimmedProductPaperTypes);
                        errorMessages.add("系列「" + seriesName + "」的烫金纸性能类型与 products 表不一致：文件中为「" + fileFoilType + "」，须与系统完全一致（如 " + sysTypes + "），已跳过该列。");
                    }
                }
            } else {
                if (!trimmedProductPaperTypes.isEmpty()) {
                    seriesNameToResolvedPaperType.put(seriesName, trimmedProductPaperTypes.get(0));
                }
            }
        }

        // 1) 预加载「特殊界面布面纸」类型，并按类型名建立索引（物料型號 = typeName）
        List<ClothPaperTypeDTO> specialTypes = clothPaperTypeService.getActiveSpecialInterfaceClothPaperTypes();
        if (specialTypes == null) specialTypes = new ArrayList<>();
        Map<String, ClothPaperTypeDTO> typeByName = new HashMap<>();
        Set<Integer> specialTypeIds = new HashSet<>();
        for (ClothPaperTypeDTO t : specialTypes) {
            if (t.getId() == null) continue;
            String typeName = t.getTypeName() != null ? t.getTypeName().trim() : "";
            if (!typeName.isEmpty()) {
                typeByName.put(typeName, t);
            }
            specialTypeIds.add(t.getId());
        }

        // 2) 预加载所有兼容性记录
        List<ClothPaperCompatibilityDTO> allCompat = clothPaperCompatibilityService.getAllCompatibility();
        if (allCompat == null) allCompat = new ArrayList<>();
        Map<String, ClothPaperCompatibilityDTO> existedByTypeAndSeries = new HashMap<>();
        for (ClothPaperCompatibilityDTO c : allCompat) {
            Integer typeId = c.getClothPaperTypeId();
            String seriesName = c.getProductName() != null ? c.getProductName().trim() : "";
            if (typeId != null && specialTypeIds.contains(typeId) && !seriesName.isEmpty()) {
                String key = typeId + "\t" + seriesName;
                existedByTypeAndSeries.putIfAbsent(key, c);
            }
        }

        for (MatrixRow row : parsed) {
            String materialModel = row.getMaterialModel() != null ? row.getMaterialModel().trim() : "";
            String materialCategory = row.getMaterialCategory() != null ? row.getMaterialCategory().trim() : "";
            ClothPaperTypeDTO type = null;
            if (!materialModel.isEmpty()) {
                type = typeByName.get(materialModel);
            }
            if (type == null && !materialCategory.isEmpty()) {
                type = typeByName.get(materialCategory);
            }
            if (type == null || type.getId() == null) {
                errors++;
                if (errorMessages.size() < 200) {
                    errorMessages.add("行「" + materialCategory + " / " + materialModel + "」：找不到對應的布面紙類型（請確認物料型號是否與系統一致）。");
                }
                continue;
            }
            Integer typeId = type.getId();

            Map<String, String> cells = row.getCells() != null ? row.getCells() : Collections.emptyMap();
            for (Map.Entry<String, String> e : cells.entrySet()) {
                String seriesName = e.getKey() != null ? e.getKey().trim() : "";
                String status = e.getValue() != null ? e.getValue().trim() : "";
                if (seriesName.isEmpty() || status.isEmpty()) continue;
                if (invalidSeries.contains(seriesName) || seriesWithPaperTypeMismatch.contains(seriesName)) continue;

                String key = typeId + "\t" + seriesName;
                ClothPaperCompatibilityDTO existing = existedByTypeAndSeries.get(key);
                try {
                    if (existing != null) {
                        if (overwrite) {
                            existing.setCompatibilityStatus(status);
                            clothPaperCompatibilityService.update(existing);
                            updated++;
                        } else {
                            skipped++;
                        }
                    } else {
                        ClothPaperCompatibilityDTO neu = new ClothPaperCompatibilityDTO();
                        neu.setClothPaperTypeId(typeId);
                        neu.setProductName(seriesName);
                        neu.setCompatibilityStatus(status);
                        String paperType = seriesNameToResolvedPaperType.get(seriesName);
                        if (paperType != null && !paperType.isEmpty()) {
                            neu.setPaperType(paperType);
                        }
                        clothPaperCompatibilityService.create(neu);
                        created++;
                        existedByTypeAndSeries.put(key, neu);
                    }
                } catch (Exception ex) {
                    errors++;
                    if (errorMessages.size() < 200) {
                        String msg = ex.getMessage();
                        if (msg != null && msg.length() > 120) {
                            msg = msg.substring(0, 117) + "...";
                        }
                        errorMessages.add("行「" + materialCategory + " / " + materialModel + "」、系列「" + seriesName + "」匯入失敗：" +
                                (msg != null ? msg : ex.getClass().getSimpleName()));
                    }
                }
            }
        }

        result.put("created", created);
        result.put("updated", updated);
        result.put("skipped", skipped);
        result.put("errors", errors);
        if (!errorMessages.isEmpty()) {
            result.put("errorMessages", errorMessages);
        }
        result.put("message", errors > 0
                ? "Import completed with " + errors + " error(s)."
                : "Import completed. Created: " + created + ", Updated: " + updated + ", Skipped: " + skipped);
        return result;
    }
}
