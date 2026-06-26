package com.it.yts_project.service;

import com.it.yts_project.dto.SiliconeMicaPearlProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.SiliconeMicaPearlProduct;
import com.it.yts_project.model.SiliconeMicaPearlCompatibility;
import java.util.List;

public interface SiliconeMicaPearlService {
    List<SiliconeMicaPearlProduct> getAllProducts();
    SiliconeMicaPearlProduct getProductById(Integer id);
    List<SiliconeMicaPearlProduct> searchProducts(String keyword);
    SiliconeMicaPearlProduct saveProduct(SiliconeMicaPearlProduct product);
    void deleteProduct(Integer id);

    List<SiliconeMicaPearlCompatibility> getCompatibilitiesByProductId(Integer productId);
    SiliconeMicaPearlCompatibility getCompatibilityById(Integer id);
    SiliconeMicaPearlCompatibility saveCompatibility(SiliconeMicaPearlCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<SiliconeMicaPearlCompatibility> compatibilities);

    PagedResult<SiliconeMicaPearlProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    SiliconeMicaPearlProductDTO getProductDetail(Integer id);
}
