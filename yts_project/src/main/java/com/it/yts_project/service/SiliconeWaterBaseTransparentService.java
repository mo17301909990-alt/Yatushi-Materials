package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeWaterBaseTransparentProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeWaterBaseTransparentProduct;
import com.it.yts_project.model.SiliconeWaterBaseTransparentCompatibility;
import java.util.List;

public interface SiliconeWaterBaseTransparentService {
    List<SiliconeWaterBaseTransparentProduct> getAllProducts();
    SiliconeWaterBaseTransparentProduct getProductById(Integer id);
    List<SiliconeWaterBaseTransparentProduct> searchProducts(String keyword);
    SiliconeWaterBaseTransparentProduct saveProduct(SiliconeWaterBaseTransparentProduct product);
    void deleteProduct(Integer id);

    List<SiliconeWaterBaseTransparentCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeWaterBaseTransparentCompatibility getCompatibilityById(Integer id);
    SiliconeWaterBaseTransparentCompatibility saveCompatibility(SiliconeWaterBaseTransparentCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeWaterBaseTransparentCompatibility> compatibilities);

    PagedResult<SiliconeWaterBaseTransparentProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeWaterBaseTransparentProductDTO getProductDetail(Integer id);
}
