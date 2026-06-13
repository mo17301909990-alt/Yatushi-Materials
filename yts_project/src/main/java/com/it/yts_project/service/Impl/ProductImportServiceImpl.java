package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.ImportResult;
import com.it.yts_project.dto.ImportConfig;
import com.it.yts_project.model.CompleteProductInfo;
import com.it.yts_project.model.Product;
import com.it.yts_project.service.CompleteProductInfoService;
import com.it.yts_project.service.ProductImportService;
import com.it.yts_project.service.ProductService;
import com.it.yts_project.util.ExcelImportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * 产品导入服务实现类
 */
@Slf4j
@Service
public class ProductImportServiceImpl implements ProductImportService {
    
    @Autowired
    private CompleteProductInfoService completeProductInfoService;
    
    @Autowired
    private ProductService productService;
    
    @Override
    public ImportResult<CompleteProductInfo> importCompleteProductInfo(MultipartFile file, ImportConfig config) {
        log.info("开始导入完整产品信息，配置: {}", config);
        
        try {
            // 使用智能导入模式（自动判断新增/更新）
            return ExcelImportUtil.importExcelSmart(
                file,
                CompleteProductInfo.class,
                this::processCompleteProductInfoData
            );
            
        } catch (Exception e) {
            log.error("导入完整产品信息失败", e);
            return ImportResult.failure("导入失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public ImportResult<Product> importBasicProductInfo(MultipartFile file, ImportConfig config) {
        log.info("开始导入基础产品信息，配置: {}", config);
        
        try {
            // 冲突检查函数：检查产品是否已存在
            Function<Product, Boolean> conflictChecker = (data) -> {
                // 根据型号或物料编号检查是否存在
                if (data.getModelNumber() != null && !data.getModelNumber().trim().isEmpty()) {
                    // 这里需要根据实际的产品服务方法调整
                    return false; // 暂时返回false，需要根据实际业务逻辑调整
                }
                return false;
            };
            
            // 数据处理函数：保存或更新数据
            return ExcelImportUtil.importExcelWithConflictHandling(
                file,
                Product.class,
                config,
                this::processBasicProductInfoData,
                conflictChecker
            );
            
        } catch (Exception e) {
            log.error("导入基础产品信息失败", e);
            return ImportResult.failure("导入失败: " + e.getMessage());
        }
    }
    
    /**
     * 处理完整产品信息数据（智能导入模式）
     * 
     * @param dataList 数据列表
     * @return 导入结果统计
     */
    private ImportResult<CompleteProductInfo> processCompleteProductInfoData(List<CompleteProductInfo> dataList) {
        log.info("开始处理完整产品信息数据（智能模式），数量: {}", dataList.size());
        
        int createdCount = 0;
        int updatedCount = 0;
        int skippedCount = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        
        for (CompleteProductInfo data : dataList) {
            // 为每个记录使用单独的事务处理
            try {
                String operation = processSingleRecord(data);
                if ("新增".equals(operation)) {
                    createdCount++;
                    successMessages.add(String.format("新增成功: 型号=%s, 物料号=%s", 
                        data.getModelNumber(), data.getMaterialNumber()));
                } else if ("更新".equals(operation)) {
                    updatedCount++;
                    successMessages.add(String.format("更新成功: 型号=%s, 物料号=%s", 
                        data.getModelNumber(), data.getMaterialNumber()));
                }
                log.debug("成功处理产品信息: 型号={}, 物料号={}, 操作={}", 
                    data.getModelNumber(), data.getMaterialNumber(), operation);
            } catch (Exception e) {
                skippedCount++;
                String errorMsg = String.format("处理失败: 型号=%s, 物料号=%s, 错误=%s", 
                    data.getModelNumber(), data.getMaterialNumber(), e.getMessage());
                errorMessages.add(errorMsg);
                log.error("处理产品信息数据失败: ID={}, 物料号={}, 错误={}", 
                    data.getId(), data.getMaterialNumber(), e.getMessage(), e);
                // 继续处理下一条记录，不中断整个导入过程
                if (e.getMessage() != null && e.getMessage().contains("violates not-null constraint")) {
                    log.error("数据库约束违反: 请检查必填字段是否为空, 数据={}", data);
                }
            }
        }
        
        log.info("完整产品信息数据处理完成 - 新增: {}, 更新: {}, 失败: {}", 
            createdCount, updatedCount, skippedCount);
        
        // 构建详细的结果消息
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append(String.format("导入完成！总计 %d 条记录\n", dataList.size()));
        resultMessage.append(String.format("✅ 新增: %d 条\n", createdCount));
        resultMessage.append(String.format("🔄 更新: %d 条\n", updatedCount));
        resultMessage.append(String.format("❌ 失败: %d 条\n", skippedCount));
        
        if (!errorMessages.isEmpty()) {
            resultMessage.append("\n失败详情:\n");
            for (String error : errorMessages) {
                resultMessage.append("• ").append(error).append("\n");
            }
        }
        
        return ImportResult.<CompleteProductInfo>builder()
            .success(skippedCount == 0)
            .message(resultMessage.toString())
            .totalCount(dataList.size())
            .createdCount(createdCount)
            .updatedCount(updatedCount)
            .skippedCount(skippedCount)
            .build();
    }
    
    /**
     * 处理单个记录（使用单独事务）
     * 
     * @param data 产品数据
     * @return 操作类型（"新增" 或 "更新"）
     */
    @Transactional(rollbackFor = Exception.class)
    public String processSingleRecord(CompleteProductInfo data) {
        // 记录Excel中读取到的原始数据
        log.info("处理导入记录: 产品ID={}, 物料号={}, 型号={}, Excel中的价格={}", 
            data.getId(), data.getMaterialNumber(), data.getModelNumber(), data.getPrice());
        
        boolean isUpdate = false;
        CompleteProductInfo existingProduct = null;
        
        // 智能判断逻辑：ID优先，物料号兜底
        if (data.getId() != null && data.getId() > 0) {
            // 有ID，检查ID是否存在
            existingProduct = completeProductInfoService.findCompleteProductInfoById(data.getId());
            if (existingProduct != null) {
                isUpdate = true;
                log.debug("找到现有产品（按ID）: ID={}", data.getId());
            } else {
                log.debug("ID不存在，将按新增处理: ID={}", data.getId());
            }
        }
        
        // 如果ID不存在或为空，检查物料号（精确匹配）
        if (!isUpdate && data.getMaterialNumber() != null && !data.getMaterialNumber().trim().isEmpty()) {
            String materialNumber = data.getMaterialNumber().trim();
            List<CompleteProductInfo> existingByMaterial = completeProductInfoService.findCompleteProductInfoByMaterialNumber(materialNumber);
            if (!existingByMaterial.isEmpty()) {
                // SQL已经使用精确匹配，直接取第一个结果
                existingProduct = existingByMaterial.get(0);
                isUpdate = true;
                log.debug("找到现有产品（按物料号精确匹配）: 物料号={}, 产品ID={}, 当前价格={}", 
                    materialNumber, existingProduct.getId(), existingProduct.getPrice());
            } else {
                log.debug("物料号不存在（精确匹配）: 物料号={}, 将按新增处理", materialNumber);
            }
        }
        
        if (isUpdate && existingProduct != null) {
            // 部分更新模式：只更新Excel中提供的字段
            updateProductPartially(existingProduct, data);
            log.debug("部分更新产品信息: ID={}, 物料号={}", existingProduct.getId(), existingProduct.getMaterialNumber());
            return "更新";
        } else {
            // 新增模式
            // 数据验证：检查必填字段
            if (data.getModelNumber() == null || data.getModelNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("型号不能为空");
            }
            if (data.getMaterialNumber() == null || data.getMaterialNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("物料号不能为空");
            }
            
            // 清除ID，让系统自动生成
            data.setId(null);
            completeProductInfoService.saveCompleteProductInfo(data);
            log.debug("新增产品信息: 型号={}, 物料号={}", data.getModelNumber(), data.getMaterialNumber());
            return "新增";
        }
    }
    
    /**
     * 部分更新产品信息（只更新Excel中提供的字段）
     * 
     * @param existingProduct 现有产品
     * @param newData 新数据
     */
    private void updateProductPartially(CompleteProductInfo existingProduct, CompleteProductInfo newData) {
        try {
            // 获取Excel中存在的列名
            Set<String> existingColumns = com.it.yts_project.util.ExcelImportUtil.getExistingColumns();
            if (existingColumns == null || existingColumns.isEmpty()) {
                // 如果获取不到表头信息，从CompleteProductInfo类中获取所有可能的列名（兼容模式）
                existingColumns = com.it.yts_project.util.ExcelImportUtil.getAllExcelPropertyValues(CompleteProductInfo.class);
                log.warn("无法获取Excel表头信息，将使用兼容模式：假设所有列都存在，共{}列", existingColumns.size());
            }
            log.info("Excel中存在的列: {}, 价格列是否存在: {}", existingColumns, existingColumns.contains("價格"));
            
            // 统一逻辑：如果Excel中列存在，则更新（有值更新，无值清空）；如果列不存在，则保持原值
            
            // 燙金紙系列
            if (existingColumns.contains("燙金紙系列")) {
                if (newData.getName() != null && !newData.getName().trim().isEmpty()) {
                    existingProduct.setName(newData.getName().trim());
                } else {
                    existingProduct.setName(null);
                }
            }
            
            // 型号（必填字段，如果列存在且不为空则更新）
            if (existingColumns.contains("型號")) {
                if (newData.getModelNumber() != null && !newData.getModelNumber().trim().isEmpty()) {
                    existingProduct.setModelNumber(newData.getModelNumber().trim());
                }
            }
            // 物料号（必填字段，如果列存在且不为空则更新）
            if (existingColumns.contains("物料編號")) {
                if (newData.getMaterialNumber() != null && !newData.getMaterialNumber().trim().isEmpty()) {
                    existingProduct.setMaterialNumber(newData.getMaterialNumber().trim());
                }
            }
            
            // 烫金纸张类型
            if (existingColumns.contains("燙金紙性能類型(請注意核對)")) {
                if (newData.getHotStampingPaperType() != null && !newData.getHotStampingPaperType().trim().isEmpty()) {
                    existingProduct.setHotStampingPaperType(newData.getHotStampingPaperType().trim());
                } else {
                    existingProduct.setHotStampingPaperType(null);
                }
            }
            
            // Leo Touch编号
            if (existingColumns.contains("Leo Touch編號")) {
                if (newData.getCompanyNumber() != null && !newData.getCompanyNumber().trim().isEmpty()) {
                    existingProduct.setCompanyNumber(newData.getCompanyNumber().trim());
                } else {
                    existingProduct.setCompanyNumber(null);
                }
            }
            
            // SPNO
            if (existingColumns.contains("SPNO")) {
                if (newData.getGpNo() != null && !newData.getGpNo().trim().isEmpty()) {
                    existingProduct.setGpNo(newData.getGpNo().trim());
                } else {
                    existingProduct.setGpNo(null);
                }
            }
            
            // 颜色
            if (existingColumns.contains("顏色")) {
                if (newData.getColor() != null && !newData.getColor().trim().isEmpty()) {
                    existingProduct.setColor(newData.getColor().trim());
                } else {
                    existingProduct.setColor(null);
                }
            }
            
            // 颜色编码
            if (existingColumns.contains("顏色編碼")) {
                if (newData.getColorNum() != null && !newData.getColorNum().trim().isEmpty()) {
                    existingProduct.setColorNum(newData.getColorNum().trim());
                } else {
                    existingProduct.setColorNum(null);
                }
            }
            
            // 金纸松紧度
            if (existingColumns.contains("金紙鬆緊度")) {
                if (newData.getTightness() != null && !newData.getTightness().trim().isEmpty()) {
                    existingProduct.setTightness(newData.getTightness().trim());
                } else {
                    existingProduct.setTightness(null);
                }
            }
            
            // 金纸性能
            if (existingColumns.contains("金紙性能")) {
                if (newData.getPerformance() != null && !newData.getPerformance().trim().isEmpty()) {
                    existingProduct.setPerformance(newData.getPerformance().trim());
                } else {
                    existingProduct.setPerformance(null);
                }
            }
            
            // 尺寸
            if (existingColumns.contains("尺寸")) {
                if (newData.getSize() != null && !newData.getSize().trim().isEmpty()) {
                    existingProduct.setSize(newData.getSize().trim());
                } else {
                    existingProduct.setSize(null);
                }
            }
            
            // 温度范围
            if (existingColumns.contains("溫度範圍")) {
                if (newData.getTemperatureRange() != null && !newData.getTemperatureRange().trim().isEmpty()) {
                    existingProduct.setTemperatureRange(newData.getTemperatureRange().trim());
                } else {
                    existingProduct.setTemperatureRange(null);
                }
            }
            
            // 产品描述
            if (existingColumns.contains("說明")) {
                if (newData.getDescription() != null && !newData.getDescription().trim().isEmpty()) {
                    existingProduct.setDescription(newData.getDescription().trim());
                } else {
                    existingProduct.setDescription(null);
                }
            }
            
            // 价格字段：如果Excel中存在"價格"列，则更新（有值更新，无值清空）
            if (existingColumns.contains("價格")) {
                Double oldPrice = existingProduct.getPrice();
                Double newPrice = newData.getPrice();
                log.info("价格列存在，准备更新: 产品ID={}, 物料号={}, Excel中的价格={}, 数据库中的旧价格={}", 
                    existingProduct.getId(), existingProduct.getMaterialNumber(), newPrice, oldPrice);
                if (newPrice != null) {
                    log.info("更新价格: 产品ID={}, 物料号={}, 旧价格={}, 新价格={}", 
                        existingProduct.getId(), existingProduct.getMaterialNumber(), oldPrice, newPrice);
                    existingProduct.setPrice(newPrice);
                } else {
                    log.info("清空价格: 产品ID={}, 物料号={}, 旧价格={}, 新价格=null", 
                        existingProduct.getId(), existingProduct.getMaterialNumber(), oldPrice);
                    existingProduct.setPrice(null);
                }
            } else {
                log.warn("跳过价格更新: Excel中不存在'價格'列，保持原价格={}, 产品ID={}, 物料号={}", 
                    existingProduct.getPrice(), existingProduct.getId(), existingProduct.getMaterialNumber());
            }
            
            // 牌子
            if (existingColumns.contains("牌子")) {
                if (newData.getPaizi() != null && !newData.getPaizi().trim().isEmpty()) {
                    existingProduct.setPaizi(newData.getPaizi().trim());
                } else {
                    existingProduct.setPaizi(null);
                }
            }
            
            // 封度
            if (existingColumns.contains("封度")) {
                if (newData.getFengdu() != null && !newData.getFengdu().trim().isEmpty()) {
                    existingProduct.setFengdu(newData.getFengdu().trim());
                } else {
                    existingProduct.setFengdu(null);
                }
            }
            
            // 单位
            if (existingColumns.contains("計量單位")) {
                if (newData.getDanwei() != null && !newData.getDanwei().trim().isEmpty()) {
                    existingProduct.setDanwei(newData.getDanwei().trim());
                } else {
                    existingProduct.setDanwei(null);
                }
            }
            
            // 是否常用
            if (existingColumns.contains("常用與否")) {
                if (newData.getIsChangyong() != null && !newData.getIsChangyong().trim().isEmpty()) {
                    existingProduct.setIsChangyong(newData.getIsChangyong().trim());
                } else {
                    existingProduct.setIsChangyong(null);
                }
            }
            
            // 是否街货（Boolean类型）
            if (existingColumns.contains("是否街貨")) {
                existingProduct.setIsJiehuo(newData.getIsJiehuo());
            }
            
            // 概述
            if (existingColumns.contains("概述")) {
                if (newData.getGaishu() != null && !newData.getGaishu().trim().isEmpty()) {
                    existingProduct.setGaishu(newData.getGaishu().trim());
                } else {
                    existingProduct.setGaishu(null);
                }
            }
            
            // 物料状态
            if (existingColumns.contains("物料狀態")) {
                if (newData.getStatus() != null && !newData.getStatus().trim().isEmpty()) {
                    existingProduct.setStatus(newData.getStatus().trim());
                } else {
                    existingProduct.setStatus(null);
                }
            }
            
            // 保存更新后的数据
            log.info("准备保存更新: 产品ID={}, 物料号={}, 价格字段值={}", 
                existingProduct.getId(), existingProduct.getMaterialNumber(), existingProduct.getPrice());
            completeProductInfoService.updateCompleteProductInfo(existingProduct.getId(), existingProduct);
            log.info("保存更新完成: 产品ID={}, 物料号={}", 
                existingProduct.getId(), existingProduct.getMaterialNumber());
            
        } catch (Exception e) {
            log.error("部分更新产品信息失败: ID={}, 错误={}", existingProduct.getId(), e.getMessage(), e);
            throw new RuntimeException("部分更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 处理基础产品信息数据
     * 
     * @param dataList 数据列表
     */
    private void processBasicProductInfoData(List<Product> dataList) {
        log.info("开始处理基础产品信息数据，数量: {}", dataList.size());
        
        for (Product data : dataList) {
            try {
                // 根据配置决定是保存还是更新
                if (data.getId() != null && data.getId() > 0) {
                    // 更新现有数据 - 需要根据实际的ProductService方法调整
                    // productService.updateProduct(data.getId(), data);
                    log.debug("更新基础产品信息: ID={}", data.getId());
                } else {
                    // 保存新数据 - 需要根据实际的ProductService方法调整
                    // productService.saveProduct(data);
                    log.debug("保存新基础产品信息: 型号={}", data.getModelNumber());
                }
            } catch (Exception e) {
                log.error("处理基础产品信息数据失败: {}", e.getMessage(), e);
                throw new RuntimeException("数据处理失败: " + e.getMessage());
            }
        }
        
        log.info("基础产品信息数据处理完成，处理数量: {}", dataList.size());
    }
}
