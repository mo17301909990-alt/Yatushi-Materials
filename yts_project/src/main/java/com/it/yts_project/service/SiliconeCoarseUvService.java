package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeCoarseUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeCoarseUvProduct;
import com.it.yts_project.model.SiliconeCoarseUvCompatibility;
import java.util.List;

public interface SiliconeCoarseUvService {
    List<SiliconeCoarseUvProduct> getAllProducts();
    SiliconeCoarseUvProduct getProductById(Integer id);
    List<SiliconeCoarseUvProduct> searchProducts(String keyword);
    SiliconeCoarseUvProduct saveProduct(SiliconeCoarseUvProduct product);
    void deleteProduct(Integer id);

    List<SiliconeCoarseUvCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeCoarseUvCompatibility getCompatibilityById(Integer id);
    SiliconeCoarseUvCompatibility saveCompatibility(SiliconeCoarseUvCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeCoarseUvCompatibility> compatibilities);

    PagedResult<SiliconeCoarseUvProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeCoarseUvProductDTO getProductDetail(Integer id);
}
