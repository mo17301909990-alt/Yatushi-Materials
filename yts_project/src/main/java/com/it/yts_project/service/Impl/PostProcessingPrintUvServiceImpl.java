package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.LeoGpNumberMapper;
import com.it.yts_project.mapper.PostProcessingPrintUvMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.model.LeoGpNumber;
import com.it.yts_project.model.PostProcessingPrintUv;
import com.it.yts_project.model.Product;
import com.it.yts_project.service.PostProcessingPrintUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 印刷UV后加工服务实现类
 */
@Service
public class PostProcessingPrintUvServiceImpl implements PostProcessingPrintUvService {
    
    @Autowired
    private PostProcessingPrintUvMapper mapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private LeoGpNumberMapper leoGpNumberMapper;
    @Override
    public List<PostProcessingPrintUv> getAllUvPrintConfigs() {
        return mapper.findAll();
    }
    
    @Override
    public List<PostProcessingPrintUv> getUvPrintConfigsWithPagination(int page, int size) {
        int offset = (page - 1) * size;
        return mapper.findWithPagination(offset, size);
    }
    
    @Override
    public int getTotalCount() {
        return mapper.getTotalCount();
    }
    
    @Override
    public PostProcessingPrintUv getUvPrintConfigById(Integer id) {
        return mapper.findById(id);
    }
    
    @Override
    public PostProcessingPrintUv createUvPrintConfig(PostProcessingPrintUv postProcessingPrintUv) {
        LocalDateTime now = LocalDateTime.now();
        postProcessingPrintUv.setCreatedAt(now);
        postProcessingPrintUv.setUpdatedAt(now);
        
        // 根据产品型号和名称查找对应的公司编号或客户GP号
        List<Product> byModelNumberAndName = productMapper.findByModelNumberAndName(
                postProcessingPrintUv.getProductModelNumber(),
                postProcessingPrintUv.getProductName());
        if (byModelNumberAndName.size() > 0) {
            List<LeoGpNumber> byProjectId = leoGpNumberMapper.findByProjectId(byModelNumberAndName.get(0).getId());
            if (!byProjectId.isEmpty()) {
                LeoGpNumber leo = byProjectId.get(0);
                // 优先使用公司编号，其次使用客户GP号
                if (leo.getCompany_number() != null && !leo.getCompany_number().trim().isEmpty()) {
                    postProcessingPrintUv.setCompanyNumber(leo.getCompany_number());
                } else if (leo.getGp_no() != null && !leo.getGp_no().trim().isEmpty()) {
                    postProcessingPrintUv.setCompanyNumber(leo.getGp_no());
                }
            }
        }
        
        // 如果公司编号仍然为空，设置为 null
        if (postProcessingPrintUv.getCompanyNumber() == null || postProcessingPrintUv.getCompanyNumber().trim().isEmpty()) {
            postProcessingPrintUv.setCompanyNumber(null);
        }
        
        mapper.insert(postProcessingPrintUv);
        return postProcessingPrintUv;
    }
    
    @Override
    public PostProcessingPrintUv updateUvPrintConfig(PostProcessingPrintUv postProcessingPrintUv) {
        postProcessingPrintUv.setUpdatedAt(LocalDateTime.now());
        
        // 重要：如果 companyNumber 已经设置（从 existing 记录中获取），则保持原值
        // 只有在 companyNumber 为 null 时才尝试自动获取（公司编号或客户GP号）
        if (postProcessingPrintUv.getCompanyNumber() == null || postProcessingPrintUv.getCompanyNumber().trim().isEmpty()) {
            // 根据产品型号和名称查找对应的公司编号或客户GP号
            List<Product> byModelNumberAndName = productMapper.findByModelNumberAndName(
                    postProcessingPrintUv.getProductModelNumber(),
                    postProcessingPrintUv.getProductName());
            if (byModelNumberAndName.size() > 0) {
                List<LeoGpNumber> byProjectId = leoGpNumberMapper.findByProjectId(byModelNumberAndName.get(0).getId());
                if (!byProjectId.isEmpty()) {
                    LeoGpNumber leo = byProjectId.get(0);
                    if (leo.getCompany_number() != null && !leo.getCompany_number().trim().isEmpty()) {
                        postProcessingPrintUv.setCompanyNumber(leo.getCompany_number());
                    } else if (leo.getGp_no() != null && !leo.getGp_no().trim().isEmpty()) {
                        postProcessingPrintUv.setCompanyNumber(leo.getGp_no());
                    }
                }
            }
            
            // 如果公司编号仍然为空，设置为 null
            if (postProcessingPrintUv.getCompanyNumber() == null || postProcessingPrintUv.getCompanyNumber().trim().isEmpty()) {
                postProcessingPrintUv.setCompanyNumber(null);
            }
        }
        
        mapper.update(postProcessingPrintUv);
        return postProcessingPrintUv;
    }
    
