package com.it.yts_project.service;

import com.it.yts_project.dto.LaminationMaterialProductDTO;
import com.it.yts_project.dto.PagedResult;
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

    // ========== 匹配查询 ==========

    /**
     * 搜索产品（关键词 + 多材料类型 + 多工序筛选 + 分页）
     */
    PagedResult<LaminationMaterialProduct> searchProducts(String keyword, List<String> materialTypes, List<String> steps, int page, int size);

    /**
     * 获取所有后加工工序步骤名称（去重）
     */
    List<String> getDistinctSteps();

    /**
     * 获取所有材料类型（去重）
     */
    List<String> getDistinctMaterialTypes();

    /**
     * 获取产品详情（含兼容性列表）
     */
    LaminationMaterialProductDTO getProductDetail(Integer id);
}
