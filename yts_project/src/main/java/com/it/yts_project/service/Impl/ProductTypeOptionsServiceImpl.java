package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.ProductTypeOptionsMapper;
import com.it.yts_project.model.ProductTypeOptions;
import com.it.yts_project.service.ProductTypeOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品类型选项服务实现类
 */
@Service
public class ProductTypeOptionsServiceImpl implements ProductTypeOptionsService {
    
    @Autowired
    private ProductTypeOptionsMapper productTypeOptionsMapper;
    
    @Override
    public List<ProductTypeOptions> getAllActiveOptions() {
        return productTypeOptionsMapper.getAllActiveOptions();
    }
    
    @Override
    public ProductTypeOptions getById(Integer id) {
        return productTypeOptionsMapper.getById(id);
    }
    
    @Override
    public List<ProductTypeOptions> getAllOptions() {
        return productTypeOptionsMapper.getAllOptions();
    }
    
    @Override
    public ProductTypeOptions save(ProductTypeOptions productType) {
        if (productType.getId() == null) {
            // 新增
            productTypeOptionsMapper.insert(productType);
        } else {
            // 更新
            productTypeOptionsMapper.update(productType);
        }
        return productType;
    }
    
    @Override
    public void deleteById(Integer id) {
        productTypeOptionsMapper.deleteById(id);
    }
}
