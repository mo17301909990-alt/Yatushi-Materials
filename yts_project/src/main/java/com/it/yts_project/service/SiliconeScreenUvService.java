package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeScreenUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeScreenUvProduct;
import com.it.yts_project.model.SiliconeScreenUvCompatibility;
import java.util.List;

public interface SiliconeScreenUvService {
    List<SiliconeScreenUvProduct> getAllProducts();
    SiliconeScreenUvProduct getProductById(Integer id);
    List<SiliconeScreenUvProduct> searchProducts(String keyword);
    SiliconeScreenUvProduct saveProduct(SiliconeScreenUvProduct product);
    void deleteProduct(Integer id);

    List<SiliconeScreenUvCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeScreenUvCompatibility getCompatibilityById(Integer id);
    SiliconeScreenUvCompatibility saveCompatibility(SiliconeScreenUvCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeScreenUvCompatibility> compatibilities);

    PagedResult<SiliconeScreenUvProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeScreenUvProductDTO getProductDetail(Integer id);
}
