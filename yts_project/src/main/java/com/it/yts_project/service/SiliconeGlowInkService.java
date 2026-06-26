package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeGlowInkProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeGlowInkProduct;
import com.it.yts_project.model.SiliconeGlowInkCompatibility;
import java.util.List;

public interface SiliconeGlowInkService {
    List<SiliconeGlowInkProduct> getAllProducts();
    SiliconeGlowInkProduct getProductById(Integer id);
    List<SiliconeGlowInkProduct> searchProducts(String keyword);
    SiliconeGlowInkProduct saveProduct(SiliconeGlowInkProduct product);
    void deleteProduct(Integer id);

    List<SiliconeGlowInkCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeGlowInkCompatibility getCompatibilityById(Integer id);
    SiliconeGlowInkCompatibility saveCompatibility(SiliconeGlowInkCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeGlowInkCompatibility> compatibilities);

    PagedResult<SiliconeGlowInkProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeGlowInkProductDTO getProductDetail(Integer id);
}
