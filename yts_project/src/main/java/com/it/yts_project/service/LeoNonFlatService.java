package com.it.yts_project.service;

import com.it.yts_project.dto.LeoNonFlatProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.LeoNonFlatProduct;
import com.it.yts_project.model.LeoNonFlatCompatibility;
import java.util.List;

public interface LeoNonFlatService {
    List<LeoNonFlatProduct> getAllProducts();
    LeoNonFlatProduct getProductById(Integer id);
    List<LeoNonFlatProduct> searchProducts(String keyword);
    LeoNonFlatProduct saveProduct(LeoNonFlatProduct product);
    void deleteProduct(Integer id);

    List<LeoNonFlatCompatibility> getCompatibilitiesByProductId(Integer productId);
    LeoNonFlatCompatibility getCompatibilityById(Integer id);
    LeoNonFlatCompatibility saveCompatibility(LeoNonFlatCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<LeoNonFlatCompatibility> compatibilities);

    PagedResult<LeoNonFlatProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    LeoNonFlatProductDTO getProductDetail(Integer id);
}
