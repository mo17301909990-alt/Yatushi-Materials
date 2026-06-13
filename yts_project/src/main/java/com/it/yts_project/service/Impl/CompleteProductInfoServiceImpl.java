package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.CompleteProductInfoMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.mapper.SpecificationMapper;
import com.it.yts_project.mapper.PricingMapper;
import com.it.yts_project.mapper.LeoGpNumberMapper;
import com.it.yts_project.model.CompleteProductInfo;
import com.it.yts_project.model.Product;
import com.it.yts_project.model.Specification;
import com.it.yts_project.model.Pricing;
import com.it.yts_project.model.LeoGpNumber;
import com.it.yts_project.service.CompleteProductInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 完整产品信息服务实现类
 */
@Service
@Transactional
public class CompleteProductInfoServiceImpl implements CompleteProductInfoService {
    
    private static final Logger log = LoggerFactory.getLogger(CompleteProductInfoServiceImpl.class);
    
    @Autowired
    private CompleteProductInfoMapper completeProductInfoMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private SpecificationMapper specificationMapper;
    
    @Autowired
    private PricingMapper pricingMapper;
    
    @Autowired
    private LeoGpNumberMapper leoGpNumberMapper;
    
    @Override
    public List<CompleteProductInfo> findAllCompleteProductInfo() {
        return completeProductInfoMapper.findAllCompleteProductInfo();
    }
    
    @Override
    public CompleteProductInfo findCompleteProductInfoById(Integer id) {
        return completeProductInfoMapper.findCompleteProductInfoById(id);
    }
    
    @Override
    public List<CompleteProductInfo> findCompleteProductInfoByMaterialNumber(String materialNumber) {
        return completeProductInfoMapper.findCompleteProductInfoByMaterialNumber(materialNumber);
    }
    
    @Override
    public List<CompleteProductInfo> findCompleteProductInfoByModelNumber(String modelNumber) {
        return completeProductInfoMapper.findCompleteProductInfoByModelNumber(modelNumber);
    }
    
    @Override
    public List<CompleteProductInfo> findCompleteProductInfoByCompanyNumber(String companyNumber) {
        return completeProductInfoMapper.findCompleteProductInfoByCompanyNumber(companyNumber);
    }
    
    @Override
    public List<CompleteProductInfo> findCompleteProductInfoByGpNo(String gpNo) {
        return completeProductInfoMapper.findCompleteProductInfoByGpNo(gpNo);
    }
    
    @Override
    public List<CompleteProductInfo> findCompleteProductInfoByHotStampingPaperType(String hotStampingPaperType) {
        return completeProductInfoMapper.findCompleteProductInfoByHotStampingPaperType(hotStampingPaperType);
    }
    
