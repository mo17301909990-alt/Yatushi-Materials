package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.LaminatingMatrixDTO;
import com.it.yts_project.dto.LaminatingMatrixDTO.FoilColumnDef;
import com.it.yts_project.dto.LaminatingMatrixDTO.MatrixRow;
import com.it.yts_project.mapper.PricingMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.model.LaminationCompatibility;
import com.it.yts_project.model.LaminationMaterialOption;
import com.it.yts_project.model.PostProcessingOption;
import com.it.yts_project.model.Pricing;
import com.it.yts_project.model.Product;
import com.it.yts_project.service.HotStampingCompatibilityService;
import com.it.yts_project.service.LaminatingMatrixService;
import com.it.yts_project.service.LaminatingService;
import com.it.yts_project.util.LaminatingMatrixExportUtil;
import com.it.yts_project.util.LaminatingMatrixImportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 燙金後過膠－應用限制 矩陣服務實現。
 * 行 = 過膠種類（前綴/後綴）+ 燙後過膠後加工（不按產品類型分行）；列 = 燙金紙選用（表頭第二行按產品類型分組）；單元格 = V/X（同一列多產品類型取「任一為 V 則 V」）。
 */
@Service
public class LaminatingMatrixServiceImpl implements LaminatingMatrixService {

    private static final Logger log = LoggerFactory.getLogger(LaminatingMatrixServiceImpl.class);

    @Autowired
    private LaminatingService laminatingService;
    @Autowired(required = false)
    private HotStampingCompatibilityService hotStampingCompatibilityService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PricingMapper pricingMapper;

    @Override
    public byte[] exportToExcel() throws Exception {
        LaminatingMatrixDTO matrix = buildMatrix();
        return LaminatingMatrixExportUtil.exportToExcel(matrix);
    }

    @Override
    public byte[] exportToWord() throws Exception {
        LaminatingMatrixDTO matrix = buildMatrix();
        return LaminatingMatrixExportUtil.exportToDocx(matrix);
    }

    @Override
    public Map<String, Object> importMatrix(MultipartFile file, String conflictStrategy) throws Exception {
        LaminatingMatrixDTO matrix = buildMatrix();
        String name = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        List<LaminatingMatrixImportUtil.ParsedRow> parsed;
        List<FoilColumnDef> columnDefsForImport = matrix.getColumnDefs();
        if (name.endsWith(".docx") || name.endsWith(".doc")) {
            LaminatingMatrixImportUtil.ParseResult pres = LaminatingMatrixImportUtil.parseWord(file.getInputStream(), matrix.getColumnDefs());
            parsed = pres.getRows();
            if (pres.getColumnDefs() != null && !pres.getColumnDefs().isEmpty()) {
                columnDefsForImport = pres.getColumnDefs();
            }
        } else if (name.endsWith(".csv")) {
            parsed = LaminatingMatrixImportUtil.parseCsv(file.getInputStream(), matrix.getColumnDefs());
        } else {
            parsed = LaminatingMatrixImportUtil.parseExcel(file.getInputStream(), matrix.getColumnDefs());
        }
        return applyImport(parsed, conflictStrategy, columnDefsForImport);
    }

