package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeSandblastUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeSandblastUvProduct;
import com.it.yts_project.model.SiliconeSandblastUvCompatibility;
import java.util.List;

public interface SiliconeSandblastUvService {
    List<SiliconeSandblastUvProduct> getAllProducts();
    SiliconeSandblastUvProduct getProductById(Integer id);
    List<SiliconeSandblastUvProduct> searchProducts(String keyword);
    SiliconeSandblastUvProduct saveProduct(SiliconeSandblastUvProduct product);
    void deleteProduct(Integer id);

    List<SiliconeSandblastUvCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeSandblastUvCompatibility getCompatibilityById(Integer id);
    SiliconeSandblastUvCompatibility saveCompatibility(SiliconeSandblastUvCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeSandblastUvCompatibility> compatibilities);

    PagedResult<SiliconeSandblastUvProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeSandblastUvProductDTO getProductDetail(Integer id);
}
