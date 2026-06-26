package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeWrinkleUvProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeWrinkleUvProduct;
import com.it.yts_project.model.SiliconeWrinkleUvCompatibility;
import java.util.List;

public interface SiliconeWrinkleUvService {
    List<SiliconeWrinkleUvProduct> getAllProducts();
    SiliconeWrinkleUvProduct getProductById(Integer id);
    List<SiliconeWrinkleUvProduct> searchProducts(String keyword);
    SiliconeWrinkleUvProduct saveProduct(SiliconeWrinkleUvProduct product);
    void deleteProduct(Integer id);

    List<SiliconeWrinkleUvCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeWrinkleUvCompatibility getCompatibilityById(Integer id);
    SiliconeWrinkleUvCompatibility saveCompatibility(SiliconeWrinkleUvCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeWrinkleUvCompatibility> compatibilities);

    PagedResult<SiliconeWrinkleUvProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeWrinkleUvProductDTO getProductDetail(Integer id);
}
