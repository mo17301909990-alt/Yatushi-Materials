package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.ClothPaperCompatibilityDTO;
import com.it.yts_project.dto.ClothPaperTypeDTO;
import com.it.yts_project.dto.CommonInterfaceMatrixDTO;
import com.it.yts_project.dto.CommonInterfaceMatrixDTO.ColumnDef;
import com.it.yts_project.dto.CommonInterfaceMatrixDTO.MatrixRow;
import com.it.yts_project.dto.HotStampingPatternAreaOptionDTO;
import com.it.yts_project.mapper.HotStampingPatternAreaCompatibilityMapper;
import com.it.yts_project.mapper.HotStampingTypeCompatibilityMapper;
import com.it.yts_project.mapper.PreProcessingStepsMapper;
import com.it.yts_project.mapper.PricingMapper;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.model.HotStampingPatternAreaCompatibility;
import com.it.yts_project.model.Pricing;
import com.it.yts_project.model.Products;
import com.it.yts_project.model.HotStampingTypeCompatibility;
import com.it.yts_project.model.HotStampingTypeOptions;
import com.it.yts_project.model.PreProcessingSteps;
import com.it.yts_project.model.PreProcessingStepsOptions;
import com.it.yts_project.service.*;
import com.it.yts_project.util.CommonInterfaceMatrixExportUtil;
import com.it.yts_project.util.CommonInterfaceMatrixImportUtil;
import com.it.yts_project.util.CommonInterfaceMatrixImportUtil.ParseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 常用界面燙印性 組合應用表 矩阵服务实现。
 * 导出时：特殊界面列仅包含「未勾选排除」的布面纸类型；行排除 paperType 为布面纸的纪录。
 */
@Service
public class CommonInterfaceMatrixServiceImpl implements CommonInterfaceMatrixService {

    @Autowired
    private ClothPaperCompatibilityService clothPaperCompatibilityService;
    @Autowired
    private ClothPaperTypeService clothPaperTypeService;
    @Autowired
    private HotStampingPatternAreaOptionService hotStampingPatternAreaOptionService;
    @Autowired
    private HotStampingPatternAreaCompatibilityMapper hotStampingPatternAreaCompatibilityMapper;
    @Autowired
    private HotStampingTypeCompatibilityMapper hotStampingTypeCompatibilityMapper;
    @Autowired
    private com.it.yts_project.mapper.HotStampingTypeOptionsMapper hotStampingTypeOptionsMapper;
    @Autowired
    private PreProcessingStepsOptionsService preProcessingStepsOptionsService;
    @Autowired
    private PreProcessingStepsMapper preProcessingStepsMapper;
    @Autowired
    private ProductsMapper productsMapper;
    @Autowired
    private PricingMapper pricingMapper;

    @Override
    public byte[] exportToExcel() throws Exception {
        CommonInterfaceMatrixDTO matrix = buildMatrix();
        return CommonInterfaceMatrixExportUtil.exportToExcel(matrix);
    }

    @Override
    public byte[] exportToWord() throws Exception {
        CommonInterfaceMatrixDTO matrix = buildMatrix();
        return CommonInterfaceMatrixExportUtil.exportToDocx(matrix);
    }

