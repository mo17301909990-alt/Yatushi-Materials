package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeGlitteringStarProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeGlitteringStarProduct;
import com.it.yts_project.model.SiliconeGlitteringStarCompatibility;
import java.util.List;

public interface SiliconeGlitteringStarService {
    List<SiliconeGlitteringStarProduct> getAllProducts();
    SiliconeGlitteringStarProduct getProductById(Integer id);
    List<SiliconeGlitteringStarProduct> searchProducts(String keyword);
    SiliconeGlitteringStarProduct saveProduct(SiliconeGlitteringStarProduct product);
    void deleteProduct(Integer id);

    List<SiliconeGlitteringStarCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeGlitteringStarCompatibility getCompatibilityById(Integer id);
    SiliconeGlitteringStarCompatibility saveCompatibility(SiliconeGlitteringStarCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeGlitteringStarCompatibility> compatibilities);

    PagedResult<SiliconeGlitteringStarProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeGlitteringStarProductDTO getProductDetail(Integer id);
}
