package com.it.yts_project.service;

import com.it.yts_project.model.Product;
import com.it.yts_project.model.Specification;
import com.it.yts_project.model.Pricing;

import java.util.List;

/**
 * 产品信息Service接口
 */
public interface ProductService {
    
    /**
     * 查询所有产品
     */
    List<Product> findAll();
    
    /**
     * 根据ID查询产品
     */
    Product findById(Integer id);
    
    /**
     * 根据燙金紙系列查询
     */
    List<Product> findByName(String name);
    
    /**
     * 根据型号查询
     */
    List<Product> findByModelNumber(String modelNumber);
    
    /**
     * 根据物料编号查询
     */
    List<Product> findByMaterialNumber(String materialNumber);
    
    /**
     * 根据烫金纸张类型查询
     */
    List<Product> findByHotStampingPaperType(String hotStampingPaperType);
    
    /**
     * 搜索产品
     */
    List<Product> searchProducts(String keyword);
    
    /**
     * 保存产品（新增或更新）
     */
    Product save(Product product);
    
    /**
     * 删除产品
     */
    void deleteById(Integer id);
    
    /**
     * 获取所有烫金纸张类型选项
     */
    List<String> getHotStampingPaperTypeOptions();
    
    /**
     * 获取所有单位选项
     */
    List<String> getDanweiOptions();
    
    /**
     * 获取所有牌子选项
     */
    List<String> getPaiziOptions();
    
    /**
     * 获取所有封度选项
     */
    List<String> getFengduOptions();
    
    /**
     * 获取所有产品名称
     */
    List<String> getAllProductNames();
    
    /**
     * 保存产品规格
     */
    Specification saveSpecification(Specification specification);
    
    /**
     * 删除产品规格
     */
    void deleteSpecificationById(Integer id);
    
    /**
     * 根据产品ID查询规格
     */
    List<Specification> getSpecificationsByProductId(Integer productId);
    
    /**
     * 保存产品价格
     */
    Pricing savePricing(Pricing pricing);
    
    /**
     * 删除产品价格
     */
    void deletePricingById(Integer id);
    
    /**
     * 根据产品ID查询价格
     */
    List<Pricing> getPricingByProductId(Integer productId);
    
    /**
     * 获取所有颜色选项
     */
    List<String> getColorOptions();
    
    /**
     * 获取所有尺寸选项
     */
    List<String> getSizeOptions();
    
    /**
     * 获取所有紧密度选项
     */
    List<String> getTightnessOptions();
    
    /**
     * 获取所有温度范围选项
     */
    List<String> getTemperatureRangeOptions();
}
