package com.it.yts_project.service;

import com.it.yts_project.dto.LeoBookBoardProductDTO;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.LeoBookBoardProduct;
import com.it.yts_project.model.LeoBookBoardCompatibility;
import java.util.List;

public interface LeoBookBoardService {
    List<LeoBookBoardProduct> getAllProducts();
    LeoBookBoardProduct getProductById(Integer id);
    List<LeoBookBoardProduct> searchProducts(String keyword);
    LeoBookBoardProduct saveProduct(LeoBookBoardProduct product);
    void deleteProduct(Integer id);

    List<LeoBookBoardCompatibility> getCompatibilitiesByProductId(Integer productId);
    LeoBookBoardCompatibility getCompatibilityById(Integer id);
    LeoBookBoardCompatibility saveCompatibility(LeoBookBoardCompatibility compatibility);
    void deleteCompatibility(Integer id);
    void batchSaveCompatibilities(List<LeoBookBoardCompatibility> compatibilities);

    PagedResult<LeoBookBoardProduct> searchProducts(String keyword, String stepName, int page, int size);
    List<String> getDistinctSteps();
    LeoBookBoardProductDTO getProductDetail(Integer id);
}