    @Override
    public CompleteProductInfo saveCompleteProductInfo(CompleteProductInfo completeProductInfo) {
        // 1. 保存产品基本信息
        Product product = new Product();
        product.setId(completeProductInfo.getId());
        product.setName(completeProductInfo.getName());
        product.setModelNumber(completeProductInfo.getModelNumber());
        product.setMaterialNumber(completeProductInfo.getMaterialNumber());
        product.setHotStampingPaperType(completeProductInfo.getHotStampingPaperType());
        product.setDescirption(completeProductInfo.getDescription());
        product.setPaizi(completeProductInfo.getPaizi());
        product.setFengdu(completeProductInfo.getFengdu());
        product.setDanwei(completeProductInfo.getDanwei());
        product.setIsChangyong(completeProductInfo.getIsChangyong());
        product.setIsJiehuo(completeProductInfo.getIsJiehuo());
        product.setGaishu(completeProductInfo.getGaishu());
        product.setStatus(completeProductInfo.getStatus() != null ? completeProductInfo.getStatus() : "可用");
        
        if (product.getId() == null) {
            productMapper.insert(product);
            completeProductInfo.setId(product.getId());
        } else {
            productMapper.update(product);
        }
        
        // 2. 保存规格信息
        if (completeProductInfo.getColor() != null || completeProductInfo.getSize() != null || 
            completeProductInfo.getTightness() != null || completeProductInfo.getPerformance() != null ||
            completeProductInfo.getTemperatureRange() != null) {
            
            Specification specification = new Specification();
            specification.setProjectId(product.getId());
            specification.setColor(completeProductInfo.getColor());
            specification.setSize(completeProductInfo.getSize());
            specification.setTightness(completeProductInfo.getTightness());
            specification.setPerformance(completeProductInfo.getPerformance());
            specification.setTemperatureRange(completeProductInfo.getTemperatureRange());
            
            // 检查是否已存在规格记录
            List<Specification> existingSpecs = specificationMapper.findByProjectId(product.getId());
            if (existingSpecs.isEmpty()) {
                try {
                    specificationMapper.insert(specification);
                } catch (Exception e) {
                    // 如果插入失败（可能是主键冲突），尝试更新现有记录
                    if (e.getMessage().contains("duplicate key") || e.getMessage().contains("违反唯一约束")) {
                        // 查找可能存在的记录并更新
                        List<Specification> allSpecs = specificationMapper.findByProjectId(product.getId());
                        if (!allSpecs.isEmpty()) {
                            specification.setId(allSpecs.get(0).getId());
                            specificationMapper.update(specification);
                        }
                    } else {
                        throw e;
                    }
                }
            } else {
                specification.setId(existingSpecs.get(0).getId());
                specificationMapper.update(specification);
            }
        }
        
        // 3. 保存价格信息（无论是否为null都要处理）
        // 检查是否已存在价格记录
        List<Pricing> existingPricing = pricingMapper.findByProjectId(product.getId());
        
        if (completeProductInfo.getPrice() != null) {
            // Excel中有价格值，更新或插入
            Pricing pricing = new Pricing();
            pricing.setProjectId(product.getId());
            pricing.setPrice(BigDecimal.valueOf(completeProductInfo.getPrice()));
            
            if (existingPricing.isEmpty()) {
                try {
                    pricingMapper.insert(pricing);
                } catch (Exception e) {
                    // 如果插入失败（可能是主键冲突），尝试更新现有记录
                    if (e.getMessage().contains("duplicate key") || e.getMessage().contains("违反唯一约束")) {
                        // 查找可能存在的记录并更新
                        List<Pricing> allPricing = pricingMapper.findByProjectId(product.getId());
                        if (!allPricing.isEmpty()) {
                            pricing.setId(allPricing.get(0).getId());
                            pricingMapper.update(pricing);
                        }
                    } else {
                        throw e;
                    }
                }
            } else {
                // 更新现有的价格记录
                pricing.setId(existingPricing.get(0).getId());
                pricingMapper.update(pricing);
            }
        } else {
            // Excel中价格为null，清空数据库中的价格（更新为null）
            if (!existingPricing.isEmpty()) {
                // 更新现有记录的价格为null
                Pricing pricing = existingPricing.get(0);
                pricing.setPrice(null);
                pricingMapper.update(pricing);
            } else {
            }
            // 如果不存在价格记录，不需要创建null记录
        }
        
        // 4. 保存Leo Touch信息
        if (completeProductInfo.getCompanyNumber() != null || completeProductInfo.getGpNo() != null) {
            LeoGpNumber leoGpNumber = new LeoGpNumber();
            leoGpNumber.setProject_id(product.getId());
            leoGpNumber.setCompany_number(completeProductInfo.getCompanyNumber());
            leoGpNumber.setGp_no(completeProductInfo.getGpNo());
            
            // 检查是否已存在Leo Touch记录
            List<LeoGpNumber> existingLeoGpNumbers = leoGpNumberMapper.findByProjectId(product.getId());
            if (existingLeoGpNumbers.isEmpty()) {
                leoGpNumberMapper.insert(leoGpNumber);
            } else {
                leoGpNumber.setId(existingLeoGpNumbers.get(0).getId());
                leoGpNumberMapper.update(leoGpNumber);
            }
        }
        
        return completeProductInfo;
    }
    
    @Override
    public CompleteProductInfo updateCompleteProductInfo(Integer id, CompleteProductInfo completeProductInfo) {
        completeProductInfo.setId(id);
        return saveCompleteProductInfo(completeProductInfo);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public java.util.Map<String, Object> batchUpdateProducts(List<Integer> ids, java.util.Map<String, Object> updateFields) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        if (ids == null || ids.isEmpty() || updateFields == null || updateFields.isEmpty()) {
            result.put("successCount", 0);
            result.put("failCount", 0);
            result.put("errorMessages", new java.util.ArrayList<>());
            result.put("totalCount", 0);
            return result;
        }
        
        int successCount = 0;
        int failCount = 0;
        java.util.List<String> errorMessages = new java.util.ArrayList<>();
        
        // 为每个记录创建独立事务（使用REQUIRES_NEW），允许部分成功
        for (Integer id : ids) {
            try {
                updateSingleProduct(id, updateFields);
                successCount++;
            } catch (Exception e) {
                failCount++;
                String errorMsg = String.format("ID %d: %s", id, e.getMessage());
                errorMessages.add(errorMsg);
                log.error("批量更新产品失败，ID: {}", id, e);
                // 继续处理下一条，不中断
            }
        }
        
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errorMessages", errorMessages);
        result.put("totalCount", ids.size());
        return result;
    }
    
