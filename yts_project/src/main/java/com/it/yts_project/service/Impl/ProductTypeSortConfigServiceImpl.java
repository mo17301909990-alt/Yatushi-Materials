package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.ProductTypeSortConfigMapper;
import com.it.yts_project.model.ProductTypeSortConfig;
import com.it.yts_project.service.ProductTypeSortConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品类型特殊映射配置服务实现类
 */
@Service
public class ProductTypeSortConfigServiceImpl implements ProductTypeSortConfigService {

    @Autowired
    private ProductTypeSortConfigMapper mapper;

    @Override
    public List<ProductTypeSortConfig> getAll() {
        return mapper.findAll();
    }

    @Override
    public ProductTypeSortConfig getById(Integer id) {
        return mapper.findById(id);
    }

    @Override
    public ProductTypeSortConfig getByProductTypeId(Integer productTypeId) {
        return mapper.findByProductTypeId(productTypeId);
    }

    @Override
    public List<ProductTypeSortConfig> getAllActive() {
        return mapper.findAllActive();
    }

    @Override
    public ProductTypeSortConfig save(ProductTypeSortConfig config) {
        if (config.getId() == null) {
            mapper.insert(config);
        } else {
            mapper.update(config);
        }
        return config;
    }

    @Override
    public void deleteById(Integer id) {
        mapper.deleteById(id);
    }
}
