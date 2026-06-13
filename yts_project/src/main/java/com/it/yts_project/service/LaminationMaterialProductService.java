package com.it.yts_project.service;

import com.it.yts_project.model.LaminationMaterialCompatibility;
import com.it.yts_project.model.LaminationMaterialProduct;

import java.util.List;

/**
 * 过胶材料产品Service接口
 */
public interface LaminationMaterialProductService {

    // ========== 产品管理 ==========

    List<LaminationMaterialProduct> getAllProducts();

    LaminationMaterialProduct getProductById(Integer id);

    List<LaminationMaterialProduct> searchProducts(String keyword);

    LaminationMaterialProduct saveProduct(LaminationMaterialProduct product);

    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    List<LaminationMaterialCompatibility> getCompatibilitiesByProductId(Integer productId);

    List<LaminationMaterialCompatibility> getAllCompatibilities();

    LaminationMaterialCompatibility getCompatibilityById(Integer id);

    LaminationMaterialCompatibility saveCompatibility(LaminationMaterialCompatibility compatibility);

    void deleteCompatibility(Integer id);

    void batchSaveCompatibilities(List<LaminationMaterialCompatibility> compatibilities);

    List<String> getAllPostProcessingSteps();
}
