package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.silicone_sandblast_uvMapper;
import com.it.yts_project.model.silicone_sandblast_uvProduct;
import com.it.yts_project.model.silicone_sandblast_uvCompatibility;
import com.it.yts_project.service.silicone_sandblast_uvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 硅胶磨砂UV服务实现类
 */
@Service
@Transactional
public class silicone_sandblast_uvServiceImpl implements silicone_sandblast_uvService {

    @Autowired
    private silicone_sandblast_uvMapper mapper;

    // ========== 产品管理 ==========

    @Override
    public List<silicone_sandblast_uvProduct> getAllProducts() {
        return mapper.findAllProducts();
    }

    @Override
    public List<silicone_sandblast_uvProduct> getActiveProducts() {
        return mapper.findActiveProducts();
    }

    @Override
    public silicone_sandblast_uvProduct getProductById(Integer id) {
        return mapper.findProductById(id);
    }

    @Override
    public List<silicone_sandblast_uvProduct> searchProducts(String keyword) {
        return mapper.searchProducts(keyword);
    }

    @Override
    public silicone_sandblast_uvProduct saveProduct(silicone_sandblast_uvProduct product) {
        if (product.getId() == null) {
            mapper.insertProduct(product);
        } else {
            mapper.updateProduct(product);
        }
        return product;
    }

    @Override
    public void deleteProduct(Integer id) {
        // 先删除关联的兼容性
        mapper.deleteCompatibilitiesByProductId(id);
        // 再删除产品
        mapper.deleteProductById(id);
    }

    // ========== 兼容性管理 ==========

    @Override
    public List<silicone_sandblast_uvCompatibility> getCompatibilitiesByProductId(Integer productId) {
        return mapper.findCompatibilitiesByProductId(productId);
    }

    @Override
    public List<silicone_sandblast_uvCompatibility> getAllCompatibilities() {
        return mapper.findAllCompatibilities();
    }

    @Override
    public silicone_sandblast_uvCompatibility getCompatibilityById(Integer id) {
        return mapper.findCompatibilityById(id);
    }

    @Override
    public silicone_sandblast_uvCompatibility saveCompatibility(silicone_sandblast_uvCompatibility compatibility) {
        if (compatibility.getId() == null) {
            mapper.insertCompatibility(compatibility);
        } else {
            mapper.updateCompatibility(compatibility);
        }
        return compatibility;
    }

    @Override
    public void deleteCompatibility(Integer id) {
        mapper.deleteCompatibilityById(id);
    }

    @Override
    public void batchSaveCompatibilities(List<silicone_sandblast_uvCompatibility> compatibilities) {
        if (compatibilities == null || compatibilities.isEmpty()) {
            return;
        }
        mapper.batchInsertCompatibilities(compatibilities);
    }
}
