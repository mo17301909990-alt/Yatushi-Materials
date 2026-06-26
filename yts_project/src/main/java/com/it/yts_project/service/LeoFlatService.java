package com.it.yts_project.service;

import com.it.yts_project.dto.LeoFlatProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.LeoFlatProduct;
import com.it.yts_project.model.LeoFlatCompatibility;
import java.util.List;

public interface LeoFlatService {
    List<LeoFlatProduct> getAllProducts();
    LeoFlatProduct getProductById(Integer id);
    List<LeoFlatProduct> searchProducts(String keyword);
    LeoFlatProduct saveProduct(LeoFlatProduct product);
    void deleteProduct(Integer id);

    List<LeoFlatCompatibility> getCompatibilitiesByProductId(Integer productId);
    LeoFlatCompatibility getCompatibilityById(Integer id);
    LeoFlatCompatibility saveCompatibility(LeoFlatCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<LeoFlatCompatibility> compatibilities);

    PagedResult<LeoFlatProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    LeoFlatProductDTO getProductDetail(Integer id);
}
