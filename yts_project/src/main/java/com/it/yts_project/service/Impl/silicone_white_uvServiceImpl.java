package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.silicone_white_uvMapper;
import com.it.yts_project.model.silicone_white_uvProduct;
import com.it.yts_project.model.silicone_white_uvCompatibility;
import com.it.yts_project.service.silicone_white_uvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 硅胶_白UV Service实现类
 */
@Service
@Transactional
public class silicone_white_uvServiceImpl implements silicone_white_uvService {

    @Autowired
    private silicone_white_uvMapper mapper;

    // ========== 产品管理 ==========

    @Override
    public List<silicone_white_uvProduct> getAllProducts() {
        return mapper.findAllProducts();
    }

    @Override
    public List<silicone_white_uvProduct> getActiveProducts() {
        return mapper.findActiveProducts();
    }

    @Override
    public silicone_white_uvProduct getProductById(Integer id) {
        return mapper.findProductById(id);
    }

    @Override
    public List<silicone_white_uvProduct> searchProducts(String keyword) {
        return mapper.searchProducts(keyword);
    }

    @Override
    public silicone_white_uvProduct saveProduct(silicone_white_uvProduct product) {
        if (product.getId() == null) {
            // 默认激活
            if (product.getIsActive() == null) {
                product.setIsActive(true);
            }
            mapper.insertProduct(product);
        } else {
            mapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        mapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<silicone_white_uvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return mapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public List<silicone_white_uvCompatibility> getAllCompatibilities() {
        return mapper.findAllCompatibilities();
    }

    @Override
    public silicone_white_uvCompatibility getCompatibilityById(Integer id) {
        return mapper.findCompatibilityById(id);
    }

    @Override
    public silicone_white_uvCompatibility saveCompatibility(silicone_white_uvCompatibility compatibility) {
        if (compatibility.getId() == null) {
            mapper.insertCompatibility(compatibility);
        } else {
            mapper.updateCompatibility(compatibility);
        }
        return compatibility;
    }

    @Override
    public void batchSaveCompatibilities(List<silicone_white_uvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }
        mapper.batchInsertCompatibilities(compatibilities);
    }

    @Override
    public void deleteCompatibility(Integer id) {
        mapper.deleteCompatibilityById(id);
    }

    @Override
    public void deleteCompatibilitiesByProductId(Integer productId) {
        mapper.deleteCompatibilitiesByProductId(productId);
    }
}
