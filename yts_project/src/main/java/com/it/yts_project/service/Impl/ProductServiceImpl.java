package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.mapper.SpecificationMapper;
import com.it.yts_project.mapper.PricingMapper;
import com.it.yts_project.model.Product;
import com.it.yts_project.model.Specification;
import com.it.yts_project.model.Pricing;
import com.it.yts_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品信息Service实现类
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private SpecificationMapper specificationMapper;
    
    @Autowired
    private PricingMapper pricingMapper;
    
    @Override
    public List<Product> findAll() {
        return productMapper.findAll();
    }
    
    @Override
    public Product findById(Integer id) {
        return productMapper.findById(id);
    }
    
    @Override
    public List<Product> findByName(String name) {
        return productMapper.findByName(name);
    }
    
    @Override
    public List<Product> findByModelNumber(String modelNumber) {
        return productMapper.findByModelNumber(modelNumber);
    }
    
    @Override
    public List<Product> findByMaterialNumber(String materialNumber) {
        return productMapper.findByMaterialNumber(materialNumber);
    }
    
    @Override
    public List<Product> findByHotStampingPaperType(String hotStampingPaperType) {
        return productMapper.findByHotStampingPaperType(hotStampingPaperType);
    }
    
    @Override
    public List<Product> searchProducts(String keyword) {
        return productMapper.searchProducts(keyword);
    }
    
    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            // 新增
            productMapper.insert(product);
        } else {
            // 更新
            productMapper.update(product);
        }
        return product;
    }
    
    @Override
    public void deleteById(Integer id) {
        // 先删除相关的规格和价格
        specificationMapper.deleteByProjectId(id);
        pricingMapper.deleteByProjectId(id);
        // 再删除产品
        productMapper.deleteById(id);
    }
    
    @Override
    public List<String> getHotStampingPaperTypeOptions() {
        return productMapper.getHotStampingPaperTypeOptions();
    }
    
    @Override
    public List<String> getDanweiOptions() {
        return productMapper.getDanweiOptions();
    }
    
    @Override
    public List<String> getPaiziOptions() {
        return productMapper.getPaiziOptions();
    }
    
    @Override
    public List<String> getFengduOptions() {
        return productMapper.getFengduOptions();
    }
    
    @Override
    public List<String> getAllProductNames() {
        return productMapper.getAllProductNames();
    }
    
    @Override
    public Specification saveSpecification(Specification specification) {
        if (specification.getId() == null) {
            specificationMapper.insert(specification);
        } else {
            specificationMapper.update(specification);
        }
        return specification;
    }
    
    @Override
    public void deleteSpecificationById(Integer id) {
        specificationMapper.deleteById(id);
    }
    
    @Override
    public List<Specification> getSpecificationsByProductId(Integer productId) {
        return specificationMapper.findByProjectId(productId);
    }
    
    @Override
    public Pricing savePricing(Pricing pricing) {
        if (pricing.getId() == null) {
            pricingMapper.insert(pricing);
        } else {
            pricingMapper.update(pricing);
        }
        return pricing;
    }
    
    @Override
    public void deletePricingById(Integer id) {
        pricingMapper.deleteById(id);
    }
    
    @Override
    public List<Pricing> getPricingByProductId(Integer productId) {
        return pricingMapper.findByProjectId(productId);
    }
    
    @Override
    public List<String> getColorOptions() {
        return specificationMapper.getColorOptions();
    }
    
    @Override
    public List<String> getSizeOptions() {
        return specificationMapper.getSizeOptions();
    }
    
    @Override
    public List<String> getTightnessOptions() {
        return specificationMapper.getTightnessOptions();
    }
    
    @Override
    public List<String> getTemperatureRangeOptions() {
        return specificationMapper.getTemperatureRangeOptions();
    }
}