    @Override
    public void deleteUvPrintConfig(Integer id) {
        mapper.deleteById(id);
    }
    
    @Override
    public List<PostProcessingPrintUv> searchByProductName(String productName) {
        return mapper.findByProductName(productName);
    }
    
    @Override
    public List<PostProcessingPrintUv> searchByProductModelNumber(String productModelNumber) {
        return mapper.findByProductModelNumber(productModelNumber);
    }
    
    @Override
    public List<PostProcessingPrintUv> searchByCompanyNumber(String companyNumber) {
        return mapper.findByCompanyNumber(companyNumber);
    }
    
    @Override
    public List<PostProcessingPrintUv> searchByPaperType(String paperType) {
        return mapper.findByPaperType(paperType);
    }
    
    @Override
    public List<String> getAllProductNames() {
        return mapper.getAllProductNames();
    }
    
    @Override
    public List<String> getAllProductModelNumbers() {
        return mapper.getAllProductModelNumbers();
    }
    
    @Override
    public List<String> getAllCompanyNumbers() {
        return mapper.getAllCompanyNumbers();
    }
    
    @Override
    public List<String> getAllPaperTypes() {
        return mapper.getAllPaperTypes();
    }
    
    @Override
    public String checkPrintUvCompatibility(
        String productName,
        String productModelNumber,
        String companyNumber,
        String paperType
    ) {
        return mapper.checkPrintUvCompatibility(
            productName,
            productModelNumber,
            companyNumber,
            paperType
        );
    }
    
    @Override
    public PostProcessingPrintUv findByUniqueKey(
        String productName,
        String productModelNumber,
        String companyNumber,
        String paperType
    ) {
        return mapper.findByUniqueKey(productName, productModelNumber, companyNumber, paperType);
    }
    
    @Override
    public void batchUpdateStatus(List<Integer> ids, String compatibilityStatus) {
        mapper.batchUpdateStatus(ids, compatibilityStatus);
    }
    
    @Override
    public List<String> getCompanyNumbersByProductName(String productName) {
        return leoGpNumberMapper.getCompanyNumbersByProductName(productName);
    }
    
    @Override
    public List<String> getCompanyNumbersByProductModelNumberAndName(String productModelNumber, String productName) {
        return leoGpNumberMapper.getCompanyNumbersByProductModelNumberAndName(productModelNumber, productName);
    }
    
    @Override
    public boolean validateCompanyNumber(String companyNumber, String productName, String productModelNumber) {
        if (companyNumber == null || companyNumber.trim().isEmpty()) {
            return false;
        }
        
        List<String> validCompanyNumbers;
        if (productModelNumber != null && !productModelNumber.trim().isEmpty()) {
            // 如果提供了产品型号，验证公司编号是否属于该型号
            validCompanyNumbers = leoGpNumberMapper.getCompanyNumbersByProductModelNumberAndName(
                productModelNumber.trim(), productName.trim());
        } else {
            // 如果只提供了产品系列，验证公司编号是否属于该系列
            validCompanyNumbers = leoGpNumberMapper.getCompanyNumbersByProductName(productName.trim());
        }
        
        return validCompanyNumbers != null && validCompanyNumbers.contains(companyNumber.trim());
    }
}
