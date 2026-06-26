package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeWatercolorInkProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeWatercolorInkProduct;
import com.it.yts_project.model.SiliconeWatercolorInkCompatibility;
import java.util.List;

public interface SiliconeWatercolorInkService {
    List<SiliconeWatercolorInkProduct> getAllProducts();
    SiliconeWatercolorInkProduct getProductById(Integer id);
    List<SiliconeWatercolorInkProduct> searchProducts(String keyword);
    SiliconeWatercolorInkProduct saveProduct(SiliconeWatercolorInkProduct product);
    void deleteProduct(Integer id);

    List<SiliconeWatercolorInkCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeWatercolorInkCompatibility getCompatibilityById(Integer id);
    SiliconeWatercolorInkCompatibility saveCompatibility(SiliconeWatercolorInkCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeWatercolorInkCompatibility> compatibilities);

    PagedResult<SiliconeWatercolorInkProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeWatercolorInkProductDTO getProductDetail(Integer id);
}
