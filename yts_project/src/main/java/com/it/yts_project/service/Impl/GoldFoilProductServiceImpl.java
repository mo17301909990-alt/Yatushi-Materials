package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.GoldFoilProductDTO;
import com.it.yts_project.dto.GoldFoilQueryParams;
import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.mapper.GoldFoilProductMapper;
import com.it.yts_project.service.GoldFoilProductService;
import com.it.yts_project.service.HotStampingCompatibilityService;
import com.it.yts_project.service.UserMatchPreferenceService;
import com.it.yts_project.service.WearResistantGoldPaperSkipConfigService;
import com.it.yts_project.util.MatchScoreCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GoldFoilProductServiceImpl implements GoldFoilProductService {

    private static final Logger log = LoggerFactory.getLogger(GoldFoilProductServiceImpl.class);

    @Autowired
    private GoldFoilProductMapper goldFoilProductMapper;
    
    @Autowired
    private HotStampingCompatibilityService compatibilityService;
    
    @Autowired
    private UserMatchPreferenceService userMatchPreferenceService;

    @Autowired
    private WearResistantGoldPaperSkipConfigService wearResistantGoldPaperSkipConfigService;

    /**
     * 获取右侧全部烫金纸供应型号列表
     * @return
     */
    @Override
    public List<GoldFoilProductDTO> getAllGoldFoilProducts() {
        return goldFoilProductMapper.getAllGoldFoilProducts();
    }

    /**
     * 对产品类型和烫金图案(X)做的处理
     * @param params 查询参数，包含公司编号、产品类型、烫金图案类型等
     * @return
     */
    @Override
    public List<GoldFoilProductDTO> getProducts(GoldFoilQueryParams params) {
        long startTime = System.currentTimeMillis(); // 性能监控
        
        // 处理产品类型和烫金图案类型（如果有）
        List<String> hotStampingPaperTypeChoose = new ArrayList<>();

        // 收集所有可能的产品类型和烫金图案类型值
        if (params.getStampingType() != null && !params.getStampingType().isEmpty()) {
            hotStampingPaperTypeChoose.add(params.getStampingType());
        }

        // 收集烫金图案类型值
        if (params.getPatternType() != null && !params.getPatternType().isEmpty()) {
            hotStampingPaperTypeChoose.add(params.getPatternType());
        }

        // ========================================
        // 交集逻辑：根据用户选择的条件，从兼容性规则表中提取可用的纸类型列表
        // ========================================
        // 如果用户选择了产品类型、图案特征、烫金类型等条件，先提取可用的纸类型列表
        if (params.getProductTypeId() != null || params.getPatternId() != null || 
            params.getHotStampingTypeOptionId() != null || params.getPreProcessingStepsOptionId() != null) {
            
            log.debug("[查询策略] 使用交集逻辑，根据兼容性规则提取可用的纸类型");
            log.debug("  - 产品类型ID: {}", params.getProductTypeId());
            log.debug("  - 图案特征ID: {}", params.getPatternId());
            log.debug("  - 烫金类型选项ID: {}", params.getHotStampingTypeOptionId());
            log.debug("  - 前工序ID: {}", params.getPreProcessingStepsOptionId());

            // 检测烫金类型的映射状态
            boolean hasDurableMapping = false;
            boolean hasCommonMapping = false;
            if (params.getHotStampingTypeOptionId() != null) {
                hasDurableMapping = compatibilityService.hasDurableMapping(params.getHotStampingTypeOptionId());
                hasCommonMapping = compatibilityService.hasCommonMapping(params.getHotStampingTypeOptionId());
                log.debug("  - 烫金类型ID {} 的映射状态:", params.getHotStampingTypeOptionId());
                log.debug("    * 耐磨映射存在: {}", hasDurableMapping);
                log.debug("    * 常用界面映射存在: {}", hasCommonMapping);
            }
            params.setHasDurableMapping(hasDurableMapping);
            params.setHasCommonMapping(hasCommonMapping);
            
            // 调用兼容性服务，根据用户选择的条件提取可用的纸类型列表（从耐磨映射表）
            // 逻辑：根据传入的条件，直接查询同时满足所有条件的规则，然后提取纸类型
            // - 如果只传入产品类型ID：查询 product_type_id = ? AND compatibility = 'V'
            // - 如果传入产品类型ID + 图案ID：查询 product_type_id = ? AND pattern_characteristic_id = ? AND compatibility = 'V'
            // - 如果传入产品类型ID + 图案ID + 烫金类型ID：查询 product_type_id = ? AND pattern_characteristic_id = ? AND hot_stamping_type_id = ? AND compatibility = 'V'
            List<String> compatiblePaperTypes = compatibilityService.getPaperPerformanceByMultipleIds(
                    params.getProductTypeId(),           
                    params.getPatternId(),               
                    params.getHotStampingTypeOptionId(), 
                    params.getPreProcessingStepsOptionId(), 
                    null                                  
            );
            // 将配置为「跳过耐磨映射」的烫金纸类型一并加入列表，使其不受耐磨规则限制
            List<String> mergedPaperTypes = mergeWithSkipPaperTypes(compatiblePaperTypes);
            
            // 获取跳过配置的纸型列表，用于在 SQL EXISTS 检查中跳过
            try {
                List<String> skipTypes = wearResistantGoldPaperSkipConfigService.getAllSkipPaperTypes();
                if (skipTypes != null && !skipTypes.isEmpty()) {
                    params.setSkipWearResistantPaperTypes(skipTypes);
                    log.debug("  - 设置跳过耐磨映射的纸型列表（用于 SQL EXISTS 检查）: {}", skipTypes);
                }
            } catch (Exception e) {
                log.warn("加载跳过耐磨映射的烫金纸类型失败（用于 SQL EXISTS 检查）", e);
            }
            
            // 根据映射状态决定处理策略
            if (params.getHotStampingTypeOptionId() != null) {
                // 情况1：两种映射都存在 - 使用耐磨映射的纸型列表，SQL层同时检查常用界面映射（取交集）
                if (hasDurableMapping && hasCommonMapping) {
                    if (mergedPaperTypes != null && !mergedPaperTypes.isEmpty()) {
                        log.debug("  - 两种映射都存在，使用耐磨映射 + 跳过配置的纸型列表: {}", mergedPaperTypes);
                        log.debug("  - SQL层将同时检查常用界面映射（取交集）");
                        params.setHotStampingPaperTypes(mergedPaperTypes);
                    } else {
                        log.warn("  - 警告：耐磨映射存在且无跳过配置，但未找到符合条件的纸类型，将返回空结果");
                        params.setHotStampingPaperTypes(Collections.singletonList("__NO_MATCH__"));
                        return new ArrayList<>();
                    }
                }
                // 情况2：只有耐磨映射 - 只使用耐磨映射的纸型列表
                else if (hasDurableMapping && !hasCommonMapping) {
                    if (mergedPaperTypes != null && !mergedPaperTypes.isEmpty()) {
                        log.debug("  - 只有耐磨映射，使用纸型列表(含跳过配置): {}", mergedPaperTypes);
                        params.setHotStampingPaperTypes(mergedPaperTypes);
                    } else {
                        log.warn("  - 警告：耐磨映射存在且无跳过配置，但未找到符合条件的纸类型，将返回空结果");
                        params.setHotStampingPaperTypes(Collections.singletonList("__NO_MATCH__"));
                        return new ArrayList<>();
                    }
                }
                // 情况3：只有常用界面映射 - 不设置纸型列表，SQL层只依赖常用界面映射的EXISTS
                else if (!hasDurableMapping && hasCommonMapping) {
                    log.debug("  - 只有常用界面映射，不设置纸型列表，SQL层将只检查常用界面映射");
                    // 不设置 hotStampingPaperTypes，让SQL层只使用常用界面映射的EXISTS条件
                }
                // 情况4：两种映射都不存在 - 返回空结果
                else {
                    log.warn("  - 警告：两种映射都不存在，将返回空结果");
                    params.setHotStampingPaperTypes(Collections.singletonList("__NO_MATCH__"));
                    return new ArrayList<>();
                }
            } else {
                // 没有选择烫金类型，但有其他条件（产品类型、图案等）
                List<String> mergedWhenNoType = mergeWithSkipPaperTypes(compatiblePaperTypes);
                if (mergedWhenNoType != null && !mergedWhenNoType.isEmpty()) {
                    log.debug("  - 提取到的可用纸类型(含跳过配置): {}", mergedWhenNoType);
                    params.setHotStampingPaperTypes(mergedWhenNoType);
                } else {
                    log.warn("  - 警告：未找到符合条件的纸类型且无跳过配置，将返回空结果");
                    params.setHotStampingPaperTypes(Collections.singletonList("__NO_MATCH__"));
                }
                // 获取跳过配置的纸型列表，用于在 SQL EXISTS 检查中跳过
                try {
                    List<String> skipTypes = wearResistantGoldPaperSkipConfigService.getAllSkipPaperTypes();
                    if (skipTypes != null && !skipTypes.isEmpty()) {
                        params.setSkipWearResistantPaperTypes(skipTypes);
                        log.debug("  - 设置跳过耐磨映射的纸型列表（用于 SQL EXISTS 检查）: {}", skipTypes);
                    }
                } catch (Exception e) {
                    log.warn("加载跳过耐磨映射的烫金纸类型失败（用于 SQL EXISTS 检查）", e);
                }
            }
        }
        // 如果有前工序选项ID，优先使用前工序匹配逻辑（当没有其他条件时）
        else if (params.getPreProcessingStepsOptionId() != null) {
            log.debug("[查询策略] 使用前工序匹配逻辑，步骤ID: {}", params.getPreProcessingStepsOptionId());
        }
        // 如果有兼容性筛选参数，使用兼容性表进行筛选（旧逻辑，保留兼容）
        else if (hasCompatibilityParams(params)) {
            List<String> compatiblePaperTypes = getCompatiblePaperTypes(params);
            if (compatiblePaperTypes != null && !compatiblePaperTypes.isEmpty()) {
                params.setHotStampingPaperTypes(compatiblePaperTypes);
            }
        }
//        } else if (!hotStampingPaperTypeChoose.isEmpty()) {
//            // 否则使用原有规则
//            List<String> hotStampingPaperTypes = applyRules(hotStampingPaperTypeChoose);
//            params.setHotStampingPaperTypes(hotStampingPaperTypes);
//        } else {
//            return new ArrayList<>();
//        }

        // ========================================
        // 匹配度计算和排序处理
        // ========================================
        List<GoldFoilProductDTO> products;
        
        // 获取匹配度规则类型，默认为价格匹配度
        String matchScoreRule = params.getMatchScoreRule();
        if (matchScoreRule == null || matchScoreRule.isEmpty()) {
            matchScoreRule = "price"; // 默认使用价格匹配度
        }
        
        // 如果按匹配度排序，需要先查询所有数据（不分页），然后计算匹配度并排序分页
        if ("matchScore".equals(params.getSortBy())) {
            // 创建临时参数，查询所有数据（不分页）
            GoldFoilQueryParams tempParams = new GoldFoilQueryParams();
            // 复制所有查询条件
            tempParams.setPreProcessingStepsOptionId(params.getPreProcessingStepsOptionId());
            tempParams.setProductTypeId(params.getProductTypeId());
            tempParams.setPatternId(params.getPatternId());
            tempParams.setClothPaperTypeId(params.getClothPaperTypeId());
            tempParams.setClothPaperCompatibilityStatus(params.getClothPaperCompatibilityStatus());
            tempParams.setCompanyNumber(params.getCompanyNumber());
            tempParams.setGpNo(params.getGpNo());
            tempParams.setPaizi(params.getPaizi());
            tempParams.setColorNum(params.getColorNum());
            tempParams.setPriceLevel(params.getPriceLevel());
            tempParams.setHotStampingPaperTypes(params.getHotStampingPaperTypes());
            tempParams.setPostProcessingStepId(params.getPostProcessingStepId());
            tempParams.setLaminationMaterialId(params.getLaminationMaterialId());
            tempParams.setPrinting(params.getPrinting());
            tempParams.setUvPrinting(params.getUvPrinting());
            tempParams.setSilkScreenLedUvSparklePowder(params.getSilkScreenLedUvSparklePowder());
            tempParams.setHotStampingTypeOptionId(params.getHotStampingTypeOptionId());
            tempParams.setPatternAreaOptionId(params.getPatternAreaOptionId());
            // 复制烫金类型映射状态标记
            tempParams.setHasDurableMapping(params.getHasDurableMapping());
            tempParams.setHasCommonMapping(params.getHasCommonMapping());
            // 不分页，不排序（SQL会按默认价格排序）
            tempParams.setPageSize(null);
            tempParams.setOffset(null);
            tempParams.setSortBy(null);
            
            log.debug("开始执行数据库查询（匹配度排序，查询所有数据，匹配度规则: {}）...", matchScoreRule);
            products = goldFoilProductMapper.getProducts(tempParams);
            log.debug("数据库查询完成，返回记录数: {}", products.size());
            
            // 根据匹配度规则类型计算匹配度
            if ("price".equals(matchScoreRule)) {
                // 使用新的价格+使用次数匹配度规则
                MatchScoreCalculator.calculatePriceUsageMatchScores(products);
            } else if ("tag".equals(matchScoreRule) && params.getUserId() != null) {
                // 使用标签配置匹配度规则
                Map<String, List<String>> userPreferences = userMatchPreferenceService.getUserPreferencesGrouped(params.getUserId());
                if (MatchScoreCalculator.hasUserPreferences(userPreferences)) {
                    MatchScoreCalculator.calculateBatchMatchScores(products, userPreferences);
                } else {
                    log.debug("用户未设置匹配偏好，跳过标签匹配度计算，返回空列表");
                    products = new ArrayList<>();
                }
            } else {
                log.warn("无效的匹配度规则类型: {}，或缺少userId，返回空列表", matchScoreRule);
                products = new ArrayList<>();
            }
            
            if (!products.isEmpty()) {
                // 按最终匹配度排序（从高到低）
                boolean isDesc = "desc".equals(params.getSortOrder());
                products.sort((a, b) -> {
                    Double finalScoreA = a.getFinalMatchScore() != null ? a.getFinalMatchScore() : 0.0;
                    Double finalScoreB = b.getFinalMatchScore() != null ? b.getFinalMatchScore() : 0.0;
                    
                    // 按最终匹配度排序（不再单独按价格排序，价格优先级已固化在区间层级中）
                    int scoreComparison = isDesc ? finalScoreB.compareTo(finalScoreA) : finalScoreA.compareTo(finalScoreB);
                    return scoreComparison;
                });
                
                // 手动分页（因为匹配度是在Java层计算的，需要先排序再分页）
                int pageSize = params.getPageSize() != null ? params.getPageSize() : 15;
                int offset = params.getOffset() != null ? params.getOffset() : 0;
                int start = offset;
                int end = Math.min(start + pageSize, products.size());
                
                List<GoldFoilProductDTO> pagedProducts;
                if (start < products.size()) {
                    pagedProducts = new ArrayList<>(products.subList(start, end));
                } else {
                    pagedProducts = new ArrayList<>();
                }
                
                // 更新当前页产品的usage_count（每次被筛选出来就+1）
                if (!pagedProducts.isEmpty()) {
                    List<Long> productIds = pagedProducts.stream()
                        .map(p -> p.getId())
                        .filter(id -> id != null)
                        .collect(java.util.stream.Collectors.toList());
                    
                    if (!productIds.isEmpty()) {
                        try {
                            goldFoilProductMapper.incrementUsageCountBatch(productIds);
                            log.debug("已更新当前页产品的usage_count，产品数量: {}", productIds.size());
                        } catch (Exception e) {
                            log.error("更新usage_count失败", e);
                        }
                    }
                }
                
                products = pagedProducts;
                
                log.debug("已计算匹配度并排序，匹配度规则: {}, 排序方式: {}, 分页: {}-{}, 当前页记录数: {}", 
                         matchScoreRule, (isDesc ? "降序" : "升序"), start, end, products.size());
            }
        } else if ("price".equals(params.getSortBy())) {
            // 按价格排序时，根据匹配度规则类型决定是否需要查询所有数据
            if ("price".equals(matchScoreRule)) {
                // 价格匹配度规则：需要查询所有数据来计算价格匹配度（不受分页影响）
                GoldFoilQueryParams tempParams = new GoldFoilQueryParams();
                // 复制所有查询条件
                tempParams.setPreProcessingStepsOptionId(params.getPreProcessingStepsOptionId());
                tempParams.setProductTypeId(params.getProductTypeId());
                tempParams.setPatternId(params.getPatternId());
                tempParams.setClothPaperTypeId(params.getClothPaperTypeId());
                tempParams.setClothPaperCompatibilityStatus(params.getClothPaperCompatibilityStatus());
                tempParams.setCompanyNumber(params.getCompanyNumber());
                tempParams.setGpNo(params.getGpNo());
                tempParams.setPaizi(params.getPaizi());
                tempParams.setColorNum(params.getColorNum());
                tempParams.setPriceLevel(params.getPriceLevel());
                tempParams.setHotStampingPaperTypes(params.getHotStampingPaperTypes());
                tempParams.setPostProcessingStepId(params.getPostProcessingStepId());
                tempParams.setLaminationMaterialId(params.getLaminationMaterialId());
                tempParams.setPrinting(params.getPrinting());
                tempParams.setUvPrinting(params.getUvPrinting());
                tempParams.setSilkScreenLedUvSparklePowder(params.getSilkScreenLedUvSparklePowder());
                tempParams.setHotStampingTypeOptionId(params.getHotStampingTypeOptionId());
                tempParams.setPatternAreaOptionId(params.getPatternAreaOptionId());
                // 复制烫金类型映射状态标记
                tempParams.setHasDurableMapping(params.getHasDurableMapping());
                tempParams.setHasCommonMapping(params.getHasCommonMapping());
                // 不分页，不排序
                tempParams.setPageSize(null);
                tempParams.setOffset(null);
                tempParams.setSortBy(null);
                
                log.debug("开始执行数据库查询（价格排序+价格匹配度，查询所有数据）...");
                products = goldFoilProductMapper.getProducts(tempParams);
                log.debug("数据库查询完成，返回记录数: {}", products.size());
                
                // 计算新的价格+使用次数匹配度
                MatchScoreCalculator.calculatePriceUsageMatchScores(products);
                
                // 按价格排序，然后分页
                boolean isPriceDesc = "desc".equals(params.getSortOrder());
                products.sort((a, b) -> {
                    Double priceA = a.getPrice() != null ? a.getPrice() : Double.MAX_VALUE;
                    Double priceB = b.getPrice() != null ? b.getPrice() : Double.MAX_VALUE;
                    return isPriceDesc ? priceB.compareTo(priceA) : priceA.compareTo(priceB);
                });
                
                // 手动分页
                int pageSize = params.getPageSize() != null ? params.getPageSize() : 15;
                int offset = params.getOffset() != null ? params.getOffset() : 0;
                int start = offset;
                int end = Math.min(start + pageSize, products.size());
                
                List<GoldFoilProductDTO> pagedProducts;
                if (start < products.size()) {
                    pagedProducts = new ArrayList<>(products.subList(start, end));
                } else {
                    pagedProducts = new ArrayList<>();
                }
                
                // 更新当前页产品的usage_count
                if (!pagedProducts.isEmpty()) {
                    List<Long> productIds = pagedProducts.stream()
                        .map(p -> p.getId())
                        .filter(id -> id != null)
                        .collect(java.util.stream.Collectors.toList());
                    
                    if (!productIds.isEmpty()) {
                        try {
                            goldFoilProductMapper.incrementUsageCountBatch(productIds);
                            log.debug("已更新当前页产品的usage_count，产品数量: {}", productIds.size());
                        } catch (Exception e) {
                            log.error("更新usage_count失败", e);
                        }
                    }
                }
                
                products = pagedProducts;
                
                log.debug("已计算价格匹配度并排序，排序方式: {}, 分页: {}-{}, 当前页记录数: {}", 
                         (isPriceDesc ? "降序" : "升序"), start, end, products.size());
            } else {
                // 标签匹配度规则：优化方案，先使用SQL完成价格排序和分页，然后计算当前页的匹配度
                log.debug("开始执行数据库查询（价格排序+标签匹配度，SQL完成分页）...");
                products = goldFoilProductMapper.getProducts(params);
                log.debug("数据库查询完成，返回记录数: {}", products.size());
                
                // 获取用户匹配偏好并计算匹配度（仅对当前页）
                if (params.getUserId() != null) {
                    Map<String, List<String>> userPreferences = userMatchPreferenceService.getUserPreferencesGrouped(params.getUserId());
                    if (MatchScoreCalculator.hasUserPreferences(userPreferences)) {
                        MatchScoreCalculator.calculateBatchMatchScores(products, userPreferences);
                        
                        // 对当前页进行价格+匹配度二级排序
                        // 注意：由于SQL已经按价格排序，这里主要是对同价位的产品按匹配度排序
                        boolean isPriceDesc = "desc".equals(params.getSortOrder());
                        products.sort((a, b) -> {
                            // 首先按价格排序（保持SQL的排序结果）
                            Double priceA = a.getPrice() != null ? a.getPrice() : Double.MAX_VALUE;
                            Double priceB = b.getPrice() != null ? b.getPrice() : Double.MAX_VALUE;
                            int priceComparison = isPriceDesc ? priceB.compareTo(priceA) : priceA.compareTo(priceB);
                            
                            if (priceComparison != 0) {
                                return priceComparison;
                            }
                            
                            // 价格相同时，按匹配度从高到低排序（降序）
                            int scoreA = a.getMatchScore() != null ? a.getMatchScore() : 0;
                            int scoreB = b.getMatchScore() != null ? b.getMatchScore() : 0;
                            return scoreB - scoreA; // 降序：高匹配度在前
                        });
                        
                        log.debug("已对当前页进行价格+{}，标签匹配度降序排序", (isPriceDesc ? "降序" : "升序"));
                    } else {
                        log.debug("用户未设置匹配偏好，使用SQL排序（无匹配度二级排序）");
                    }
                } else {
                    log.debug("未提供userId，使用SQL排序（无匹配度计算）");
                }
                
                // 更新当前页产品的usage_count
                if (!products.isEmpty()) {
                    List<Long> productIds = products.stream()
                        .map(p -> p.getId())
                        .filter(id -> id != null)
                        .collect(java.util.stream.Collectors.toList());
                    
                    if (!productIds.isEmpty()) {
                        try {
                            goldFoilProductMapper.incrementUsageCountBatch(productIds);
                            log.debug("已更新当前页产品的usage_count，产品数量: {}", productIds.size());
                        } catch (Exception e) {
                            log.error("更新usage_count失败", e);
                        }
                    }
                }
            }
        } else {
            // 其他排序方式，SQL已经完成排序和分页
            log.debug("开始执行数据库查询...");
            products = goldFoilProductMapper.getProducts(params);
            log.debug("数据库查询完成，返回记录数: {}", products.size());
            
            // 根据匹配度规则类型计算匹配度（仅用于显示）
            if ("price".equals(matchScoreRule)) {
                // 价格匹配度规则：需要查询所有数据来计算（不受分页影响）
                GoldFoilQueryParams tempParams = new GoldFoilQueryParams();
                // 复制所有查询条件
                tempParams.setPreProcessingStepsOptionId(params.getPreProcessingStepsOptionId());
                tempParams.setProductTypeId(params.getProductTypeId());
                tempParams.setPatternId(params.getPatternId());
                tempParams.setClothPaperTypeId(params.getClothPaperTypeId());
                tempParams.setClothPaperCompatibilityStatus(params.getClothPaperCompatibilityStatus());
                tempParams.setCompanyNumber(params.getCompanyNumber());
                tempParams.setGpNo(params.getGpNo());
                tempParams.setPaizi(params.getPaizi());
                tempParams.setColorNum(params.getColorNum());
                tempParams.setPriceLevel(params.getPriceLevel());
                tempParams.setHotStampingPaperTypes(params.getHotStampingPaperTypes());
                tempParams.setPostProcessingStepId(params.getPostProcessingStepId());
                tempParams.setLaminationMaterialId(params.getLaminationMaterialId());
                tempParams.setPrinting(params.getPrinting());
                tempParams.setUvPrinting(params.getUvPrinting());
                tempParams.setSilkScreenLedUvSparklePowder(params.getSilkScreenLedUvSparklePowder());
                tempParams.setHotStampingTypeOptionId(params.getHotStampingTypeOptionId());
                tempParams.setPatternAreaOptionId(params.getPatternAreaOptionId());
                // 复制烫金类型映射状态标记
                tempParams.setHasDurableMapping(params.getHasDurableMapping());
                tempParams.setHasCommonMapping(params.getHasCommonMapping());
                // 不分页，不排序
                tempParams.setPageSize(null);
                tempParams.setOffset(null);
                tempParams.setSortBy(null);
                
                List<GoldFoilProductDTO> allProducts = goldFoilProductMapper.getProducts(tempParams);
                MatchScoreCalculator.calculatePriceUsageMatchScores(allProducts);
                
                // 创建产品ID到最终匹配度的映射
                Map<Long, Double> idToFinalScoreMap = new HashMap<>();
                for (GoldFoilProductDTO product : allProducts) {
                    if (product.getId() != null && product.getFinalMatchScore() != null) {
                        idToFinalScoreMap.put(product.getId(), product.getFinalMatchScore());
                    }
                }
                
                // 为当前页的产品设置匹配度
                for (GoldFoilProductDTO product : products) {
                    if (product.getId() != null && idToFinalScoreMap.containsKey(product.getId())) {
                        Double finalScore = idToFinalScoreMap.get(product.getId());
                        product.setFinalMatchScore(finalScore);
                        product.setMatchScore(finalScore != null ? (int) Math.round(finalScore) : 0);
                    }
                }
                
                // 更新当前页产品的usage_count
                if (!products.isEmpty()) {
                    List<Long> productIds = products.stream()
                        .map(p -> p.getId())
                        .filter(id -> id != null)
                        .collect(java.util.stream.Collectors.toList());
                    
                    if (!productIds.isEmpty()) {
                        try {
                            goldFoilProductMapper.incrementUsageCountBatch(productIds);
                            log.debug("已更新当前页产品的usage_count，产品数量: {}", productIds.size());
                        } catch (Exception e) {
                            log.error("更新usage_count失败", e);
                        }
                    }
                }
                
                log.debug("已计算价格匹配度（用于显示），当前页产品数量: {}", products.size());
            } else if ("tag".equals(matchScoreRule) && params.getUserId() != null) {
                // 标签匹配度规则
                Map<String, List<String>> userPreferences = userMatchPreferenceService.getUserPreferencesGrouped(params.getUserId());
                if (MatchScoreCalculator.hasUserPreferences(userPreferences)) {
                    MatchScoreCalculator.calculateBatchMatchScores(products, userPreferences);
                    log.debug("已计算标签匹配度（用于显示），产品数量: {}", products.size());
                } else {
                    log.debug("用户未设置匹配偏好，不计算匹配度");
                }
                
                // 更新当前页产品的usage_count
                if (!products.isEmpty()) {
                    List<Long> productIds = products.stream()
                        .map(p -> p.getId())
                        .filter(id -> id != null)
                        .collect(java.util.stream.Collectors.toList());
                    
                    if (!productIds.isEmpty()) {
                        try {
                            goldFoilProductMapper.incrementUsageCountBatch(productIds);
                            log.debug("已更新当前页产品的usage_count，产品数量: {}", productIds.size());
                        } catch (Exception e) {
                            log.error("更新usage_count失败", e);
                        }
                    }
                }
            } else {
                log.debug("未提供userId或无效的匹配度规则，不计算匹配度");
                
                // 即使不计算匹配度，也要更新usage_count
                if (!products.isEmpty()) {
                    List<Long> productIds = products.stream()
                        .map(p -> p.getId())
                        .filter(id -> id != null)
                        .collect(java.util.stream.Collectors.toList());
                    
                    if (!productIds.isEmpty()) {
                        try {
                            goldFoilProductMapper.incrementUsageCountBatch(productIds);
                            log.debug("已更新当前页产品的usage_count，产品数量: {}", productIds.size());
                        } catch (Exception e) {
                            log.error("更新usage_count失败", e);
                        }
                    }
                }
            }
            
            if ("matchScore".equals(params.getSortBy())) {
                log.warn("警告：按匹配度排序但未提供userId或匹配度规则，无法计算匹配度");
            }
        }
        
        // ========================================
        // 【优化】移除了过胶兼容性的Java层二次筛选
        // 原因：这会导致额外的数据库查询和大量数据传输
        // 解决方案：将过胶兼容性筛选逻辑移到SQL层
        // ========================================
        // 已注释：
        // if (hasLaminationCompatibilityParams(params)) {
        //     products = filterByLaminationCompatibility(products, params);
        // }
        
        // 性能日志
        long duration = System.currentTimeMillis() - startTime;
        log.debug("[性能监控] getProducts 耗时: {}ms, 结果数: {}", duration, products.size());
        if (duration > 1000) {
            log.warn("[性能警告] getProducts 查询超时！耗时: {}ms", duration);
        }
        
        return products;
    }

    /**
     * 获取匹配查询的总记录数（用于分页）
     */
    @Override
    public Long countProducts(GoldFoilQueryParams params) {
        // 如果按匹配度排序，需要先查询所有数据来计算匹配度
        // 因为匹配度是在Java层计算的，无法在SQL中直接count
        // 注意：价格排序时的价格匹配度规则也需要查询所有数据
        String matchScoreRule = params.getMatchScoreRule();
        if (matchScoreRule == null || matchScoreRule.isEmpty()) {
            matchScoreRule = "price"; // 默认使用价格匹配度
        }
        
        if ("matchScore".equals(params.getSortBy()) || 
            ("price".equals(params.getSortBy()) && "price".equals(matchScoreRule))) {
            // 创建临时参数，查询所有数据（不分页，不排序）
            GoldFoilQueryParams tempParams = new GoldFoilQueryParams();
            // 使用反射或手动复制所有查询条件（简化版：直接复用参数，只修改分页和排序）
            // 这里简化处理：直接查询所有数据，然后计算匹配度
            tempParams.setPreProcessingStepsOptionId(params.getPreProcessingStepsOptionId());
            tempParams.setProductTypeId(params.getProductTypeId());
            tempParams.setPatternId(params.getPatternId());
            tempParams.setClothPaperTypeId(params.getClothPaperTypeId());
            tempParams.setClothPaperCompatibilityStatus(params.getClothPaperCompatibilityStatus());
            tempParams.setCompanyNumber(params.getCompanyNumber());
            tempParams.setGpNo(params.getGpNo());
            tempParams.setPaizi(params.getPaizi());
            tempParams.setColorNum(params.getColorNum());
            tempParams.setPriceLevel(params.getPriceLevel());
            tempParams.setHotStampingPaperTypes(params.getHotStampingPaperTypes());
            tempParams.setPostProcessingStepId(params.getPostProcessingStepId());
            tempParams.setLaminationMaterialId(params.getLaminationMaterialId());
            tempParams.setPrinting(params.getPrinting());
            tempParams.setUvPrinting(params.getUvPrinting());
            tempParams.setSilkScreenLedUvSparklePowder(params.getSilkScreenLedUvSparklePowder());
            tempParams.setHotStampingTypeOptionId(params.getHotStampingTypeOptionId());
            tempParams.setPatternAreaOptionId(params.getPatternAreaOptionId());
            tempParams.setUserId(params.getUserId());
            // 复制烫金类型映射状态标记
            tempParams.setHasDurableMapping(params.getHasDurableMapping());
            tempParams.setHasCommonMapping(params.getHasCommonMapping());
            // 不分页，不排序
            tempParams.setPageSize(null);
            tempParams.setOffset(null);
            tempParams.setSortBy(null);
            tempParams.setSortOrder(null);
            
            // 查询所有匹配的产品
            List<GoldFoilProductDTO> allProducts = goldFoilProductMapper.getProducts(tempParams);
            
            // 根据匹配度规则类型计算匹配度
            if ("price".equals(matchScoreRule)) {
                // 价格匹配度规则：直接返回所有产品的数量
                MatchScoreCalculator.calculatePriceBasedMatchScores(allProducts);
                return (long) allProducts.size();
            } else if ("tag".equals(matchScoreRule) && params.getUserId() != null) {
                // 标签匹配度规则：需要检查用户偏好
                Map<String, List<String>> userPreferences = userMatchPreferenceService.getUserPreferencesGrouped(params.getUserId());
                if (MatchScoreCalculator.hasUserPreferences(userPreferences)) {
                    MatchScoreCalculator.calculateBatchMatchScores(allProducts, userPreferences);
                    return (long) allProducts.size();
                } else {
                    // 如果没有偏好，返回0
                    return 0L;
                }
            } else {
                // 无效的匹配度规则或缺少userId
                return 0L;
            }
        }
        
        // 其他情况，使用SQL count（性能更好）
        return goldFoilProductMapper.countProducts(params);
    }

    /**
     * 根据产品类型和烫金图案类型值，转换为实际的烫金纸类型值
     *
     * @param values 产品类型和烫金图案类型值列表
     * @return 烫金纸类型值列表
     */
    protected List<String> applyRules(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        List<String> result = new ArrayList<>();

        if (values.contains("3.1 精平裝/咭書")) {
            result.addAll(Arrays.asList("普通金纸", "普通耐磨", "高耐磨"));
        }
        if (values.contains("3.2 賀咭/紙袋") || values.stream().anyMatch(v -> v.contains("4.2") || v.contains("4.3")
                || v.contains("4.4")|| v.contains("4.6"))) {
            result.addAll(Arrays.asList("普通金纸", "普通耐磨"));
        }
        if (values.contains("3.3 包裝") || values.stream().anyMatch(v -> v.contains("4.11"))) {
            result.add("高耐磨");
        }
        if (values.stream().anyMatch(v ->v.contains("4.5"))){
            result.addAll(Arrays.asList("普通金纸","高耐磨"));
        }
        if (values.stream().anyMatch(v ->v.contains("4.8"))){
            result.addAll(Arrays.asList("普通耐磨","高耐磨"));
        }
        if (values.stream().anyMatch(v -> v.contains("4.7")|| v.contains("4.9") || v.contains("4.10"))) {
            result.add("普通耐磨");
        }
        if (values.stream().anyMatch(v -> v.contains("4.1"))) {
            result.add("普通金纸");
        }

        return result.isEmpty() ? null : new ArrayList<>(result); // 如果结果为空，返回 null，否则返回新的列表
    }
    
    /**
     * 检查是否有兼容性筛选参数
     */
    private boolean hasCompatibilityParams(GoldFoilQueryParams params) {
        return
               (params.getProductType() != null && !params.getProductType().isEmpty() ||
               (params.getPatternCharacteristic() != null && !params.getPatternCharacteristic().isEmpty())
                       ||
                       (params.getHotStampingType() != null && !params.getHotStampingType().isEmpty())
               );
    }

    /**
     * 将「跳过耐磨映射」配置的烫金纸类型合并到基础纸型列表中
     * 语义：这些纸型在 Match 中不受耐磨映射限制，始终参与候选
     */
    private List<String> mergeWithSkipPaperTypes(List<String> baseTypes) {
        List<String> result = new ArrayList<>();
        if (baseTypes != null && !baseTypes.isEmpty()) {
            result.addAll(baseTypes);
        }
        try {
            List<String> skipTypes = wearResistantGoldPaperSkipConfigService.getAllSkipPaperTypes();
            if (skipTypes != null && !skipTypes.isEmpty()) {
                for (String type : skipTypes) {
                    if (type != null && !type.trim().isEmpty() && !result.contains(type)) {
                        result.add(type);
                    }
                }
            }
        } catch (Exception e) {
            // 出现异常时不影响正常匹配逻辑，只记录日志
            log.warn("加载跳过耐磨映射的烫金纸类型失败，将仅使用耐磨映射结果", e);
        }
        return result;
    }
    
    /**
     * 检查是否有过胶兼容性筛选参数
     */
    private boolean hasLaminationCompatibilityParams(GoldFoilQueryParams params) {
        return
               (params.getPostProcessingStepId() != null) ||
               (params.getLaminationMaterialId() != null) ||
               (params.getInterfaceTypeId() != null) ||
               (params.getLaminationCompatibilityStatus() != null);
    }
    
    /**
     * 根据兼容性参数获取兼容的烫金纸类型
     */
    private List<String> getCompatiblePaperTypes(GoldFoilQueryParams params) {
        try {
            // 构建兼容性查询参数
            CompatibilityQueryParams compatibilityParams = new CompatibilityQueryParams();
            compatibilityParams.setPatternCharacteristic(params.getPatternCharacteristic());
            compatibilityParams.setProductType(params.getProductType());
            compatibilityParams.setHotStampingType(params.getHotStampingType());
            // 获取兼容的烫金类型
            List<String> compatibleHotStampingTypes = compatibilityService.getRecommendedHotStampingTypes(compatibilityParams);
            
            // 根据兼容的烫金类型，映射到对应的烫金纸类型
            return compatibleHotStampingTypes;
            
        } catch (Exception e) {
            log.error("兼容性筛选失败，使用原有规则", e);
            return null;
        }
    }
    
    /**
     * 将烫金类型映射到烫金纸类型
     */
    private List<String> mapHotStampingTypesToPaperTypes(List<String> hotStampingTypes, String paperPerformance) {
        List<String> paperTypes = new ArrayList<>();
        
        // 如果指定了烫金纸性能类型，直接使用
        if (paperPerformance != null && !paperPerformance.isEmpty()) {
            // 映射到数据库中的实际值
            switch (paperPerformance) {
                case "普通金紙":
                    paperTypes.add("普通金纸");
                    break;
                case "普通耐磨":
                    paperTypes.add("普通耐磨");
                    break;
                case "高耐磨":
                    paperTypes.add("高耐磨");
                    break;
            }
        } else {
            // 根据兼容的烫金类型推断烫金纸类型
            // 这里可以根据业务规则进行映射
            if (hotStampingTypes != null && !hotStampingTypes.isEmpty()) {
                // 默认返回所有类型，让数据库进一步筛选
                paperTypes.addAll(Arrays.asList("普通金纸", "普通耐磨", "高耐磨"));
            }
        }
        
        return paperTypes;
    }
    
    // ========================================
    // 【已优化移除】以下方法已不再需要
    // ========================================
    // 原因：Java层二次筛选导致：
    // 1. 额外的数据库查询
    // 2. 大量数据在网络传输
    // 3. 内存占用高
    // 
    // 解决方案：将过胶兼容性筛选逻辑移到SQL层
    // ========================================
    
    /* 已移除方法：
     * private List<GoldFoilProductDTO> filterByLaminationCompatibility(...)
     * private String extractFoilSeriesFromProduct(...)
     */

    @Override
    public List<String> getDistinctCompanyNumbers() {
        return goldFoilProductMapper.getDistinctCompanyNumbers();
    }

    @Override
    public List<String> getDistinctGpNumbers() {
        return goldFoilProductMapper.getDistinctGpNumbers();
    }
}
