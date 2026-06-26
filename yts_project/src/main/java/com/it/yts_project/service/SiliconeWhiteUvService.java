package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeWhiteUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeWhiteUvProduct;
import com.it.yts_project.model.SiliconeWhiteUvCompatibility;
import java.util.List;

public interface SiliconeWhiteUvService {
    List<SiliconeWhiteUvProduct> getAllProducts();
    SiliconeWhiteUvProduct getProductById(Integer id);
    List<SiliconeWhiteUvProduct> searchProducts(String keyword);
    SiliconeWhiteUvProduct saveProduct(SiliconeWhiteUvProduct product);
    void deleteProduct(Integer id);

    List<SiliconeWhiteUvCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeWhiteUvCompatibility getCompatibilityById(Integer id);
    SiliconeWhiteUvCompatibility saveCompatibility(SiliconeWhiteUvCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeWhiteUvCompatibility> compatibilities);

    PagedResult<SiliconeWhiteUvProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeWhiteUvProductDTO getProductDetail(Integer id);
}