    private LaminatingMatrixDTO buildMatrix() {
        List<LaminationCompatibility> all = laminatingService.getCompatibilityList(
                null, null, null, null, null, null, 1, Integer.MAX_VALUE);
        if (all == null) all = new ArrayList<>();

        // 行維度：不按產品類型分開，只按 (過膠材料, 燙後過膠後加工) 去重；產品類型僅在表頭「燙金紙選用」下列分組（普通金紙/鐳射燙金紙）
        LinkedHashMap<String, MatrixRow> rowMap = new LinkedHashMap<>();
        for (LaminationCompatibility c : all) {
            Integer matId = c.getLaminationMaterialId();
            String matName = c.getLaminationMaterialName() != null ? c.getLaminationMaterialName().trim() : "";
            Integer stepId = c.getPostProcessingStepId();
            String stepName = c.getPostProcessingStepName() != null ? c.getPostProcessingStepName().trim() : "";
            String rowKey = (matId != null ? matId : "") + "\t" + (stepId != null ? stepId : "");
            rowMap.putIfAbsent(rowKey, new MatrixRow());
            MatrixRow row = rowMap.get(rowKey);
            row.setLaminationMaterialId(matId);
            row.setLaminationMaterialName(matName);
            splitPrefixSuffix(matName, row);
            row.setPostProcessingStepId(stepId);
            row.setPostProcessingStepName(stepName);
        }

        // 列维度：按 (產品類型, 系列, 型號, 是否街貨) 去重，一列對應一種燙金紙性能類型下的一個型號，以便不同性能類型下同型號可有不同兼容性
        LinkedHashSet<String> colKeys = new LinkedHashSet<>();
        List<FoilColumnDef> columnDefs = new ArrayList<>();
        for (LaminationCompatibility c : all) {
            String pt = c.getProductType() != null ? c.getProductType().trim() : "";
            String series = c.getFoilSeries() != null ? c.getFoilSeries().trim() : "";
            String model = c.getModelNumber() != null ? c.getModelNumber().trim() : "";
            Boolean jiehuo = c.getIsJiehuo() != null && c.getIsJiehuo();
            String key = pt + "|" + series + "|" + model + "|" + (Boolean.TRUE.equals(jiehuo) ? "J" : "N");
            if (colKeys.add(key)) {
                FoilColumnDef col = new FoilColumnDef(series, model, jiehuo);
                if (!pt.isEmpty()) col.setFoilTypeName(pt);
                if (series != null && !series.isEmpty()) {
                    List<Product> products = (model != null && !model.isEmpty())
                            ? productMapper.findByModelNumberAndName(model, series)
                            : productMapper.findByName(series);
                    if (products != null && !products.isEmpty()) {
                        Integer productId = products.get(0).getId();
                        if (productId != null) {
                            List<Pricing> prices = pricingMapper.findByProjectId(productId);
                            if (prices != null && !prices.isEmpty()) {
                                BigDecimal minPrice = prices.stream()
                                        .map(Pricing::getPrice)
                                        .filter(Objects::nonNull)
                                        .min(BigDecimal::compareTo)
                                        .orElse(null);
                                col.setPrice(minPrice);
                            }
                        }
                    }
                }
                columnDefs.add(col);
            }
        }
        sortFoilColumns(columnDefs);

        // 單元格：每格對應 (過膠材料, 燙後加工, 產品類型+系列+型號)，不再合併多產品類型
        for (LaminationCompatibility c : all) {
            Integer matId = c.getLaminationMaterialId();
            Integer stepId = c.getPostProcessingStepId();
            String rowKey = (matId != null ? matId : "") + "\t" + (stepId != null ? stepId : "");
            MatrixRow row = rowMap.get(rowKey);
            if (row == null) continue;
            String pt = c.getProductType() != null ? c.getProductType().trim() : "";
            String series = c.getFoilSeries() != null ? c.getFoilSeries().trim() : "";
            String model = c.getModelNumber() != null ? c.getModelNumber().trim() : "";
            Boolean jiehuo = c.getIsJiehuo() != null && c.getIsJiehuo();
            String colKey = pt + "|" + series + "|" + model + "|" + (Boolean.TRUE.equals(jiehuo) ? "J" : "N");
            String status = c.getCompatibility() != null ? c.getCompatibility().trim() : "";
            if (status.isEmpty()) continue;
            row.getCells().put(colKey, status);
        }

        List<MatrixRow> rows = new ArrayList<>(rowMap.values());
        sortRows(rows);

        LaminatingMatrixDTO dto = new LaminatingMatrixDTO();
        dto.setColumnDefs(columnDefs);
        dto.setRows(rows);
        return dto;
    }

    /** 材料名称按「-」或「－」拆成前缀、后缀，供分列显示及合并 */
    private static void splitPrefixSuffix(String matName, MatrixRow row) {
        if (matName == null || matName.trim().isEmpty()) return;
        int dashIdx = matName.indexOf("-");
        if (dashIdx < 0) dashIdx = matName.indexOf("－");
        if (dashIdx >= 0) {
            row.setLaminationMaterialPrefix(matName.substring(0, dashIdx).trim());
            row.setLaminationMaterialSuffix(matName.substring(dashIdx + 1).trim());
        } else {
            row.setLaminationMaterialPrefix(matName.trim());
            row.setLaminationMaterialSuffix(matName.trim());
        }
    }

