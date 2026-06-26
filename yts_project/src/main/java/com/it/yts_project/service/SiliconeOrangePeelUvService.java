package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeOrangePeelUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeOrangePeelUvProduct;
import com.it.yts_project.model.SiliconeOrangePeelUvCompatibility;
import java.util.List;

public interface SiliconeOrangePeelUvService {
    List<SiliconeOrangePeelUvProduct> getAllProducts();
    SiliconeOrangePeelUvProduct getProductById(Integer id);
    List<SiliconeOrangePeelUvProduct> searchProducts(String keyword);
    SiliconeOrangePeelUvProduct saveProduct(SiliconeOrangePeelUvProduct product);
    void deleteProduct(Integer id);

    List<SiliconeOrangePeelUvCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeOrangePeelUvCompatibility getCompatibilityById(Integer id);
    SiliconeOrangePeelUvCompatibility saveCompatibility(SiliconeOrangePeelUvCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeOrangePeelUvCompatibility> compatibilities);

    PagedResult<SiliconeOrangePeelUvProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeOrangePeelUvProductDTO getProductDetail(Integer id);
}