    @Override
    public Map<String, Object> importMatrix(MultipartFile file, String conflictStrategy) throws Exception {
        CommonInterfaceMatrixDTO matrix = buildMatrix();
        String name = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        ParseResult parseResult;
        if (name.endsWith(".docx") || name.endsWith(".doc")) {
            parseResult = CommonInterfaceMatrixImportUtil.parseWord(file.getInputStream(),
                    matrix.getPatternAreaColumns(), matrix.getStampingTypeColumns(),
                    matrix.getPreProcessColumns(), matrix.getSpecialInterfaceColumns());
        } else {
            byte[] bytes = file.getBytes();
            try {
                parseResult = CommonInterfaceMatrixImportUtil.parseExcel(new ByteArrayInputStream(bytes),
                        matrix.getPatternAreaColumns(), matrix.getStampingTypeColumns(),
                        matrix.getPreProcessColumns(), matrix.getSpecialInterfaceColumns());
            } catch (IllegalArgumentException e) {
                String msg = e.getMessage() != null ? e.getMessage() : "";
                if (msg.contains("系列") || msg.contains("Cannot locate header")) {
                    try {
                        parseResult = CommonInterfaceMatrixImportUtil.parseWord(new ByteArrayInputStream(bytes),
                                matrix.getPatternAreaColumns(), matrix.getStampingTypeColumns(),
                                matrix.getPreProcessColumns(), matrix.getSpecialInterfaceColumns());
                    } catch (Exception e2) {
                        throw new IllegalArgumentException("Parse failed as Excel (no row with '系列'). If file is Word, save as .docx and re-upload. Original: " + msg, e2);
                    }
                } else {
                    throw e;
                }
            }
        }
        List<MatrixRow> parsed = parseResult != null ? parseResult.getRows() : null;
        int totalRows = parsed != null ? parsed.size() : 0;
        Map<String, Object> result = new HashMap<>();
        result.put("totalRows", totalRows);
        if (parsed == null || parsed.isEmpty()) {
            result.put("created", 0);
            result.put("updated", 0);
            result.put("skipped", 0);
            result.put("errors", 0);
            result.put("message", "No data rows to apply.");
            return result;
        }
        if (parseResult.getPpNewColumnKeys() != null && !parseResult.getPpNewColumnKeys().isEmpty()) {
            ensurePreProcessOptionsForNewColumns(parseResult.getPpNewColumnKeys(), parseResult.getPpNewColumnPreProcessType());
        }
        List<String> errorMessages = new ArrayList<>();
        int[] counts = applyParsedRowsToDb(parsed, conflictStrategy, errorMessages);
        result.put("created", counts[0]);
        result.put("updated", counts[1]);
        result.put("skipped", counts[2]);
        result.put("errors", counts[3]);
        result.put("errorMessages", errorMessages);
        result.put("message", counts[3] > 0
                ? "Import completed with " + counts[3] + " error(s)."
                : "Import completed. Created: " + counts[0] + ", Updated: " + counts[1] + ", Skipped: " + counts[2]);
        return result;
    }

    /** 单条错误详情最大条数，避免响应过大 */
    private static final int MAX_ERROR_MESSAGES = 200;

    /** 导入前为所有 PP_NEW_ 列确保存在前工序选项（工艺类型），并设 description 为前工序类型（如「测试」），便于区分工序类型与工艺类型。 */
    private void ensurePreProcessOptionsForNewColumns(Set<String> ppNewColumnKeys, Map<String, String> ppNewColumnPreProcessType) {
        if (ppNewColumnKeys == null) return;
        for (String key : ppNewColumnKeys) {
            if (key == null || !key.startsWith("PP_NEW_")) continue;
            String stepName = key.substring(7).trim();
            if (stepName.isEmpty()) continue;
            String preProcessType = (ppNewColumnPreProcessType != null) ? ppNewColumnPreProcessType.get(stepName) : null;
            try {
                PreProcessingStepsOptions existingOpt = preProcessingStepsOptionsService.getByPreProcessingStepsName(stepName);
                if (existingOpt == null) {
                    PreProcessingStepsOptions newOpt = new PreProcessingStepsOptions();
                    newOpt.setPreProcessingStepsName(stepName);
                    newOpt.setSteps(stepName);
                    newOpt.setProcess(stepName);
                    if (preProcessType != null && !preProcessType.isEmpty()) newOpt.setDescription(preProcessType);
                    newOpt.setIsActive(true);
                    newOpt.setDisplayOrder(9999);
                    preProcessingStepsOptionsService.create(newOpt);
                } else if (preProcessType != null && !preProcessType.isEmpty()
                        && (existingOpt.getDescription() == null || existingOpt.getDescription().trim().isEmpty())) {
                    existingOpt.setDescription(preProcessType);
                    preProcessingStepsOptionsService.update(existingOpt);
                }
            } catch (Exception ignored) { /* 已存在或创建失败时跳过 */ }
        }
    }

