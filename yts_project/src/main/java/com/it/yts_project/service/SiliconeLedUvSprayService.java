package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeLedUvSprayProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeLedUvSprayProduct;
import com.it.yts_project.model.SiliconeLedUvSprayCompatibility;
import java.util.List;

public interface SiliconeLedUvSprayService {
    List<SiliconeLedUvSprayProduct> getAllProducts();
    SiliconeLedUvSprayProduct getProductById(Integer id);
    List<SiliconeLedUvSprayProduct> searchProducts(String keyword);
    SiliconeLedUvSprayProduct saveProduct(SiliconeLedUvSprayProduct product);
    void deleteProduct(Integer id);

    List<SiliconeLedUvSprayCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeLedUvSprayCompatibility getCompatibilityById(Integer id);
    SiliconeLedUvSprayCompatibility saveCompatibility(SiliconeLedUvSprayCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeLedUvSprayCompatibility> compatibilities);

    PagedResult<SiliconeLedUvSprayProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeLedUvSprayProductDTO getProductDetail(Integer id);
}