    /**
     * 更新单个产品（独立事务）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    protected void updateSingleProduct(Integer id, java.util.Map<String, Object> updateFields) {
        // 获取现有产品信息
        CompleteProductInfo existingInfo = findCompleteProductInfoById(id);
        if (existingInfo == null) {
            throw new RuntimeException("产品ID " + id + " 不存在");
        }
        
        // 只更新提供的字段
        if (updateFields.containsKey("name")) {
            existingInfo.setName((String) updateFields.get("name"));
        }
        if (updateFields.containsKey("hotStampingPaperType")) {
            existingInfo.setHotStampingPaperType((String) updateFields.get("hotStampingPaperType"));
        }
        if (updateFields.containsKey("status")) {
            existingInfo.setStatus((String) updateFields.get("status"));
        }
        if (updateFields.containsKey("paizi")) {
            existingInfo.setPaizi((String) updateFields.get("paizi"));
        }
        if (updateFields.containsKey("fengdu")) {
            existingInfo.setFengdu((String) updateFields.get("fengdu"));
        }
        if (updateFields.containsKey("danwei")) {
            existingInfo.setDanwei((String) updateFields.get("danwei"));
        }
        if (updateFields.containsKey("isChangyong")) {
            existingInfo.setIsChangyong((String) updateFields.get("isChangyong"));
        }
        if (updateFields.containsKey("isJiehuo")) {
            existingInfo.setIsJiehuo((Boolean) updateFields.get("isJiehuo"));
        }
        if (updateFields.containsKey("companyNumber")) {
            existingInfo.setCompanyNumber((String) updateFields.get("companyNumber"));
        }
        if (updateFields.containsKey("gpNo")) {
            existingInfo.setGpNo((String) updateFields.get("gpNo"));
        }
        if (updateFields.containsKey("color")) {
            existingInfo.setColor((String) updateFields.get("color"));
        }
        if (updateFields.containsKey("size")) {
            existingInfo.setSize((String) updateFields.get("size"));
        }
        if (updateFields.containsKey("tightness")) {
            existingInfo.setTightness((String) updateFields.get("tightness"));
        }
        if (updateFields.containsKey("performance")) {
            existingInfo.setPerformance((String) updateFields.get("performance"));
        }
        if (updateFields.containsKey("temperatureRange")) {
            existingInfo.setTemperatureRange((String) updateFields.get("temperatureRange"));
        }
        if (updateFields.containsKey("price")) {
            Object priceObj = updateFields.get("price");
            if (priceObj instanceof Number) {
                existingInfo.setPrice(((Number) priceObj).doubleValue());
            }
        }
        if (updateFields.containsKey("description")) {
            existingInfo.setDescription((String) updateFields.get("description"));
        }
        if (updateFields.containsKey("gaishu")) {
            existingInfo.setGaishu((String) updateFields.get("gaishu"));
        }
        
        // 保存更新后的信息
        saveCompleteProductInfo(existingInfo);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public java.util.Map<String, Object> batchDeleteProducts(List<Integer> ids) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        if (ids == null || ids.isEmpty()) {
            result.put("successCount", 0);
            result.put("failCount", 0);
            result.put("errorMessages", new java.util.ArrayList<>());
            result.put("totalCount", 0);
            return result;
        }
        
        int successCount = 0;
        int failCount = 0;
        java.util.List<String> errorMessages = new java.util.ArrayList<>();
        
        // 为每个记录创建独立事务（使用REQUIRES_NEW），允许部分成功
        for (Integer id : ids) {
            try {
                deleteSingleProduct(id);
                successCount++;
            } catch (Exception e) {
                failCount++;
                String errorMsg = String.format("ID %d: %s", id, e.getMessage());
                errorMessages.add(errorMsg);
                log.error("批量删除产品失败，ID: {}", id, e);
                // 继续处理下一条，不中断
            }
        }
        
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errorMessages", errorMessages);
        result.put("totalCount", ids.size());
        return result;
    }
    
    /**
     * 删除单个产品（独立事务）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    protected void deleteSingleProduct(Integer id) {
        // 删除相关的规格、价格和Leo Touch信息
        specificationMapper.deleteByProjectId(id);
        pricingMapper.deleteByProjectId(id);
        leoGpNumberMapper.deleteByProjectId(id);
        // 删除产品
        productMapper.deleteById(id);
    }
}