    /**
     * 将解析后的矩阵行写入 DB。返回 [created, updated, skipped, errors]。
     * errorMessages 会追加每条失败的具体原因（最多 MAX_ERROR_MESSAGES 条）。
     */
    private int[] applyParsedRowsToDb(List<MatrixRow> parsed, String conflictStrategy, List<String> errorMessages) {
        boolean overwrite = "overwrite".equalsIgnoreCase(conflictStrategy);
        int created = 0, updated = 0, skipped = 0, errors = 0;
        LocalDateTime now = LocalDateTime.now();

        for (MatrixRow row : parsed) {
            String paperType = row.getPaperType() != null ? row.getPaperType().trim() : "";
            String seriesName = row.getSeriesName() != null ? row.getSeriesName().trim() : "";
            if (seriesName.isEmpty()) continue;
            Map<String, String> cells = row.getCells() != null ? row.getCells() : Collections.emptyMap();

            for (Map.Entry<String, String> e : cells.entrySet()) {
                String key = e.getKey();
                String value = e.getValue() != null ? e.getValue().trim() : "";
                if (value.isEmpty()) continue;
                String ctx = "系列「" + seriesName + "」类型「" + paperType + "」列「" + key + "」";
                if (key.startsWith("PA_")) {
                    Integer id = parseId(key.substring(3));
                    if (id == null) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": 列ID无效");
                        continue;
                    }
                    try {
                        HotStampingPatternAreaCompatibility existing = hotStampingPatternAreaCompatibilityMapper.findByProductNameAndPatternAreaIdAndPaperType(seriesName, id, paperType);
                        if (existing != null) {
                            if (overwrite) {
                                existing.setCompatibilityStatus(value);
                                existing.setUpdatedAt(now);
                                hotStampingPatternAreaCompatibilityMapper.update(existing);
                                updated++;
                            } else {
                                skipped++;
                            }
                        } else {
                            HotStampingPatternAreaCompatibility neu = new HotStampingPatternAreaCompatibility();
                            neu.setProductName(seriesName);
                            neu.setHotStampingPatternxAreaId(id);
                            neu.setPaperType(paperType);
                            neu.setCompatibilityStatus(value);
                            neu.setCreatedAt(now);
                            neu.setUpdatedAt(now);
                            hotStampingPatternAreaCompatibilityMapper.insert(neu);
                            created++;
                        }
                    } catch (Exception ex) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": " + resolveExceptionMessage(ex));
                    }
                } else if (key.startsWith("ST_NEW_")) {
                    // 新增烫金類型/位置類型列：從 key 中解析烫金類型與位置類型，確保 hot_stamping_type_options 中存在對應選項，然後寫入兼容性
                    String raw = key.substring("ST_NEW_".length());
                    String stampingType;
                    String positionType;
                    int sep = raw.indexOf("__POS__");
                    if (sep >= 0) {
                        stampingType = raw.substring(0, sep).trim();
                        positionType = raw.substring(sep + "__POS__".length()).trim();
                    } else {
                        stampingType = raw.trim();
                        positionType = "";
                    }
                    if (stampingType.isEmpty()) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": 新烫金列缺少烫金類型名稱");
                        continue;
                    }
                    try {
                        // 在現有烫金類型選項中查找匹配的 (stampingType, positionType)
                        List<HotStampingTypeOptions> allTypes = hotStampingTypeOptionsMapper.getAllTypes();
                        HotStampingTypeOptions target = null;
                        String posNorm = (positionType == null || positionType.isEmpty()) ? "" : positionType.trim();
                        for (HotStampingTypeOptions t : allTypes) {
                            if (t.getStampingType() == null) continue;
                            String ts = t.getStampingType().trim();
                            String tp = t.getPositionType() != null ? t.getPositionType().trim() : "";
                            if (ts.equals(stampingType) && tp.equals(posNorm)) {
                                target = t;
                                break;
                            }
                        }
                        if (target == null) {
                            // 創建新的烫金類型選項
                            HotStampingTypeOptions neuType = new HotStampingTypeOptions();
                            neuType.setStampingType(stampingType);
                            neuType.setPositionType(posNorm.isEmpty() ? null : posNorm);
                            neuType.setDescription(null);
                            neuType.setIsActive(true);
                            neuType.setSortOrder(9999);
                            hotStampingTypeOptionsMapper.insert(neuType);
                            target = neuType;
                        }
                        if (target.getId() == null) {
                            errors++;
                            addErrorMessage(errorMessages, ctx + ": 新烫金類型選項創建后無ID");
                            continue;
                        }
                        Integer id = target.getId().intValue();
                        HotStampingTypeCompatibility existing = hotStampingTypeCompatibilityMapper.findByProductNameAndPaperTypeAndHotStampingTypeId(seriesName, paperType, id);
                        if (existing != null) {
                            if (overwrite) {
                                existing.setCompatibilityStatus(value);
                                existing.setUpdatedAt(now);
                                hotStampingTypeCompatibilityMapper.update(existing);
                                updated++;
                            } else {
                                skipped++;
                            }
                        } else {
                            HotStampingTypeCompatibility neu = new HotStampingTypeCompatibility();
                            neu.setProductName(seriesName);
                            neu.setPaperType(paperType);
                            neu.setHotStampingTypeId(id);
                            neu.setCompatibilityStatus(value);
                            neu.setCreatedAt(now);
                            neu.setUpdatedAt(now);
                            hotStampingTypeCompatibilityMapper.insert(neu);
                            created++;
                        }
                    } catch (Exception ex) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": " + resolveExceptionMessage(ex));
                    }
                } else if (key.startsWith("ST_")) {
                    // 已有烫金類型列（ST_数字ID）
                    Integer id = parseId(key.substring(3));
                    if (id == null) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": 列ID无效");
                        continue;
                    }
                    try {
                        HotStampingTypeCompatibility existing = hotStampingTypeCompatibilityMapper.findByProductNameAndPaperTypeAndHotStampingTypeId(seriesName, paperType, id);
                        if (existing != null) {
                            if (overwrite) {
                                existing.setCompatibilityStatus(value);
                                existing.setUpdatedAt(now);
                                hotStampingTypeCompatibilityMapper.update(existing);
                                updated++;
                            } else {
                                skipped++;
                            }
                        } else {
                            HotStampingTypeCompatibility neu = new HotStampingTypeCompatibility();
                            neu.setProductName(seriesName);
                            neu.setPaperType(paperType);
                            neu.setHotStampingTypeId(id);
                            neu.setCompatibilityStatus(value);
                            neu.setCreatedAt(now);
                            neu.setUpdatedAt(now);
                            hotStampingTypeCompatibilityMapper.insert(neu);
                            created++;
                        }
                    } catch (Exception ex) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": " + resolveExceptionMessage(ex));
                    }
                } else if (key.startsWith("PP_NEW_")) {
                    // 新增適用界面(前工序)列：先按名稱取得或創建前工序選項，再寫入 pre_processing_steps 映射
                    String stepName = key.substring(7).trim();
                    if (stepName.isEmpty()) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": 前工序列名為空");
                        continue;
                    }
                    try {
                        PreProcessingStepsOptions option = preProcessingStepsOptionsService.getByPreProcessingStepsName(stepName);
                        if (option == null) {
                            PreProcessingStepsOptions newOpt = new PreProcessingStepsOptions();
                            newOpt.setPreProcessingStepsName(stepName);
                            newOpt.setSteps(stepName);
                            newOpt.setProcess(stepName);
                            newOpt.setIsActive(true);
                            newOpt.setDisplayOrder(9999);
                            option = preProcessingStepsOptionsService.create(newOpt);
                        }
                        Integer stepId = option.getId();
                        if (stepId == null) {
                            errors++;
                            addErrorMessage(errorMessages, ctx + ": 前工序选项创建后无ID");
                            continue;
                        }
                        PreProcessingSteps existing = preProcessingStepsMapper.findByUniqueFields(stepId, seriesName, null, paperType);
                        if (existing != null) {
                            if (overwrite) {
                                existing.setStatus(value);
                                existing.setUpdatedAt(now);
                                preProcessingStepsMapper.update(existing);
                                updated++;
                            } else {
                                skipped++;
                            }
                        } else {
                            PreProcessingSteps neu = new PreProcessingSteps();
                            neu.setStepId(stepId);
                            neu.setSeriesName(seriesName);
                            neu.setProductId(null);
                            neu.setPaperType(paperType);
                            neu.setStatus(value);
                            neu.setStepName("");
                            neu.setStepOrder(0);
                            neu.setIsActive(true);
                            neu.setCreatedAt(now);
                            neu.setUpdatedAt(now);
                            preProcessingStepsMapper.insert(neu);
                            created++;
                        }
                    } catch (Exception ex) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": " + resolveExceptionMessage(ex));
                    }
                } else if (key.startsWith("PP_")) {
                    Integer stepId = parseId(key.substring(3));
                    if (stepId == null) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": 列ID无效");
                        continue;
                    }
                    try {
                        PreProcessingSteps existing = preProcessingStepsMapper.findByUniqueFields(stepId, seriesName, null, paperType);
                        if (existing != null) {
                            if (overwrite) {
                                existing.setStatus(value);
                                existing.setUpdatedAt(now);
                                preProcessingStepsMapper.update(existing);
                                updated++;
                            } else {
                                skipped++;
                            }
                        } else {
                            PreProcessingSteps neu = new PreProcessingSteps();
                            neu.setStepId(stepId);
                            neu.setSeriesName(seriesName);
                            neu.setProductId(null);
                            neu.setPaperType(paperType);
                            neu.setStatus(value);
                            neu.setStepName("");
                            neu.setStepOrder(0);
                            neu.setIsActive(true);
                            neu.setCreatedAt(now);
                            neu.setUpdatedAt(now);
                            preProcessingStepsMapper.insert(neu);
                            created++;
                        }
                    } catch (Exception ex) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": " + resolveExceptionMessage(ex));
                    }
                } else if (key.startsWith("SI_NEW_")) {
                    // 新增適用界面列：先按名稱取得或創建布面紙類型，再寫入兼容性。分類優先取自表頭上一行（分組）；無則用列名（類型名），不再用「導入」
                    String typeName;
                    String category;
                    if (key.contains("__CAT__")) {
                        int sep = key.indexOf("__CAT__");
                        typeName = key.substring(7, sep).trim();
                        String catPart = key.substring(sep + 7).trim();
                        category = !catPart.isEmpty() ? catPart : null;
                    } else {
                        typeName = key.substring(7).trim();
                        category = null;
                    }
                    if (category == null) {
                        category = "导入";
                    }
                    if (typeName.isEmpty()) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": 列名為空");
                        continue;
                    }
                    try {
                        ClothPaperTypeDTO typeDto = clothPaperTypeService.getByTypeName(typeName);
                        if (typeDto == null) {
                            ClothPaperTypeDTO newType = new ClothPaperTypeDTO();
                            newType.setTypeName(typeName);
                            newType.setCategory(category);
                            newType.setExcludeFromCommonInterfaceMatrix(false);
                            newType.setIsActive(true);
                            typeDto = clothPaperTypeService.create(newType);
                        } else if (!"导入".equals(category) && "导入".equals(typeDto.getCategory())) {
                            // 類型已存在但分類仍是「導入」：用本次導入的分類（如測試1）覆寫
                            typeDto.setCategory(category);
                            clothPaperTypeService.update(typeDto);
                        }
                        Integer clothPaperTypeId = typeDto.getId();
                        if (clothPaperTypeId == null) {
                            errors++;
                            addErrorMessage(errorMessages, ctx + ": 布面纸类型创建后无ID");
                            continue;
                        }
                        ClothPaperCompatibilityDTO existing = clothPaperCompatibilityService.getByProjectAndTypeAndPaperType(seriesName, clothPaperTypeId, paperType);
                        if (existing != null) {
                            if (overwrite) {
                                existing.setCompatibilityStatus(value);
                                clothPaperCompatibilityService.update(existing);
                                updated++;
                            } else {
                                skipped++;
                            }
                        } else {
                            ClothPaperCompatibilityDTO neu = new ClothPaperCompatibilityDTO();
                            neu.setProductName(seriesName);
                            neu.setClothPaperTypeId(clothPaperTypeId);
                            neu.setPaperType(paperType);
                            neu.setCompatibilityStatus(value);
                            clothPaperCompatibilityService.create(neu);
                            created++;
                        }
                    } catch (Exception ex) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": " + resolveExceptionMessage(ex));
                    }
                } else if (key.startsWith("SI_")) {
                    Integer clothPaperTypeId = parseId(key.substring(3));
                    if (clothPaperTypeId == null) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": 列ID无效");
                        continue;
                    }
                    try {
                        ClothPaperCompatibilityDTO existing = clothPaperCompatibilityService.getByProjectAndTypeAndPaperType(seriesName, clothPaperTypeId, paperType);
                        if (existing != null) {
                            if (overwrite) {
                                existing.setCompatibilityStatus(value);
                                clothPaperCompatibilityService.update(existing);
                                updated++;
                            } else {
                                skipped++;
                            }
                        } else {
                            ClothPaperCompatibilityDTO neu = new ClothPaperCompatibilityDTO();
                            neu.setProductName(seriesName);
                            neu.setClothPaperTypeId(clothPaperTypeId);
                            neu.setPaperType(paperType);
                            neu.setCompatibilityStatus(value);
                            clothPaperCompatibilityService.create(neu);
                            created++;
                        }
                    } catch (Exception ex) {
                        errors++;
                        addErrorMessage(errorMessages, ctx + ": " + resolveExceptionMessage(ex));
                    }
                }
            }
        }
        return new int[] { created, updated, skipped, errors };
    }

    private static void addErrorMessage(List<String> list, String msg) {
        if (list.size() < MAX_ERROR_MESSAGES) {
            list.add(msg);
        } else if (list.size() == MAX_ERROR_MESSAGES) {
            list.add("… 更多错误已省略，请查看服务器日志");
        }
    }

    private static String resolveExceptionMessage(Throwable ex) {
        if (ex == null) return "未知错误";
        String msg = ex.getMessage();
        if (ex instanceof org.springframework.dao.DuplicateKeyException || (msg != null && (msg.contains("duplicate") || msg.contains("unique") || msg.contains("重复")))) {
            return "重复记录，已存在相同系列/类型/列";
        }
        if (ex.getCause() != null) msg = ex.getCause().getMessage();
        if (msg != null && msg.length() > 120) msg = msg.substring(0, 117) + "...";
        return msg != null ? msg : ex.getClass().getSimpleName();
    }

    private static Integer parseId(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 构建矩阵：列来自各配置表，特殊界面列仅含「未排除」的布面纸类型；行来自兼容性数据并排除布面纸 paperType。
     */
    private CommonInterfaceMatrixDTO buildMatrix() {
        CommonInterfaceMatrixDTO dto = new CommonInterfaceMatrixDTO();

        List<ColumnDef> paColumns = buildPatternAreaColumns();
        List<ColumnDef> stColumns = buildStampingTypeColumns();
        List<ColumnDef> ppColumns = buildPreProcessColumns();
        List<ColumnDef> siColumns = buildSpecialInterfaceColumns();

        dto.setPatternAreaColumns(paColumns);
        dto.setStampingTypeColumns(stColumns);
        dto.setPreProcessColumns(ppColumns);
        dto.setSpecialInterfaceColumns(siColumns);

        List<MatrixRow> rows = buildRows(paColumns, stColumns, ppColumns, siColumns);
        dto.setRows(rows);
        return dto;
    }

    private List<ColumnDef> buildPatternAreaColumns() {
        List<HotStampingPatternAreaOptionDTO> options = hotStampingPatternAreaOptionService.getAllActivePatternAreaOptions();
        if (options == null) return new ArrayList<>();
        return options.stream()
                .filter(o -> o.getId() != null && o.getOptionName() != null)
                .map(o -> new ColumnDef(o.getId(), o.getOptionName(), "燙金圖案"))
                .collect(Collectors.toList());
    }

    private List<ColumnDef> buildStampingTypeColumns() {
        List<HotStampingTypeOptions> types = hotStampingTypeOptionsMapper.getActiveTypes();
        if (types == null) return new ArrayList<>();
        return types.stream()
                .filter(t -> t.getId() != null && t.getStampingType() != null)
                .map(t -> {
                    String stampingType = t.getStampingType() != null ? t.getStampingType().trim() : "";
                    if (stampingType.isEmpty()) return null;
                    String positionType = t.getPositionType() != null ? t.getPositionType().trim() : "";
                    String name = positionType.isEmpty() ? "默认" : positionType;
                    // group 固定為「燙金類型」，subGroup 用於表頭分組顯示具體烫金類型（如 平面烫金）
                    return new ColumnDef(t.getId().intValue(), name, "燙金類型", stampingType);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /** 前工序列：界面分类表头在分类前加「(前工序)」，优先用 description（前工序类型），无则用 steps/process，空则用「常用界面」。 */
    private List<ColumnDef> buildPreProcessColumns() {
        final String prefix = "(前工序)";
        return preProcessingStepsOptionsService.getAllActiveOptions().stream()
                .filter(o -> o.getId() != null && o.getPreProcessingStepsName() != null)
                .map(o -> {
                    String raw = (o.getDescription() != null && !o.getDescription().trim().isEmpty()) ? o.getDescription().trim()
                            : (o.getSteps() != null && !o.getSteps().trim().isEmpty()) ? o.getSteps().trim()
                            : (o.getProcess() != null && !o.getProcess().trim().isEmpty()) ? o.getProcess().trim() : "常用界面";
                    return new ColumnDef(o.getId(), o.getPreProcessingStepsName(), "常用界面", prefix + raw);
                })
                .collect(Collectors.toList());
    }

    /**
     * 特殊界面列：仅包含「未勾选排除常用界面矩阵」的布面纸类型。
     * subGroup 取自布面纸的 category；未设置分类时用类型名称（如 珍珠紙面、掃金粉面），不再用「特殊界面」。
     */
    private List<ColumnDef> buildSpecialInterfaceColumns() {
        List<ClothPaperTypeDTO> types = clothPaperTypeService.getActiveClothPaperTypesForCommonInterfaceMatrix();
        if (types == null) return new ArrayList<>();
        return types.stream()
                .filter(t -> t.getId() != null && t.getTypeName() != null)
                .map(t -> {
                    String sub = (t.getCategory() != null && !t.getCategory().trim().isEmpty())
                            ? t.getCategory().trim() : t.getTypeName();
                    return new ColumnDef(t.getId(), t.getTypeName(), "特殊界面", sub);
                })
                .collect(Collectors.toList());
    }

    private List<MatrixRow> buildRows(List<ColumnDef> paColumns, List<ColumnDef> stColumns,
                                     List<ColumnDef> ppColumns, List<ColumnDef> siColumns) {
        List<ClothPaperCompatibilityDTO> allCompat = clothPaperCompatibilityService.getAllCompatibility();
        if (allCompat == null) allCompat = new ArrayList<>();

        Set<String> rowKeys = new LinkedHashSet<>();
        for (ClothPaperCompatibilityDTO c : allCompat) {
            String paperType = c.getPaperType() != null ? c.getPaperType().trim() : "";
            String seriesName = c.getProductName() != null ? c.getProductName().trim() : "";
            if (seriesName.isEmpty()) continue;
            if (isClothPaperType(paperType)) continue;
            rowKeys.add(paperType + "\t" + seriesName);
        }

        Map<String, BigDecimal> rowKeyToPrice = buildRowKeyToPriceMap(rowKeys);
        List<String> sortedKeys = new ArrayList<>(rowKeys);
        sortedKeys.sort((a, b) -> {
            String[] aa = a.split("\t", 2);
            String[] bb = b.split("\t", 2);
            int paperTypeCmp = (aa[0].compareTo(bb[0]));
            if (paperTypeCmp != 0) return paperTypeCmp;
            BigDecimal pa = rowKeyToPrice.get(a);
            BigDecimal pb = rowKeyToPrice.get(b);
            int priceCmp = Comparator.nullsLast(BigDecimal::compareTo).compare(pa, pb);
            if (priceCmp != 0) return priceCmp;
            return (aa.length > 1 && bb.length > 1) ? aa[1].compareTo(bb[1]) : 0;
        });

        Map<String, String> paMap = buildPatternAreaCompatMap();
        Map<String, String> stMap = buildStampingTypeCompatMap();
        List<PreProcessingSteps> allPpSteps = preProcessingStepsMapper.getAllActiveSteps() != null
                ? preProcessingStepsMapper.getAllActiveSteps() : new ArrayList<>();
        Map<String, String> ppMap = buildPreProcessCompatMap(allPpSteps);
        Map<String, String> siMap = buildSpecialInterfaceCompatMap(allCompat);

        List<MatrixRow> rows = new ArrayList<>();
        for (String key : sortedKeys) {
            String[] parts = key.split("\t", 2);
            String paperType = parts.length > 0 ? parts[0] : "";
            String seriesName = parts.length > 1 ? parts[1] : "";

            MatrixRow row = new MatrixRow();
            row.setPaperType(paperType);
            row.setSeriesName(seriesName);

            for (ColumnDef c : paColumns) {
                String status = paMap.get(seriesName + "\t" + c.getId() + "\t" + paperType);
                if (status != null) row.getCells().put("PA_" + c.getId(), status);
            }
            for (ColumnDef c : stColumns) {
                String status = stMap.get(seriesName + "\t" + paperType + "\t" + c.getId());
                if (status != null) row.getCells().put("ST_" + c.getId(), status);
            }
            for (ColumnDef c : ppColumns) {
                String status = ppMap.get(seriesName + "\t" + paperType + "\t" + c.getId());
                if (status != null) row.getCells().put("PP_" + c.getId(), status);
            }
            for (ColumnDef c : siColumns) {
                String status = siMap.get(seriesName + "\t" + c.getId() + "\t" + paperType);
                if (status != null) row.getCells().put("SI_" + c.getId(), status);
            }
            rows.add(row);
        }
        return rows;
    }

    /** 一次性加载 PA 兼容数据，避免按行按列查库 */
    private Map<String, String> buildPatternAreaCompatMap() {
        List<HotStampingPatternAreaCompatibility> all = hotStampingPatternAreaCompatibilityMapper.findAll();
        if (all == null) all = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (HotStampingPatternAreaCompatibility c : all) {
            String productName = c.getProductName() != null ? c.getProductName().trim() : "";
            String paperType = c.getPaperType() != null ? c.getPaperType().trim() : "";
            Integer areaId = c.getHotStampingPatternxAreaId();
            String status = c.getCompatibilityStatus() != null ? c.getCompatibilityStatus().trim() : "";
            if (areaId != null && !status.isEmpty())
                map.put(productName + "\t" + areaId + "\t" + paperType, status);
        }
        return map;
    }

    /** 从已加载的布面纸兼容列表构建 SI 列查找表，避免按行按列查库 */
    private Map<String, String> buildSpecialInterfaceCompatMap(List<ClothPaperCompatibilityDTO> allCompat) {
        Map<String, String> map = new HashMap<>();
        for (ClothPaperCompatibilityDTO c : allCompat) {
            String productName = c.getProductName() != null ? c.getProductName().trim() : "";
            String paperType = c.getPaperType() != null ? c.getPaperType().trim() : "";
            Integer typeId = c.getClothPaperTypeId();
            String status = c.getCompatibilityStatus() != null ? c.getCompatibilityStatus().trim() : "";
            if (typeId != null && !status.isEmpty())
                map.put(productName + "\t" + typeId + "\t" + paperType, status);
        }
        return map;
    }

    /**
     * 构建 (paperType + "\\t" + seriesName) -> 最低价格，用于同一烫金纸类型下按价格排序。
     */
    private Map<String, BigDecimal> buildRowKeyToPriceMap(Set<String> rowKeys) {
        Map<String, BigDecimal> map = new HashMap<>();
        if (rowKeys == null || rowKeys.isEmpty()) return map;
        Set<String> seriesNames = new HashSet<>();
        for (String key : rowKeys) {
            String[] parts = key.split("\t", 2);
            if (parts.length > 1 && !parts[1].isEmpty()) seriesNames.add(parts[1]);
        }
        Map<String, Integer> seriesPaperToProductId = new HashMap<>();
        for (String seriesName : seriesNames) {
            List<Products> products = productsMapper.getProductsBySeriesName(seriesName);
            if (products == null) continue;
            for (Products p : products) {
                String pt = p.getHot_stamping_paper_type() != null ? p.getHot_stamping_paper_type().trim() : "";
                Integer id = p.getId();
                if (id != null) seriesPaperToProductId.put(seriesName + "\t" + pt, id);
            }
        }
        Set<Integer> productIds = new HashSet<>(seriesPaperToProductId.values());
        Map<Integer, BigDecimal> productIdToMinPrice = new HashMap<>();
        for (Integer pid : productIds) {
            List<Pricing> list = pricingMapper.findByProjectId(pid);
            if (list != null && !list.isEmpty()) {
                BigDecimal minP = list.stream()
                        .map(Pricing::getPrice)
                        .filter(Objects::nonNull)
                        .min(BigDecimal::compareTo)
                        .orElse(null);
                if (minP != null) productIdToMinPrice.put(pid, minP);
            }
        }
        for (String key : rowKeys) {
            String[] parts = key.split("\t", 2);
            String paperType = parts.length > 0 ? parts[0] : "";
            String seriesName = parts.length > 1 ? parts[1] : "";
            Integer pid = seriesPaperToProductId.get(seriesName + "\t" + paperType);
            if (pid != null) {
                BigDecimal price = productIdToMinPrice.get(pid);
                if (price != null) map.put(key, price);
            }
        }
        return map;
    }

    private static boolean isClothPaperType(String paperType) {
        if (paperType == null || paperType.isEmpty()) return false;
        return paperType.contains("布面纸") || paperType.contains("布面紙");
    }

    private Map<String, String> buildStampingTypeCompatMap() {
        List<HotStampingTypeCompatibility> all = hotStampingTypeCompatibilityMapper.findAll();
        if (all == null) all = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (HotStampingTypeCompatibility c : all) {
            String productName = c.getProductName() != null ? c.getProductName().trim() : "";
            String paperType = c.getPaperType() != null ? c.getPaperType().trim() : "";
            Integer typeId = c.getHotStampingTypeId();
            String status = c.getCompatibilityStatus() != null ? c.getCompatibilityStatus().trim() : "";
            if (typeId != null && !status.isEmpty())
                map.put(productName + "\t" + paperType + "\t" + typeId, status);
        }
        return map;
    }

    private Map<String, String> buildPreProcessCompatMap(List<PreProcessingSteps> allSteps) {
        Map<String, String> map = new HashMap<>();
        for (PreProcessingSteps s : allSteps) {
            String seriesName = s.getSeriesName() != null ? s.getSeriesName().trim() : "";
            String paperType = s.getPaperType() != null ? s.getPaperType().trim() : "";
            Integer stepId = s.getStepId();
            String status = s.getStatus() != null ? s.getStatus().trim() : "";
            if (stepId != null && !status.isEmpty())
                map.put(seriesName + "\t" + paperType + "\t" + stepId, status);
        }
        return map;
    }
}