    /** 按產品類型分组，同类型内按价格从小到大排（系列从左到右价格升序） */
    private static void sortFoilColumns(List<FoilColumnDef> columnDefs) {
        // 价格升序：小的在前，无价格的列排最后
        Comparator<FoilColumnDef> byPriceAsc = Comparator.comparing(
                FoilColumnDef::getPrice,
                Comparator.nullsLast(BigDecimal::compareTo)
        );
        columnDefs.sort(Comparator
                .comparing(FoilColumnDef::getTypeGroupName, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(byPriceAsc)
                .thenComparing(FoilColumnDef::getFoilSeries, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(f -> Boolean.TRUE.equals(f.getIsJiehuo()) ? 1 : 0)
                .thenComparing(FoilColumnDef::getModelNumber, Comparator.nullsFirst(Comparator.naturalOrder())));
    }

    /** 按 前綴、後綴、燙後加工 排序，使相同(前綴,後綴)相鄰便於合併 */
    private static void sortRows(List<MatrixRow> rows) {
        rows.sort(Comparator
                .comparing(MatrixRow::getLaminationMaterialPrefix, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(MatrixRow::getLaminationMaterialSuffix, Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(MatrixRow::getPostProcessingStepName, Comparator.nullsFirst(Comparator.naturalOrder())));
    }

    private Map<String, Object> applyImport(List<LaminatingMatrixImportUtil.ParsedRow> parsed, String conflictStrategy,
                                           List<FoilColumnDef> columnDefs) {
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

        // 1）列 → 單一產品類型（當前矩陣表頭第二行）
        Map<String, String> colKeyToProductType = new HashMap<>();
        if (columnDefs != null) {
            for (FoilColumnDef col : columnDefs) {
                String pt = col.getTypeGroupName();
                if (pt != null && !pt.isEmpty()) {
                    colKeyToProductType.put(col.cellKey(), pt.trim());
                }
            }
        }

        // 2）列（舊格式 系列|型號|J/N）→ 該燙金紙在庫中出現過的所有產品類型，用於無列定義時兜底
        Map<String, Set<String>> seriesModelJToProductTypes = new HashMap<>();
        try {
            List<LaminationCompatibility> existingAll = laminatingService.getCompatibilityList(
                    null, null, null, null, null, null, 1, Integer.MAX_VALUE);
            if (existingAll != null) {
                for (LaminationCompatibility c : existingAll) {
                    String series = c.getFoilSeries() != null ? c.getFoilSeries().trim() : "";
                    String model = c.getModelNumber() != null ? c.getModelNumber().trim() : "";
                    boolean jiehuo = c.getIsJiehuo() != null && c.getIsJiehuo();
                    String key = series + "|" + model + "|" + (jiehuo ? "J" : "N");
                    String pt = c.getProductType() != null ? c.getProductType().trim() : "";
                    if (!series.isEmpty() && !pt.isEmpty()) {
                        seriesModelJToProductTypes
                                .computeIfAbsent(key, k -> new LinkedHashSet<>())
                                .add(pt);
                    }
                }
            }
        } catch (Exception e) {
            // 讀取全量兼容性失敗時，不影響導入流程
        }

        List<String> allPtsFromLaminating = laminatingService.getAllProductTypes();
        List<String> allPtsFromHotStamping = hotStampingCompatibilityService != null
                ? hotStampingCompatibilityService.getAllProductTypes()
                : Collections.emptyList();

        List<LaminationMaterialOption> materials = laminatingService.getActiveMaterialOptions();
        List<PostProcessingOption> steps = laminatingService.getActiveProcessingOptions();
        Map<String, Integer> materialNameToId = materials.stream()
                .filter(m -> m.getMaterialName() != null)
                .collect(Collectors.toMap(LaminationMaterialOption::getMaterialName, LaminationMaterialOption::getId, (a, b) -> a));
        Map<String, Integer> stepNameToId = steps.stream()
                .filter(s -> s.getStepName() != null)
                .collect(Collectors.toMap(PostProcessingOption::getStepName, PostProcessingOption::getId, (a, b) -> a));

        List<LaminationCompatibility> toSave = new ArrayList<>();
        boolean anyRowHasCells = false;
        boolean skippedByNoProductType = false;
        // 系列+型號 → 對應的燙金紙性能類型集合（來自 products.hotStampingPaperType），用於首導或無現有數據時決定 productType
        Map<String, Set<String>> seriesModelToPaperTypes = new HashMap<>();
        for (int i = 0; i < parsed.size(); i++) {
            LaminatingMatrixImportUtil.ParsedRow pr = parsed.get(i);
            if (!pr.getCells().isEmpty()) anyRowHasCells = true;
            String matName = pr.getLaminationMaterialName();
            Integer matId = materialNameToId.get(matName);
            if (matId == null) {
                String safeName = matName != null ? matName.trim() : "";
                // 1）明顯是表頭/空行的情況，當作無效行直接跳過
                if (safeName.isEmpty()
                        || "過膠種類(前綴)".equals(safeName)
                        || "過膠種類(後綴)".equals(safeName)
                        || "過膠種類".equals(safeName)
                        || "過膠材料".equals(safeName)) {
                    skipped++;
                    continue;
                }
                // 2）根據「後綴」回填：若 DB 中僅有一個材料名以 \"-後綴\" 結尾，則視為該材料
                List<LaminationMaterialOption> suffixMatches = materials.stream()
                        .filter(m -> m.getMaterialName() != null)
                        .filter(m -> {
                            String full = m.getMaterialName().trim();
                            return full.equals(safeName) || full.endsWith("-" + safeName) || full.endsWith("－" + safeName);
                        })
                        .toList();
                if (suffixMatches.size() == 1) {
                    matId = suffixMatches.get(0).getId();
                } else {
                    errorMessages.add("第" + (i + 2) + "行: 過膠種類「" + pr.getLaminationMaterialName() + "」不存在");
                    errors++;
                    continue;
                }
            }
            Integer stepId = null;
            if (pr.getPostProcessingStepName() != null && !pr.getPostProcessingStepName().isEmpty()) {
                stepId = stepNameToId.get(pr.getPostProcessingStepName());
                if (stepId == null) {
                    errorMessages.add("第" + (i + 2) + "行: 燙後過膠後加工「" + pr.getPostProcessingStepName() + "」不存在");
                    errors++;
                    continue;
                }
            }
            for (Map.Entry<String, String> e : pr.getCells().entrySet()) {
                String colKey = e.getKey();
                String status = e.getValue();
                if (status == null || status.isEmpty()) continue;
                if (!"V".equals(status) && !"X".equals(status)) continue;
                String foilSeries;
                String modelNumber;
                boolean isJiehuo;
                String columnProductType = null; // 新格式下列標題自帶產品類型（type|series|model|J/N）
                String[] parts = colKey.split("\\|", -1);
                if (parts.length >= 4) {
                    columnProductType = parts[0] != null ? parts[0].trim() : "";
                    foilSeries = parts[1];
                    modelNumber = parts[2];
                    isJiehuo = "J".equalsIgnoreCase(parts[3]);
                } else if (parts.length >= 3) {
                    foilSeries = parts[0];
                    modelNumber = parts[1];
                    isJiehuo = "J".equalsIgnoreCase(parts[2]);
                } else {
                    // 列標題來自「具體型號」行的顯示名（如 SY+、S19(街貨)），無 cellKey 格式
                    String label = colKey.trim();
                    isJiehuo = label.contains("(街貨)");
                    foilSeries = label.replace("(街貨)", "").trim();
                    modelNumber = null;
                }
                if (foilSeries == null || foilSeries.isEmpty()) continue;

                String seriesModelJKey = (foilSeries != null ? foilSeries : "") + "|" + (modelNumber != null ? modelNumber : "") + "|" + (isJiehuo ? "J" : "N");
                Set<String> targetProductTypes = new LinkedHashSet<>();
                // 新格式（type|series|model|J/N）：以文件表頭為準，只用該列對應的燙金紙性能類型，不從 products/現有數據覆蓋或追加
                if (columnProductType != null && !columnProductType.isEmpty()) {
                    // 校驗：該系列在 products 中是否真的存在該燙金紙性能類型，避免表內列放錯
                    String seriesKey = foilSeries != null ? foilSeries.trim() : "";
                    String modelKey = modelNumber != null ? modelNumber.trim() : "";
                    String smKey = seriesKey + "|" + modelKey;
                    Set<String> paperTypes = seriesModelToPaperTypes.get(smKey);
                    if (paperTypes == null) {
                        paperTypes = new LinkedHashSet<>();
                        List<Product> productsForSeries;
                        if (!modelKey.isEmpty()) {
                            productsForSeries = productMapper.findByModelNumberAndName(modelKey, seriesKey);
                        } else {
                            productsForSeries = productMapper.findByName(seriesKey);
                        }
                        if (productsForSeries != null) {
                            for (Product p : productsForSeries) {
                                String pt = p.getHotStampingPaperType();
                                if (pt != null && !pt.trim().isEmpty()) paperTypes.add(pt.trim());
                            }
                        }
                        seriesModelToPaperTypes.put(smKey, paperTypes);
                    }
                    if (paperTypes.isEmpty()) {
                        errorMessages.add(String.format("系列「%s」在產品中不存在，請先維護產品資料後再導入；或檢查表內該列是否寫錯。", seriesKey + (modelKey.isEmpty() ? "" : " " + modelKey)));
                        errors++;
                        continue;
                    }
                    if (!paperTypes.contains(columnProductType)) {
                        errorMessages.add(String.format("系列「%s」在產品中不存在「%s」燙金紙性能類型，請檢查表內該列是否寫錯位置。", seriesKey + (modelKey.isEmpty() ? "" : " " + modelKey), columnProductType));
                        errors++;
                        continue;
                    }
                    targetProductTypes.add(columnProductType);
                } else {
                    Set<String> fromExisting = seriesModelJToProductTypes.get(seriesModelJKey);
                    if (fromExisting != null) targetProductTypes.addAll(fromExisting);
                    String colPt = colKeyToProductType.get(colKey);
                    if (colPt != null && !colPt.isEmpty()) targetProductTypes.add(colPt);
                    String rowPt = pr.getProductType() != null ? pr.getProductType().trim() : null;
                    if (rowPt != null && !rowPt.isEmpty()) targetProductTypes.add(rowPt);
                    if (targetProductTypes.isEmpty()) {
                        String seriesKey = foilSeries != null ? foilSeries.trim() : "";
                        String modelKey = modelNumber != null ? modelNumber.trim() : "";
                        String smKey = seriesKey + "|" + modelKey;
                        Set<String> paperTypes = seriesModelToPaperTypes.get(smKey);
                        if (paperTypes == null) {
                            paperTypes = new LinkedHashSet<>();
                            List<Product> productsForSeries;
                            if (!modelKey.isEmpty()) {
                                productsForSeries = productMapper.findByModelNumberAndName(modelKey, seriesKey);
                            } else {
                                productsForSeries = productMapper.findByName(seriesKey);
                            }
                            if (productsForSeries != null) {
                                for (Product p : productsForSeries) {
                                    String pt = p.getHotStampingPaperType();
                                    if (pt != null && !pt.trim().isEmpty()) paperTypes.add(pt.trim());
                                }
                            }
                            seriesModelToPaperTypes.put(smKey, paperTypes);
                        }
                        if (paperTypes != null && !paperTypes.isEmpty()) targetProductTypes.addAll(paperTypes);
                    }
                    if (targetProductTypes.isEmpty() && allPtsFromLaminating != null) targetProductTypes.addAll(allPtsFromLaminating);
                    if (targetProductTypes.isEmpty() && allPtsFromHotStamping != null) targetProductTypes.addAll(allPtsFromHotStamping);
                }
                if (targetProductTypes.isEmpty()) {
                    skippedByNoProductType = true;
                    if (toSave.size() < 3) {
                        log.info("[LaminatingImport] skip cell: colKey='{}', targetProductTypes empty (fromExisting/colPt/rowPt/getAllProductTypes all empty)", colKey);
                    }
                    continue;
                }

                for (String productType : targetProductTypes) {
                    LaminationCompatibility comp = new LaminationCompatibility();
                    comp.setFoilSeries(foilSeries);
                    comp.setModelNumber(modelNumber == null || modelNumber.isEmpty() ? null : modelNumber);
                    comp.setProductType(productType);
                    comp.setLaminationMaterialId(matId);
                    comp.setPostProcessingStepId(stepId);
                    comp.setCompatibility(status);
                    comp.setIsJiehuo(isJiehuo);
                    comp.setInterfaceTypeId(0);
                    toSave.add(comp);
                }
            }
        }

        if (toSave.isEmpty() && !parsed.isEmpty() && !anyRowHasCells) {
            errorMessages.add("無法從文件中讀取燙金紙相容性數據（未識別到列標題），請使用系統導出的 Word 模板重新導入");
            errors++;
        }
        if (toSave.isEmpty() && anyRowHasCells && skippedByNoProductType) {
            errorMessages.add("無法寫入：系統中尚無產品類型（過膠相容性與燙金相容性表皆無 product_type），請先於任一相容性中配置至少一筆數據後再導入");
            errors++;
        }

        boolean overwrite = "overwrite".equalsIgnoreCase(conflictStrategy);
        if (overwrite) {
            // 覆蓋模式：批量 upsert，走 lamination_compatibility_unique 五列唯一索引
            try {
                laminatingService.batchSaveCompatibilityFast(toSave);
                updated = toSave.size();
                created = 0;
            } catch (Exception ex) {
                errors++;
                errorMessages.add("批量导入失败: " + ex.getMessage());
            }
        } else {
            // skip 模式：逐條查找，同鍵則跳過否則新建
            for (LaminationCompatibility comp : toSave) {
                try {
                    LaminationCompatibility existing = laminatingService.findByUniqueKeyNew(
                            comp.getFoilSeries(), comp.getModelNumber(), comp.getProductType(),
                            comp.getLaminationMaterialId(), comp.getPostProcessingStepId());
                    if (existing != null) {
                        skipped++;
                    } else {
                        laminatingService.saveCompatibility(comp);
                        created++;
                    }
                } catch (Exception ex) {
                    errors++;
                    errorMessages.add("保存失败: " + comp.getFoilSeries() + "/" + comp.getModelNumber() + " - " + ex.getMessage());
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
}
